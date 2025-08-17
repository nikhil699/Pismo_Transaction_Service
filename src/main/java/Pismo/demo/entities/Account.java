package Pismo.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // maps to BIGSERIAL
    @Column(name = "id")
    private Long id;

    @Column(name = "document_number", nullable = false, unique = true, length = 32)
    private String documentNumber;
}