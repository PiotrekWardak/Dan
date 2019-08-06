package KeysValidation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ExcelRead {


    protected static Map<String,String> readRecipients(String path) {

        Map<String, String> nadawcy = new HashMap<>();;
        try {
            FileInputStream excelFile = new FileInputStream(new File(path));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                if (currentRow.getRowNum() > 0) {

                    Iterator<Cell> cellIterator = currentRow.iterator();
                    String nazwa = null;
                    String email = null;

                    while (cellIterator.hasNext()) {

                        Cell currentCell = cellIterator.next();
                        if (currentCell.getColumnIndex() == 1) {
                            nazwa = currentCell.getStringCellValue();
                        } else if (currentCell.getColumnIndex() == 2) {
                            email = currentCell.getStringCellValue();
                        }
                    }
                    nadawcy.put(email, nazwa);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nadawcy;


    }







}
