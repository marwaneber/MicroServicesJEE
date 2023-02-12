package com.example.servicedeliberationordinaire.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Semestre {
    private Long idSemestre;
    private String LibLong;
    private String LibCourt;
}
