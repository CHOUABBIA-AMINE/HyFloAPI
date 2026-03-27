/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineMapper
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Phase A: Fix Structure field access.
 *                             Structure has no getName(); actual fields are
 *                             code, designationAr, designationEn, designationFr.
 *                             owner/manager name now reads getDesignationFr().
 *
 *  @Type       : Class (Utility / Static Mapper)
 *  @Layer      : Mapper
 *  @Package    : Network / Core
 *
 *  Entity source-of-truth:
 *    Infrastructure (parent): code, name, installationDate, commissioningDate,
 *                             decommissioningDate, operationalStatus, owner (Structure)
 *    Pipeline adds           : nominalDiameter, length, nominalThickness,
 *                             nominalRoughness, designMaxServicePressure,
 *                             operationalMaxServicePressure, designMinServicePressure,
 *                             operationalMinServicePressure, designCapacity,
 *                             operationalCapacity, nominalConstructionMaterial,
 *                             nominalExteriorCoating, nominalInteriorCoating,
 *                             pipelineSystem, departureTerminal, arrivalTerminal,
 *                             manager (Structure), vendors
 *    Structure              : code, designationAr, designationEn, designationFr
 *                             — NO getName()
 **/

package dz.sh.trc.hyflo.network.core.mapper;

import dz.sh.trc.hyflo.network.common.model.Alloy;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.core.dto.command.PipelineCommandDTO;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDTO;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import dz.sh.trc.hyflo.network.core.model.Terminal;
import dz.sh.trc.hyflo.general.organization.model.Structure;

public final class PipelineMapper {

    private PipelineMapper() {}

    // =====================================================================
    // entity → PipelineReadDTO
    // =====================================================================

    public static PipelineReadDTO toReadDTO(Pipeline entity) {
        if (entity == null) return null;

        return PipelineReadDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .length(entity.getLength())
                .nominalDiameter(entity.getNominalDiameter())
                .nominalThickness(entity.getNominalThickness())
                .operationalMaxServicePressure(entity.getOperationalMaxServicePressure())
                .operationalMinServicePressure(entity.getOperationalMinServicePressure())
                .operationalCapacity(entity.getOperationalCapacity())
                .installationDate(entity.getInstallationDate())
                .commissioningDate(entity.getCommissioningDate())
                .decommissioningDate(entity.getDecommissioningDate())
                .operationalStatusId(entity.getOperationalStatus() != null
                        ? entity.getOperationalStatus().getId() : null)
                .operationalStatusCode(entity.getOperationalStatus() != null
                        ? entity.getOperationalStatus().getCode() : null)
                .departureTerminalId(entity.getDepartureTerminal() != null
                        ? entity.getDepartureTerminal().getId() : null)
                .departureTerminalCode(entity.getDepartureTerminal() != null
                        ? entity.getDepartureTerminal().getCode() : null)
                .arrivalTerminalId(entity.getArrivalTerminal() != null
                        ? entity.getArrivalTerminal().getId() : null)
                .arrivalTerminalCode(entity.getArrivalTerminal() != null
                        ? entity.getArrivalTerminal().getCode() : null)
                .pipelineSystemId(entity.getPipelineSystem() != null
                        ? entity.getPipelineSystem().getId() : null)
                .pipelineSystemCode(entity.getPipelineSystem() != null
                        ? entity.getPipelineSystem().getCode() : null)
                // Infrastructure.owner (Structure) — Structure has no getName();
                // designationFr is SONATRACH canonical display name.
                .ownerId(entity.getOwner() != null ? entity.getOwner().getId() : null)
                .ownerName(entity.getOwner() != null
                        ? entity.getOwner().getDesignationFr() : null)
                // Pipeline.manager (Structure) — same fix
                .managerId(entity.getManager() != null ? entity.getManager().getId() : null)
                .managerName(entity.getManager() != null
                        ? entity.getManager().getDesignationFr() : null)
                .build();
    }

    // =====================================================================
    // PipelineCommandDTO → new Pipeline entity
    // =====================================================================

