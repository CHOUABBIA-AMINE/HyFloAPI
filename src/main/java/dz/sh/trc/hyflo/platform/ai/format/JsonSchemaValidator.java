package dz.sh.trc.hyflo.platform.ai.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Validates a raw LLM output string against a {@link SchemaDescriptor}.
 *
 * <h3>Four validation levels, in order:</h3>
 * <ol>
 *   <li><strong>Syntax</strong> — is the string valid JSON?</li>
 *   <li><strong>Required fields</strong> — are all required keys present
 *       and non-null at the top level?</li>
 *   <li><strong>Type conformance</strong> — do declared enum fields contain
 *       only allowed values?</li>
 *   <li><strong>Range constraints</strong> — do numeric fields satisfy
 *       declared min/max bounds?</li>
 * </ol>
 *
 * <p>All errors from all levels are collected and returned together so
 * that {@link OutputCorrectionPromptBuilder} can give the LLM a complete
 * list of corrections to make in a single re-try.</p>
 *
 * <h3>Design rule:</h3>
 * <p>This class uses only Jackson's {@link JsonNode} API for validation.
 * It does NOT perform final deserialization into business types — that
 * responsibility belongs to {@link StructuredOutputEnforcer}.</p>
 *
 * <h3>Schema definition:</h3>
 * <p>Use the nested {@link SchemaDescriptor} and its builder to declare
 * validation rules without depending on JSON Schema Draft 7 libraries,
 * keeping the dependency footprint minimal (only Jackson).</p>
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * SchemaDescriptor schema = SchemaDescriptor.builder()
 *     .requiredField("score")
 *     .requiredField("severity")
 *     .enumField("severity", List.of("LOW","MEDIUM","HIGH","CRITICAL"))
 *     .rangeField("score", 0.0, 100.0)
 *     .build();
 *
 * ValidationResult result = validator.validate(rawJson, schema);
 * if (!result.isValid()) {
 *     // pass result.errors() to OutputCorrectionPromptBuilder
 * }
 * }</pre>
 */
@Component
public class JsonSchemaValidator {

