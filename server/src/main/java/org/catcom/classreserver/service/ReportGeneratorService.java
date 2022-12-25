package org.catcom.classreserver.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.opencsv.CSVWriter;
import org.apache.pdfbox.util.Charsets;
import org.catcom.classreserver.model.reservation.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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

    public ByteArrayResource genCSVReport(List<Reservation> toGen, List<String> notes)
    {
        try {

            var byteStream = new ByteArrayOutputStream();
            var byteWriter = new OutputStreamWriter(byteStream, StandardCharsets.UTF_8);

            var csvWriter = new CSVWriter(byteWriter);

            String[] columnTexts = { "อาคาร", "ห้อง", "จองโดย", "รายละเอียด", "วันที่", "เวลา", "หมายเหตุ" };

            csvWriter.writeNext(columnTexts);

            for (int i = 0; i < toGen.size(); i++)
            {
                var reservation = toGen.get(i);
                var rowTexts = new String[columnTexts.length];
                rowTexts[0] = reservation.getRoom().getBuilding().getName();
                rowTexts[1] = reservation.getRoom().getName();
                rowTexts[2] = reservation.getOwner().getFullName() + " (" + reservation.getOwner().getEmail() + ")";
                rowTexts[3] = requireNonNullElse(reservation.getReserveNote(), "-");

                var locale = new Locale("th", "TH");
                var zoneId = ZoneId.of("Asia/Bangkok");

                var zt1 = reservation.getStartTime().atOffset(ZoneOffset.UTC).atZoneSameInstant(zoneId);
                var zt2 = reservation.getFinishTime().atOffset(ZoneOffset.UTC).atZoneSameInstant(zoneId);

                var dateFormat = DateTimeFormatter.ofPattern("dd MM yyyy", locale).withChronology(ThaiBuddhistChronology.INSTANCE);
                var timeFormat = DateTimeFormatter.ofPattern("HH:mm น.", locale);

                rowTexts[4] = zt1.format(dateFormat);
                rowTexts[5] = zt1.format(timeFormat) + " - " + zt2.format(timeFormat);

                rowTexts[6] = requireNonNullElse(notes.get(i), "-");

                csvWriter.writeNext(rowTexts);
            }

            byteWriter.flush();

            return new ByteArrayResource(byteStream.toByteArray());

        } catch (IOException e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "IOException: " + e.getMessage());
        }

    }

    public ByteArrayResource genPDFReport(List<Reservation> toGen, List<String> notes)
    {

        var stream = new ByteArrayOutputStream();

        var sarabunNew = "thsarabunnew";
        var sarabunNewBold = "thsarabunnew-bold";


        var document = new Document();

        try
        {
            PdfWriter.getInstance(document, stream);

            document.open();

            //logo

            var logoImage = Image.getInstance("src/main/resources/static/img/kmitl_logo.png");
            logoImage.scalePercent(7.5f);
            logoImage.setAlignment(Element.ALIGN_CENTER);
            document.add(logoImage);

            // header
            var header = new Paragraph();
            header.setFont(FontFactory.getFont(sarabunNewBold, BaseFont.IDENTITY_H, 18));
            header.setAlignment(Element.ALIGN_CENTER);
            header.add("หลักฐาน การขอใช้ห้องเรียนของบุคลากร คณะวิทยาศาสตร์\n");
            header.add("สถาบันเทคโนโลยีพระจอมเกล้าเจ้าคุณทหารลาดกระบัง\n");
            document.add(header);

            document.add(Chunk.NEWLINE);

            // paragraph 1
            var p1 = new Paragraph();
            p1.setFont(FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 16));
            p1.setAlignment(Element.ALIGN_RIGHT);
            p1.add("วันที่ ............ เดือน ........................ พ.ศ. ............\n");
            p1.add("ชื่อ ....................................................... นามสกุล ...................................................... ตำแหน่ง ..................................................\n");
            p1.add("ภาค/วิชา .................................................. คณะ/สำนัก .................................................... โทร .................................................\n");
            document.add(p1);

            document.add(Chunk.NEWLINE);

            String[] columnTexts = { "อาคาร", "ห้อง", "จองโดย", "รายละเอียด", "วันที่", "เวลา", "หมายเหตุ" };
            float[] columnWidths = { 3, 3, 4, 6, 4, 4, 6 };

            var table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            var fth = FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 12);

            for (var col : columnTexts)
            {
                var tc = new PdfPCell();
                tc.setPhrase(new Phrase(new Chunk(col, fth)));
                tc.setHorizontalAlignment(Element.ALIGN_CENTER);
                tc.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tc.setBorderWidth(1);
                tc.setMinimumHeight(28);
                table.addCell(tc);
            }

            // fill reservation data into rows
            for (int i = 0; i < toGen.size(); i++)
            {

                var reservation = toGen.get(i);

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

                rowTexts[6] = requireNonNullElse(notes.get(i), "-");

                for (var row : rowTexts) {
                    var tr = new PdfPCell();
                    tr.setPhrase(new Phrase(new Chunk(row, fth)));
                    tr.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tr.setBorderWidth(1);
                    tr.setMinimumHeight(28);
                    table.addCell(tr);
                }

            }

            // fill with blank rows
            for (int i = toGen.size(); i < 15; i++) {
                for (var ignored : columnTexts) {
                    var tr = new PdfPCell();
                    tr.setPhrase(new Phrase(new Chunk("", fth)));
                    tr.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tr.setBorderWidth(1);
                    tr.setMinimumHeight(28);
                    table.addCell(tr);
                }
            }

            document.add(table);

            document.add(Chunk.NEWLINE);

            var footer1 = new Paragraph();
            footer1.setFont(FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 16));
            footer1.setAlignment(Element.ALIGN_RIGHT);
            footer1.add("ลงชื่อ ............................................. ผู้รับรอง\n");
            document.add(footer1);

            var footer2 = new Paragraph();
            footer2.setFont(FontFactory.getFont(sarabunNew, BaseFont.IDENTITY_H, 16));
            footer2.setAlignment(Element.ALIGN_LEFT);
            footer2.add("                                                                                                           (...........................................)");
            document.add(footer2);

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
