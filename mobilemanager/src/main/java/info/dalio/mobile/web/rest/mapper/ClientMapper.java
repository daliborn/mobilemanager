package info.dalio.mobile.web.rest.mapper;

import info.dalio.mobile.domain.*;
import info.dalio.mobile.web.rest.dto.ClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper {

    ClientDTO clientToClientDTO(Client client);

    @Mapping(target = "repairs", ignore = true)
    Client clientDTOToClient(ClientDTO clientDTO);
}