    public static Pipeline toEntity(PipelineCommandDTO dto) {
        if (dto == null) return null;

        Pipeline entity = new Pipeline();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setInstallationDate(dto.getInstallationDate());
        entity.setCommissioningDate(dto.getCommissioningDate());
        entity.setDecommissioningDate(dto.getDecommissioningDate());
        entity.setNominalDiameter(dto.getNominalDiameter());
        entity.setLength(dto.getLength());
        entity.setNominalThickness(dto.getNominalThickness());
        entity.setNominalRoughness(dto.getNominalRoughness());
        entity.setDesignMaxServicePressure(dto.getDesignMaxServicePressure());
        entity.setOperationalMaxServicePressure(dto.getOperationalMaxServicePressure());
        entity.setDesignMinServicePressure(dto.getDesignMinServicePressure());
        entity.setOperationalMinServicePressure(dto.getOperationalMinServicePressure());
        entity.setDesignCapacity(dto.getDesignCapacity());
        entity.setOperationalCapacity(dto.getOperationalCapacity());

        if (dto.getOperationalStatusId() != null) {
            OperationalStatus s = new OperationalStatus();
            s.setId(dto.getOperationalStatusId());
            entity.setOperationalStatus(s);
        }
        if (dto.getOwnerId() != null) {
            Structure s = new Structure();
            s.setId(dto.getOwnerId());
            entity.setOwner(s);
        }
        if (dto.getManagerId() != null) {
            Structure s = new Structure();
            s.setId(dto.getManagerId());
            entity.setManager(s);
        }
        if (dto.getPipelineSystemId() != null) {
            PipelineSystem ps = new PipelineSystem();
            ps.setId(dto.getPipelineSystemId());
            entity.setPipelineSystem(ps);
        }
        if (dto.getDepartureTerminalId() != null) {
            Terminal t = new Terminal();
            t.setId(dto.getDepartureTerminalId());
            entity.setDepartureTerminal(t);
        }
        if (dto.getArrivalTerminalId() != null) {
            Terminal t = new Terminal();
            t.setId(dto.getArrivalTerminalId());
            entity.setArrivalTerminal(t);
        }
        if (dto.getNominalConstructionMaterialId() != null) {
            Alloy a = new Alloy();
            a.setId(dto.getNominalConstructionMaterialId());
            entity.setNominalConstructionMaterial(a);
        }
        if (dto.getNominalExteriorCoatingId() != null) {
            Alloy a = new Alloy();
            a.setId(dto.getNominalExteriorCoatingId());
            entity.setNominalExteriorCoating(a);
        }
        if (dto.getNominalInteriorCoatingId() != null) {
            Alloy a = new Alloy();
            a.setId(dto.getNominalInteriorCoatingId());
            entity.setNominalInteriorCoating(a);
        }

        return entity;
    }

    // =====================================================================
    // PipelineCommandDTO → update existing Pipeline entity (patch)
    // =====================================================================

    public static void updateEntity(PipelineCommandDTO dto, Pipeline entity) {
        if (dto == null || entity == null) return;

        if (dto.getCode() != null)                          entity.setCode(dto.getCode());
        if (dto.getName() != null)                          entity.setName(dto.getName());
        if (dto.getInstallationDate() != null)              entity.setInstallationDate(dto.getInstallationDate());
        if (dto.getCommissioningDate() != null)             entity.setCommissioningDate(dto.getCommissioningDate());
        if (dto.getDecommissioningDate() != null)           entity.setDecommissioningDate(dto.getDecommissioningDate());
        if (dto.getNominalDiameter() != null)               entity.setNominalDiameter(dto.getNominalDiameter());
        if (dto.getLength() != null)                        entity.setLength(dto.getLength());
        if (dto.getNominalThickness() != null)              entity.setNominalThickness(dto.getNominalThickness());
        if (dto.getNominalRoughness() != null)              entity.setNominalRoughness(dto.getNominalRoughness());
        if (dto.getDesignMaxServicePressure() != null)      entity.setDesignMaxServicePressure(dto.getDesignMaxServicePressure());
        if (dto.getOperationalMaxServicePressure() != null) entity.setOperationalMaxServicePressure(dto.getOperationalMaxServicePressure());
        if (dto.getDesignMinServicePressure() != null)      entity.setDesignMinServicePressure(dto.getDesignMinServicePressure());
        if (dto.getOperationalMinServicePressure() != null) entity.setOperationalMinServicePressure(dto.getOperationalMinServicePressure());
        if (dto.getDesignCapacity() != null)                entity.setDesignCapacity(dto.getDesignCapacity());
        if (dto.getOperationalCapacity() != null)           entity.setOperationalCapacity(dto.getOperationalCapacity());

        if (dto.getOperationalStatusId() != null) {
            OperationalStatus s = new OperationalStatus();
            s.setId(dto.getOperationalStatusId());
            entity.setOperationalStatus(s);
        }
        if (dto.getManagerId() != null) {
            Structure s = new Structure();
            s.setId(dto.getManagerId());
            entity.setManager(s);
        }
        if (dto.getPipelineSystemId() != null) {
            PipelineSystem ps = new PipelineSystem();
            ps.setId(dto.getPipelineSystemId());
            entity.setPipelineSystem(ps);
        }
    }
}
