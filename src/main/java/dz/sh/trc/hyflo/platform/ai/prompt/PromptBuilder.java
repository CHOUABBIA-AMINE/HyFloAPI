package dz.sh.trc.hyflo.platform.ai.prompt;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal prompt assembly utility for Step 5.1 adapters.
 *
 * <p>Produces a plain structured prompt string from a system preamble,
 * a user instruction, and an ordered set of context key/value sections.
 * It is intentionally simple — the full PromptRenderer with Mustache
 * templates is introduced in Step 4.3 and will replace this builder
 * for all prompt-versioned calls.</p>
 *
 * <h3>Output format:</h3>
 * <pre>
 * ### SYSTEM
 * {systemPreamble}
 *
 * ### CONTEXT
 * {key}: {value}
 * {key}: {value}
 *
 * ### INSTRUCTION
 * {instruction}
 * </pre>
 */
public final class PromptBuilder {

    private String systemPreamble = "";
    private String instruction    = "";
    private final LinkedHashMap<String, String> sections = new LinkedHashMap<>();

    private PromptBuilder() {}

    public static PromptBuilder create() { return new PromptBuilder(); }

    public PromptBuilder system(String preamble) {
        this.systemPreamble = preamble != null ? preamble : "";
        return this;
    }

    public PromptBuilder section(String key, String value) {
        if (value != null && !value.isBlank()) sections.put(key, value);
        return this;
    }

    public PromptBuilder sections(Map<String, String> map) {
        if (map != null) map.forEach(this::section);
        return this;
    }

    public PromptBuilder instruction(String instruction) {
        this.instruction = instruction != null ? instruction : "";
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        if (!systemPreamble.isBlank()) {
            sb.append("### SYSTEM\n").append(systemPreamble).append("\n\n");
        }
        if (!sections.isEmpty()) {
            sb.append("### CONTEXT\n");
            sections.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
            sb.append("\n");
        }
        if (!instruction.isBlank()) {
            sb.append("### INSTRUCTION\n").append(instruction);
        }
        return sb.toString().strip();
    }
}