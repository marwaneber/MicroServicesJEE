package com.example.servicedeliberationordinaire.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Professeur {
    private Long idProf;
    private String nomProf;
    private String prenomProf;
}
