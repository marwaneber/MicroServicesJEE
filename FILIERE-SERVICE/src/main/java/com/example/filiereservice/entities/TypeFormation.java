package com.example.filiereservice.entities;

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
public class TypeFormation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTypeFormation;
    private String LibLong;
    private String LibCourt;
    private String LibArCourt;
    private String LibArLong;

    @OneToMany(mappedBy = "typeFormation")
    private Collection<Filiere> filiereCollection;

}
