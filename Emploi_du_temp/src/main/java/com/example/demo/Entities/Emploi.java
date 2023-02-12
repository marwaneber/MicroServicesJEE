package com.example.demo.Entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Emploi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true,nullable = true)
    Long idSemestre;
    String nomSemestre;
    Long idFiliere;
    String nomFiliere;
    @OneToMany(mappedBy = "emploi",orphanRemoval = true,cascade = CascadeType.PERSIST)
    List<Jours> jours;





}
