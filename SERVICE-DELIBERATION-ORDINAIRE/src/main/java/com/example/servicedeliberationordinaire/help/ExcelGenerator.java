package com.example.servicedeliberationordinaire.help;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.example.servicedeliberationordinaire.Model.Etudiant;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelGenerator {

    private List <Etudiant> etudiantList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private String fil;
    private String mod;
    private String semes;
    private String elem;
    private String prof;
    private int annee;
    CellStyle unlockedCellStyle;

    public ExcelGenerator(List <Etudiant> etudiantList, String fil, String mod, String semes, String elem, String prof, int annee) {
        this.etudiantList = etudiantList;
        this.annee=annee;
        this.elem=elem;
        this.semes=semes;
        this.prof=prof;
        this.mod=mod;
        this.fil=fil;
        workbook = new XSSFWorkbook();
        unlockedCellStyle = workbook.createCellStyle();
    }
    private void writeHeader() {
        sheet = workbook.createSheet("Etudiant");
        sheet.protectSheet("password");
        Row row = sheet.createRow(5);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setLocked(true);
        style.setTopBorderColor((short) 0);

        createCell(row, 0, "CNE", style);
        createCell(row, 1, "NOM", style);
        createCell(row, 2, "PRENOM", style);
        createCell(row, 3, "CONTROLE", style);
        createCell(row, 4, "TP", style);
        createCell(row, 5, "EXAM", style);
    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
            cell.setCellStyle(unlockedCellStyle);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
            cell.setCellStyle(unlockedCellStyle);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
            cell.setCellStyle(unlockedCellStyle);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
            cell.setCellStyle(unlockedCellStyle);
        }
        cell.setCellStyle(style);
    }
    private void write() {
        int rowCount = 6;
        CellStyle style = workbook.createCellStyle();
        CellStyle style1 = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setLocked(true);
        style1.setIndention((short) 12);
        style1.setFont(font);
        style1.setLocked(false);
        Row row3 = sheet.createRow(0);
        createCell(row3, 0, "Filière: ", style);
        createCell(row3, 1, fil, style);
        createCell(row3, 2, "Année universitaire: ", style);
        createCell(row3, 3,annee+"/"+(annee+1), style);
        Row row1 = sheet.createRow(1);
        createCell(row1, 0, "Semestre: ", style);
        createCell(row1, 1, semes, style);
        createCell(row1, 2, "Module: ", style);
        createCell(row1, 3, mod, style);
        Row row2 = sheet.createRow(2);
        createCell(row2, 0, "Prof: ", style);
        createCell(row2, 1, "Pr."+prof, style);
        if(!elem.equals(mod)){
            createCell(row2, 2, "Element: ", style);
            createCell(row2, 3, elem, style);
        }
        Row row4 = sheet.createRow(3);
        createCell(row4, 0, "Session: ", style);
        createCell(row4, 1, "Normale", style);

        for (Etudiant etudiant: etudiantList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, etudiant.getCNE(), style);
            createCell(row, columnCount++, etudiant.getLastname(), style);
            createCell(row, columnCount++, etudiant.getName(), style);
            createCell(row, columnCount++, "", style1);
            createCell(row, columnCount++, "", style1);
            createCell(row, columnCount++, "", style1);
        }
    }
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }







//    private void writeInfo() {
//        int rowCount = 1;
//        CellStyle style3 = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setFontHeight(14);
//        style3.setFont(font);
//        style3.setLocked(true);
//        Row row = sheet.createRow(0);
//        createCell(row, 0, "Filière: ", style3);
//        createCell(row, 1, etudiant.getCNE(), style3);
//        createCell(row, 2, "Année universitaire: ", style3);
//        createCell(row, 3, etudiant.getCNE(), style3);
//        Row row1 = sheet.createRow(1);
//        createCell(row, 0, "Filière: ", style3);
//        createCell(row, 1, etudiant.getCNE(), style3);
//
//
//
//
//
//
//        for (Etudiant etudiant: etudiantList) {
//            Row row = sheet.createRow(rowCount++);
//            int columnCount = 0;
//            createCell(row, columnCount++, etudiant.getCNE(), style);
//            createCell(row, columnCount++, etudiant.getLastname(), style);
//            createCell(row, columnCount++, etudiant.getName(), style);
//            createCell(row, columnCount++, "", style1);
//            createCell(row, columnCount++, "", style1);
//            createCell(row, columnCount++, "", style1);
//        }
//    }


//    Row row3 = sheet.createRow(0);
//    createCell(row3, 0, "Filière: ", style);
//    createCell(row3, 1, Filiere, style);
//    createCell(row3, 2, "Année universitaire: ", style);
//    createCell(row3, 3,annee, style);
//    Row row1 = sheet.createRow(1);
//    createCell(row1, 0, "Semestre: ", style);
//    createCell(row1, 1, Semestre, style);
//    createCell(row1, 2, "Module: ", style);
//    createCell(row1, 3, Module, style);
//    Row row2 = sheet.createRow(2);
//    createCell(row2, 0, "Prof: ", style);
//    createCell(row2, 1, Prof, style);
//    createCell(row2, 0, "Element: ", style);
//    createCell(row2, 1, Element, style);
}
