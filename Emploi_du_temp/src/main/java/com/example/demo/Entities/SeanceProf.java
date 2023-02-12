package com.example.demo.Entities;


import com.example.demo.Enumeration.Heure;
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
public class SeanceProf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    Long id;
    Long idModule;
    String nomModule;
    Heure heure;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "joursprofid",nullable = true)
    @ToString.Exclude
    JoursProf joursProf;

}
