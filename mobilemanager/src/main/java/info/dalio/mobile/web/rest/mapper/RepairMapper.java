package info.dalio.mobile.web.rest.mapper;

import info.dalio.mobile.domain.*;
import info.dalio.mobile.repository.UserRepository;
import info.dalio.mobile.web.rest.dto.RepairDTO;

import org.mapstruct.*;

import javax.inject.Inject;

/**
 * Mapper for the entity Repair and its DTO RepairDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class RepairMapper {

    @Inject
    private UserRepository userRepository;

    @Mapping(source = "client.name", target = "clientName")
    public abstract RepairDTO repairToRepairDTO(Repair repair);

    @Mapping(source = "clientName", target = "client")
    public abstract Repair repairDTOToRepair(RepairDTO repairDTO);

    public Client clientFromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
