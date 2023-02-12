package com.example.filiereservice.Modules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Module {
    private Long idModule;
    private String LibLong;
    private String LibCourt;
    private double NoteMin;
    private int coef;
    private Long idSemestre;
}
