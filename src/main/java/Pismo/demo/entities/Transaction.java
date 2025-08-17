package Pismo.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")              // BIGSERIAL in DB
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)       // FK → accounts(id)
    private Account account;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_type_id", nullable = false) // FK → operation_types(id)
    private OperationType operationType;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "event_date", nullable = false)
    private OffsetDateTime eventDate;    // DB: TIMESTAMPTZ
}