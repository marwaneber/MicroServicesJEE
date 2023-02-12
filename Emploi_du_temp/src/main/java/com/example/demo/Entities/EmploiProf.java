package com.example.demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmploiProf {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        @Column(unique = true,nullable = true)
        Long idProf;
        String nomProf;
        String prenomProf;
        @OneToMany(mappedBy = "emploiProf",orphanRemoval = true,cascade = CascadeType.ALL)
        List<JoursProf> joursProfs;








}
