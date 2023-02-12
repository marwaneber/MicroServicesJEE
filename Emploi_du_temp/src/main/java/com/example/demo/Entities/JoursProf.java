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
//@ToString(exclude = "emploi")
public class JoursProf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
      Long id;
      Jour jour;

    @OneToMany(mappedBy = "joursProf",orphanRemoval = true,cascade = CascadeType.ALL)
    List<SeanceProf> seanceProfs;

    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name="emploiProfId", nullable=true)
    @ToString.Exclude
    EmploiProf emploiProf;

}
