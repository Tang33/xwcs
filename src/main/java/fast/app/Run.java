package fast.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fast.main.conf.ApiBody;
import fast.main.conf.ApiHead;
import fast.main.util.ClassUtil;
import fast.main.util.Super;

@ApiHead(value="在线编码操作类")
public class Run extends Super{
	@ApiBody(name = "保存", context = "保存数据", type = "Post")
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String tds = getValue(this.getForm().get("tds"));
			if(tds!=null&&tds.trim().equals("0")) {//保存jsp


			}else if(tds!=null&&tds.trim().equals("2")) {//保存java


			}
			return this.toJson("000", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	@ApiBody(name = "初始化tree", context = "初始在线编辑树", type = "Post")
	public String file(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			Set<Class<?>> clazzs = ClassUtil.getClasses("fast.app");
			if (clazzs == null) {
				return this.toJson("001", "没有类！");
			}
			List<String> names=new ArrayList<String>();
			String[] trees=new String[] {"Index","Login","Run"};

			for (Class<?> clazz : clazzs) {
				if (clazz!=null) {
					String classname= clazz.toString();
					classname= classname.substring(classname.lastIndexOf(".")+1,classname.length());
					if(!Arrays.asList(trees).contains(classname)) {
						names.add(classname);	
					}		
				}
			}
			return this.toJson("000", "保存成功！",names);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
}
