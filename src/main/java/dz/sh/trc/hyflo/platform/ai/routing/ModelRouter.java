package dz.sh.trc.hyflo.platform.ai.routing;

/**
 * Port for model selection based on request context and routing rules.
 *
 * <p>The router is called by LLM adapters before constructing the final
 * prompt call. It returns a model ID string that the adapter uses to
 * override the default model configured in {@code application.properties}.</p>
 *
 * <h3>Layer position:</h3>
 * <pre>
 *   SpringAiLLMAdapter / OllamaLLMAdapter
 *           ↓ calls
 *   ModelRouter.route(RoutingContext)
 *           ↓ returns
 *   modelId (String)  →  injected into ChatOptions / OllamaOptions
 * </pre>
 *
 * <h3>Contract for implementations:</h3>
 * <ul>
 *   <li>Must always return a non-null, non-blank model ID</li>
 *   <li>Must never throw — return the default model ID on any error</li>
 *   <li>Must be stateless — routing decisions based only on the
 *       {@link RoutingContext} argument, never on mutable state</li>
 * </ul>
 *
 * <p>The default implementation is {@link RuleBasedModelRouter}.</p>
 */
public interface ModelRouter {

    /**
     * Selects the target model ID for the given routing context.
     *
     * @param context the routing context carrying agent type, execution mode,
     *                criticality, and optional routing hint
     * @return non-null, non-blank model ID (e.g. {@code "gpt-4o"},
     *         {@code "gpt-4o-mini"}, {@code "mistral"}, {@code "llama3"})
     */
    String route(RoutingContext context);

    /**
     * Returns the model ID to use when no rule matches and no explicit
     * default is configured.
     *
     * @return fallback model ID — 