package dz.sh.trc.hyflo.platform.ai.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight, regex-based JSON field extractor for raw LLM responses.
 *
 * <p>LLMs occasionally wrap JSON in markdown fences or add preamble text.
 * This extractor strips fences, then reads individual scalar fields without
 * a full JSON parser dependency. It is deliberately forgiving — any parse
 * failure returns the supplied default, never throws.</p>
 *
 * <p><strong>Lifecycle:</strong> this class will be deleted when Step 4.3
 * ({@code StructuredOutputConverter}) is merged. All callers will switch
 * to typed POJO binding at that point.</p>
 */
final class SimpleJsonExtractor {

    //private static final Logger log = LoggerFactory.getLogger(SimpleJsonExtractor.class);

    private final String raw;

    SimpleJsonExtractor(String raw) {
        this.raw = stripFences(raw != null ? raw : "");
    }

    /** Extracts a JSON string field by key, returning {@code defaultValue} on failure. */
    String string(String key, String defaultValue) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"");
        Matcher m = p.matcher(raw);
        return m.find() ? m.group(1) : defaultValue;
    }

    /** Extracts a JSON integer field by key. */
    int integer(String key, int defaultValue) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher m = p.matcher(raw);
        if (!m.find()) return defaultValue;
        try { return Integer.parseInt(m.group(1)); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    /** Extracts a JSON double field by key. */
    double decimal(String key, double defaultValue) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+\\.?\\d*)");
        Matcher m = p.matcher(raw);
        if (!m.find()) return defaultValue;
        try { return Double.parseDouble(m.group(1)); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    /** Extracts a JSON boolean field by key. */
    boolean bool(String key, boolean defaultValue) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(true|false)");
        Matcher m = p.matcher(raw);
        if (!m.find()) return defaultValue;
        return Boolean.parseBoolean(m.group(1));
    }

    /** Extracts a JSON array of strings by key. */
    List<String> stringList(String key, List<String> defaultValue) {
        Pattern outer = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\\[([^\\]]*)]");
        Matcher om = outer.matcher(raw);
        if (!om.find()) return defaultValue;
        String arrayContent = om.group(1);
        Pattern item = Pattern.compile("\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"");
        Matcher im = item.matcher(arrayContent);
        List<String> result = new ArrayList<>();
        while (im.find()) result.add(im.group(1));
        return result.isEmpty() ? defaultValue : result;
    }

    private static String stripFences(String s) {
        return s.replaceAll("```(?:json)?\\s*", "").replaceAll("```", "").strip();
    }
}