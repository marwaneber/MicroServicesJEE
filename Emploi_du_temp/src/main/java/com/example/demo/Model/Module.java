package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor

@ToString
public class Module {
    private Long idModule;
    private String LibLong;
    private Long idSemestre;
    private Long idFiliere;


}
