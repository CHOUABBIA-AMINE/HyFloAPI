package dz.sh.hyflo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Synthetic test-only entity; excluded from the application JAR. */
@Entity
@Table(name = "hyflo_test_probe")
public class SchemaProbe {

    @Id
    private Long id;

    @Column(name = "label", nullable = false, length = 64)
    private String label;

    protected SchemaProbe() {
    }

    public SchemaProbe(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
