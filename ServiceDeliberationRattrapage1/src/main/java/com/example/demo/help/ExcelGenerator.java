package com.example.demo.help;
import com.example.demo.model.Etudiant;
import com.example.demo.model.NoteElementO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ExcelGenerator {

    private List <Etudiant> etudiantList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private String fil;
    private String mod;
    private String semes;
    private String elem;
    private Long idElement;
    private String prof;
    private int annee;
    CellStyle unlockedCellStyle;


    public ExcelGenerator(List <Etudiant> etudiantList, String fil, String mod, String semes, String elem, String prof, int annee, Long idElement) {
        this.etudiantList = etudiantList;
        this.annee=annee;
        this.elem=elem;
        this.semes=semes;
        this.prof=prof;
        this.mod=mod;
        this.fil=fil;
        this.idElement = idElement;
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
        createCell(row, 6, "EXAM Ratt", style);
    }
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
            cell.setCellStyle(unlockedCellStyle);
        } else if (valueOfCell instanceof Integer) {
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
        createCell(row4, 1, "Rattrapage", style);

        for (Etudiant etudiant: etudiantList) {
            NoteElementO noteElementO = etudiant.getNoteElementO();
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, etudiant.getCNE(), style);
            createCell(row, columnCount++, etudiant.getLastname(), style);
            createCell(row, columnCount++, etudiant.getName(), style);
            createCell(row, columnCount++, noteElementO.getNoteControl(), style);
            createCell(row, columnCount++, noteElementO.getNoteTP(), style);
            createCell(row, columnCount++, noteElementO.getNoteExam(), style);
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
}
