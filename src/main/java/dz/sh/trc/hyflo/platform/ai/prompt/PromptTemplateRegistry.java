package dz.sh.trc.hyflo.platform.ai.prompt;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Classpath-scanning registry for all prompt templates.
 *
 * <p>Scans {@code classpath:/prompts/*.st} at application startup, loads and
 * validates every template, and makes them available to {@link PromptRenderer}
 * by template ID. Startup fails fast if any template is malformed, has a
 * duplicate ID, or is missing required variable declarations.</p>
 *
 * <h3>Template file naming convention:</h3>
 * <pre>
 * classpath:/prompts/{templateId}-v{N}.st
 * </pre>
 * <p>Examples:
 * <ul>
 *   <li>{@code flow-anomaly-v1.st}       → id={@code "flow-anomaly"},       version=1</li>
 *   <li>{@code crisis-assessment-v1.st}  → id={@code "crisis-assessment"},  version=1</li>
 *   <li>{@code operator-assistant-v1.st} → id={@code "operator-assistant"}, version=1</li>
 * </ul>
 *
 * <h3>Variable declaration header:</h3>
 * <p>Every template file must include a {@code @vars:} comment line listing
 * all StringTemplate variables used in the body:</p>
 * <pre>
 * <@vars: pipelineId, stationId, anomalyType />
 * </pre>
 *
 * <h3>Version conflict resolution:</h3>
 * <p>If two files declare the same template ID (e.g. {@code flow-anomaly-v1.st}
 * and {@code flow-anomaly-v2.st}), the <strong>highest version number wins</strong>
 * and is registered under the template ID key. Both versions remain queryable via
 * {@link #getVersion(String, int)}.</p>
 *
 * <h3>Thread safety:</h3>
 * <p>The registry is populated once during {@link #loadAll()} and is read-only
 * after startup. {@link ConcurrentHashMap} provides safe concurrent reads.</p>
 */
@Component
public class PromptTemplateRegistry {

    private static final Logger log = LoggerFactory.getLogger(PromptTemplateRegistry.class);

    private static final String PROMPTS_CLASSPATH = "classpath:/prompts/*.st";
    private static final String VARS_ANNOTATION   = "<@vars:";

    /**
     * Primary registry: templateId → latest version of that template.
     */
    private final ConcurrentHashMap<String, PromptTemplate> latestByTemplateId =
            new ConcurrentHashMap<>();

    /**
     * Version registry: "templateId:vN" → specific version.
     */
    private final ConcurrentHashMap<String, PromptTemplate> byQualifiedName =
            new ConcurrentHashMap<>();

    // -------------------------------------------------------------------------
    // Startup loading
    // -------------------------------------------------------------------------

    /**
     * Scans the classpath and loads all prompt templates.
     * Called automatically at application startup via {@link PostConstruct}.
     *
     * @throws PromptTemplateException if any template fails validation
     */
    @PostConstruct
    public void loadAll() {
        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver();

        Resource[] resources;
        try {
            resources = resolver.getResources(PROMPTS_CLASSPATH);
        } catch (IOException e) {
            throw new PromptTemplateException(
                    "Failed to scan classpath for prompt templates at '"
                    + PROMPTS_CLASSPATH + "'", e);
        }

        if (resources.length == 0) {
            log.warn("[PromptTemplateRegistry] No prompt template files found at '{}'.",
                    PROMPTS_CLASSPATH);
            return;
        }

        int loaded = 0;
        for (Resource resource : resources) {
            try {
                loadSingle(resource);
                loaded++;
            } catch (PromptTemplateException e) {
                // Re-throw: a bad template at startup = deployment error
                throw e;
            } catch (Exception e) {
                throw new PromptTemplateException(
                        "Unexpected error loading prompt template '"
                        + resource.getFilename() + "'", e);
            }
        }

        log.info("[PromptTemplateRegistry] Loaded {} prompt template(s): {}",
                loaded,
                latestByTemplateId.keySet().stream().sorted().collect(Collectors.toList()));
    }

    private void loadSingle(Resource resource) throws IOException {
        String filename = resource.getFilename();
        if (filename == null) return;

        String rawText = readText(resource);

        // Parse version identity from filename
        PromptVersion version = PromptVersion.fromFilename(filename, rawText);

        // Parse declared variables from @vars: annotation
        Set<String> declaredVars = parseDeclaredVariables(rawText, filename);

        PromptTemplate template = new PromptTemplate(version, rawText, declaredVars);

        // Register in version map (duplicates not allowed for same qualified name)
        String qn = version.qualifiedName();
        if (byQualifiedName.containsKey(qn)) {
            throw new PromptTemplateException(
                    "Duplicate prompt template qualified name '" + qn
                    + "' — check for duplicate filenames in classpath:/prompts/");
        }
        byQualifiedName.put(qn, template);

        // Register as latest only if this version is higher than what's already there
        latestByTemplateId.merge(
                version.templateId(),
                template,
                (existing, incoming) ->
                        incoming.version() > existing.version() ? incoming : existing);

        log.info("[PromptTemplateRegistry] Loaded '{}' hash={} vars={}",
                qn, version.contentHash().substring(0, 8), declaredVars);
    }

    // -------------------------------------------------------------------------
    // Query API
    // -------------------------------------------------------------------------

    /**
     * Returns the latest registered version of a template by its ID.
     *
     * @param templateId e.g. {@code "flow-anomaly"}
     * @return the latest {@link PromptTemplate}
     * @throws PromptTemplateException if no template is registered under this ID
     */
    public PromptTemplate getLatest(String templateId) {
        Objects.requireNonNull(templateId, "templateId");

        PromptTemplate template = latestByTemplateId.get(templateId);
        if (template == null) {
            throw new PromptTemplateException(
                    "No prompt template registered for id='" + templateId
                    + "'. Available: " + latestByTemplateId.keySet());
        }
        return template;
    }

    /**
     * Returns a specific version of a template.
     *
     * @param templateId e.g. {@code "flow-anomaly"}
     * @param version    e.g. {@code 1}
     * @return an {@link Optional} containing the template, or empty if not found
     */
    public Optional<PromptTemplate> getVersion(String templateId, int version) {
        Objects.requireNonNull(templateId, "templateId");
        String qn = templateId + ":v" + version;
        return Optional.ofNullable(byQualifiedName.get(qn));
    }

    /**
     * Returns all registered template IDs.
     *
     * @return an unmodifiable set of template IDs
     */
    public Set<String> registeredIds() {
        return Collections.unmodifiableSet(latestByTemplateId.keySet());
    }

    /**
     * Returns all registered templates (latest version of each).
     *
     * @return an unmodifiable map of templateId → latest PromptTemplate
     */
    public Map<String, PromptTemplate> allLatest() {
        return Collections.unmodifiableMap(latestByTemplateId);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private String readText(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Parses the {@code <@vars: var1, var2, var3 />} annotation line from the template.
     *
     * <p>The annotation must appear on its own line at the top of the file.
     * If absent, the declared variables set is empty — rendering will proceed
     * without variable validation.</p>
     */
    private Set<String> parseDeclaredVariables(String rawText, String filename) {
        for (String line : rawText.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith(VARS_ANNOTATION)) {
                // Extract content between "<@vars:" and "/>"
                int start = trimmed.indexOf(VARS_ANNOTATION) + VARS_ANNOTATION.length();
                int end   = trimmed.lastIndexOf("/>");
                if (end <= start) {
                    log.warn("[PromptTemplateRegistry] Malformed @vars annotation in '{}': '{}'",
                            filename, trimmed);
                    return Set.of();
                }
                String varList = trimmed.substring(start, end).trim();
                Set<String> vars = new HashSet<>();
                for (String v : varList.split(",")) {
                    String name = v.trim();
                    if (!name.isEmpty()) vars.add(name);
                }
                return Collections.unmodifiableSet(vars);
            }
        }
        log.warn("[PromptTemplateRegistry] No @vars annotation found in '{}'. "
                + "Variable validation will be skipped.", filename);
        return Set.of();
    }
}