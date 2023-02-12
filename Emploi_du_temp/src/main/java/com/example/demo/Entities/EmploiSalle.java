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
public class EmploiSalle {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;
        @Column(unique = true,nullable = true)
        Long idSalle;
        String libSalle;

        @OneToMany(mappedBy = "emploiSalle",orphanRemoval = true,cascade = CascadeType.ALL)
        List<JoursSalle> joursSalles;









}
