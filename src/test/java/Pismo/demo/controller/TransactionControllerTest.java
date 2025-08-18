package Pismo.demo.controller;

import Pismo.demo.entities.Account;
import Pismo.demo.entities.OperationType;
import Pismo.demo.entities.Transaction;
import Pismo.demo.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTransaction() throws Exception {
        // Arrange: build nested entities since Transaction holds objects, not raw IDs
        Account acc = Account.builder()
                .id(1L)
                .documentNumber("12345678900")
                .build();

        OperationType op = OperationType.builder()
                .id((short) 4)
                .description("PAYMENT")
                .build();

        Transaction tx = Transaction.builder()
                .id(1L)
                .account(acc)
                .operationType(op)
                .amount(new BigDecimal("200.50"))
                .eventDate(OffsetDateTime.now())
                .build();

        Mockito.when(transactionService.createTransaction(1L, (short) 4, new BigDecimal("200.50")))
                .thenReturn(tx);

        String payload = """
                {
                  "accountId": 1,
                  "operationTypeId": 4,
                  "amount": 200.50
                }
                """;


        mockMvc.perform(post("/transactions")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isCreated())
                // If your controller returns a DTO with "transactionId":
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.operationTypeId").value(4))
                .andExpect(jsonPath("$.amount").value(200.50));
    }
}
