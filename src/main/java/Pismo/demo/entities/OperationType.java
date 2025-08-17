package Pismo.demo.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "operation_types")
public class OperationType {

    @Id
    @Column(name = "id")              // SMALLINT in DB
    private Short id;

    @Column(name = "description", nullable = false, unique = true, length = 64)
    private String description;
}

