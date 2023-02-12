package com.example.serviceDeliberationAnnuelle.help;

import com.example.serviceDeliberationAnnuelle.Enumeration.Resultat;
import com.example.serviceDeliberationAnnuelle.model.NoteSemestre;
import com.example.serviceDeliberationAnnuelle.model.ResultatEtape;
import com.lowagie.text.Font;
import com.lowagie.text.*;
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

    public static ByteArrayInputStream etapeReport(List<ResultatEtape> resultatEtapes) {
        List<NoteSemestre> semestres = resultatEtapes.get(0).getNoteSemestres();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
//            table.setWidths(new int[]{});
            table.setWidths(new int[]{10,5,7,5,8,5,8,6,7});

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

            for (NoteSemestre semestre: semestres
            ) {
                hcell = new PdfPCell(new Phrase(semestre.getLibCourt(), headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hcell.setBackgroundColor(Color.lightGray);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Annee", headFont));
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


            for (ResultatEtape resultatEtape : resultatEtapes) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(resultatEtape.getEtudiantCne()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(resultatEtape.getEtudiantPrenom()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(resultatEtape.getEtudiantNom()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                for (NoteSemestre noteSemestre: resultatEtape.getNoteSemestres()) {
                    double d = (double) Math.round(noteSemestre.getNoteSemestreF() * 100) / 100;
                    cell = new PdfPCell(new Phrase(String.valueOf(d)));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    table.addCell(cell);

                    int anneS = noteSemestre.getAnneeS()+1;
                    String annee = noteSemestre.getAnneeS()+"/"+anneS;
                    cell = new PdfPCell(new Phrase(annee));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPaddingRight(5);
                    table.addCell(cell);
                }
                double d1 = (double) Math.round(resultatEtape.getMoyenneEtape() * 100) / 100;
                cell = new PdfPCell(new Phrase(String.valueOf(d1)));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(resultatEtape.getResultat())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                if (resultatEtape.getResultat().equals(Resultat.V)){
                    cell.setBackgroundColor(Color.green);
                }else if (resultatEtape.getResultat().equals(Resultat.VC)){
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
                Paragraph para = new Paragraph("PV Annee :"+resultatEtapes.get(0).getEtapeLib()+"\n", headFont1);
                para.setAlignment(Element.ALIGN_CENTER);
                document.add(para);
                Phrase titre = new Phrase("\n"+"Année Universitaire : ", headFont);
                document.add(titre);
                int anneeU=resultatEtapes.get(0).getAnnee();
                int anneeS = anneeU + 1;
                Phrase titre6 = new Phrase(anneeU+"/"+anneeS+"\n");
                document.add(titre6);
                Phrase titre1 = new Phrase("Filière : ", headFont);
                document.add(titre1);
                Phrase titre7 = new Phrase(resultatEtapes.get(0).getFiliere()+"\n");
                document.add(titre7);
                Phrase titre2 = new Phrase("Année : ", headFont);
                document.add(titre2);
                Phrase titre8 = new Phrase(resultatEtapes.get(0).getEtapeLib()+"\n");
                document.add(titre8);
                Phrase titre3 = new Phrase("Responsable : ", headFont);
                document.add(titre3);
                Phrase titre10 = new Phrase(resultatEtapes.get(0).getResponsable()+"\n");
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

