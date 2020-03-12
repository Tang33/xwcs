package fast.main.util;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;




public class Excel3 {
	/**
	 * 创建excel文档，2003 限制65535
     * [@param](http://my.oschina.net/u/2303379) list 数据
     * @param keys list中map的key数组集合
     * @param columnNames excel的列名
     * */
    public static Workbook createWorkBook2007(List<Map<String, Object>> list,String []keys,String columnNames[]) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet("001");
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<keys.length;i++){
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
      
        //f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        f.setFontName("Times New Roman");
        
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 15);
        f.setColor(IndexedColors.BLACK.getIndex());
       

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
      
        cs.setBorderLeft(BorderStyle.MEDIUM);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setBorderTop(BorderStyle.MEDIUM);
        cs.setBorderBottom(BorderStyle.MEDIUM);
       // cs.setBorder
       
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(BorderStyle.NONE);
        cs2.setBorderRight(BorderStyle.NONE);
        cs2.setBorderTop(BorderStyle.NONE);
        cs2.setBorderBottom(BorderStyle.NONE);
        cs2.setAlignment(HorizontalAlignment.GENERAL);
        //设置列名
        for(int i=0;i<columnNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        for(int i=0;i<columnNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 0; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow( i+1);
            // 在row行上创建一个方格
            for(short j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
}
	/**
     * 创建excel文档，可以超过100万条
     * [@param](http://my.oschina.net/u/2303379) list 数据
     * @param keys list中map的key数组集合
     * @param columnNames excel的列名
     * */
    public static SXSSFWorkbook  createWorkBook(List<Map<String, Object>> list,String []keys,String columnNames[]) {
        // 创建excel工作簿
    	SXSSFWorkbook  wb = new SXSSFWorkbook (1000);
        // 创建第一个sheet（页），并命名wb.createSheet("001");
    	SXSSFSheet  sheet =  wb.createSheet("001");
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<keys.length;i++){
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        SXSSFRow  row = sheet.createRow((short) 0);
        row.setHeight((short)330);
        // 创建两种单元格格式
        XSSFCellStyle  cs = (XSSFCellStyle) wb.createCellStyle();
        XSSFCellStyle  cs2 = (XSSFCellStyle) wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
     
        //f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        f.setFontName("Times New Roman");
        
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 15);
        f.setColor(IndexedColors.BLACK.getIndex());
       

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
      
        cs.setBorderLeft(BorderStyle.MEDIUM);
        cs.setBorderRight(BorderStyle.MEDIUM);
        cs.setBorderTop(BorderStyle.MEDIUM);
        cs.setBorderBottom(BorderStyle.MEDIUM);
       // cs.setBorder
       
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(BorderStyle.NONE);
        cs2.setBorderRight(BorderStyle.NONE);
        cs2.setBorderTop(BorderStyle.NONE);
        cs2.setBorderBottom(BorderStyle.NONE);
        cs2.setAlignment(HorizontalAlignment.GENERAL);
        //设置列名
        for(int i=0;i<columnNames.length;i++){
        	SXSSFCell  cell =  row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        for(int i=0;i<columnNames.length;i++){
        	SXSSFCell cell =  row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 0; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
        	SXSSFRow row1 = sheet.createRow(i+1);
            // 在row行上创建一个方格
            for(int j=0;j<keys.length;j++){
            	SXSSFCell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
}
}
