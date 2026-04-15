package dz.sh.trc.hyflo.platform.ai.prompt;

/**
 * Thrown when a prompt template cannot be loaded, parsed, or rendered.
 *
 * <p>This is an unchecked exception. Template loading failures are fatal —
 * they indicate a misconfigured deployment and are detected at application
 * startup by {@link PromptTemplateRegistry}. Render-time failures indicate
 * a programming error (missing variable) and must not be silently swallowed.</p>
 */
public class PromptTemplateException extends RuntimeException {

    public PromptTemplateException(String message) {
        super(message);
    }

    public PromptTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}