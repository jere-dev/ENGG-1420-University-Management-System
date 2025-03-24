package ca.uoguelph.backend;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Database {
    private static FileInputStream file;
    private static XSSFWorkbook workbook;

    public static void loadExcelSheet(String path) {
        try {
            file = new FileInputStream(new File(path));
            workbook = new XSSFWorkbook(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> loadStrings(int index) {
        ArrayList<ArrayList<String>> returnArrayList = new ArrayList<ArrayList<String>>();
        XSSFSheet sheet = workbook.getSheetAt(index);
        int rowEnd = 0;

        int i = 0;

        for (Row row : sheet) {
            if (i == 0) {
                for (Cell cell : sheet.getRow(0)) {
                    rowEnd++;
                    if (cell.getCellType() == CellType.BLANK) {
                        break;
                    }
                }
                i++;
                continue;
            }
            ArrayList<String> list = new ArrayList<String>();
            int j = 0;
            for (Cell cell : row) {
                if (j == rowEnd) {
                    break;
                }
                if (cell.getCellType() == CellType.BLANK) {
                    return returnArrayList;
                }
                cell.setCellType(CellType.STRING);
                list.add(cell.getStringCellValue());
                j++;
            }
            if (0 != list.size()) {
                returnArrayList.add(list);
            }
        }

        return returnArrayList;
    }

    public static void print() {
        XSSFSheet sheet = workbook.getSheetAt(0); // Read first sheet
        for (Row row : sheet) {
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                    case NUMERIC -> System.out.print(cell.getNumericCellValue() + "\t");
                    case BOOLEAN -> System.out.print(cell.getBooleanCellValue() + "\t");
                    case BLANK -> {
                    }
                    default -> System.out.print("UNKNOWN\t");
                }
            }
            // System.out.println();
        }
    }
}
