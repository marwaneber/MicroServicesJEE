package com.example.servicedeliberationordinaire.help;

import com.example.servicedeliberationordinaire.Entities.NoteElementModule;
import com.example.servicedeliberationordinaire.Enumeration.Resultat;
import com.example.servicedeliberationordinaire.Model.Element;
import com.example.servicedeliberationordinaire.Model.Etudiant;
import com.example.servicedeliberationordinaire.Model.Module;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class notesFromExcel {
    public static List<NoteElementModule> noteModuleList(MultipartFile file, Element element,Module module, int anneeU) throws IOException {
        List<NoteElementModule> noteElementModules = new ArrayList<NoteElementModule>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        for(Sheet sheet : workbook) {
            System.out.println("Sheet: " + sheet.getSheetName());
            for(Row row : sheet) {
                Etudiant etudiant = new Etudiant();
                NoteElementModule noteElementModule =new NoteElementModule();
                double noteTPf;
                double noteControlf;
                double noteExam;
                double notefinale;
                //System.out.print("numero de la ligne "+row.getRowNum() + "\t");
                if(row.getRowNum()<6){
                    continue;
                }
                for(Cell cell : row) {
                    CellType cellType = cell.getCellType();
                    //System.out.print("index du collonne+"+ cell.getColumnIndex() + "\t");
                    if(cell.getColumnIndex()==0){
                        if(cellType == CellType.STRING){
                            etudiant.setCNE(cell.getStringCellValue());
                        } else if (cellType == CellType.NUMERIC) {
                            int i = (int) cell.getNumericCellValue();
                            etudiant.setCNE(Integer.toString(i));
                        }
                    }

//                    if(cell.getColumnIndex()==1){
//                        if(cellType == CellType.STRING){
//                            etudiant.setName(cell.getStringCellValue());
//                        }
//                    }
//                    if(cell.getColumnIndex()==2){
//                        if(cellType == CellType.STRING){
//                            etudiant.setLastname(cell.getStringCellValue());
//                        }
//                    }
                    if(cell.getColumnIndex()==3){
                        if(cellType == CellType.NUMERIC){
                            noteElementModule.setNoteControl(cell.getNumericCellValue());
                        }
                    }
                    if(cell.getColumnIndex()==4){
                        if(cellType == CellType.NUMERIC){
                            noteElementModule.setNoteTP(cell.getNumericCellValue());
                        }
                    }
                    if(cell.getColumnIndex()==5){
                        if(cellType == CellType.NUMERIC){
                            noteElementModule.setNoteExam(cell.getNumericCellValue());
                        }
                    }

                    if(cellType == CellType.STRING) {
                        System.out.print(cell.getStringCellValue() + "\t");
                    } else if(cellType == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue() + "\t");
                    }
                }
                System.out.println();

                //----Chaque ligne //

                noteTPf = noteElementModule.getNoteTP()*element.getCoefTP();
                noteControlf = noteElementModule.getNoteControl()*element.getCoefControl();
                noteExam= noteElementModule.getNoteExam()*element.getCoefExam();
                notefinale= (noteExam+noteTPf+noteControlf)/(element.getCoefTP()+element.getCoefControl()+element.getCoefExam());

                noteElementModule.setNoteElement(notefinale);
                noteElementModule.setElement(element);
                noteElementModule.setModule(module);


                noteElementModule.setLadiff(etudiant.getCNE()+module.getIdSemestre()+element.getIdElement()+module.getIdModule()+anneeU);

                if(noteElementModule.getNoteElement()>=element.getNoteMin()){
                    noteElementModule.setValidete(Resultat.V);
                } else  {
                    noteElementModule.setValidete(Resultat.R);
                }
                noteElementModule.setIdModule(module.getIdModule());
                noteElementModule.setIdElement(element.getIdElement());
                noteElementModule.setIdEtudiant(etudiant.getId());
                noteElementModule.setAnneeU(anneeU);            // noteModule.setNomModule(module.getNomModule());
                //----Chaque ligne //
                noteElementModule.setEtudiant(etudiant);



                noteElementModules.add(noteElementModule);

                /** etudiant.setNoteModule(noteModule);**/


                System.out.println("verif  "+etudiant);


            }
        }


        return noteElementModules;
    }

}
