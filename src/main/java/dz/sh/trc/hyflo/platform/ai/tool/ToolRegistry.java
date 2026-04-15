package dz.sh.trc.hyflo.platform.ai.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Central registry for all platform AI tools.
 *
 * <p>At startup, scans the Spring context for all beans annotated with
 * {@link AiTool} and indexes them by name and domain. No tool registration
 * code is required anywhere else.</p>
 *
 * <h3>Lookup contract:</h3>
 * <ul>
 *   <li>{@link #getByName(String)} — exact name match, used by
 *       {@link ToolExecutor} for dispatch</li>
 *   <li>{@link #getByDomain(AiTool.Domain)} — domain-filtered list,
 *       used by agents to build their tool schema for the LLM</li>
 *   <li>{@link #getAll()} — full list, for health checks and admin</li>
 * </ul>
 *
 * <h3>Registration rules (enforced at startup):</h3>
 * <ul>
 *   <li>Duplicate tool names cause an {@link IllegalStateException} —
 *       fail fast is preferred over silent override</li>
 *   <li>A tool bean must implement {@link ToolFunction} — misconfigured
 *       beans cause {@link IllegalArgumentException}</li>
 * </ul>
 */
@Component
public class ToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(ToolRegistry.class);

    private final ApplicationContext applicationContext;

    // Primary index: tool name → (ToolFunction bean + AiTool metadata)
    private final Map<String, RegisteredTool> byName = new LinkedHashMap<>();

    // Secondary index: domain → tool list
    private final Map<AiTool.Domain, List<RegisteredTool>> byDomain =
            new EnumMap<>(AiTool.Domain.class);

    public ToolRegistry(ApplicationContext applicationContext) {
        this.applicationContext = Objects.requireNonNull(applicationContext);
    }

    @PostConstruct
    void discover() {
        // Find all beans whose class carries @AiTool
        Map<String, Object> candidates = applicationContext
                .getBeansWithAnnotation(AiTool.class);

        for (Map.Entry<String, Object> entry : candidates.entrySet()) {
            String beanName = entry.getKey();
            Object bean     = entry.getValue();

            AiTool meta = bean.getClass().getAnnotation(AiTool.class);
            if (meta == null) {
                // Proxy case: check superclass / interfaces
                meta = findAiToolAnnotation(bean.getClass());
            }
            if (meta == null) {
                log.warn("[ToolRegistry] bean '{}' is in @AiTool scan but annotation "
                        + "not found — skipping", beanName);
                continue;
            }

            if (!(bean instanceof ToolFunction<?, ?> fn)) {
                throw new IllegalArgumentException(
                        "[ToolRegistry] bean '" + beanName + "' is annotated @AiTool "
                        + "but does not implement ToolFunction — fix the implementation");
            }

            String toolName = meta.name();
            if (byName.containsKey(toolName)) {
                throw new IllegalStateException(
                        "[ToolRegistry] duplicate tool name '" + toolName + "' — "
                        + "each @AiTool must have a unique name");
            }

            RegisteredTool rt = new RegisteredTool(toolName, meta, fn);
            byName.put(toolName, rt);
            byDomain.computeIfAbsent(meta.domain(), d -> new ArrayList<>()).add(rt);

            log.info("[ToolRegistry] registered tool '{}' domain={} readOnly={}",
                    toolName, meta.domain(), meta.readOnly());
        }

        log.info("[ToolRegistry] discovery complete — {} tool(s) registered", byName.size());
    }

    /**
     * Retrieves a tool by its exact name.
     *
     * @param name the tool name as declared in {@link AiTool#name()}
     * @return the registered tool metadata + function, or empty if not found
     */
    public Optional<RegisteredTool> getByName(String name) {
        return Optional.ofNullable(byName.get(name));
    }

    /**
     * Returns all tools registered for the given domain.
     */
    public List<RegisteredTool> getByDomain(AiTool.Domain domain) {
        return Collections.unmodifiableList(
                byDomain.getOrDefault(domain, List.of()));
    }

    /**
     * Returns all registered tools in registration order.
     */
    public List<RegisteredTool> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(byName.values()));
    }

    /** Total number of registered tools. */
    public int size() { return byName.size(); }

    // -------------------------------------------------------------------------
    // Annotation resolution helper (handles CGLIB proxies)
    // -------------------------------------------------------------------------

    private static AiTool findAiToolAnnotation(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) return null;
        AiTool a = clazz.getAnnotation(AiTool.class);
        if (a != null) return a;
        return findAiToolAnnotation(clazz.getSuperclass());
    }

    // -------------------------------------------------------------------------
    // Value object: registered tool entry
    // -------------------------------------------------------------------------

    /**
     * Immutable snapshot of a registered tool's metadata and function reference.
     */
    public record RegisteredTool(
            String        name,
            AiTool        metadata,
            ToolFunction<?, ?> function
    ) {}
}