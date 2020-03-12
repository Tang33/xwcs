package fast.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import fast.app.CheckFileFormatUtil;
import fast.app.Test;
import fast.main.service.BaseService;
import fast.main.util.EasyExcelUtil;
import fast.main.util.Excel;
import fast.main.util.Excel2;
import fast.main.util.Excel3;
import fast.main.util.ExcelUtil;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

/**
 * 首页控制
 * 
 * @author tenviy
 * 
 */

@Controller
public class UploadController extends Super {

	@Autowired
	BaseService bs;
	Test test = new Test();
	@RequestMapping("upload.do")
	@ResponseBody
	public String upload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form, @RequestParam("file") MultipartFile[] file) throws ServletException, IOException {
		System.out.println(file);
		List<String> listjgall = new ArrayList<String>();
		HashMap<String, String> listjg = new HashMap<String, String>();
		if (file != null && file.length > 0) {
			String lx = request.getParameter("lx");
			String name = request.getParameter("name");
			if (lx == null || lx.trim().equals("")) {
				lx = "public";
			}
			JSONObject res = new JSONObject();
			System.out.println(res);
			for (int m = 0; m < file.length; m++) {
				JSONObject resUrl = new JSONObject();
				MultipartFile file1 = file[m];
				// 文件后缀
				String type = file1.getOriginalFilename().substring(file1.getOriginalFilename().lastIndexOf("."));// 取文件格式后缀名
				 String myFileName = file1.getOriginalFilename();// 文件原名称
//				String filename =file1.getOriginalFilename() + System.currentTimeMillis()+ type;
				String filename = System.nanoTime() + myFileName;
		
				// 文件网络存储路径
				String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + "/upload/" + lx;
				// 文件本地络存储路径
				String path = request.getSession().getServletContext().getRealPath("/upload/" + lx);
			
				File up = new File(request.getSession().getServletContext().getRealPath("/upload"));
				if (!up.exists()) {
					up.mkdir();
				}
				File fpath = new File(path);
				if (!fpath.exists()) {
					fpath.mkdir();
				}
				File bcfile = new File(path + "/" + filename);
				if (!bcfile.exists()) {
					bcfile.mkdir();
				}
				
				//test.doGet(request,response,path);
				try {
					file1.transferTo(bcfile);
					String url = returnUrl + "/" + filename;
					String bdpath = "/upload/" + lx + "/" + filename;
					Map<String, Object> map = bs
							.queryOne("select path from fast_wjgl where lx='" + lx + "' and name='" + name + "'");
					String msg = "";
					if (map != null && !map.isEmpty()) {
						int j = bs.update("update fast_wjgl set url='" + url + "',type='" + type + "',path='" + bdpath
								+ "' where lx='" + lx + "' and name='" + name + "'");
						if (j > 0) {
							msg = "修改成功！";
							String uu = String.valueOf(map.get("PATH"));
							String uup = request.getSession().getServletContext().getRealPath(uu);
							File uufile = new File(uup);
							if (uufile.exists()) {
								System.out.println("文件存在");
								if (m == 0) {
									uufile.delete();
								}
							} else {
								System.out.println("文件不存在");
							}
						}
					} else {
						int i = bs.insert("insert into fast_wjgl(lx,name,url,type,path) select '" + lx + "','" + name
								+ "','" + url + "','" + type + "','" + bdpath + "' from dual "
								+ "where not exists (select 1 from fast_wjgl where lx='" + lx + "' and name='" + name
								+ "')");
						if (i > 0) {
							msg = "新增成功！";
						}
					}
					resUrl.put("src", url);
					resUrl.put("bdsrc", path + "\\" + filename);
					listjgall.add(resUrl.toString());
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (listjgall.size() == 1) {

				res.put("ybm", file[0].getOriginalFilename());
			}
			res.put("code", 0);
			res.put("msg", "");
			res.put("data", listjgall.toString());
			return res.toString();
		} else {
			System.out.println("文件为空");
		}
		return null;
	}

	@RequestMapping("uploadgd.do")
	@ResponseBody
	public String uploadgd(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form, @RequestParam("file") MultipartFile importFile)
			throws IllegalStateException, IOException {
		String dwid = request.getParameter("dwid");
		// 获取文件，将文件上传至服务器
		String uploadPath = "D:\\uploadfiles1";

		File existsFile = new File(uploadPath);
		if (!existsFile.exists()) {
			existsFile.mkdirs();
		}

		String allName = importFile.getOriginalFilename();
		String filename = allName.substring(0, allName.lastIndexOf("."));
		String type = allName.substring(allName.lastIndexOf("."), allName.length());

		String wjid = System.currentTimeMillis() + "";

		File files = new File(uploadPath + "\\" + filename + "_" + wjid + type);
		importFile.transferTo(files);

		String nr = "";
		int i = bs.insert("insert into fast_scwjgl(wjmc,scrq,wjid,scz) values ('" + filename + "_" + wjid + type
				+ "',sysdate,'" + wjid + "','"+dwid+"')");
		if (i > 0) {
			nr = "插入成功！";
		} else {

			nr = "插入失败！";
		}

		JSONObject res = new JSONObject();
		res.put("mesg", "上传成功");
		res.put("nr", nr);
		return res.toString();

	}
	
	@RequestMapping("uploadBB.do")
	@ResponseBody
	public String uploadBB(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form, @RequestParam("file") MultipartFile importFile)
			throws IllegalStateException, IOException {

		// 获取文件，将文件上传至服务器
		String uploadPath = "D:\\uploadBB";

		File existsFile = new File(uploadPath);
		if (!existsFile.exists()) {
			existsFile.mkdirs();
		}

		String allName = importFile.getOriginalFilename();
		String filename = allName.substring(0, allName.lastIndexOf("."));
		String type = allName.substring(allName.lastIndexOf("."), allName.length());

		String wjid = System.currentTimeMillis() + "";

		File files = new File(uploadPath + "\\" + filename + "_" + wjid + type);
		importFile.transferTo(files);

		JSONObject res = new JSONObject();
		res.put("mesg", "上传成功");
		res.put("src", files);
		return res.toString();

	}
	
	@RequestMapping("downgd.do")
	@ResponseBody
	public String downgd(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {

		InputStream fis = null;
		try {
			
			String path = "D:\\uploadfiles1\\" + request.getParameter("filePath");
			File file = new File(path);
			Date nowTime = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

			String filename = request.getParameter("filePath");
			filename = new String(filename.getBytes("GB2312"), "ISO_8859_1");
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(file));
			byte bytes[] = new byte[1024];// 设置缓冲区为1024个字节，即1KB
			int len = 0;
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + filename);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			while ((len = fis.read(bytes)) != -1) {
				toClient.write(bytes, 0, len);
			}
			toClient.flush();
			toClient.close();
		} catch (IOException e) {

		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}

	@RequestMapping("downBB.do")
	@ResponseBody
	public String downBB(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		InputStream fis = null;
		try {
			
			String path = request.getParameter("filePath");
			File file = new File(path);
			String filename = request.getParameter("bbmc");
			filename = new String(filename.getBytes("GB2312"), "ISO_8859_1");
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(file));
			byte bytes[] = new byte[1024];// 设置缓冲区为1024个字节，即1KB
			int len = 0;
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + filename);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			while ((len = fis.read(bytes)) != -1) {
				toClient.write(bytes, 0, len);
			}
			toClient.flush();
			toClient.close();
		} catch (IOException e) {

		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}
	
	@RequestMapping("qysksdbg/upload.do")
	@ResponseBody
	public String qysksdbgUpload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form, @RequestParam("file") MultipartFile[] file) {
		System.out.println(file);
		List<String> listjgall = new ArrayList<String>();
		HashMap<String, String> listjg = new HashMap<String, String>();
		if (file != null && file.length > 0) {
			String lx = request.getParameter("lx");
			JSONObject res = new JSONObject();
			for (int m = 0; m < file.length; m++) {
				JSONObject resUrl = new JSONObject();
				MultipartFile file1 = file[m];
				// 文件后缀
				String type = file1.getOriginalFilename().substring(file1.getOriginalFilename().lastIndexOf("."));// 取文件格式后缀名
				// String myFileName = file.getOriginalFilename();// 文件原名称
				String filename = System.currentTimeMillis() + type;
				// 文件网络存储路径
				String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + "/upload/" + lx;
				// 文件本地络存储路径
				String path = request.getSession().getServletContext().getRealPath("/upload/" + lx);
				File up = new File(request.getSession().getServletContext().getRealPath("/upload"));
				if (!up.exists()) {
					up.mkdir();
				}
				File fpath = new File(path);
				if (!fpath.exists()) {
					fpath.mkdir();
				}
				File bcfile = new File(path + "/" + filename);
				if (!bcfile.exists()) {
					bcfile.mkdir();
				}
				try {
					file1.transferTo(bcfile);
					String url = returnUrl + "/" + filename;
					String bdpath = "/upload/" + lx + "/" + filename;
					Map<String, Object> map = bs
							.queryOne("select path from fast_wjgl where lx='" + lx + "' and name='" + filename + "'");
					String msg = "";
					if (map != null && !map.isEmpty()) {
						int j = bs.update("update fast_wjgl set url='" + url + "',type='" + type + "',path='" + bdpath
								+ "' where lx='" + lx + "' and name='" + filename + "'");
						if (j > 0) {
							msg = "修改成功！";
							String uu = String.valueOf(map.get("PATH"));
							String uup = request.getSession().getServletContext().getRealPath(uu);
							File uufile = new File(uup);
							if (uufile.exists()) {
								System.out.println("文件存在");
								if (m == 0) {
									uufile.delete();
								}
							} else {
								System.out.println("文件不存在");
							}
						}
					} else {
						int i = bs.insert("insert into fast_wjgl(lx,name,url,type,path) select '" + lx + "','"
								+ filename + "','" + url + "','" + type + "','" + bdpath + "' from dual "
								+ "where not exists (select 1 from fast_wjgl where lx='" + lx + "' and name='"
								+ filename + "')");
						if (i > 0) {
							msg = "新增成功！";
						}
					}
					resUrl.put("src", url);
					resUrl.put("bdsrc", path + "/" + filename);
					resUrl.put("originalFilename", file1.getOriginalFilename());
					listjgall.add(resUrl.toString());
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			res.put("code", 0);
			res.put("msg", "");
			res.put("data", listjgall.toString());
			return res.toString();
		} else {
			System.out.println("文件为空");
		}
		return null;
	}

	@RequestMapping("dc.do")
	@ResponseBody
	public void dc(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> form) {
		try {
			String yearNmonth = getValue(form.get("date")).toString();// 年月
			String jd = getValue(form.get("jd")).toString();// 街道
			String type = getValue(form.get("type")).toString();// 合计或月明细
			String nsName = getValue(form.get("nsName")).toString();// 纳税人名称
			String sortname = getValue(form.get("sortname")).toString();// 税种名字
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");

			if (!ssdw_dm.equals("00")) {
				jd = ssdw_dm;
			}
			String px = getValue(form.get("px")).toString();// 排序
			String qyxz = getValue(form.get("qyxz")).toString();// 企业性质
			String islp = getValue(form.get("islp")).toString();// 是否合伙
			String tjkj = getValue(form.get("tjkj")).toString();// 统计口径
			String hylist = getValue(form.get("hylist")).toString();// 行业
			String zdsyh = getValue(form.get("zdsyh")).toString();// 重点税源

			String zdycx = getValue(form.get("zdycx")).toString();// 写死 80000
			String column = "企业名称, 街道名称," + getValue(form.get("columns")).toString() + ",合计";// 写死 80000
			String[] columnNames = column.split(",");
			String starTime = "";
			String endTime = "";
			String[] star = yearNmonth.split(" - ");
			if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
				starTime = star[0];
				endTime = star[1];
			}
			starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
			endTime = endTime.substring(0, 4) + endTime.substring(5, 7);

			List<Mode> list = new ArrayList<Mode>();
			list.add(new Mode("IN", "String", nsName));// 纳税人名字
			list.add(new Mode("IN", "String", jd.equals("") ? "%" : jd));// 街道代码
			list.add(new Mode("IN", "String", starTime));// 时间起
			list.add(new Mode("IN", "String", endTime));// 时间止
			list.add(new Mode("IN", "String", type));// 是否按月合计
			list.add(new Mode("IN", "String", qyxz.equals("") ? "%" : qyxz));// 企业性质
			list.add(new Mode("IN", "String", hylist.equals("") ? "%" : hylist));// 行业代码
			list.add(new Mode("IN", "String", islp.equals("") ? "0" : hylist));// 是否合伙
			list.add(new Mode("IN", "String", sortname));// 税种名字
			list.add(new Mode("IN", "String", px));// 排序方式
			list.add(new Mode("IN", "String", tjkj.equals("") ? "0" : tjkj));// 统计口径
			list.add(new Mode("IN", "String", zdsyh.equals("") ? "%" : zdsyh));// 重点税源户代码
			list.add(new Mode("IN", "String", "0"));// 写死
			list.add(new Mode("IN", "String", "800000"));// 写死
			if (!(zdycx.equals("%") || zdycx.equals(""))) {
				list.add(new Mode("IN", "String", zdycx));
			}
			list.add(new Mode("OUT", "RS", ""));
			System.out.println(list);
			// SJ_CX_NEW.QUERYBYQY201605_NEW1
			List<Map<String, Object>> rs = null;
			if (!(zdycx.equals("%") || zdycx.equals(""))) {
				rs = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY201605_NEW2", list);// 调用存储过程
			} else {
				rs = (List<Map<String, Object>>) JdbcConnectedPro.call("SJ_CX_NEW.QUERYBYQY201605_NEW1", list);
			}

			// excel标题
			String[] keys = null;
			String[] keys1 = null;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fileName = "zdycx" + sdf.format(new Date()) + ".xls";
			String keystr = "";
			String keystr1 = "";
			for (int i = 0; i < columnNames.length; i++) {
				String result = getValue(columnNames[i]);
				if (result.indexOf("企业名称") > -1) {
					keystr += "NSRMC,";
				} else if (result.indexOf("街道名称") > -1) {
					keystr += "JD_MC,";

				} else if (result.indexOf("营改增增值税") > -1) {
					keystr += "YGZZZS,";
					keystr1 += "YGZZZS,";

				} else if (result.indexOf("增值税") > -1) {
					keystr += "ZZS,";
					keystr1 += "ZZS,";
				} else if (result.indexOf("营业税") > -1) {
					keystr += "YYS,";
					keystr1 += "YYS,";
				} else if (result.indexOf("企业所得税") > -1) {
					keystr += "QYSDS_GS,";
					keystr1 += "QYSDS_GS,";
				} else if (result.indexOf("个人所得税") > -1) {
					keystr += "GRSDS,";
					keystr1 += "GRSDS,";
				} else if (result.indexOf("房产税") > -1) {
					keystr += "FCS,";
					keystr1 += "FCS,";
				} else if (result.indexOf("印花税") > -1) {
					keystr += "YHS,";
					keystr1 += "YHS,";
				} else if (result.indexOf("车船税") > -1) {
					keystr += "CCS,";
					keystr += "CCS,";
				} else if (result.indexOf("城市维护建设税") > -1) {
					keystr += "CSWHJSS,";
					keystr1 += "CSWHJSS,";
				} else if (result.indexOf("地方教育附加") > -1) {
					keystr += "DFJYFJ,";
					keystr1 += "DFJYFJ,";
				} else if (result.indexOf("教育附加") > -1) {
					keystr += "JYFJ,";
					keystr1 += "JYFJ,";
				} else if (result.indexOf("城镇土地使用税") > -1) {
					keystr += "CZTDSYS,";
					keystr1 += "CZTDSYS,";
				} else if (result.indexOf("环保税") > -1) {
					keystr += "HBS,";
					keystr1 += "HBS,";
				} else if (result.indexOf("合计") > -1) {
					keystr += "HJ,";
					keystr1 += "HJ,";
				}
			}
			keystr = keystr.substring(0, keystr.length() - 1);
			keys = keystr.split(",");
			keys1 = keystr1.split(",");
			List<Map<String, Object>> rss = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < rs.size(); i++) {
				Map<String, Object> maps = rs.get(i);
				double hj = 0;
				for (int j = 0; j < keys1.length; j++) {
					String Se = String.valueOf(maps.get(keys1[j]));
					hj += Double.parseDouble(Se);
				}
				String hj1 = formatDouble(hj);
				maps.put("HJ", hj1);
				rss.add(maps);
			}
			// 创建HSSFWorkbook
			Workbook wb = ExcelUtil.createWorkBook(rss, keys, columnNames);
			OutputStream output = response.getOutputStream();
			response.reset(); // 清除buffer缓存
			// String fileName = URLEncoder.encode("社保记录_"+sdf.format(new Date())+".xls",
			// "UTF-8");
			// 定义浏览器响应表头，并定义下载名
			response.setHeader("Content-disposition", "attachment;filename=" + fileName);
			// 定义下载的类型，标明是excel文件
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			// 把创建好的excel写入到输出流
			wb.write(output);
			// 随手关门
			output.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String formatDouble(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		// 设置保留多少位小数
		nf.setMaximumFractionDigits(2);
		// 取消科学计数法
		nf.setGroupingUsed(false);
		// 返回结果
		return nf.format(d);
	}

	@RequestMapping("download.do")
	@ResponseBody
	public String download(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> form) {
		String url = "";
		String lx = request.getParameter("lx");
		String name = request.getParameter("name");
		if (lx == null || lx.trim().equals("")) {
			lx = "public";
		}
		String sql = "select url from fast_wjgl where lx='" + lx + "' and name='" + name + "'";
		System.out.println(sql);
		Map<String, Object> map = bs.queryOne(sql);
		if (map != null) {
			url = String.valueOf(map.get("URL"));
		}
		return url;
	}

	@RequestMapping("mbdc.do")
	@ResponseBody
	public void mbdc(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> form) {

		try {

			String yearNmonth = getValue(form.get("yearNmonth"));// 年月
			String jd = getValue(form.get("jdlist"));// 街道
			String type = getValue(form.get("cxls"));// 合计或月明细
			String nsName = getValue(form.get("nsName"));// 纳税人名称

			String sortname = getValue(form.get("sortname"));// 税种名字
			String px = getValue(form.get("px"));// 排序
			String qyxz = getValue(form.get("qyxz"));// 企业性质
			String islp = getValue(form.get("type1"));// 是否合伙
			String tjkj = getValue(form.get("tjkj"));// 统计口径
			String hylist = getValue(form.get("hylist"));// 行业
			String zdsyh = getValue(form.get("zdsyh"));// 重点税源
			String dczd = getValue(form.get("dczd"));// 导出字段

			String dcmb = getValue(form.get("dcmb"));// 重点税源
			String mbsql = "select * from MBDC_QYQCMBGL where uuid ='" + dcmb + "'";
			Map<String, Object> mbmap = bs.queryOne(mbsql);
			if (mbmap != null) {

				String starTime = "";
				String endTime = "";
				String[] star = yearNmonth.split(" - ");
				if (yearNmonth != null && !yearNmonth.trim().equals("") && star != null && star.length > 0) {
					starTime = star[0];
					endTime = star[1];
					starTime = starTime.substring(0, 4) + starTime.substring(5, 7);
					endTime = endTime.substring(0, 4) + endTime.substring(5, 7);
				}
				String ssdw_dm = getValue(request.getSession().getAttribute("dwid"));

				if (!ssdw_dm.equals("00")) {
					int staryear = Integer.parseInt(starTime.substring(0, 4));
					int starmonth = Integer.parseInt(starTime.substring(4, 6));
					int endryear = Integer.parseInt(endTime.substring(0, 4));
					int endmonth = Integer.parseInt(endTime.substring(4, 6));
					int m = ((endryear - staryear) * 12 + endmonth - starmonth) + 1;
					for (int i = 0; i < m; i++) {
						String rq = staryear + "" + (starmonth < 10 ? "0" + starmonth : starmonth);
						String statesql = "select state from xwcs_cxzt where to_Char(rkrq,'yyyyMM')='" + rq + "'";
						Map<String, Object> map = bs.queryOne(statesql);
						if (map == null) {
							SimpleDateFormat dateparse = new SimpleDateFormat("yyyyMM");
							Date date = dateparse.parse(rq);
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月");// 可以方便地修改日期格式
							String resultrq = dateFormat.format(date);
						} else {
							if (getValue(map.get("STATE")).equals("0")) {
								endTime = rq;
								break;
							} else if (getValue(map.get("STATE")).equals("")) {
								endTime = rq;
								break;
							}
						}
						starmonth++;
						if (starmonth > 12) {
							starmonth = 1;
							staryear++;
						}
					}
				}

				String filename = getValue(mbmap.get("WJBDLJ"));
				InputStream is = new FileInputStream(new File(filename));
				Workbook wb = null;
				if (filename.lastIndexOf(".xls") > -1) {
					// 创建excel工作簿
					wb = new HSSFWorkbook(is);
				} else {
					// 创建excel工作簿
					wb = new XSSFWorkbook(is);
				}
				Sheet sheet = wb.getSheetAt(0);

				int column = Integer.parseInt(getValue(mbmap.get("SJQSL"))) - 1;
				String sqlnsrxx = "select * from mbdc_nsrxx where mbid='" + getValue(mbmap.get("UUID")) + "'";
				List<Map<String, Object>> nsrxxlist = bs.query(sqlnsrxx);
				for (int j = 0; j < nsrxxlist.size(); j++) {
					List<Mode> list = new ArrayList<Mode>();
					list.add(new Mode("IN", "String", getValue(nsrxxlist.get(j).get("NSRMC"))));// 纳税人名字
					list.add(new Mode("IN", "String", jd));// 街道代码
					list.add(new Mode("IN", "String", starTime));// 时间起
					list.add(new Mode("IN", "String", endTime));// 时间止
					list.add(new Mode("IN", "String", type));// 是否按月合计
					list.add(new Mode("IN", "String", qyxz));// 企业性质
					list.add(new Mode("IN", "String", hylist));// 行业代码
					list.add(new Mode("IN", "String", islp));// 是否合伙
					list.add(new Mode("IN", "String", sortname));// 税种名字
					list.add(new Mode("IN", "String", px));// 排序方式
					list.add(new Mode("IN", "String", tjkj));// 统计口径
					list.add(new Mode("IN", "String", zdsyh));// 重点税源户代码
					list.add(new Mode("IN", "String", "0"));// 写死
					list.add(new Mode("IN", "String", "800000"));// 写死
					list.add(new Mode("OUT", "RS", ""));
					List<Map<String, Object>> rs = (List<Map<String, Object>>) JdbcConnectedPro
							.call("SJ_CX_NEW.QUERYBYQY201605_NEW1", list);// 调用存储过程
					double sum = 0;
					for (int i = 0; i < rs.size(); i++) {
						Map<String, Object> maps = rs.get(i);
						String ZZS = String.valueOf(maps.get("ZZS"));
						String YGZZZS = String.valueOf(maps.get("YGZZZS"));
						String YYS = String.valueOf(maps.get("YYS"));
						String QYSDS = String.valueOf(maps.get("QYSDS"));
						String GRSDS = String.valueOf(maps.get("GRSDS"));
						String FCS = String.valueOf(maps.get("FCS"));
						String YHS = String.valueOf(maps.get("YHS"));
						String CCS = String.valueOf(maps.get("CCS"));
						String CSWHJSS = String.valueOf(maps.get("CSWHJSS"));
						String DFJYFJ = String.valueOf(maps.get("DFJYFJ"));
						String JYFJ = String.valueOf(maps.get("JYFJ"));
						String CZTDSYS = String.valueOf(maps.get("CZTDSYS"));//
						String HBS = String.valueOf(maps.get("HBS"));
						double ZZS1 = Double.parseDouble(ZZS);
						double YGZZZS1 = Double.parseDouble(YGZZZS);
						double YYS1 = Double.parseDouble(YYS);
						double QYSDS1 = Double.parseDouble(QYSDS);
						double GRSDS1 = Double.parseDouble(GRSDS);
						double FCS1 = Double.parseDouble(FCS);
						double YHS1 = Double.parseDouble(YHS);
						double CCS1 = Double.parseDouble(CCS);
						double CSWHJSS1 = Double.parseDouble(CSWHJSS);
						double DFJYFJ1 = Double.parseDouble(DFJYFJ);
						double JYFJ1 = Double.parseDouble(JYFJ);
						double CZTDSYS1 = Double.parseDouble(CZTDSYS);
						double HBS1 = Double.parseDouble(HBS);
						double hj = ZZS1 + YGZZZS1 + YYS1 + QYSDS1 + GRSDS1 + FCS1 + YHS1 + CCS1 + CSWHJSS1 + DFJYFJ1
								+ JYFJ1 + CZTDSYS1 + HBS1;
						String hj1 = formatDouble(hj);
						maps.put("HJ", hj1);
						String value = String.valueOf(maps.get(dczd));
						sum += Double.parseDouble(value);
					}
					int rowstart = Integer.parseInt(getValue(mbmap.get("SJQSH"))) - 1
							+ Integer.parseInt(getValue(nsrxxlist.get(j).get("ROWNUMBER")));
					if (rowstart <= sheet.getLastRowNum()) {
						sheet.getRow(rowstart).createCell(column).setCellValue(sum);
					} else {
						sheet.createRow(rowstart).createCell(column).setCellValue(sum);
					}

				}
				OutputStream output = response.getOutputStream();
				response.reset(); // 清除buffer缓存
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String dcfileName = URLEncoder.encode("清册模板导出_" + sdf.format(new Date()) + ".xls", "UTF-8");
				// 定义浏览器响应表头，并定义下载名
				response.setHeader("Content-disposition", "attachment;filename=" + dcfileName);
				// 定义下载的类型，标明是excel文件
				response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				// 把创建好的excel写入到输出流
				wb.write(output);
				// 随手关门
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("downloadllq.do")
	@ResponseBody
	public String downloadllq(HttpServletRequest request, HttpServletResponse response) {
		// 创建HSSFWorkbook
		OutputStream output;
		try {
			output = response.getOutputStream();
			String path = request.getSession().getServletContext().getRealPath("/upload/ChromeSetup.exe");

			response.reset(); // 清除buffer缓存
			// String fileName = URLEncoder.encode("社保记录_"+sdf.format(new Date())+".xls",
			// "UTF-8");
			// 定义浏览器响应表头，并定义下载名
			response.setHeader("Content-disposition", "attachment;filename=ChromeSetup.exe");
			// 定义下载的类型，标明是excel文件
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			FileInputStream in = new FileInputStream(path);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
				output.write(buf, 0, len);
			}

			// 随手关门
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 导出 maplist table表格数据的查询
	@RequestMapping("outSdbg.do")
	public void outSdbg(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			// 获取页面传递过来的input中的值
			String fz = getValue(request.getParameter("fz"));
			// 获取表单中的数据
			String form = getValue(request.getParameter("form"));
			String rk_rq2 = getValue(request.getParameter("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];

			String sqlcs = "";

			JSONArray form1 = JSONArray.parseArray(form);
			// 下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
					//JSONObject obj = (JSONObject) form1.get(i);
					JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}

			System.out.println("前台传过来的值sql:" + sqlcs);

			String sql = "select * from xwcs_gsdr_yssjrk  where 1=1  ";
			sql += sqlcs;
			if (!rk_rq.equals("")) {
				sql += " and rk_rq='" + rk_rq + "' ";
			}
			if (!fz.equals("")) {

				if (fz.equals("sjje")) {
					sql = "select t." + fz + ",sum(t.qxj) qxj,"
							+ "SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjrk t where 1=1";
				} else {

					sql = "select t." + fz + ",sum(t.sjje) sjje,sum(t.qxj) qxj,"
							+ "SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from xwcs_gsdr_yssjrk t where 1=1";
				}

				sql += sqlcs;

				if (!rk_rq.equals("")) {
					sql += " and rk_rq='" + rk_rq + "' ";
				}
				sql += " group by t." + fz + " ";
			}
			sql = "select row_.*, rownum rowno from (" + sql + ") row_ ";

			// 执行sql 执行sql
			System.out.println(sql);
			List<Map<String, Object>> sjjgall = bs.query(sql);

			// 查询count
			String sqlcount = "select count(*) cs from xwcs_gsdr_yssjrk a where 1=1 " + sqlcs;
			if (!rk_rq.equals("")) {
				sqlcount += " and rk_rq='" + rk_rq + "' ";

			}
			if (!fz.equals("")) {
				sqlcount += " group by a." + fz + " ";
				sqlcount = "select count(cs) CS from (" + sqlcount + ")";
			}
			System.out.println(sqlcount);
			List<Map<String, Object>> sjjgallcount = bs.query(sqlcount);
			System.out.println("数据量1：" + sjjgallcount.get(0));
			String cont = getValue(sjjgallcount.get(0).get("CS"));
			System.out.println("数据量：" + cont);
			// 数据
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sjjgall.size(); i++) {
				Map<String, Object> map = sjjgall.get(i);
				map.put("NSRMC", map.get("NSRMC"));
				map.put("NSRSBH", map.get("NSRSBH"));
				map.put("ZSXMMC", map.get("ZSXMMC"));
				map.put("ZSPM", map.get("ZSPM"));
				map.put("ZSPMDM", map.get("ZSPMDM"));
				map.put("SKSHQQ", map.get("SKSHQQ"));
				map.put("SKSHQZ", map.get("SKSHQZ"));
				map.put("DJZCLX", map.get("DJZCLX"));

				map.put("SSGLY", map.get("SSGLY"));
				map.put("HYML", map.get("HYML"));
				map.put("HYDL", map.get("HYDL"));
				map.put("HYZL", map.get("HYZL"));
				map.put("HY", map.get("HY"));
				map.put("ZSDLFS", map.get("ZSDLFS"));
				map.put("JSYJ", map.get("JSYJ"));
				map.put("KSSL", map.get("KSSL"));

				map.put("SL", map.get("SL"));
				map.put("SJJE", map.get("SJJE"));
				map.put("SKSSSWJG", map.get("SKSSSWJG"));
				map.put("ZGSWS", map.get("ZGSWS"));
				map.put("YSKMDM", map.get("YSKMDM"));
				map.put("YSKM", map.get("YSKM"));
				map.put("YSFPBL", map.get("YSFPBL"));
				map.put("ZYJBL", map.get("ZYJBL"));

				map.put("SSJBL", map.get("SSJBL"));
				map.put("DSJBL", map.get("DSJBL"));
				map.put("QXJBL", map.get("QXJBL"));
				map.put("XZJBL", map.get("XZJBL"));
				map.put("ZYDFPBL", map.get("ZYDFPBL"));
				map.put("DSDFPBL", map.get("DSDFPBL"));
				map.put("SJDFPBL", map.get("SJDFPBL"));
				map.put("SKGK", map.get("SKGK"));

				map.put("KJRQ", map.get("KJRQ"));
				map.put("SJRQ", map.get("SJRQ"));
				map.put("SJXHRQ", map.get("SJXHRQ"));
				map.put("SJXHR", map.get("SJXHR"));
				map.put("RKRQ", map.get("RKRQ"));
				map.put("RKXHRQ", map.get("RKXHRQ"));
				map.put("RKXHR", map.get("RKXHR"));
				map.put("PZZL", map.get("PZZL"));

				map.put("PZZG", map.get("PZZG"));
				map.put("PZHM", map.get("PZHM"));
				map.put("SKZL", map.get("SKZL"));
				map.put("SKSX", map.get("SKSX"));
				map.put("JDXZ", map.get("JDXZ"));
				map.put("YHHB", map.get("YHHB"));
				map.put("YHYYWD", map.get("YHYYWD"));
				map.put("YHZH", map.get("YHZH"));

				map.put("HZRQ", map.get("HZRQ"));
				map.put("HZR", map.get("HZR"));
				map.put("HZPZZL", map.get("HZPZZL"));
				map.put("HZPZZG", map.get("HZPZZG"));
				map.put("HZPZHM", map.get("HZPZHM"));
				map.put("TZLX", map.get("TZLX"));
				map.put("JKQX", map.get("JKQX"));
				map.put("ZSSWJG", map.get("ZSSWJG"));

				map.put("DZSPHM", map.get("DZSPHM"));
				map.put("ZYJ", map.get("ZYJ"));
				map.put("SSJ", map.get("SSJ"));
				map.put("DSJ", map.get("DSJ"));
				map.put("QXJ", map.get("QXJ"));
				map.put("XZJ", map.get("XZJ"));
				map.put("ZYDFP", map.get("ZYDFP"));
				map.put("SJDFP", map.get("SJDFP"));

				map.put("DSDFP", map.get("DSDFP"));
				map.put("DJXH", map.get("DJXH"));
				map.put("ZFJGLX", map.get("ZFJGLX"));
				map.put("KDQSSZYQYLX", map.get("KDQSSZYQYLX"));
				map.put("CBSX", map.get("CBSX"));
				map.put("YZPZZL", map.get("YZPZZL"));
				map.put("SYBH", map.get("SYBH"));
				map.put("KJNSRXX", map.get("KJNSRXX"));
				map.put("YZPZXH", map.get("YZPZXH"));
				map.put("KPR", map.get("KPR"));
				map.put("QSBJ", map.get("QSBJ"));
				map.put("RK_RQ", map.get("RK_RQ"));
				map.put("SSWJ", map.get("SSWJ"));
				map.put("ID", map.get("ID"));
				map.put("DRFS", map.get("DRFS"));
				map.put("SJZW", map.get("SJZW"));
				lists.add(map);
			}

			String fileName = "入库原始数据";

			String columnNames[] = { "纳税人识别号", "纳税人名称", "征收项目", "征收品目", "征收品目代码", "税款所属期起", "税款所属期止", "登记注册类型", "税收管理员",
					"行业门类", "行业大类", "行业中类", "行业", "征收代理方式", "计税依据", "课税数量", "税率", "实缴金额", "税款所属税务机关", "主管税务所（科、分局）",
					"预算科目代码", "预算科目", "预算分配比例", "中央级比例", "省市级比例", "地市级比例", "区县级比例", "乡镇级比例", "中央待分配比例", "地市待分配比例",
					"省局待分配比例", "收款国库", "开具日期", "上解日期", "上解销号日期", "上解销号人", "入库日期", "入库销号人", "票证种类", "票证字轨", "票证号码",
					"税款种类", "税款属性", "街道乡镇", "银行行别", "银行营业网点", "银行账号", "调账类型", "缴款期限", "征收税务机关", "电子税票号码", "中央级", "省市级",
					"地市级", "区县级", "乡镇级", "中央待分配", "省级待分配", "地市待分配", "登记序号", "总分机构类型", "跨地区税收转移企业类型" };// 列名
			// String
			// columnNames2[]={"","","","","","","","","","","","全日制教育","在职教育","","","","",""};
			String keys[] = { "NSRSBH", "NSRMC", "ZSXMMC", "ZSPM", "ZSPMDM", "SKSHQQ", "SKSHQZ", "DJZCLX", "SSGLY",
					"HYML", "HYDL", "HYZL", "HY", "ZSDLFS", "JSYJ", "KSSL", "SL", "SJJE", "SKSSSWJG", "ZGSWS", "YSKMDM",
					"YSKM", "YSFPBL", "ZYJBL", "SSJBL", "DSJBL", "QXJBL", "XZJBL", "ZYDFPBL", "DSDFPBL", "SJDFPBL",
					"SKGK", "KJRQ", "SJRQ", "SJXHR", "RKRQ", "RKXHRQ", "RKXHR", "PZZL", "PZZG", "PZHM", "SKZL", "SKSX",
					"JDXZ", "YHHB", "YHYYWD", "YHZH", "TZLX", "JKQX", "ZSSWJG", "DZSPHM", "ZYJ", "SSJ", "DSJ", "QXJ",
					"XZJ", "ZYDFP", "SJDFP", "DSDFP", "DJXH", "ZFJGLX", "KDQSSZYQYLX" };// MAP中的KEY
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				Excel.createWorkBook(lists, keys, columnNames).write(os);

			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
			ServletOutputStream out;

			out = response.getOutputStream();

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
				e.printStackTrace();
			} finally {

				if (bis != null)
					bis.close();
				if (bos != null)
					bos.flush();
				bos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// 导出 maplist table表格数据的查询
	@RequestMapping("outSdbg1.do")
	public void outSdbg1(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取页面传递过来的input中的值
			String fz = getValue(request.getParameter("fz"));
			// 获取表单中的数据
			String form = getValue(request.getParameter("form"));
			String rk_rq2 = getValue(request.getParameter("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];
			String sqlcs = "";

			JSONArray form1 = JSONArray.parseArray(form);
			// 下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
					//JSONObject obj = (JSONObject) form1.get(i);
					JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}


			
			
			System.out.println("前台传过来的值sql:" + sqlcs);

			String sql = "select * from xwcs_gsdr_yssjtk  where 1=1";

			sql += sqlcs;

			if (!rk_rq.equals("")) {
				sql += " and rk_rq='" + rk_rq + "' ";
			}
			if (!fz.equals("")) {

				if (fz.equals("se")) {
					sql = "select t." + fz + ",sum(t.dfbl) dfbl  from xwcs_gsdr_yssjtk t where 1=1";
				} else {
					sql = "select t." + fz + ",sum(t.se) se,sum(t.dfbl) dfbl  from xwcs_gsdr_yssjtk t where 1=1";
				}

				sql += sqlcs;

				if (!rk_rq.equals("")) {
					sql += " and t.rk_rq='" + rk_rq + "' ";
				}
				sql += " group by t." + fz + " ";
			}

			// 总数据分页查询
			// sql = "select row_.*, rownum rowno from (" + sql + ") row_ ";
			System.out.println(sql);
			List<Map<String, Object>> sjjgall = bs.query(sql);

			// 查询count
			String sqlcount = "select count(*) cs from xwcs_gsdr_yssjtk a where 1=1" + sqlcs;
			if (!rk_rq.equals("")) {
				sqlcount += " and rk_rq='" + rk_rq + "' ";

			}
			if (!fz.equals("")) {
				sqlcount += " group by a." + fz + " ";
				sqlcount = "select count(cs) CS from (" + sqlcount + ")";
			}
			System.out.println(sqlcount);
			List<Map<String, Object>> sjjgallcount = bs.query(sqlcount);
			System.out.println("sjjgallcount:" + sjjgallcount.get(0));
			String cont = "0";

			if (sjjgallcount.get(0) != null) {
				cont = getValue(sjjgallcount.get(0).get("CS"));
			}

			System.out.println("数据量1：" + sjjgallcount.get(0));
			System.out.println("数据量：" + cont);
			// 数据
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sjjgall.size(); i++) {

				Map<String, Object> map = sjjgall.get(i);
				map.put("NSRMC", map.get("NSRMC"));
				map.put("NSRSBH", map.get("NSRSBH"));
				map.put("PZZL", map.get("PZZL"));
				map.put("PZZG", map.get("PZZG"));
				map.put("PZHM", map.get("PZHM"));
				map.put("ZSXM", map.get("ZSXM"));
				map.put("ZSPM", map.get("ZSPM"));
				map.put("SKSSQQ", map.get("SKSSQQ"));

				map.put("XMMC", map.get("XMMC"));
				map.put("SKSSQZ", map.get("SKSSQZ"));
				map.put("SKZL", map.get("SKZL"));
				map.put("SKSX", map.get("SKSX"));
				map.put("HYML", map.get("HYML"));
				map.put("HYDL", map.get("HYDL"));
				map.put("HYZL", map.get("HYZL"));
				map.put("HY", map.get("HY"));

				map.put("SE", map.get("SE"));
				map.put("YSKM", map.get("YSKM"));
				map.put("YSKMMC", map.get("YSKMMC"));
				map.put("YSFPBL", map.get("YSFPBL"));
				map.put("SKGK", map.get("SKGK"));
				map.put("TTSJLX", map.get("TTSJLX"));
				map.put("TDSFYJWSZH", map.get("TDSFYJWSZH"));

				map.put("YHYYWD", map.get("YHYYWD"));

				map.put("ZHMC", map.get("ZHMC"));
				map.put("YHZH", map.get("YHZH"));
				map.put("KPRQ", map.get("KPRQ"));
				map.put("THRQ", map.get("THRQ"));
				map.put("XHRQ", map.get("XHRQ"));
				map.put("XHR", map.get("XHR"));
				map.put("JDXZ", map.get("JDXZ"));
				map.put("TTJG", map.get("TTJG"));

				map.put("SSGLY", map.get("SSGLY"));
				map.put("SKSSSWJG", map.get("SKSSSWJG"));
				map.put("ZGSWS", map.get("ZGSWS"));
				map.put("SWJG", map.get("SWJG"));
				map.put("TDSFYYLX", map.get("TDSFYYLX"));
				map.put("DZSPHM", map.get("DZSPHM"));
				map.put("TZLX", map.get("TZLX"));
				map.put("CZLX", map.get("CZLX"));

				map.put("SSJMXZDL", map.get("SSJMXZDL"));
				map.put("SSJMXZXL", map.get("SSJMXZXL"));
				map.put("FHJG", map.get("FHJG"));
				map.put("LRR", map.get("LRR"));
				map.put("KPR", map.get("KPR"));
				map.put("LRRQ", map.get("LRRQ"));
				map.put("RK_RQ", map.get("RK_RQ"));
				map.put("SSWJ", map.get("SSWJ"));

				map.put("ID", map.get("ID"));
				map.put("DRFS", map.get("DRFS"));
				map.put("SJZW", map.get("SJZW"));
				map.put("DFBL", map.get("DFBL"));
				map.put("QXJ", map.get("QXJ"));
				lists.add(map);
			}

			String fileName = "退库原始数据";

			String columnNames[] = { "纳税人识别号", "纳税人名称", "票证种类", "票证字轨", "票证号码", "征收项目", "征收品目", "税款所属期起", "细目名称",
					"税款所属期止", "税款种类", "税款属性", "登记注册类型", "行业门类", "行业大类", "行业中类", "行业", "税额", "预算科目", "预算科目名称", "预算分配比例",
					"收款国库", "提退税金类型", "退抵税（费）依据文书字号", "银行营业网点", "账户名称", "银行账号", "开票日期", "退还日期", "销号日期", "销号人", "街道乡镇",
					"提退机构", "税收管理员", "税款所属税务机关", "主管税务所（科、分局）", "税务机关", "退抵税费原因类型", "电子税票号码", "调账类型", "操作类型",
					"税收减免性质大类", "税收减免性质小类", "复核结果", "录入人", "开票人", "录入日期", "导入日期", "所属文件", "ID", "导入方式", "数据指纹", "地方比例",
					"区县级金额" };// 列名
			// String
			// columnNames2[]={"","","","","","","","","","","","全日制教育","在职教育","","","","",""};
			String keys[] = { "NSRSBH", "NSRMC", "PZZL", "PZZG", "PZHM", "ZSXM", "ZSPM", "SKSSQQ", "XMMC", "SKSSQZ",
					"SKZL", "SKSX", "DJZCLX", "HYML", "HYDL", "HYZL", "HY", "SE", "YSKM", "YSKMMC", "YSFPBL", "SKGK",
					"TTSJLX", "TDSFYJWSZH", "YHYYWD", "ZHMC", "YHZH", "KPRQ", "THRQ", "XHRQ", "XHR", "JDXZ", "TTJG",
					"SSGLY", "SKSSSWJG", "ZGSWS", "SWJG", "TDSFYYLX", "DZSPHM", "TZLX",
					"CZLX", "SSJMXZDL", "SSJMXZXL", "FHJG", "LRR", "KPR", "LRRQ", "RK_RQ", "SSWJ", "ID", "DRFS", "SJZW",
					"DFBL", "QXJ" };// MAP中的KEY
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				Excel.createWorkBook(lists, keys, columnNames).write(os);

			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
			ServletOutputStream out;

			out = response.getOutputStream();

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
				e.printStackTrace();
			} finally {

				if (bis != null)
					bis.close();
				if (bos != null)
					bos.flush();
				bos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// 导出 maplist table表格数据的查询
	@RequestMapping("outSdbg2.do")
	public void outSdbg2(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			// 获取页面传递过来的input中的值
			String fz = getValue(request.getParameter("fz"));
			// 获取表单中的数据
			String form = getValue(request.getParameter("form"));
			String rk_rq2 = getValue(request.getParameter("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];

			String sqlcs = "";

			JSONArray form1 = JSONArray.parseArray(form);
			// 下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
					//JSONObject obj = (JSONObject) form1.get(i);
					JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}

			System.out.println("前台传过来的值sql:" + sqlcs);

			String sql = "select * from xwcs_gsdr_yssjgs where 1=1";
			sql += sqlcs;

			if (!rk_rq.equals("")) {
				sql += " and rk_rq='" + rk_rq + "' ";

			}

			if (!fz.equals("")) {

				if (fz.equals("yzsje")) {
					sql = "select t." + fz + " from xwcs_gsdr_yssjgs t where 1=1";

				} else {
					sql = "select t." + fz + ",sum(t.yzsje) yzsje from xwcs_gsdr_yssjgs t where 1=1";
				}

				sql += sqlcs;

				if (!rk_rq.equals("")) {
					sql += " and rk_rq='" + rk_rq + "' ";
				}

				sql += " group by t." + fz + " ";
			}

			// 总数据分页查询
			// sql = "select row_.*, rownum rowno from (" + sql + ") row_ ";
			System.out.println(sql);
			List<Map<String, Object>> sjjgall = bs.query(sql);

			// 查询count
			String sqlcount = "select count(*) cs from xwcs_gsdr_yssjgs a where 1=1" + sqlcs;
			if (!rk_rq.equals("")) {
				sqlcount += " and rk_rq='" + rk_rq + "' ";

			}
			if (!fz.equals("")) {
				sqlcount += " group by a." + fz + " ";
				sqlcount = "select count(cs) CS from (" + sqlcount + ")";
			}
			System.out.println(sqlcount);
			List<Map<String, Object>> sjjgallcount = bs.query(sqlcount);
			System.out.println("sjjgallcount:" + sjjgallcount.get(0));
			String cont = "0";

			if (sjjgallcount.get(0) != null) {
				cont = getValue(sjjgallcount.get(0).get("CS"));
			}

			System.out.println("数据量1：" + sjjgallcount.get(0));
			System.out.println("数据量：" + cont);
			// 数据
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < sjjgall.size(); i++) {

				Map<String, Object> map = sjjgall.get(i);
				map.put("nsrsbh", map.get("NSRSBH"));
				map.put("nsrmc", map.get("NSRMC"));
				map.put("sfzjlx", map.get("SFZJLX"));
				map.put("sfzjhm", map.get("SFZJHM"));
				map.put("djzclx", map.get("DJZCLX"));
				map.put("zsxm", map.get("ZSXM"));
				map.put("zspm", map.get("ZSPM"));
				map.put("yzsje", map.get("YZSJE"));
				map.put("skzt", map.get("SKZT"));
				map.put("sbrq", map.get("SBRQ"));
				map.put("kjrq", map.get("KJRQ"));
				map.put("sjrq", map.get("SJRQ"));
				map.put("rkrq", map.get("RKRQ"));
				map.put("skssqq", map.get("SKSSQQ"));
				map.put("skssqz", map.get("SKSSQZ"));
				map.put("sksx", map.get("SKSX"));
				map.put("skjnfs", map.get("SKJNFS"));
				map.put("yske", map.get("YSKE"));
				map.put("ysfpbl", map.get("YSFPBL"));
				map.put("skgk", map.get("SKGK"));
				map.put("pzzl", map.get("PZZL"));
				map.put("dzsphm", map.get("DZSPHM"));
				map.put("kpr", map.get("KPR"));
				map.put("sjxhr", map.get("SJXHR"));
				map.put("zsjg", map.get("ZSJG"));
				map.put("zgsws", map.get("ZGSWS"));
				map.put("jdxz", map.get("JDXZ"));
				map.put("skssswjg", map.get("SKSSSWJG"));
				map.put("yzpzzl", map.get("YZPZZL"));
				map.put("xjhzpzhm", map.get("XJHZPZHM"));
				map.put("rk_rq", map.get("RK_RQ"));
				lists.add(map);
			}

			String fileName = "个人所得税原始数据";

			String columnNames[] = { "纳税人识别号", "纳税人名称", "身份证件类型", "身份证件号码", "登记注册类型", "征收项目", "征收品目", "应征税金额", "税款状态",
					"申报日期", "开具日期", "上解日期", "入库日期", "税款所属期起", "税款所属期止", "税款属性", "税款缴纳方式", "预算科目", "预算分配比例", "收款国库",
					"票证种类", "开票人", "征收机关", "主管税务所", "街道乡镇", "税款所属税务机关", "应征凭证种类", "现金汇总凭证号码", "导入日期" };// 列名
			// String
			// columnNames2[]={"","","","","","","","","","","","全日制教育","在职教育","","","","",""};
			String keys[] = { "nsrsbh", "nsrmc", "sfzjlx", "sfzjhm", "djzclx", "zsxm", "zspm", "yzsje", "skzt", "sbrq",
					"kjrq", "sjrq", "rkrq", "skssqq", "skssqz", "sksx", "skjnfs", "yske", "ysfpbl", "skgk", "pzzl",
					"kpr", "zsjg", "zgsws", "jdxz", "skssswjg", "yzpzzl", "xjhzpzhm", "rk_rq" };// MAP中的KEY
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				Excel.createWorkBook(lists, keys, columnNames).write(os);

			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
			ServletOutputStream out;

			out = response.getOutputStream();

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
				e.printStackTrace();
			} finally {

				if (bis != null)
					bis.close();
				if (bos != null)
					bos.flush();
				bos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// 导出 maplist table表格数据的查询
	@RequestMapping("outSdbg3.do")
	public void outSdbg3(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）

			// 获取页面传递过来的input中的值
			String fz = getValue(request.getParameter("fz"));
			// 获取表单中的数据
			String tableName = getValue(request.getParameter("tableName"));
			String form = getValue(request.getParameter("form"));
			String rk_rq2 = getValue(request.getParameter("drrq"));
			String arr[] = rk_rq2.split("-");
			String rk_rq = arr[0] + arr[1];

			String sqlcs = "";

			JSONArray form1 = JSONArray.parseArray(form);
			// 下标从一开始取值
			if (form1.size() > 1) {
				for (int i = 1; i < form1.size(); i++) {
					//JSONObject obj = (JSONObject) form1.get(i);
					JSONObject obj = (JSONObject) JSONObject.toJSON(form1.get(i));
					sqlcs += " " + getValue(obj.get("value"));
				}
			}

			System.out.println("前台传过来的值sql:" + sqlcs);
			String sql = "select * from "+tableName+" where 1=1 and (- rk_rq=?-) ";// and rownum<=100
			sql += sqlcs;

//			if (!rk_rq.equals("")) {
//				sql += " and rk_rq='" + rk_rq + "' ";
//
//			}

			if (!fz.equals("")) {

				if (fz.equals("sjje")) {
					sql = "select t." + fz + ","
							+ "SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from "+tableName+" t where 1=1 and (- rk_rq=?-)";

				} else {
					sql = "select t." + fz + ",sum(t.sjje) sjje,"
							+ "SUM(t.zyj) zyj,SUM(t.ssj) ssj,SUM(t.dsj) dsj,SUM(t.xzj) xzj from "+tableName+" t where 1=1 and (- rk_rq=?-)";
				}

				sql += sqlcs;
//				if (!rk_rq.equals("")) {
//					sql += " and rk_rq='" + rk_rq + "' ";
//
//				}
				
				sql += " group by t." + fz + " ";
			}

			// 总数据分页查询
			// sql = "select row_.*, rownum rowno from (" + sql + ") row_ ";
			sql = this.getSql2(sql, rk_rq);
			System.out.println(sql);
			long time = System.currentTimeMillis();
			List<Map<String, Object>> sjjgall = bs.query(sql);
			System.err.println("查询耗时"+(((System.currentTimeMillis()-time))/1000)+"秒");
			String fileName = "个人所得税原始数据";

			String columnNames[] = { "纳税人识别号", "纳税人名称", "征收项目", "征收品目", "征收品目代码", "税款所属期起", "税款所属期止", "登记注册类型", "税收管理员",
					"行业门类", "行业大类", "行业中类", "行业", "征收代理方式", "计税依据", "课税数量", "税率", "实缴金额", "税款所属税务机关", "主管税务所", "预算科目代码",
					"预算科目", "预算分配比例", "中央级比例", "省市级比例", "地市级比例", "区县级比例", "乡镇级比例", "中央待分配比例", "地市待分配比例", "省局待分配比例",
					"收款国库", "开具日期", "上解日期", "上解销号日期", "上解销号人", "入库销号日期", "入库销号人", "票证种类", "票证字轨", "票证号码", "税款种类",
					"税款属性", "街道乡镇", "银行行别", "银行营业网点", "银行账号", "汇总日期", "汇总人", "汇总票证种类", "汇总票证字轨", "汇总票证号码", "调账类型",
					"缴款期限", "征收税务机关", "电子税票号码", "中央级", "省市级", "地市级", "区县级", "乡镇级", "中央待分配", "省级待分配", "地市待分配", "登记序号",
					"导入日期" };// 列名
			// String
			// columnNames2[]={"","","","","","","","","","","","全日制教育","在职教育","","","","",""};
			String keys[] = { "NSRSBH", "NSRMC", "ZSXMMC", "ZSPM", "ZSPMDM", "SKSHQQ", "SKSHQZ", "DJZCLX", "SSGLY",
					"HYML", "HYDL", "HYZL", "HY", "ZSDLFS", "JSYJ", "KSSL", "SL", "SJJE", "SKSSSWJG", "ZGSWS", "YSKMDM",
					"YSKM", "YSFPBL", "ZYJBL", "SSJBL", "DSJBL", "QXJBL", "XZJBL", "ZYDFPBL", "DSDFPBL", "SJDFPBL",
					"SKGK", "KJRQ", "SJRQ", "SJXHRQ", "SJXHR", "RKXHRQ", "RKXHR", "PZZL", "PZZG", "PZHM", "SKZL",
					"SKSX", "JDXZ", "YHHB", "YHYYWD", "YHZH", "HZRQ", "HZR", "HZPZZL", "HZPZZG", "HZPZHM", "TZLX",
					"JKQX", "ZSSWJG", "DZSPHM", "ZYJ", "SSJ", "DSJ", "QXJ", "XZJ", "ZYDFP", "SJDFP", "DSDFP", "DJXH",
					"RK_RQ" };// MAP中的KEY
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				long time1 = System.currentTimeMillis();
				Excel3.createWorkBook(sjjgall, keys, columnNames).write(os);
				System.err.println("导出耗时"+(((System.currentTimeMillis()-time1))/1000)+"秒");
			} catch (IOException e) {
				e.printStackTrace();
			}
			long time1 = System.currentTimeMillis();
			//EasyExcelUtil.writeExcelWithStringList(os, sjjgall, table, excelTypeEnum);
//			Excel2.createWorkBook(os,sjjgall, keys, columnNames);
//			System.err.println("导出耗时"+(((System.currentTimeMillis()-time1))/1000)+"秒");
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
			ServletOutputStream out;

			out = response.getOutputStream();

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
				e.printStackTrace();
			} finally {

				if (bis != null)
					bis.close();
				if (bos != null)
					bos.flush();
				bos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
