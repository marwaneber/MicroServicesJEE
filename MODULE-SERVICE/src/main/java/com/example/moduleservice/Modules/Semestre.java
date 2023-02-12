package com.example.moduleservice.Modules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Semestre {
    private Long idSemestre;
    private String LibLong;
    private String LibCourt;
    private String LibArCourt;
    private String LibArLong;
    private Long idSession;
}
