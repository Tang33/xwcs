package fast.main.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;


public class Excel2 {
	/**
     * 创建excel文档，
     * [@param](http://my.oschina.net/u/2303379) list 数据
	 * @param os 
     * @param keys list中map的key数组集合
     * @param columnNames excel的列名
     * */
    public static void createWorkBook(ByteArrayOutputStream os, List<Map<String, Object>> list,String []keys,String columnNames[]) {
    	//导出excle
    	//这里指定不需要表头，因为String通常表头已被包含在data里
        ExcelWriter writer = new ExcelWriter(os,  ExcelTypeEnum.XLSX,false);
		//ExcelWriter writer = new ExcelWriter(os, ExcelTypeEnum.XLSX);
         Sheet sheet1 = new Sheet(1,0);
         sheet1.setSheetName("sheet1");
         List<List<String>> data = new ArrayList<>();
         
         for (int i = 0; i < list.size(); i++) {
            List<String> item = new ArrayList<>();
            for (String string : keys) {
            	 item.add(list.get(i).get(string) == null?" ": list.get(i).get(string).toString());
			}
            data.add(item);
         }
         List<List<String>> head = new ArrayList<List<String>>();
         for (String string : columnNames) {
        	 List<String> headCoulumn1 = new ArrayList<String>();
        	 headCoulumn1.add(string);
        	 head.add(headCoulumn1);
		}
        
         Table table = new Table(1);
         table.setHead(head);
         writer.write0(data, sheet1, table);
         writer.finish();

		
		
		
		
       
}
}
