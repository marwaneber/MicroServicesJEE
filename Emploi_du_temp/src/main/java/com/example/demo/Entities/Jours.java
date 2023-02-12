package com.example.demo.Entities;

import com.example.demo.Enumeration.Jour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Jours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
      Long idJours;
      Jour jour;

    @OneToMany(mappedBy = "jours",orphanRemoval = true,cascade = CascadeType.ALL)
    List<Seance> seances;



    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name="emploiId")
    @ToString.Exclude
     Emploi emploi;


}
