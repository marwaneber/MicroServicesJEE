package com.example.serviceinscriptionenligne.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Departement {
    private Long id;
    private String LibLong;
    private String LibCourt;
    private String LibArLong;
    private String LibArCourt;
    private Collection<Filiere> filiereLists;
}
