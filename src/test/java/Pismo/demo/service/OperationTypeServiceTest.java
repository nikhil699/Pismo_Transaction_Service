package Pismo.demo.service;



import Pismo.demo.entities.OperationType;
import Pismo.demo.repositories.OperationTypeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OperationTypeServiceTest {

    private final OperationTypeRepository repo = Mockito.mock(OperationTypeRepository.class);
    private final OperationTypeService service = new OperationTypeService(repo);

    @Test
    void testGetByIdSuccess() {
        OperationType op = new OperationType((short) 1, "CASH PURCHASE");
        when(repo.findById((short) 1)).thenReturn(Optional.of(op));

        OperationType found = service.getById((short) 1);

        assertThat(found.getDescription()).isEqualTo("CASH PURCHASE");
    }

}