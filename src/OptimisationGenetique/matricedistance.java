package OptimisationGenetique;

//package net.codejava.excel;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
 
/*   récupère une matrice de distances de 250 villes du département de l'Yonne.    */


public class matricedistance {
     
    public static int[][] genermat() throws IOException {
    	int[][] res= new int[250][250];
    	int Lcurrent=-1;
        int Ccurrent=1;
        String excelFilePath = "matriceYonne.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
         
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
         
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
             
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                 
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        //System.out.print(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        //System.out.print(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                    	int c= (int)(cell.getNumericCellValue());
                        //System.out.print(c);
                        if(Lcurrent<250&&Ccurrent<250){
                        res[Ccurrent][Lcurrent]=c;
                        res[Lcurrent][Ccurrent]=c;}
                        
                        Ccurrent++;
                        break;
                }
                //System.out.print(" - ");
            }
           // System.out.println();
            Lcurrent++;
            Ccurrent=Lcurrent+1;
        }
         
        workbook.close();
        inputStream.close();
        return res;
    }
 
}
