package dz.sh.trc.hyflo.platform.localization.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dz.sh.trc.hyflo.platform.localization.dto.TranslationResponse;
import dz.sh.trc.hyflo.platform.localization.service.LocalizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/localization")
@Tag(name = "Localization API", description = "Translation key resolution for multi-language support")
@RequiredArgsConstructor
public class LocalizationController {

    private final LocalizationService localizationService;

    @Operation(summary = "Translate a key", description = "Returns the translated value for a given key and language")
    @GetMapping("/translate")
    public ResponseEntity<TranslationResponse> translate(
            @RequestParam String key,
            @RequestParam(defaultValue = "FR") String lang) {
        String value = localizationService.translate(key, lang);
        return ResponseEntity.ok(new TranslationResponse(key, lang, value));
    }

    @Operation(summary = "Get all translations for a key", description = "Returns all language variants for a translation key")
    @GetMapping("/keys/{code}")
    public ResponseEntity<Map<String, String>> getAllTranslations(@PathVariable String code) {
        return ResponseEntity.ok(localizationService.getTranslations(code));
    }
}
