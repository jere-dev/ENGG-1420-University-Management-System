package ca.uoguelph.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.util.Pair;

public class Database {
    private static FileInputStream file;
    private static XSSFWorkbook workbook;
    private static String Wpath;

    public static void loadExcelSheet(String path) {
        try {
            file = new FileInputStream(new File(path));
            workbook = new XSSFWorkbook(file);
            Wpath = path;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editRow(int index, int rowNum, ArrayList<String> rowData) {
        FileOutputStream outputStream;
        XSSFSheet sheet = workbook.getSheetAt(index);
        Row row = sheet.getRow(rowNum);
        int cellIndex = 0;
        for(String string : rowData){
            row.getCell(cellIndex).setCellValue(string);
            cellIndex++;
        }
        try {
            outputStream = new FileOutputStream(Wpath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            loadExcelSheet(Wpath);
            System.out.println("runs");
        } catch (Exception e) {
            System.out.println("ERROR: Couldn't write to file");
        }
    }

    public static ArrayList<Pair<ArrayList<String>, Integer>> loadStrings(int index) {
        ArrayList<Pair<ArrayList<String>, Integer>> returnArrayList = new ArrayList<Pair<ArrayList<String>, Integer>>();
        XSSFSheet sheet = workbook.getSheetAt(index);
        int rowEnd = 0;

        int i = 0;

        int rowNum = 0;
        for (Row row : sheet) {
            rowNum++;
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
                returnArrayList.add(new Pair<ArrayList<String>,Integer>(list, rowNum));
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
