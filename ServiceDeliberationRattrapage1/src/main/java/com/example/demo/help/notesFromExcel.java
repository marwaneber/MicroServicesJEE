package com.example.demo.help;

import com.example.demo.entities.NoteElementR;
import com.example.demo.model.Element;
import com.example.demo.model.Etudiant;
import com.example.demo.model.Module;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class notesFromExcel {
    public static List<NoteElementR> noteModuleList(MultipartFile file, Element element, Module module, int anneeU) throws IOException {
        List<NoteElementR> noteElementModules = new ArrayList<NoteElementR>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        for(Sheet sheet : workbook) {
            System.out.println("Sheet: " + sheet.getSheetName());
            for(Row row : sheet) {
                Etudiant etudiant = new Etudiant();
                NoteElementR noteElementModule =new NoteElementR();
                double noteTPf;
                double noteControlf;
                double noteExamR;
                double notefinale;
                if(row.getRowNum()<6){
                    continue;
                }
                for(Cell cell : row) {
                    CellType cellType = cell.getCellType();
                    if(cell.getColumnIndex()==0){
                        if(cellType == CellType.STRING){
                            etudiant.setCNE(cell.getStringCellValue());
                        } else if (cellType == CellType.NUMERIC) {
                            int i = (int) cell.getNumericCellValue();
                            etudiant.setCNE(Integer.toString(i));
                        }
                    }
                    if(cell.getColumnIndex()==3){
                        if(cellType == CellType.NUMERIC){
                            noteElementModule.setNoteCotrole(cell.getNumericCellValue());
                        }
                    }
                    if(cell.getColumnIndex()==4){
                        if(cellType == CellType.NUMERIC){
                            noteElementModule.setNoteTp(cell.getNumericCellValue());
                        }
                    }
                    if(cell.getColumnIndex()==6){
                        if(cellType == CellType.NUMERIC){
                            noteElementModule.setNoteExamR(cell.getNumericCellValue());
                        }
                    }

                    if(cellType == CellType.STRING) {
                        System.out.print(cell.getStringCellValue() + "\t");
                    } else if(cellType == CellType.NUMERIC) {
                        System.out.print(cell.getNumericCellValue() + "\t");
                    }
                }
                //----Chaque ligne //
                noteTPf = noteElementModule.getNoteTp()*element.getCoefTP();
                noteControlf = noteElementModule.getNoteCotrole()*element.getCoefControl();
                noteExamR= noteElementModule.getNoteExamR()*element.getCoefExam();
                notefinale= (noteExamR+noteTPf+noteControlf)/(element.getCoefTP()+element.getCoefControl()+element.getCoefExam());
                noteElementModule.setNoteElementR(notefinale);
                noteElementModule.setElement(element);
                noteElementModule.setModule(module);
                noteElementModule.setLadiff(etudiant.getCNE()+module.getIdSemestre()+element.getIdElement()+module.getIdModule()+anneeU);
                noteElementModule.setIdModule(module.getIdModule());
                noteElementModule.setIdElement(element.getIdElement());
                noteElementModule.setIdEtudiant(etudiant.getId());
                noteElementModule.setAnneeU(anneeU);
                //----Chaque ligne //
                noteElementModule.setEtudiant(etudiant);
                noteElementModules.add(noteElementModule);
                }
        }


        return noteElementModules;
    }

}
