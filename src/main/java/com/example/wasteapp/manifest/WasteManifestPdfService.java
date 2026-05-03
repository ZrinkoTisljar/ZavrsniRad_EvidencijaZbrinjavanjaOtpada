package com.example.wasteapp.manifest;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * KLASA: WasteManifestPdfService
 * SVRHA: Generira PDF dokument pratećeg lista otpada (PL-O) prilagođen zakonskom obrascu.
 */
@Service
public class WasteManifestPdfService {

    public byte[] generatePdf(WasteManifest wm) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Podesili smo margine
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, out);

            document.open();

            // 1. ZAGLAVLJE
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("PRATEĆI LIST ZA OTPAD (PL-O)", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

            String formattedDate = wm.getIssuedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));
            document.add(new Paragraph("BROJ PL-O: " + wm.getManifestNumber(), boldFont));
            document.add(new Paragraph("Datum izdavanja: " + formattedDate, normalFont));
            document.add(Chunk.NEWLINE);

            // 2. BLOK A: POŠILJKA OTPADA
            PdfPTable tableA = createSectionTable("A. POŠILJKA OTPADA");
            addCell(tableA, "Ključni broj otpada:", wm.getWorkOrder().getWasteType().getCode(), normalFont);
            addCell(tableA, "Naziv otpada:", wm.getWorkOrder().getWasteType().getName(), normalFont);
            addCell(tableA, "Procijenjena količina:", wm.getWorkOrder().getQuantity().toString() + " " + wm.getWorkOrder().getUnit().name(), normalFont);
            addCell(tableA, "Napomena / Opis:", wm.getNote() != null ? wm.getNote() : "-", normalFont);//
            document.add(tableA);

            // 3. BLOK B: POŠILJATELJ OTPADA (Iz baze)
            String userName = wm.getWorkOrder().getUser().getFullName();
            if (userName == null || userName.isEmpty()) {
                userName = wm.getWorkOrder().getUser().getCompanyName();
            }
            String oib = wm.getWorkOrder().getUser().getOib();

            PdfPTable tableB = createSectionTable("B. POŠILJATELJ OTPADA");
            addCell(tableB, "Naziv / Ime i prezime:", userName, boldFont);
            addCell(tableB, "OIB:", oib != null ? oib : "Nije upisano", normalFont);
            addCell(tableB, "Adresa polazišta:", wm.getWorkOrder().getPickupAddress() != null ? wm.getWorkOrder().getPickupAddress() : "Nije upisano", normalFont);
            addCell(tableB, "Kontakt osoba (Email/Tel):", wm.getWorkOrder().getUser().getEmail(), normalFont);
            document.add(tableB);

            // 4. BLOK C: PRIJEVOZNIK (Ručni unos na terenu)
            PdfPTable tableC = createSectionTable("C. PRIJEVOZNIK (Ispunjava vozač)");
            addCell(tableC, "Naziv prijevoznika:", "______________________________________", normalFont);
            addCell(tableC, "OIB prijevoznika:", "______________________________________", normalFont);
            addCell(tableC, "Registarska oznaka vozila:", "______________________________________", normalFont);
            addCell(tableC, "Ime i prezime vozača:", "______________________________________", normalFont);
            addCell(tableC, "Datum preuzimanja:", "_______ . _______ . ______________", normalFont);
            document.add(tableC);

            // 5. BLOK D: PRIMATELJ (Ručni unos na deponiju/reciklažnom)
            PdfPTable tableD = createSectionTable("D. PRIMATELJ (Ispunjava osoba na vagi)");
            addCell(tableD, "Naziv odredišta:", "______________________________________", normalFont);
            addCell(tableD, "Točna izvagana količina:", "____________________ kg / t", normalFont);
            addCell(tableD, "Datum vaganja:", "_______ . _______ . ______________", normalFont);
            document.add(tableD);

            document.add(Chunk.NEWLINE);

            // 6. BLOK ZA POTPISE
            PdfPTable signatureTable = new PdfPTable(3);
            signatureTable.setWidthPercentage(100);
            signatureTable.setSpacingBefore(20f);

            PdfPCell sig1 = new PdfPCell(new Phrase("Potpis pošiljatelja (B):\n\n\n_______________________", normalFont));
            sig1.setBorder(Rectangle.NO_BORDER);
            sig1.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell sig2 = new PdfPCell(new Phrase("Potpis vozača (C):\n\n\n_______________________", normalFont));
            sig2.setBorder(Rectangle.NO_BORDER);
            sig2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell sig3 = new PdfPCell(new Phrase("M.P. i potpis primatelja (D):\n\n\n_______________________", normalFont));
            sig3.setBorder(Rectangle.NO_BORDER);
            sig3.setHorizontalAlignment(Element.ALIGN_CENTER);

            signatureTable.addCell(sig1);
            signatureTable.addCell(sig2);
            signatureTable.addCell(sig3);

            document.add(signatureTable);

            // Podnožje
            document.add(Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Ovaj obrazac generiran je iz IT sustava WasteApp i služi kao prateći dokument za legalan prijevoz i zbrinjavanje otpada sukladno Pravilniku.", new Font(Font.HELVETICA, 8, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException("Greška kod generiranja PDF-a", e);
        }
    }

    // Pomoćne metode za urednije crtanje tablica
    private PdfPTable createSectionTable(String title) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setWidths(new float[]{1.3f, 2.7f}); // Omjer kolona

        PdfPCell titleCell = new PdfPCell(new Phrase(title, new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE)));
        titleCell.setColspan(2);
        titleCell.setBackgroundColor(Color.DARK_GRAY);
        titleCell.setPadding(5f);
        table.addCell(titleCell);

        return table;
    }

    private void addCell(PdfPTable table, String label, String value, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setPadding(5f);
        labelCell.setBackgroundColor(new Color(245, 245, 245)); // Svijetlo siva

        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setPadding(5f);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}