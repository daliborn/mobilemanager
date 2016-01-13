package info.dalio.mobile.web.rest.mapper;

import info.dalio.mobile.domain.*;
import info.dalio.mobile.web.rest.dto.RepairDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Repair and its DTO RepairDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RepairMapper {

    @Mapping(source = "client.id", target = "clientId")
    RepairDTO repairToRepairDTO(Repair repair);

    @Mapping(source = "clientId", target = "client")
    Repair repairDTOToRepair(RepairDTO repairDTO);

    default Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
