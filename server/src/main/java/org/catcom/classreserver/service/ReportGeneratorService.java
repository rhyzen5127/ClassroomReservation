package org.catcom.classreserver.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.AllArgsConstructor;
import org.catcom.classreserver.model.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.Chronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;

import static java.util.Objects.requireNonNullElse;

@Service
public class ReportGeneratorService
{

    private static final Logger LOG = LoggerFactory.getLogger(ReportGeneratorService.class);

    static
    {
        LOG.info("Registering fonts...");
        FontFactory.registerDirectory("src/main/resources/static/fonts");
        LOG.info("Fonts registered.");
    }

    public ByteArrayResource genReport(List<Reservation> toGen)
    {

        var stream = new ByteArrayOutputStream();

        var sarabunNew = "thsarabunnew";

        var document = new Document();

        try
        {
            PdfWriter.getInstance(document, stream);

            document.open();

            var f1 = FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 16);

            var header = new Chunk("Hello World มะลิ", f1);
            document.add(header);

            var h1 = new Paragraph("Lorem Ipsum Dolor Sit Amet");
            h1.setFont(f1);
            h1.setAlignment(Element.ALIGN_CENTER);
            document.add(h1);

            document.add(Chunk.NEWLINE);

            String[] columnTexts = { "อาคาร", "ห้อง", "จองโดย", "รายละเอียด", "วันที่", "เวลา", "หมายเหตุ" };
            float[] columnWidths = { 1, 1, 2, 3, 2, 2, 3 };

            var table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            var fth = FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 12, Font.BOLD);


            for (var col : columnTexts)
            {
                var tc = new PdfPCell();
                tc.setPhrase(new Phrase(new Chunk(col, fth)));
                tc.setHorizontalAlignment(Element.ALIGN_CENTER);
                tc.setVerticalAlignment(Element.ALIGN_CENTER);
                tc.setBorderWidth(1);
                table.addCell(tc);
            }

            var ftr = FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 12);


            for (var reservation : toGen)
            {

                var rowTexts = new String[columnTexts.length];
                rowTexts[0] = reservation.getRoom().getBuilding().getName();
                rowTexts[1] = reservation.getRoom().getName();
                rowTexts[2] = reservation.getOwner().getFullName() + "\n(" + reservation.getOwner().getEmail() + ")";
                rowTexts[3] = requireNonNullElse(reservation.getReserveNote(), "-");

                var locale = new Locale("th", "TH");
                var zoneId = ZoneId.of("Asia/Bangkok");

                var zt1 = reservation.getStartTime().atOffset(ZoneOffset.UTC).atZoneSameInstant(zoneId);
                var zt2 = reservation.getFinishTime().atOffset(ZoneOffset.UTC).atZoneSameInstant(zoneId);

                var dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale).withChronology(ThaiBuddhistChronology.INSTANCE);
                var timeFormat = DateTimeFormatter.ofPattern("HH:mm น.", locale);

                rowTexts[4] = zt1.format(dateFormat);
                rowTexts[5] = zt1.format(timeFormat) + " - " + zt2.format(timeFormat);

                rowTexts[6] = requireNonNullElse(reservation.getReserveNote(), "-");

                for (var row : rowTexts) {
                    var tr = new PdfPCell();
                    tr.setPhrase(new Phrase(new Chunk(row, ftr)));
                    tr.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tr.setVerticalAlignment(Element.ALIGN_CENTER);
                    tr.setBorderWidth(1);
                    table.addCell(tr);
                }
            }

            document.add(table);
            document.close();
            stream.flush();
        }
        catch (IOException | DocumentException e)
        {
            throw new RuntimeException(e);
        }


        return new ByteArrayResource(stream.toByteArray());

    }

}