    private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidator.class);

    private final ObjectMapper objectMapper;

    public JsonSchemaValidator(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper must not be null");
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Validates {@code rawJson} against the provided {@link SchemaDescriptor}.
     *
     * @param rawJson  the raw string returned by the LLM (may be null, empty, or malformed)
     * @param schema   the validation descriptor
     * @return a {@link ValidationResult} with all errors found (empty = valid)
     */
    public ValidationResult validate(String rawJson, SchemaDescriptor schema) {
        Objects.requireNonNull(schema, "SchemaDescriptor must not be null");

        List<String> errors = new ArrayList<>();

        // Level 1: Syntax
        if (rawJson == null || rawJson.isBlank()) {
            errors.add("Output is null or empty — the response must be a JSON object.");
            return ValidationResult.invalid(errors);
        }

        String cleaned = extractJsonBlock(rawJson);

        JsonNode root;
        try {
            root = objectMapper.readTree(cleaned);
        } catch (JsonProcessingException e) {
            errors.add("Output is not valid JSON: " + e.getOriginalMessage()
                    + ". Respond with a JSON object only — no markdown, no prose.");
            return ValidationResult.invalid(errors);
        }

        if (!root.isObject()) {
            errors.add("Output must be a JSON object ({...}), not an array or primitive.");
            return ValidationResult.invalid(errors);
        }

        // Level 2: Required fields
        for (String requiredField : schema.requiredFields()) {
            JsonNode node = root.get(requiredField);
            if (node == null || node.isNull()) {
                errors.add("Required field '" + requiredField + "' is missing or null.");
            }
        }

        // Level 3: Enum conformance
        for (Map.Entry<String, List<String>> entry : schema.enumFields().entrySet()) {
            String     fieldName     = entry.getKey();
            List<String> allowed     = entry.getValue();
            JsonNode   fieldNode     = root.get(fieldName);
            if (fieldNode != null && !fieldNode.isNull()) {
                String value = fieldNode.asText();
                if (!allowed.contains(value)) {
                    errors.add("Field '" + fieldName + "' has invalid value '" + value
                            + "'. Allowed values: " + allowed + ".");
                }
            }
        }

        // Level 4: Range constraints
        for (Map.Entry<String, double[]> entry : schema.rangeFields().entrySet()) {
            String   fieldName = entry.getKey();
            double[] range     = entry.getValue(); // [min, max]
            JsonNode fieldNode = root.get(fieldName);
            if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
                double val = fieldNode.asDouble();
                if (val < range[0] || val > range[1]) {
                    errors.add("Field '" + fieldName + "' value " + val
                            + " is out of range [" + range[0] + ", " + range[1] + "].");
                }
            }
        }

        if (errors.isEmpty()) {
            log.debug("[JsonSchemaValidator] validation passed");
            return ValidationResult.valid(cleaned);
        }

        log.debug("[JsonSchemaValidator] {} validation error(s) found", errors.size());
        return ValidationResult.invalid(errors);
    }

    // -------------------------------------------------------------------------
    // JSON block extraction (handles LLM markdown fence contamination)
    // -------------------------------------------------------------------------

    /**
     * Strips markdown code fences (```json ... ``` or ``` ... ```) that some
     * LLMs incorrectly include in their output even when instructed not to.
     * Returns the raw string unchanged if no fence is detected.
     */
    static String extractJsonBlock(String raw) {
        String trimmed = raw.strip();

        // Handle ```json ... ``` or ``` ... ```
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline != -1) {
                String body = trimmed.substring(firstNewline + 1);
                int closingFence = body.lastIndexOf("```");
                if (closingFence != -1) {
                    return body.substring(0, closingFence).strip();
                }
            }
        }
        return trimmed;
    }

    // -------------------------------------------------------------------------
    // Nested types
    // -------------------------------------------------------------------------

    /**
     * Result of a validation run.
     *
     * <p>On success, {@link #cleanedJson()} contains the fence-stripped,
     * syntax-validated JSON string ready for {@link ObjectMapper#readValue}.</p>
     */
    public record ValidationResult(
            boolean      isValid,
            List<String> errors,
            String       cleanedJson
    ) {
        static ValidationResult valid(String cleanedJson) {
            return new ValidationResult(true, List.of(), cleanedJson);
        }

        static ValidationResult invalid(List<String> errors) {
            return new ValidationResult(false,
                    Collections.unmodifiableList(errors), null);
        }
    }

    // -------------------------------------------------------------------------
    // Schema descriptor
    // -------------------------------------------------------------------------

    /**
     * Lightweight schema descriptor. Declared once per agent output type and
     * passed to {@link StructuredOutputEnforcer} to drive validation + correction.
     *
     * <p>Instances are immutable and safe to share as static constants.</p>
     */
    public static final class SchemaDescriptor {

        private final List<String>              requiredFields;
        private final Map<String, List<String>> enumFields;
        private final Map<String, double[]>     rangeFields;
        private final String                    schemaHint; // LLM-readable description

        private SchemaDescriptor(SchemaBuilder b) {
            this.requiredFields = Collections.unmodifiableList(
                    new ArrayList<>(b.requiredFields));
            this.enumFields     = Collections.unmodifiableMap(b.enumFields);
            this.rangeFields    = Collections.unmodifiableMap(b.rangeFields);
            this.schemaHint     = b.schemaHint != null ? b.schemaHint : "";
        }

        public List<String>              requiredFields() { return requiredFields; }
        public Map<String, List<String>> enumFields()     { return enumFields; }
        public Map<String, double[]>     rangeFields()    { return rangeFields; }
        public String                    schemaHint()     { return schemaHint; }

        public static SchemaBuilder builder() { return new SchemaBuilder(); }

        public static final class SchemaBuilder {
            private final List<String>              requiredFields = new ArrayList<>();
            private final Map<String, List<String>> enumFields     = new java.util.LinkedHashMap<>();
            private final Map<String, double[]>     rangeFields    = new java.util.LinkedHashMap<>();
            private String                          schemaHint;

            public SchemaBuilder requiredField(String name) {
                requiredFields.add(name); return this;
            }

            public SchemaBuilder enumField(String name, List<String> allowed) {
                enumFields.put(name, Collections.unmodifiableList(new ArrayList<>(allowed)));
                return this;
            }

            public SchemaBuilder rangeField(String name, double min, double max) {
                rangeFields.put(name, new double[]{min, max}); return this;
            }

            /** LLM-readable schema description embedded in correction prompts. */
            public SchemaBuilder schemaHint(String hint) {
                this.schemaHint = hint; return this;
            }

            public SchemaDescriptor build() { return new SchemaDescriptor(this); }
        }
    }
}