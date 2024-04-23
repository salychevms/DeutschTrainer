package de.salychevms.deutschtrainer.DataExchange.Controlles;

import de.salychevms.deutschtrainer.DataExchange.Classes.BasicPairStatisticInfoClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import de.salychevms.deutschtrainer.DataExchange.Classes.UserPairStatisticInfoClass;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DataExchangeInOutController {

    public void writeUserStatisticToExcel(List<UserPairStatisticInfoClass> userStatistics) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet=workbook.createSheet("UserStatistic: "+userStatistics.get(0).getTelegramId());

            Row header=sheet.createRow(0);
            header.createCell(0).setCellValue("telegramId");
            header.createCell(1).setCellValue("userLanguageId");
            header.createCell(2).setCellValue("userDictionaryId");
            header.createCell(3).setCellValue("userStatistics");
            header.createCell(4).setCellValue("statisticId");
            header.createCell(5).setCellValue("pairId");
            header.createCell(6).setCellValue("deId");
            header.createCell(7).setCellValue("ruId");
            header.createCell(8).setCellValue("deWord");
            header.createCell(9).setCellValue("ruWord");
            header.createCell(10).setCellValue("dateAdded");
            header.createCell(11).setCellValue("isNewWord");
            header.createCell(12).setCellValue("lastTraining");
            header.createCell(13).setCellValue("iterationsAll");
            header.createCell(14).setCellValue("iterationsPerDay");
            header.createCell(15).setCellValue("iterationsPerWeek");
            header.createCell(16).setCellValue("iterationsPerMonth");
            header.createCell(17).setCellValue("failsAll");
            header.createCell(18).setCellValue("failsPerDay");
            header.createCell(19).setCellValue("failsPerWeek");
            header.createCell(20).setCellValue("failsPerMonth");

            int rowNum=1;
            for (UserPairStatisticInfoClass userStatisticInfoClass : userStatistics) {
                Row row=sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(userStatisticInfoClass.getTelegramId());
                row.createCell(1).setCellValue(userStatisticInfoClass.getUserLanguageId());
                row.createCell(2).setCellValue(userStatisticInfoClass.getUserDictionaryId());
                row.createCell(3).setCellValue(userStatisticInfoClass.getStatisticId());
                row.createCell(4).setCellValue(userStatisticInfoClass.getPairId());
                row.createCell(5).setCellValue(userStatisticInfoClass.getDeId());
                row.createCell(6).setCellValue(userStatisticInfoClass.getRuId());
                row.createCell(7).setCellValue(userStatisticInfoClass.getDeWord());
                row.createCell(8).setCellValue(userStatisticInfoClass.getRuWord());
                row.createCell(9).setCellValue(userStatisticInfoClass.getDateAdded());
                row.createCell(10).setCellValue(userStatisticInfoClass.isNewWord());
                row.createCell(11).setCellValue(userStatisticInfoClass.getLastTraining());
                row.createCell(12).setCellValue(userStatisticInfoClass.getIterationsAll());
                row.createCell(13).setCellValue(userStatisticInfoClass.getIterationsPerDay());
                row.createCell(14).setCellValue(userStatisticInfoClass.getIterationsPerWeek());
                row.createCell(15).setCellValue(userStatisticInfoClass.getIterationsPerMonth());
                row.createCell(16).setCellValue(userStatisticInfoClass.getFailsAll());
                row.createCell(17).setCellValue(userStatisticInfoClass.getFailsPerDay());
                row.createCell(18).setCellValue(userStatisticInfoClass.getFailsPerWeek());
                row.createCell(19).setCellValue(userStatisticInfoClass.getFailsPerMonth());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"+":="+"hh:mm:ss");
            String date = dateFormat.format(new Date());
            String fileName = date+".xlsx";
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)){
                workbook.write(fileOutputStream);
                System.out.println("Данные успешно записаны в файл " + fileName);
            }  catch (IOException e) {
                System.err.println("Ошибка при записи в файл: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании Excel-документа: " + e.getMessage());
        }
    }

}
