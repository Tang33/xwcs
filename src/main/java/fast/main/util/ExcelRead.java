package fast.main.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.excel.support.ExcelTypeEnum;

public class ExcelRead {
	public static List<Map<String, String>> pomExcel(String fileName, InputStream is,Map<String, Integer> map) {
		//定义一个Workbook
		System.out.println("--------正在解析excel");
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		try {
			Set<String> set = map.keySet();
			List<List<String>> lists=new ArrayList<List<String>>();
			long startMini = System.currentTimeMillis();
			if(fileName.endsWith(".xls")){
				//97以前
				System.out.println("--------正在解析xls文件");
				lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLS);
			}else if(fileName.endsWith(".xlsx")){
				//07以后
				System.out.println("--------正在解析xlsx文件");
				lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLSX);
			}
			System.out.println(lists.size());
			System.out.println("--------解析完成，正在转换excel为list,map");
			for (int i = 0; i < lists.size(); i++) {
				//System.out.println("第"+i+"行");
				
				Map<String, String> rmap=new HashMap<String, String>();
				List<String> zlist=lists.get(i);
				if(i==0) {
					System.out.println("--------正在读取表头");
					for (int j = 0; j < zlist.size(); j++) {
						String result=zlist.get(j);
						if(result==null||result.trim().equals("")) {
							result="";
						}
						for (String key:set) {
							if(key==null||key.trim().equals("")) {
								key="";
							}
							if(result.trim().equals(key.trim())) {
								System.out.println("列："+key+"，在第"+j+"列");
								map.put(key, j);
							}
						}
					}
				}else {
					for (String key:set) {
						Integer no=map.get(key);
						if(no<zlist.size()&&no>-1) {
							String result=zlist.get(no);
							rmap.put(key, result);
						}
					}
				}
				if(rmap==null||rmap.isEmpty()) {
					continue;
				}
				list.add(rmap);
			}
			long endMini = System.currentTimeMillis();
			System.out.println();
			System.out.println("--------数据获取完成:");
			System.out.println("=== ，耗时： " + (endMini - startMini)/1000.0);
		} catch (Exception e) {
			try {
				is.close();
			} catch(IOException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		}
		return list;  
	}  
	
//	public static List<Map<String, String>> pomExcel(String fileName, InputStream is,Map<String, Integer> map) {
//		//定义一个Workbook
//		System.out.println("--------正在解析excel");
//		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
//		Workbook workbook=null;
//
//		try {
//
//			Set<String> set = map.keySet();
//			if(fileName.endsWith(".xls")){
//				//97-07
//				System.out.println("--------正在解析xls文件");
//				workbook=new HSSFWorkbook(is);
//				System.out.println("--------解析完成");
//				//获得sheet
//				Sheet sheet = workbook.getSheetAt(0);
//				System.out.println("--------获取第一个sheet表格");
//				PoiUtil putil=new PoiUtil();
//				//遍历sheet获得每一行
//				for (Row row : sheet) {
//					Map<String, String> rmap=new HashMap<String, String>();
//					//第一行
//					if(row.getRowNum()==0){
//						System.out.println("--------获取第一行表头:");
//						for (int i = 0; i < row.getLastCellNum(); i++) {
//							Cell cell = row.getCell((short) i);
//							String result=putil.getValueByCellType(cell);
//							for (String key:set) {
//								if(result.trim().equals(key.trim())) {
//									System.out.println("列："+key+"，在第"+i+"列");
//									map.put(key, i);
//								}
//							}
//						}
//						System.out.println("");
//						System.out.println("--------正在获取数据:");
//						continue;
//					}
//					for (String key:set) {
//						Integer no=map.get(key);
//						Cell cell = row.getCell(no);
//						String result=putil.getValueByCellType(cell);
//						rmap.put(key, result);
//					}
//					if(rmap==null||rmap.isEmpty()) {
//						continue;
//					}
//					list.add(rmap);
//					//将对象存入集合
//					list.add(rmap);
//				}
//			}else if(fileName.endsWith(".xlsx")){
//				//07以后
//				System.out.println("--------正在解析xlsx文件");
//				long startMini = System.currentTimeMillis();
//				List<List<String>> lists=EasyExcelUtil.readExcelWithStringList(is, ExcelTypeEnum.XLSX);
//				for (int i = 0; i < lists.size(); i++) {
//					Map<String, String> rmap=new HashMap<String, String>();
//					List<String> zlist=lists.get(i);
//					if(i==0) {
//						for (int j = 0; j < zlist.size(); j++) {
//							String result=zlist.get(j);
//							for (String key:set) {
//								if(result.trim().equals(key.trim())) {
//									System.out.println("列："+key+"，在第"+j+"列");
//									map.put(key, j);
//								}
//							}
//						}
//					}else {
//						for (String key:set) {
//							Integer no=map.get(key);
//							String result=zlist.get(no);
//							rmap.put(key, result);
//						}
//					}
//					if(rmap==null||rmap.isEmpty()) {
//						continue;
//					}
//					list.add(rmap);
//				}
//
//				long endMini = System.currentTimeMillis();
//				System.out.println();
//				System.out.println(" take time is " + (endMini - startMini)/1000.0);
//			}
//
//			System.out.println("--------数据获取完成:");
//		} catch (Exception e) {
//			try {
//				is.close();
//			} catch(IOException ee) {
//				ee.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//		return list;  
//	}  
}
