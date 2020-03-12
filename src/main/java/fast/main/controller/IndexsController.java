package fast.main.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.conf.ApiBody;
import fast.main.conf.ApiHead;
import fast.main.conf.SpringBeanProxy;
import fast.main.service.BaseService;
import fast.main.util.Super;


@Controller
@RequestMapping("indexs")
public class IndexsController extends Super {
	
	@Autowired
	BaseService bs;
	
	
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name="初始化主页面",context="根据session初始化整个系统页面",type="Post")
	public String init(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		try {
			if (request.getSession().getAttribute("user") != null) {
				return "index";
			} else {
				return "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "index";
		}
	}
	
	
	@RequestMapping(value="/load.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name="加载功能树",context="加载主页面左侧功能菜单",type="Post")
	public String load(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		try {
			Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
			String gndmpar=request.getParameter("gndm");
			String sql="select * from fast_gncd where 1=1 and (-sjgndm=? -) and yxbz='Y' order by px";
			sql = this.getSql2(sql, new Object[] {gndmpar});
			List<Map<String, Object>> list = bs.query(sql);
			String dwid=getValue(user.get("DWID"));
			if (!dwid.equals("00")) {
				String sqlcd="select * from fast_qxgl where dwid='"+dwid+"'";
				List<Map<String, Object>> listcd = bs.query(sqlcd);
				for (int i = 0; i < list.size(); i++) {
					String cdid=getValue(list.get(i).get("UUID"));
					boolean cz=false;
					for (int j = 0; j < listcd.size(); j++) {
						if (getValue(listcd.get(j).get("CDID")).equals(cdid)) {
							cz=true;
						}
					}
					if (!cz) {
						list.remove(i);
						i--;
					}
				}
			}
			
			request.setAttribute("list", list);
			return this.toJson("000", "查询成功！", list, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("001", "查询失败！", 0);
		}

	}
	
	
	@RequestMapping(value="/codeList.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name="API初始化",context="初始化API文档",type="Post")
	public String codeList(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		List<HashMap<String, Object>> codeList=new ArrayList<HashMap<String,Object>>();
		try {
			String packageName = "fast/app"; 
			List<String> list=SpringBeanProxy.getClassName(packageName,true);
			for (int i = 0; i < list.size(); i++) {
				HashMap<String,Object> map=new HashMap<String,Object>();
				//类名，包含包名
				String claName=list.get(i);
				//反射类
				Class<?> cla= Class.forName(claName);
				ApiHead anno = (ApiHead) cla.getAnnotation(ApiHead.class);
				String vString ="";
				if(anno!=null) {
					vString=anno.value();
				}
				//获取类中的方法
				Method[] name = cla.getDeclaredMethods();
				List<HashMap<String, String>> metList=new ArrayList<HashMap<String,String>>();
				//循环方法，判断方法是否包含注解
				for (Method str : name) {
					HashMap<String, String> metMap=new HashMap<String, String>();
					// 判断是否方法上存在注解
					boolean annotationPresent = str.isAnnotationPresent(ApiBody.class);
					if (annotationPresent) {
						// 获取自定义注解对象
						ApiBody methodAnno = str.getAnnotation(ApiBody.class);
						// 根据对象获取注解值
						String value = methodAnno.name();
						String descript = methodAnno.context();
						String type = methodAnno.type();
						String key=str.getName();
						metMap.put("key", key);
						metMap.put("value", value);
						metMap.put("descript", descript);
						metMap.put("type", type);
						metList.add(metMap);
					}
				}
				map.put("claName", claName);
				map.put("value", vString);
				map.put("metList", metList);
				codeList.add(map);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this.toJson("000", "查询成功！", codeList);
	}

	@RequestMapping(value="/getPage.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name="加载页面数据",context="初始化页面代码信息",type="Post")
	public String getPage(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		String url=request.getParameter("url");
		String type=request.getParameter("type");

		String str="";
		String file_path="C:\\Users\\tenviy\\git";
		if(url!=null&&!url.equals("")) {
			if(type.equals("java")) {
				file_path=file_path+"/App/src/main/java/fast/app/"+url+".java";
			}else if(type.equals("jsp")) {
				file_path=file_path+"/App/src/main/webapp/jsp/xtgl/"+url+".jsp";
			}else if(type.equals("js")) {
				file_path=file_path+"/App/src/main/webapp/js/xtgl/"+url+".js";
			}else if(type.equals("css")) {
				file_path=file_path+"/App/src/main/webapp/css/xtgl/"+url+".css";
			}

			str=readToString(file_path);
			if(str==null||str.trim().equals("null")||str.trim().equals("")) {
				str="";
			}
			if(str.equals("")) {
				File file=new File(file_path);
				try {
					if(!file.exists()) {
						file.createNewFile();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return this.toJson("006", "无数据！", str);
			}
		}
		return this.toJson("000", "查询成功！", str);
	}

	public String readToString(String fileName) {  
		String encoding = "UTF-8";  
		File file = new File(fileName);  
		if(file.exists()) {
			Long filelength = file.length();  
			byte[] filecontent = new byte[filelength.intValue()];  
			try {  
				FileInputStream in = new FileInputStream(file);  
				in.read(filecontent);  
				in.close();  
			} catch (FileNotFoundException e) {  
				e.printStackTrace();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
			try {  
				return new String(filecontent, encoding);  
			} catch (UnsupportedEncodingException e) {  
				System.err.println("The OS does not support " + encoding);  
				e.printStackTrace();  
				return null;  
			}  
		}else {
			return null;
		}

	}
	public void writeFile(String fileName, String content) {   
		try {   
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
			File file1 = new File(fileName);
			Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file1), "UTF-8"));
			writer.write(content);   
			writer.close();   
		} catch (IOException e) {   
			e.printStackTrace();   
		}   
	}  


	@RequestMapping(value="/save.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name="保存数据",context="保存在线代码",type="Post")
	public String save(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) throws IOException {
		String url=request.getParameter("url");
		String type=request.getParameter("type");
		String file_path="C:\\Users\\tenviy\\git";
		
		if(url!=null&&!url.equals("")) {
			if(type.equals("java")) {
				file_path=file_path+"/App/src/main/java/fast/app/"+url+".java";
				String str=request.getParameter("str");
				writeFile(file_path,str);
				System.out.println("----------:"+url+".java文件保存成功");
				System.out.println("----------路径:"+file_path);
			}else if(type.equals("jsp")) {
				String file_path1=file_path+"/App/src/main/webapp/jsp/xtgl/"+url+".jsp";
				String file_path2=file_path+"/App/src/main/webapp/js/xtgl/"+url+".js";
				String file_path3=file_path+"/App/src/main/webapp/css/xtgl/"+url+".css";
				
				String str_jsp=request.getParameter("str_jsp");
				String str_js=request.getParameter("str_js");
				String str_css=request.getParameter("str_css");
				str_jsp=new String(str_jsp.getBytes());
				str_js=new String(str_js.getBytes());
				str_css=new String(str_css.getBytes());
				writeFile(file_path1,str_jsp);
				writeFile(file_path2,str_js);
				writeFile(file_path3,str_css);
				System.out.println("----------:"+url+".jsp(js、css)文件保存成功");
				System.out.println("----------路径jsp:"+file_path1);
				System.out.println("----------路径js:"+file_path2);
				System.out.println("----------路径css:"+file_path3);
				
			}
		}
		return this.toJson("000", "写入成功！");
	}
	
	
	//实时查询QYQC表中STATUS = 0的数据
	@RequestMapping(value="/queryQYQC.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryQYQC(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> rmap) {
		try {
			//获取当前用户的街道代码
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			List<Map<String, Object>> list = null;
			if (!ssdw_dm.equals("00")) {
				String sqlString = "SELECT * from FAST_QYQC WHERE STATUS = '0' and SSJD ='"+ssdw_dm+"'ORDER BY 'ID' ASC";
				
				list = bs.query(sqlString);
				
			}
			return this.toJson("000","查询成功!!",list);
		} catch (Exception e) {
			// TODO: handle exception
			return this.toJson("001","查询失败!");
		}
	}
}
