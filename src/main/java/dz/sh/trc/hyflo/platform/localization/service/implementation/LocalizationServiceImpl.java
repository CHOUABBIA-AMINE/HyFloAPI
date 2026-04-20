package dz.sh.trc.hyflo.platform.localization.service.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.platform.localization.model.TranslationValue;
import dz.sh.trc.hyflo.platform.localization.repository.TranslationValueRepository;
import dz.sh.trc.hyflo.platform.localization.service.LocalizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocalizationServiceImpl implements LocalizationService {

    private static final String DEFAULT_LANGUAGE = "FR";

    private final TranslationValueRepository translationValueRepository;

    @Override
    public String translate(String key, String language) {
        return translate(key, language, Map.of());
    }

    @Override
    public String translate(String key, String language, Map<String, String> placeholders) {
        if (key == null) return "";

        String lang = (language != null) ? language.toUpperCase() : DEFAULT_LANGUAGE;

        String value = translationValueRepository
                .findByTranslationKey_CodeAndLanguage(key, lang)
                .map(TranslationValue::getValue)
                .orElseGet(() -> {
                    // Fallback: try default language if different
                    if (!DEFAULT_LANGUAGE.equals(lang)) {
                        return translationValueRepository
                                .findByTranslationKey_CodeAndLanguage(key, DEFAULT_LANGUAGE)
                                .map(TranslationValue::getValue)
                                .orElse(key);
                    }
                    return key; // Return key code as-is (never throw)
                });

        // Substitute placeholders: {actorName} → "Ahmed"
        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                value = value.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        return value;
    }

    @Override
    public Map<String, String> getTranslations(String key) {
        List<TranslationValue> values = translationValueRepository.findAllByTranslationKey_Code(key);
        Map<String, String> result = new HashMap<>();
        for (TranslationValue tv : values) {
            result.put(tv.getLanguage(), tv.getValue());
        }
        return result;
    }
}
