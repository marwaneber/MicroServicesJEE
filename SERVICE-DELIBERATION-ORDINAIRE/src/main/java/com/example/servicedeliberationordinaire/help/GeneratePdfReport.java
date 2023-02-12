package com.example.servicedeliberationordinaire.help;
import com.example.servicedeliberationordinaire.Entities.NoteElementModule;
import com.example.servicedeliberationordinaire.Enumeration.Resultat;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class GeneratePdfReport {
    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

    public static ByteArrayInputStream citiesReport(List<NoteElementModule> notesElementList) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();



        try {

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{8,6,7,5,4,4, 6, 5});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Font headFont1 = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("CNE", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Nom", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Prenom", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Control", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("TP", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Exam", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Note finale", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Resultat", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);


            for (NoteElementModule noteElement : notesElementList) {

//                System.out.println("verifff");
                PdfPCell cell=new PdfPCell();

                cell = new PdfPCell(new Phrase(noteElement.getEtudiant().getCNE()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(noteElement.getEtudiant().getName()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(noteElement.getEtudiant().getLastname()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(noteElement.getNoteControl())));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(noteElement.getNoteTP())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(noteElement.getNoteExam())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(noteElement.getNoteElement())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(noteElement.getValidete())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                if (noteElement.getValidete().equals(Resultat.V)){
                    cell.setBackgroundColor(Color.green);
                }else {
                    cell.setBackgroundColor(Color.orange);
                }
                cell.setPaddingRight(5);
                table.addCell(cell);
            }

            PdfWriter.getInstance(document, out);
            document.open();
            try {
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                Paragraph par = new Paragraph("Meknès, le : "+format.format(date), headFont);
                par.setAlignment(Element.ALIGN_RIGHT);
                document.add(par);
                Paragraph para = new Paragraph("PV Session Ordinaire"+"\n", headFont1);
                para.setAlignment(Element.ALIGN_CENTER);
                document.add(para);
                Phrase titre = new Phrase("\n"+"Année Universitaire : ", headFont);
                document.add(titre);
                int anneeU=notesElementList.get(0).getElement().getAnneeUniversitaire();
                int anneeS = anneeU + 1;
                Phrase titre6 = new Phrase(anneeU+"/"+anneeS+"\n");
                document.add(titre6);
                Phrase titre1 = new Phrase("Filiere : ", headFont);
                document.add(titre1);
                Phrase titre7 = new Phrase(notesElementList.get(0).getElement().getFiliere().getLibLong()+"\n");
                document.add(titre7);
                Phrase titre2 = new Phrase("Semestre : ", headFont);
                document.add(titre2);
                Phrase titre8 = new Phrase(notesElementList.get(0).getElement().getSemestre().getLibCourt()+"\n");
                document.add(titre8);
                Phrase titre3 = new Phrase("Professeur : ", headFont);
                document.add(titre3);
                Phrase titre10 = new Phrase(notesElementList.get(0).getElement().getProfesseur().getNomProf()+" "+notesElementList.get(0).getElement().getProfesseur().getPrenomProf()+"\n");
                document.add(titre10);
                Phrase titre5 = new Phrase("Module : ", headFont);
                document.add(titre5);
                Phrase titre11 = new Phrase(notesElementList.get(0).getElement().getModule().getLibLong()+"\n");
                document.add(titre11);
                Phrase titre4 = new Phrase("ELement : ", headFont);
                Phrase titre12 = new Phrase(notesElementList.get(0).getElement().getLibLong());
                if(titre12.equals(titre11)){
                    document.add(titre4);
                    document.add(titre12);
                }

            } catch (Exception e){
                ///
            }
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
