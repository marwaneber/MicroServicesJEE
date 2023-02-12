package com.example.demo.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Salle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String libsalle;
    String description;

    @OneToMany(mappedBy = "salle",cascade = CascadeType.ALL)
    List<Seance> seances;




}
