package com.example.filiereservice.entities;
import com.example.filiereservice.Modules.Module;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.Collection;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Semestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSemestre;
    private String LibLong;
    private String LibCourt;
    private String LibArCourt;
    private String LibArLong;
    private float CofSem;
    private float notMin;
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Etape etape;

    @Transient
    private Collection<Module> modules;
}