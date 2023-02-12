package com.example.serviceDelibrationSemestre.help;

import com.example.serviceDelibrationSemestre.Enumeration.Resultat;
import com.example.serviceDelibrationSemestre.model.NoteModule;
import com.example.serviceDelibrationSemestre.model.ResultatSemestre;
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

    public static ByteArrayInputStream semestreReport( List<ResultatSemestre> resultatSemestres) {
        List<NoteModule> modules = resultatSemestres.get(0).getNoteModules();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            PdfPTable table = new PdfPTable(17);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{11,5,8,5,5,5,5,5,5,4,5,5,5,5,5,6,7});

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

            for (NoteModule module: modules
            ) {
                hcell = new PdfPCell(new Phrase(module.getLibCourt(), headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setBackgroundColor(Color.lightGray);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Année", headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setBackgroundColor(Color.lightGray);
                table.addCell(hcell);

            }


            hcell = new PdfPCell(new Phrase("Note finale", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Résultat", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(Color.lightGray);
            table.addCell(hcell);


            for (ResultatSemestre resultatSemestre : resultatSemestres) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(resultatSemestre.getEtudiantCne()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(resultatSemestre.getEtudiantPrenom()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(resultatSemestre.getEtudiantNom()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                for (NoteModule noteModule: resultatSemestre.getNoteModules()) {
                    double d = (double) Math.round(noteModule.getNoteModuleF() * 100) / 100;
                    cell = new PdfPCell(new Phrase(String.valueOf(d)));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    table.addCell(cell);

                    int anneS = noteModule.getAnneeM()+1;
                    String annee = noteModule.getAnneeM()+"/"+anneS;
                    cell = new PdfPCell(new Phrase(annee));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    table.addCell(cell);


                }
                double d1 = (double) Math.round(resultatSemestre.getMoyenneSemestre() * 100) / 100;
                cell = new PdfPCell(new Phrase(String.valueOf(d1)));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(resultatSemestre.getResultat())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                if (resultatSemestre.getResultat().equals(Resultat.V)){
                    cell.setBackgroundColor(Color.green);
                }else if (resultatSemestre.getResultat().equals(Resultat.VC)){
                    cell.setBackgroundColor(Color.orange);
                }else{
                    cell.setBackgroundColor(Color.red);
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
                Paragraph para = new Paragraph("PV Semestre"+"\n", headFont1);
                para.setAlignment(Element.ALIGN_CENTER);
                document.add(para);
                Phrase titre = new Phrase("\n"+"Année Universitaire : ", headFont);
                document.add(titre);
                int anneeU=resultatSemestres.get(0).getAnnee();
                int anneeS = anneeU + 1;
                Phrase titre6 = new Phrase(anneeU+"/"+anneeS+"\n");
                document.add(titre6);
                Phrase titre1 = new Phrase("Filière : ", headFont);
                document.add(titre1);
                Phrase titre7 = new Phrase(resultatSemestres.get(0).getFiliere()+"\n");
                document.add(titre7);
                Phrase titre2 = new Phrase("Semestre : ", headFont);
                document.add(titre2);
                Phrase titre8 = new Phrase(resultatSemestres.get(0).getSemestreLibCourt()+"\n");
                document.add(titre8);
                Phrase titre3 = new Phrase("Responsable : ", headFont);
                document.add(titre3);
                Phrase titre10 = new Phrase(resultatSemestres.get(0).getResponsable()+"\n");
                document.add(titre10);

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

