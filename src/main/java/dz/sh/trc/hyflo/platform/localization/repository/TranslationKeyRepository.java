package dz.sh.trc.hyflo.platform.localization.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.platform.localization.model.TranslationKey;

@Repository
public interface TranslationKeyRepository extends JpaRepository<TranslationKey, Long> {
    Optional<TranslationKey> findByCode(String code);
    List<TranslationKey> findByModule(String module);
    boolean existsByCode(String code);
}
