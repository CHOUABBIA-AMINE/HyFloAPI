package dz.sh.trc.hyflo.platform.ai.prompt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Immutable version identity for a prompt template.
 *
 * <p>A {@code PromptVersion} uniquely identifies a specific version of a prompt
 * template and carries a SHA-256 content hash for audit integrity. Two prompt
 * templates with the same {@link #templateId} and {@link #version} but different
 * file contents will have different {@link #contentHash} values — making silent
 * drift detectable in logs and audit trails.</p>
 *
 * <h3>Version encoding:</h3>
 * <p>The version number is extracted from the template filename:
 * {@code flow-anomaly-v1.st} → id={@code "flow-anomaly"}, version={@code 1}.
 * This convention keeps versioning visible in the file system and in Git history
 * without requiring a database for the common case.</p>
 *
 * <h3>Audit use:</h3>
 * <p>Every {@link dz.sh.trc.hyflo.platform.ai.agent.AgentResponse} carries the
 * {@link #templateId} and {@link #version} so operators can reproduce exactly
 * which prompt produced a given output by checking out the matching file version.</p>
 */
public final class PromptVersion {

    private final String templateId;
    private final int    version;
    private final String contentHash;   // SHA-256 hex of raw template text
    private final String qualifiedName; // "flow-anomaly:v1"

    private PromptVersion(String templateId, int version, String rawText) {
        this.templateId   = Objects.requireNonNull(templateId, "templateId");
        this.version      = version;
        this.contentHash  = sha256(
                Objects.requireNonNull(rawText, "rawText"));
        this.qualifiedName = templateId + ":v" + version;
    }

    // -------------------------------------------------------------------------
    // Factory
    // -------------------------------------------------------------------------

    /**
     * Creates a {@code PromptVersion} from a filename and the raw template text.
     *
     * <p>Filename convention: {@code {templateId}-v{N}.st}, e.g.
     * {@code flow-anomaly-v1.st} → id={@code "flow-anomaly"}, version={@code 1}.</p>
     *
     * @param filename the classpath resource filename (without path prefix)
     * @param rawText  the raw template text (used for hash computation)
     * @return a new {@code PromptVersion}
     * @throws PromptTemplateException if the filename does not match the naming convention
     */
    public static PromptVersion fromFilename(String filename, String rawText) {
        Objects.requireNonNull(filename, "filename");

        // Strip .st suffix
        String base = filename.endsWith(".st")
                ? filename.substring(0, filename.length() - 3)
                : filename;

        // Extract version: last segment matching -vN
        int vIdx = base.lastIndexOf("-v");
        if (vIdx < 0) {
            throw new PromptTemplateException(
                    "Prompt template filename does not follow naming convention "
                    + "'{id}-v{N}.st': '" + filename + "'");
        }

        String idPart      = base.substring(0, vIdx);
        String versionPart = base.substring(vIdx + 2); // skip "-v"

        int version;
        try {
            version = Integer.parseInt(versionPart);
        } catch (NumberFormatException e) {
            throw new PromptTemplateException(
                    "Version segment in filename '" + filename + "' is not a valid integer: '"
                    + versionPart + "'");
        }
        if (version < 1) {
            throw new PromptTemplateException(
                    "Version number in filename '" + filename + "' must be >= 1, got: " + version);
        }

        return new PromptVersion(idPart, version, rawText);
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /** The template identifier (e.g. {@code "flow-anomaly"}). */
    public String templateId()   { return templateId; }

    /** The version number (e.g. {@code 1}). */
    public int    version()      { return version; }

    /** SHA-256 hex of the raw template text at load time. */
    public String contentHash()  { return contentHash; }

    /** Qualified name in the form {@code "templateId:vN"}, e.g. {@code "flow-anomaly:v1"}. */
    public String qualifiedName(){ return qualifiedName; }

    @Override
    public String toString()     { return qualifiedName + " [" + contentHash.substring(0, 8) + "]"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromptVersion pv)) return false;
        return version == pv.version && templateId.equals(pv.templateId);
    }

    @Override
    public int hashCode() { return Objects.hash(templateId, version); }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}