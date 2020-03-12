package fast.main.util;


import java.text.DecimalFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
/**
 * POI操作工具类
 * @author 80605
 */
public class PoiUtil {
	/**
	 * 根据列的类型获取列的值自动转换String类型
	 * @param cell
	 * @return
	 */
	public String getValueByCellType(Cell cell){
		String result="";
		if(cell!=null){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING :
				result=cell.getRichStringCellValue().toString();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				result=String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA :
				result=String.valueOf(cell.getCellFormula().trim());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if(HSSFDateUtil.isCellDateFormatted(cell)){
					result=DateUtil.parseUtilToString(cell.getDateCellValue());
				}else{
					result=new DecimalFormat("#.######").format(cell.getNumericCellValue());
				}
				break;
			}
		}
		return result;
	}
}
