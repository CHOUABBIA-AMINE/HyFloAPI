package dz.sh.trc.hyflo.platform.localization.service;

import java.util.Map;

/**
 * Core localization service for translating keys into human-readable text.
 * Supports placeholder substitution and multi-language output.
 *
 * Usage:
 *   localizationService.translate("FLOW_READING_SUBMITTED", "FR")
 *   → "Lecture de débit soumise pour validation"
 *
 *   localizationService.translate("NOTIFICATION_READING_SUBMITTED", "FR",
 *       Map.of("actorName", "Ahmed", "readingId", "42"))
 *   → "Ahmed a soumis la lecture #42 pour validation"
 */
public interface LocalizationService {

    /**
     * Translates a key into the specified language.
     * Returns the key code itself if no translation is found (never throws).
     */
    String translate(String key, String language);

    /**
     * Translates a key with placeholder substitution.
     * Placeholders use {name} syntax: "Hello {actorName}" → "Hello Ahmed"
     */
    String translate(String key, String language, Map<String, String> placeholders);

    /**
     * Returns all translations for a key (keyed by language code).
     * Example: {"FR": "Soumis", "EN": "Submitted", "AR": "مقدم"}
     */
    Map<String, String> getTranslations(String key);
}
