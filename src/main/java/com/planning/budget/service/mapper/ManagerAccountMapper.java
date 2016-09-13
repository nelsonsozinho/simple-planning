package com.planning.budget.service.mapper;

import com.planning.budget.domain.*;
import com.planning.budget.service.dto.ManagerAccountDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ManagerAccount and its DTO ManagerAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManagerAccountMapper {

    ManagerAccountDTO managerAccountToManagerAccountDTO(ManagerAccount managerAccount);

    List<ManagerAccountDTO> managerAccountsToManagerAccountDTOs(List<ManagerAccount> managerAccounts);

    ManagerAccount managerAccountDTOToManagerAccount(ManagerAccountDTO managerAccountDTO);

    List<ManagerAccount> managerAccountDTOsToManagerAccounts(List<ManagerAccountDTO> managerAccountDTOs);
}
