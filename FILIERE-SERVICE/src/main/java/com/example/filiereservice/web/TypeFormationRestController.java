package com.example.filiereservice.web;

import com.example.filiereservice.Exception.FiliereDefinedException;
import com.example.filiereservice.entities.TypeFormation;
import com.example.filiereservice.repositories.TypeFormationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class TypeFormationRestController {
    private TypeFormationRepository typeFormationRepository;

    public TypeFormationRestController(TypeFormationRepository typeFormationRepository) {
        this.typeFormationRepository = typeFormationRepository;
    }
    @PostMapping(path="/addType")
    @ResponseBody
    public TypeFormation addType(@RequestBody TypeFormation typeFormation){
        return typeFormationRepository.save(typeFormation);
    }
    @GetMapping(path="/typesFormation")
    public List<TypeFormation> typeFormationList(){
        return typeFormationRepository.findAll();
    }
    @PutMapping(path="/modifyTypeFormation/{typeID}")
    @ResponseBody
    public TypeFormation modifyTypeFormation(
            @RequestBody TypeFormation typeFormation,
            @PathVariable Long typeID
    ) throws FiliereDefinedException {
        TypeFormation oldType = typeFormationRepository.findById(typeID).orElse(null);
        if (oldType != null){
            oldType.setLibLong(typeFormation.getLibLong());
            oldType.setLibCourt(typeFormation.getLibCourt());
            oldType.setLibArLong(typeFormation.getLibArLong());
            oldType.setLibArCourt(typeFormation.getLibArCourt());
            return typeFormationRepository.save(oldType);
        } else {
            throw new FiliereDefinedException("Ce type de formation n'existe pas!!");
        }
    }
    @DeleteMapping(path="/typesFormation/{id}/deleteType")
    public void deleteType(@PathVariable Long id) throws FiliereDefinedException {
        boolean modExist = typeFormationRepository.existsById(id);
        if (!modExist){
            throw new FiliereDefinedException("Type Formation with id "+id+"not found");
        }
        typeFormationRepository.deleteById(id);
    }
}
