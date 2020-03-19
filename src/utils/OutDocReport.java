package utils;

import data.AbstrDoc;
import data.InDoc;
import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OutDocReport {

    public static void createReport(ArrayList<data.OutDoc> docs, String fileName) throws SQLException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet();

        Font font = book.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)14);

        CellStyle headStyle = book.createCellStyle();
        headStyle.setBorderTop(BorderStyle.MEDIUM);
        headStyle.setBorderBottom(BorderStyle.MEDIUM);
        headStyle.setBorderLeft(BorderStyle.MEDIUM);
        headStyle.setBorderRight(BorderStyle.MEDIUM);
        headStyle.setFont(font);
        headStyle.setFillForegroundColor(IndexedColors.ORANGE.index);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setWrapText(true);
        headStyle.setVerticalAlignment(VerticalAlignment.TOP);

        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setWrapText(true);

        Row row = sheet.createRow(0);

        Cell docNum = row.createCell(0);
        docNum.setCellValue("Номер, дата регистрации");
        docNum.setCellStyle(headStyle);

        Cell abonent = row.createCell(1);
        abonent.setCellValue("Кому отправлено");
        abonent.setCellStyle(headStyle);

        Cell desc = row.createCell(2);
        desc.setCellValue("Описание");
        desc.setCellStyle(headStyle);

        Cell inNum = row.createCell(4);
        inNum.setCellValue("Номер Вх. док., дата");
        inNum.setCellStyle(headStyle);

        Cell person = row.createCell(3);
        person.setCellValue("Исполнитель");
        person.setCellStyle(headStyle);

        Cell sysTrf = row.createCell(5);
        sysTrf.setCellValue("Как отправлено");
        sysTrf.setCellStyle(headStyle);

        int i_row = 1;

        for (data.OutDoc doc : docs) {
            int row_count = 1;
            ArrayList<data.Abonent> abonents = DBControl.AbonentLink.getOfDoc(doc);
            row_count = Math.max(row_count, abonents.size());

            ArrayList<data.Person> people = DBControl.PersonLink.getOfDoc(doc);
            row_count = Math.max(row_count, people.size());

            ArrayList<data.InDoc> inDocData = new ArrayList<>();
            for (AbstrDoc outDoc : DBControl.DocLink.getInDocs(doc)) {
                inDocData.add((InDoc) outDoc);
            }
            row_count = Math.max(row_count, inDocData.size());

            ArrayList<data.SystemTransfer> transfers = DBControl.SysTrfLink.getOfDoc(doc);
            row_count = Math.max(row_count, transfers.size());

            ArrayList<Row> rows = new ArrayList<>(row_count);

            for (int i = 0; i < row_count; i++) {
                rows.add(sheet.createRow(i_row));
                ++i_row;
            }

            LocalDate date = LocalDate.parse(doc.getDateDc());
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
            Cell cell = rows.get(0).createCell(0);
            cell.setCellValue("№" + doc.getNumDoc() + " от " + dateStr);
            cell.setCellStyle(cellStyle);

            if (abonents.size() == 0) {
                cell = rows.get(0).createCell(1);
                cell.setCellStyle(cellStyle);
            }
            else {
                for (int i = 0; i < abonents.size(); i++) {
                    cell = rows.get(i).createCell(1);
                    cell.setCellValue(abonents.get(i).getNameAbonent());
                    cell.setCellStyle(cellStyle);
                }
            }

            cell = rows.get(0).createCell(2);
            cell.setCellValue(doc.getDescOutDoc());
            cell.setCellStyle(cellStyle);

            if (people.size() == 0) {
                cell = rows.get(0).createCell(3);
                cell.setCellStyle(cellStyle);
            }
            else {
                for (int i = 0; i < people.size(); i++) {
                    cell = rows.get(i).createCell(3);
                    cell.setCellValue(people.get(i).getLastName() + " " + people.get(i).getFirstName() + " " +
                            people.get(i).getPatronPers());
                    cell.setCellStyle(cellStyle);
                }
            }

            if (inDocData.size() == 0) {
                cell = rows.get(0).createCell(4);
                cell.setCellStyle(cellStyle);
            }
            else {
                for (int i = 0; i < inDocData.size(); i++) {
                    date = LocalDate.parse(inDocData.get(i).getDateDc());
                    dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
                    cell = rows.get(i).createCell(4);
                    cell.setCellValue("№" + inDocData.get(i).getCurrNum() + " от " + dateStr);
                    cell.setCellStyle(cellStyle);
                }
            }

            if (transfers.size() == 0) {
                cell = rows.get(0).createCell(5);
                cell.setCellStyle(cellStyle);
            }
            else {
                for (int  i = 0; i < transfers.size(); i++) {
                    cell = rows.get(i).createCell(5);
                    cell.setCellValue(transfers.get(i).getNameSysTrf());
                    cell.setCellStyle(cellStyle);
                }
            }

        }
        for (int sz=0; sz < 7; sz++) {
            sheet.setColumnWidth(sz, 4000);
        }

        sheet.setColumnWidth(0, 2500);
        sheet.setColumnWidth(5, 2500);
        sheet.setColumnWidth(2, 10000);
        sheet.setRepeatingRows(CellRangeAddress.valueOf("1"));
        try {
            book.write(new FileOutputStream(fileName));
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ОШИБКА СОХРАНЕНИЯ");
            alert.setContentText("ПРОИЗОШЛА ОШИБКА ПРИ СОХРАНЕНИИ ОТЧЕТА!");
            alert.showAndWait();
        }

    }
}
