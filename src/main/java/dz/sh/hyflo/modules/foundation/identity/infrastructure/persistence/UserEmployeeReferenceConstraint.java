/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserEmployeeReferenceConstraint
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Class
 * @Layer       : Persistence
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence
 *
 * @Description : Verifies the scalar cross-module employee reference after Hibernate schema management.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@DependsOn("entityManagerFactory")
public class UserEmployeeReferenceConstraint implements InitializingBean, DisposableBean {
    private static final String FK = "T_10_01_08_FK_04";
    private final JdbcTemplate jdbc;
    private final Environment environment;
    private boolean testConstraintManaged;

    public UserEmployeeReferenceConstraint(JdbcTemplate jdbc, Environment environment) {
        this.jdbc = jdbc;
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() {
        String mode = environment.getProperty("spring.jpa.hibernate.ddl-auto", "validate");
        boolean disposableTest = "create-drop".equals(mode) && environment.acceptsProfiles(Profiles.of("test"));
        boolean disposableDev = "update".equals(mode) && environment.acceptsProfiles(Profiles.of("dev"));
        if (!validUnique()) throw new IllegalStateException("Required employee uniqueness constraint is invalid");
        if (!validForeignKey()) {
            if ((!disposableTest && !disposableDev) || foreignKeyNameCount() != 0
                    || jdbc.queryForObject("SELECT COUNT(*) FROM T_10_01_08", Long.class) != 0L) {
                throw new IllegalStateException("Required employee foreign key is invalid");
            }
            jdbc.execute("ALTER TABLE T_10_01_08 ADD CONSTRAINT " + FK
                    + " FOREIGN KEY (F_03) REFERENCES T_10_02_09(F_00) ON DELETE RESTRICT ON UPDATE RESTRICT");
        }
        if (!validForeignKey()) throw new IllegalStateException("Required employee foreign key is invalid");
        testConstraintManaged = disposableTest;
    }

    private long foreignKeyNameCount() {
        return jdbc.queryForObject("""
                SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
                WHERE CONSTRAINT_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08'
                    AND CONSTRAINT_NAME=?
                """, Long.class, FK);
    }

    private boolean validForeignKey() {
        return foreignKeyNameCount() == 1
                && jdbc.queryForObject("""
                    SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE
                    WHERE CONSTRAINT_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08'
                        AND CONSTRAINT_NAME=? AND COLUMN_NAME='F_03'
                        AND BINARY REFERENCED_TABLE_NAME='T_10_02_09'
                        AND REFERENCED_TABLE_SCHEMA=DATABASE() AND REFERENCED_COLUMN_NAME='F_00'
                    """, Long.class, FK) == 1L
                && jdbc.queryForObject("""
                    SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE
                    WHERE CONSTRAINT_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08' AND CONSTRAINT_NAME=?
                    """, Long.class, FK) == 1L
                && jdbc.queryForObject("""
                    SELECT COUNT(*) FROM information_schema.REFERENTIAL_CONSTRAINTS
                    WHERE CONSTRAINT_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08' AND CONSTRAINT_NAME=?
                        AND DELETE_RULE IN ('RESTRICT','NO ACTION') AND UPDATE_RULE IN ('RESTRICT','NO ACTION')
                    """, Long.class, FK) == 1L;
    }

    private boolean validUnique() {
        return jdbc.queryForObject("""
                SELECT COUNT(*) FROM information_schema.STATISTICS
                WHERE TABLE_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08'
                    AND INDEX_NAME='T_10_01_08_UK_03'
                """, Long.class) == 1L
                && jdbc.queryForObject("""
                    SELECT COUNT(*) FROM information_schema.STATISTICS
                    WHERE TABLE_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08'
                        AND INDEX_NAME='T_10_01_08_UK_03' AND COLUMN_NAME='F_03' AND NON_UNIQUE=0
                    """, Long.class) == 1L
                && jdbc.queryForObject("""
                    SELECT COUNT(*) FROM information_schema.COLUMNS
                    WHERE TABLE_SCHEMA=DATABASE() AND BINARY TABLE_NAME='T_10_01_08'
                        AND COLUMN_NAME='F_03' AND IS_NULLABLE='YES'
                    """, Long.class) == 1L;
    }

    @Override
    public void destroy() {
        if (testConstraintManaged && validForeignKey()) {
            jdbc.execute("ALTER TABLE T_10_01_08 DROP FOREIGN KEY " + FK);
        }
    }
}
