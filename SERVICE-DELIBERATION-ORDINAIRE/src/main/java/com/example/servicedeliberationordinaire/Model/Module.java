package com.example.servicedeliberationordinaire.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Module {
    private Long idModule;
    private String LibLong;
    private String LibCourt;
    private double NoteMin;
    private int coef;
    private Long idFiliere;
    private int nbrElement;
    private Long idProf;
    private Long idSemestre;
    private Collection<Element> elements;
}
