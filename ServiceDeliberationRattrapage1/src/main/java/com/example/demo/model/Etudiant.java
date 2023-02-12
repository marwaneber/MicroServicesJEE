package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor @NoArgsConstructor
@ToString
public class Etudiant {
    private Long id;
    private String name;
    private String lastname;
    private String CNE;
    private Long idFiliere;
    private NoteElementO noteElementO;

}
