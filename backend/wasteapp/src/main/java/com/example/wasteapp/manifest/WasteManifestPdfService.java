package com.example.wasteapp.manifest;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * KLASA: WasteManifestPdfService
 * SVRHA: Generira PDF dokument pratećeg lista otpada.
 */
@Service
public class WasteManifestPdfService {

    /**
     * PROGRAMSKI ENTITET: servisna metoda
     * SVRHA: Iz WasteManifest entiteta generira binarni PDF sadržaj.
     */
    public byte[] generatePdf(WasteManifest wm) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, out);

            document.open();

            // Naslov dokumenta
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("PRATEĆI LIST OTPADA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Osnovni podaci o dokumentu
            Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL);

            String formattedDate = wm.getIssuedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));
            document.add(new Paragraph("Broj dokumenta: " + wm.getManifestNumber(), normalFont));
            document.add(new Paragraph("Datum izdavanja: " + wm.getIssuedAt(), normalFont));
            document.add(Chunk.NEWLINE);

            // Tablica s podacima
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            addCell(table, "Korisnik", true);
            addCell(table, wm.getWorkOrder().getUser().getEmail(), false);

            addCell(table, "Vrsta otpada", true);
            addCell(table, wm.getWorkOrder().getWasteType().getName(), false);

            addCell(table, "Šifra otpada", true);
            addCell(table, wm.getWorkOrder().getWasteType().getCode(), false);

            addCell(table, "Lokacija", true);
            addCell(table, wm.getWorkOrder().getCollectionPoint().getName(), false);

            addCell(table, "Grad", true);
            addCell(table, wm.getWorkOrder().getCollectionPoint().getCity(), false);

            addCell(table, "Adresa", true);
            addCell(table, wm.getWorkOrder().getCollectionPoint().getStreet(), false);

            addCell(table, "Količina", true);
            addCell(table, wm.getWorkOrder().getQuantity().toString(), false);

            addCell(table, "Jedinica", true);
            addCell(table, wm.getWorkOrder().getUnit().name(), false);

            addCell(table, "Status naloga", true);
            addCell(table, wm.getWorkOrder().getStatus().name(), false);

            addCell(table, "Napomena", true);
            addCell(table, wm.getNote() == null ? "-" : wm.getNote(), false);

            document.add(table);

            // Podnožje / napomena
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(
                    "Ovaj dokument je generiran iz sustava za evidenciju zbrinjavanja otpada.",
                    normalFont
            ));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException("Greška kod generiranja PDF-a", e);
        }
    }

    /**
     * Pomoćna metoda za dodavanje ćelije u PDF tablicu.
     */
    private void addCell(PdfPTable table, String text, boolean header) {
        Font font = header
                ? new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE)
                : new Font(Font.HELVETICA, 11, Font.NORMAL);

        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "-" : text, font));
        cell.setPadding(8f);

        if (header) {
            cell.setBackgroundColor(Color.DARK_GRAY);
        }

        table.addCell(cell);
    }
}