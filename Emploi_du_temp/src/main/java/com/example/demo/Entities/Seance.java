package com.example.demo.Entities;


import com.example.demo.Enumeration.Heure;
import com.example.demo.Enumeration.Jour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    Long idSeance;
    Long idModule;
    String NomModule;
    Heure heure;
    String nomSalle;
    Long idSalle;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "joursid",nullable = true)
    @ToString.Exclude
    Jours jours;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "salleid",nullable = true)
    @ToString.Exclude
    Salle salle;



}
