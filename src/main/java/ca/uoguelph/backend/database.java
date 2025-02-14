package ca.uoguelph.backend;


import java.io.File; 
import java.io.FileInputStream; 
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  

public class database {
    private static FileInputStream file;
    private static XSSFWorkbook workbook;

    public database(String path){
        try {
            file = new FileInputStream(new File(path));
            workbook = new XSSFWorkbook(file); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void print(){
        XSSFSheet sheet = workbook.getSheetAt(0);  // Read first sheet
        for(Row row : sheet){
            for(Cell cell: row){
                switch (cell.getCellType()) {
                    case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                    case NUMERIC -> System.out.print(cell.getNumericCellValue() + "\t");
                    case BOOLEAN -> System.out.print(cell.getBooleanCellValue() + "\t");
                    case BLANK -> {}
                    default -> System.out.print("UNKNOWN\t");
                }
            }
            // System.out.println();
        }
    }
}
