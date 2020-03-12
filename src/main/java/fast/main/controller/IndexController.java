package fast.main.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import fast.main.service.BaseService;
import fast.main.util.ExcelUtil;
import fast.main.util.Super;

/**
 * 首页控制
 * 
 * @author tenviy
 * 
 */

@Controller
public class IndexController extends Super {

	@Autowired
	BaseService bs;

	/*
	 * @RequestMapping("index.do") public String init(HttpServletRequest
	 * request,HttpServletResponse response){ List<Map<String, Object>> list =
	 * bs.query("select * from xtgl_gncd where sj_gnxh=0 order by px");
	 * 
	 * return "index"; }
	 */

	// http://localhost:8080/fast/ajax.do&ctrl=Cd_query

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("index.do")
	public String init(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		Object o = new Object();
		try {
			// 获取主文本，格式ctrl=ipx_getWB;
			// ipx为类名，getWB为方法名;
			// 如ctrl=ipx时，默认方法名为init;
			String ctrl = form.get("ctrl");
			String czlx = form.get("czlx");
			String cla = ctrl;
			String met = "init";
			String arrs[] = ctrl.split("_");
			if(ctrl==null|ctrl.equals("")) {
				return "jsp/login.jsp";
			}
			if (czlx != null && czlx != "") {
				if (czlx.equals("0")) {
					cla = "CbbData";
					if (arrs.length == 2) {
						//						cla=arrs[0];
						met = arrs[1];
					}
				} else {
					cla = "ChzData";
					if (arrs.length == 2) {
						//						cla=arrs[0];
						met = arrs[1];
					}
				}
			} else {
				if (arrs.length == 2) {
					cla = arrs[0];
					met = arrs[1];
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("request", request);
			map.put("response", response);
			map.put("bs", bs);
			map.put("form", form);
			Class cc = Class.forName("fast.app." + cla);
			Method method = cc.getMethod(met, Map.class);
			o = method.invoke(cc.newInstance(), map);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("ajax.do")
	@ResponseBody
	public void init1(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		Object o = new Object();
		try {
			// 获取主文本，格式ctrl=ipx_getWB;
			// ipx为类名，getWB为方法名;
			// 如ctrl=ipx时，默认方法名为init;
			String ctrl = form.get("ctrl");
			String czlx = form.get("czlx");
			String cla = ctrl;
			String met = "init";
			String arrs[] = ctrl.split("_");
			if (czlx != null && czlx != "") {
				//	czlx="0";
				if (czlx.equals("0")) {
					cla = "CbbData";
					if (arrs.length == 2) {
						//						cla=arrs[0];
						met = arrs[1];
					}
				} else {
					cla = "ChzData";
					if (arrs.length == 2) {
						//						cla=arrs[0];
						met = arrs[1];
					}
				}
			} else {
				if (arrs.length == 2) {
					cla = arrs[0];
					met = arrs[1];
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("request", request);
			map.put("response", response);
			map.put("bs", bs);
			map.put("form", form);
			Class cc = Class.forName("fast.app." + cla);
			Method method = cc.getMethod(met, Map.class);
			o = method.invoke(cc.newInstance(), map);
			response.setContentType("application/json");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = null;
			out = response.getWriter();
			out.print(o.toString());
			out.flush();
			out.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("export.do")
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) throws IOException {
		Map<String, Object> o = new HashMap<String, Object>();
		try {
			// 获取主文本，格式ctrl=ipx_getWB;
			// ipx为类名，getWB为方法名;
			// 如ctrl=ipx时，默认方法名为init;
			String ctrl = form.get("ctrl");
			String czlx = form.get("czlx");
			String cla = ctrl;
			String met = "init";
			String arrs[] = ctrl.split("_");
			if (czlx != null && czlx != "") {
				if (czlx.equals("0")) {
					cla = "CbbData";
					if (arrs.length == 2) {
						//						cla=arrs[0];
						met = arrs[1];
					}
				} else {
					cla = "ChzData";
					if (arrs.length == 2) {
						//						cla=arrs[0];
						met = arrs[1];
					}
				}
			} else {
				if (arrs.length == 2) {
					cla = arrs[0];
					met = arrs[1];
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("request", request);
			map.put("response", response);
			map.put("bs", bs);
			map.put("form", form);
			Class cc = Class.forName("fast.app." + cla);
			Method method = cc.getMethod(met, Map.class);
			o = (Map<String, Object>)method.invoke(cc.newInstance(), map);
			String fileName=(String) o.get("fileName");
		
			List<Map<String, Object>> list=(List<Map<String, Object>>) o.get("list");
			String cols[] = (String[]) o.get("cols");//列名
			String keys[] = (String[]) o.get("keys");//map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				ExcelUtil.createWorkBook(list, keys, cols).write(os);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
			ServletOutputStream out = response.getOutputStream();
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(out);
				byte[] buff = new byte[2048];
				int bytesRead;
				// Simple read/write loop.
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (final IOException e) {
				throw e;
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("indexAction.do")
	public String action(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		
				return "login";
			
	}

}
