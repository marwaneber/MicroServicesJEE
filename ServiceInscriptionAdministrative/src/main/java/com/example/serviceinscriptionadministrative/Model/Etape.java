package com.example.serviceinscriptionadministrative.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Etape {
    private Long idEtape;
    private int LibEtape;
    private Boolean OI;
}
