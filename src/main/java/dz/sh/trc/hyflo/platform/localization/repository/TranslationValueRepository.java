package dz.sh.trc.hyflo.platform.localization.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.platform.localization.model.TranslationValue;

@Repository
public interface TranslationValueRepository extends JpaRepository<TranslationValue, Long> {
    Optional<TranslationValue> findByTranslationKey_CodeAndLanguage(String code, String language);
    List<TranslationValue> findAllByTranslationKey_Code(String code);
}
