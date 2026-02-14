/**
 *	
 *	@Author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineIntelligenceService
 *	@CreatedOn	: 02-14-2026
 *	@UpdatedOn	: 02-14-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Core
 *
 *  @Description: Intelligence and analytics service for pipeline data.
 *                Aggregates data from pipeline segments to provide comprehensive
 *                insights, computed metrics, and intelligence reports.
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.dto.PipelineSegmentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Intelligence service for pipeline analytics and data aggregation.
 * 
 * This service handles:
 * 1. Aggregating segment-level data (coordinates, facilities, materials)
 * 2. Computing pipeline-wide metrics
 * 3. Analyzing material diversity across segments
 * 4. Generating intelligence reports
 * 
 * IMPORTANT FIELD MAPPINGS:
 * 
 * Pipeline (Nominal/Design):
 * - pipeline.getNominalConstructionMaterialId() - Design material
 * - pipeline.getNominalExteriorCoatingId() - Design exterior coating
 * - pipeline.getNominalInteriorCoatingId() - Design interior coating
 * - pipeline.getDepartureTerminalId() - Pipeline start terminal
 * - pipeline.getArrivalTerminalId() - Pipeline end terminal
 * 
 * Segment (Actual/Implemented):
 * - segment.getConstructionMaterialId() - Actual material used
 * - segment.getExteriorCoatingId() - Actual exterior coating
 * - segment.getInteriorCoatingId() - Actual interior coating
 * - segment.getDepartureFacilityId() - Segment start facility
 * - segment.getArrivalFacilityId() - Segment end facility
 * - segment.getCoordinateIds() - GPS coordinates for segment
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineIntelligenceService {

    private final PipelineService pipelineService;
    private final PipelineSegmentService pipelineSegmentService;

    /**
     * Get comprehensive intelligence report for a pipeline.
     * Includes pipeline data and aggregated segment data.
     * 
     * @param pipelineId Pipeline ID
     * @return Intelligence report map with all computed metrics
     */
    public Map<String, Object> getPipelineIntelligence(Long pipelineId) {
        log.info("📊 Generating intelligence report for pipeline ID: {}", pipelineId);
        
        // Load pipeline and segments
        PipelineDTO pipeline = pipelineService.getById(pipelineId);
        List<PipelineSegmentDTO> segments = pipelineSegmentService.findByPipeline(pipelineId);
        
        log.info("   Pipeline: {} ({})", pipeline.getName(), pipeline.getCode());
        log.info("   Segments: {} segments found", segments.size());
        
        // Build intelligence report
        Map<String, Object> intelligence = new HashMap<>();
        
        // 1. Basic Pipeline Info
        intelligence.put("pipelineId", pipeline.getId());
        intelligence.put("pipelineCode", pipeline.getCode());
        intelligence.put("pipelineName", pipeline.getName());
        
        // 2. Segment Count
        intelligence.put("segmentCount", segments.size());
        
        // 3. Total Coordinates (aggregated from all segments)
        int totalCoordinates = segments.stream()
            .mapToInt(seg -> seg.getCoordinateIds() != null ? seg.getCoordinateIds().size() : 0)
            .sum();
        intelligence.put("totalCoordinates", totalCoordinates);
        log.info("   Total coordinates: {}", totalCoordinates);
        
        // 4. Total Length (from segments)
        double totalLength = segments.stream()
            .mapToDouble(seg -> seg.getLength() != null ? seg.getLength() : 0.0)
            .sum();
        intelligence.put("totalLength", totalLength);
        log.info("   Total length: {} km", totalLength);
        
        // 5. Unique Facilities (segment-level)
        Set<Long> uniqueFacilityIds = new HashSet<>();
        segments.forEach(seg -> {
            if (seg.getDepartureFacilityId() != null) {
                uniqueFacilityIds.add(seg.getDepartureFacilityId());
            }
            if (seg.getArrivalFacilityId() != null) {
                uniqueFacilityIds.add(seg.getArrivalFacilityId());
            }
        });
        intelligence.put("uniqueFacilityCount", uniqueFacilityIds.size());
        intelligence.put("uniqueFacilityIds", uniqueFacilityIds);
        log.info("   Unique facilities: {}", uniqueFacilityIds.size());
        
        // 6. Material Analysis (segment-level actual materials vs pipeline nominal)
        Map<String, Object> materialAnalysis = analyzeMaterials(pipeline, segments);
        intelligence.put("materialAnalysis", materialAnalysis);
        
        // 7. Segment Details Summary
        List<Map<String, Object>> segmentSummaries = segments.stream()
            .map(this::createSegmentSummary)
            .collect(Collectors.toList());
        intelligence.put("segments", segmentSummaries);
        
        log.info("✅ Intelligence report generated successfully");
        return intelligence;
    }

    /**
     * Analyze material diversity between nominal (pipeline) and actual (segments).
     * 
     * CORRECT FIELD USAGE:
     * - Pipeline: getNominalConstructionMaterialId(), getNominalExteriorCoatingId(), getNominalInteriorCoatingId()
     * - Segment: getConstructionMaterialId(), getExteriorCoatingId(), getInteriorCoatingId()
     */
    private Map<String, Object> analyzeMaterials(PipelineDTO pipeline, List<PipelineSegmentDTO> segments) {
        Map<String, Object> analysis = new HashMap<>();
        
        // Nominal materials (from pipeline design)
        analysis.put("nominalConstructionMaterialId", pipeline.getNominalConstructionMaterialId());
        analysis.put("nominalExteriorCoatingId", pipeline.getNominalExteriorCoatingId());
        analysis.put("nominalInteriorCoatingId", pipeline.getNominalInteriorCoatingId());
        
        // Actual materials used in segments
        Set<Long> actualConstructionMaterials = segments.stream()
            .map(PipelineSegmentDTO::getConstructionMaterialId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        
        Set<Long> actualExteriorCoatings = segments.stream()
            .map(PipelineSegmentDTO::getExteriorCoatingId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        
        Set<Long> actualInteriorCoatings = segments.stream()
            .map(PipelineSegmentDTO::getInteriorCoatingId)
            .filter(id -> id != null)
            .collect(Collectors.toSet());
        
        analysis.put("actualConstructionMaterialIds", actualConstructionMaterials);
        analysis.put("actualExteriorCoatingIds", actualExteriorCoatings);
        analysis.put("actualInteriorCoatingIds", actualInteriorCoatings);
        
        // Diversity metrics
        analysis.put("constructionMaterialDiversity", actualConstructionMaterials.size());
        analysis.put("exteriorCoatingDiversity", actualExteriorCoatings.size());
        analysis.put("interiorCoatingDiversity", actualInteriorCoatings.size());
        
        // Check if actual matches nominal
        boolean constructionMatches = actualConstructionMaterials.size() == 1 && 
            actualConstructionMaterials.contains(pipeline.getNominalConstructionMaterialId());
        boolean exteriorMatches = actualExteriorCoatings.size() <= 1 && 
            (actualExteriorCoatings.isEmpty() || actualExteriorCoatings.contains(pipeline.getNominalExteriorCoatingId()));
        boolean interiorMatches = actualInteriorCoatings.size() <= 1 && 
            (actualInteriorCoatings.isEmpty() || actualInteriorCoatings.contains(pipeline.getNominalInteriorCoatingId()));
        
        analysis.put("constructionMaterialMatchesNominal", constructionMatches);
        analysis.put("exteriorCoatingMatchesNominal", exteriorMatches);
        analysis.put("interiorCoatingMatchesNominal", interiorMatches);
        
        log.info("   Material diversity: construction={}, exterior={}, interior={}",
            actualConstructionMaterials.size(),
            actualExteriorCoatings.size(),
            actualInteriorCoatings.size());
        
        return analysis;
    }

    /**
     * Create summary for a single segment.
     * 
     * CORRECT FIELD USAGE:
     * - segment.getCode() - Segment code
     * - segment.getName() - Segment name
     * - segment.getLength() - Segment length
     * - segment.getDiameter() - Segment diameter
     * - segment.getThickness() - Segment thickness
     * - segment.getStartPoint() - Start chainage
     * - segment.getEndPoint() - End chainage
     * - segment.getCoordinateIds() - Set of coordinate IDs
     * - segment.getDepartureFacilityId() - Start facility
     * - segment.getArrivalFacilityId() - End facility
     * - segment.getConstructionMaterialId() - Material used
     */
    private Map<String, Object> createSegmentSummary(PipelineSegmentDTO segment) {
        Map<String, Object> summary = new HashMap<>();
        
        summary.put("id", segment.getId());
        summary.put("code", segment.getCode());
        summary.put("name", segment.getName());
        summary.put("length", segment.getLength());
        summary.put("diameter", segment.getDiameter());
        summary.put("thickness", segment.getThickness());
        summary.put("roughness", segment.getRoughness());
        summary.put("startPoint", segment.getStartPoint());
        summary.put("endPoint", segment.getEndPoint());
        
        // Segment-specific IDs
        summary.put("departureFacilityId", segment.getDepartureFacilityId());
        summary.put("arrivalFacilityId", segment.getArrivalFacilityId());
        summary.put("constructionMaterialId", segment.getConstructionMaterialId());
        summary.put("exteriorCoatingId", segment.getExteriorCoatingId());
        summary.put("interiorCoatingId", segment.getInteriorCoatingId());
        
        // Coordinate count for this segment
        int coordinateCount = segment.getCoordinateIds() != null ? segment.getCoordinateIds().size() : 0;
        summary.put("coordinateCount", coordinateCount);
        
        return summary;
    }

    /**
     * Get all coordinates for a pipeline (aggregated from segments).
     * Coordinates belong to segments, not directly to pipeline.
     * 
     * @param pipelineId Pipeline ID
     * @return Set of all coordinate IDs across all segments
     */
    public Set<Long> getAllPipelineCoordinates(Long pipelineId) {
        log.info("📍 Getting all coordinates for pipeline ID: {}", pipelineId);
        
        List<PipelineSegmentDTO> segments = pipelineSegmentService.findByPipeline(pipelineId);
        
        Set<Long> allCoordinates = segments.stream()
            .flatMap(seg -> seg.getCoordinateIds() != null ? seg.getCoordinateIds().stream() : java.util.stream.Stream.empty())
            .collect(Collectors.toSet());
        
        log.info("   Found {} unique coordinates across {} segments", allCoordinates.size(), segments.size());
        return allCoordinates;
    }

    /**
     * Get all unique facilities for a pipeline (aggregated from segments).
     * Facilities belong to segments (departure/arrival), not directly to pipeline.
     * Note: Pipeline has departure/arrival TERMINALS, segments have departure/arrival FACILITIES.
     * 
     * @param pipelineId Pipeline ID
     * @return Set of all unique facility IDs
     */
    public Set<Long> getAllPipelineFacilities(Long pipelineId) {
        log.info("🏭 Getting all facilities for pipeline ID: {}", pipelineId);
        
        List<PipelineSegmentDTO> segments = pipelineSegmentService.findByPipeline(pipelineId);
        
        Set<Long> allFacilities = new HashSet<>();
        segments.forEach(seg -> {
            if (seg.getDepartureFacilityId() != null) {
                allFacilities.add(seg.getDepartureFacilityId());
            }
            if (seg.getArrivalFacilityId() != null) {
                allFacilities.add(seg.getArrivalFacilityId());
            }
        });
        
        log.info("   Found {} unique facilities across {} segments", allFacilities.size(), segments.size());
        return allFacilities;
    }

    /**
     * Compute actual pipeline length from segments.
     * Pipeline.length may be nominal; actual length is sum of segment lengths.
     * 
     * @param pipelineId Pipeline ID
     * @return Total length in kilometers
     */
    public double computeActualPipelineLength(Long pipelineId) {
        log.info("📏 Computing actual length for pipeline ID: {}", pipelineId);
        
        List<PipelineSegmentDTO> segments = pipelineSegmentService.findByPipeline(pipelineId);
        
        double totalLength = segments.stream()
            .mapToDouble(seg -> seg.getLength() != null ? seg.getLength() : 0.0)
            .sum();
        
        log.info("   Actual length: {} km ({} segments)", totalLength, segments.size());
        return totalLength;
    }

    /**
     * Get material usage statistics across all segments.
     * Shows which materials are actually used and in how many segments.
     * 
     * @param pipelineId Pipeline ID
     * @return Map of material ID to usage count
     */
    public Map<Long, Long> getMaterialUsageStatistics(Long pipelineId) {
        log.info("🔧 Getting material usage statistics for pipeline ID: {}", pipelineId);
        
        List<PipelineSegmentDTO> segments = pipelineSegmentService.findByPipeline(pipelineId);
        
        // Count usage of each construction material
        Map<Long, Long> materialUsage = segments.stream()
            .map(PipelineSegmentDTO::getConstructionMaterialId)
            .filter(id -> id != null)
            .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
        
        log.info("   Material diversity: {} different materials used", materialUsage.size());
        materialUsage.forEach((materialId, count) -> 
            log.info("      Material ID {}: used in {} segments", materialId, count)
        );
        
        return materialUsage;
    }
}
