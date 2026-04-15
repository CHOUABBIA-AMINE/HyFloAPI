package dz.sh.trc.hyflo.intelligence.capability;

/**
 * Root marker interface for all AI capability contracts in the intelligence layer.
 *
 * <p>Every domain-specific AI capability interface must extend this marker.
 * This allows:
 * <ul>
 *   <li>ArchUnit rules to enforce that capability interfaces never import
 *       Spring AI or platform classes directly</li>
 *   <li>Spring conditional wiring to disable all AI beans uniformly
 *       when {@code hyflo.ai.enabled=false}</li>
 *   <li>Consistent discovery of all AI capability contracts in the codebase</li>
 * </ul>
 *
 * <p>No Spring, no Spring AI, no platform imports allowed at this level.
 * Pure Java contract only.</p>
 */
public interface AiCapability {
}