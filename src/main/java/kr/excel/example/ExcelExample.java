package kr.excel.example;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelExample {
    public static void main(String[] args) {
        //엑셀이 없을 경우 대비 예외처리
        try{
            //외부 엑셀 파일을 스트림을 통해서 들고옴
            FileInputStream file = new FileInputStream(new File("/Users/bkwinner/Desktop/Exam/JavaProject0/example.xlsx"));
            //엑셀의 파일에 있는 내용을 그대로 읽어서 workbook을 이용해 메모리에 가상의 엑셀을 만든다
            Workbook workbook = WorkbookFactory.create(file);
            //메모리에 있는 엑셀에서 sheet 뽑아냄
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date dateValue = cell.getDateCellValue();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String formattedDate = dateFormat.format(dateValue);
                                System.out.print(formattedDate + "\t");
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                if (numericValue == Math.floor(numericValue)) {
                                    int intValue = (int) numericValue;
                                    System.out.print(intValue + "\t");
                                } else {
                                    System.out.print(numericValue + "\t");
                                }
                            }
                            break;
                        case STRING:
                            String stringValue = cell.getStringCellValue();
                            System.out.print(stringValue + "\t");
                            break;
                        case BOOLEAN:
                            boolean booleanValue = cell.getBooleanCellValue();
                            System.out.print(booleanValue + "\t");
                            break;
                        case FORMULA:
                            String formulaValue = cell.getCellFormula();
                            System.out.print(formulaValue + "\t");
                            break;
                        case BLANK:
                            System.out.print("\t");
                            break;
                        default:
                            System.out.print("\t");
                            break;
                    }
                }
                System.out.println();
            }
            file.close();//스트림 닫아줌
            System.out.println("엑셀에서 데이터 읽어오기 성공");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
