package Pismo.demo.entities;

import jakarta.persistence.*;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")              // BIGSERIAL in DB
    private Long id;

    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;
}
