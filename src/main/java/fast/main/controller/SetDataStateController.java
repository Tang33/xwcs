package fast.main.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

@Controller
public class SetDataStateController extends Super {
	@Autowired
	BaseService bs;

	// 设置可查不可查
	@RequestMapping("SetDataState_doState.do")
	@ResponseBody
	public String doState(HttpServletRequest request, HttpServletResponse response) {
		try {
			String rkrq = getValue(request.getParameter("rkrq")).replaceAll("-", "");
			;
			String state = getValue(request.getParameter("State"));
			String updSql = "";
			String sql = "select 1 from xwcs_cxzt where to_char(rkrq,'yyyymm')='" + rkrq + "' ";
			List<Map<String, Object>> list = bs.query(sql);
			if (list != null && list.size() > 0) {
				updSql = "update xwcs_cxzt set state='" + state + "' where to_char(rkrq,'yyyymm')='" + rkrq + "' ";
				bs.update(updSql);
			} else {
				updSql = "insert into xwcs_cxzt(rkrq,state) values(to_date('" + rkrq + "','yyyymm'),'" + state + "')";
				bs.insert(updSql);
			}

			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	// 设置可查不可查
	@RequestMapping("SetDataState_add.do")
	@ResponseBody
	public String add(HttpServletRequest request, HttpServletResponse response) {
		try {
			String rkrq = getValue(request.getParameter("rkrqadd")).replaceAll("-", "");
			;
			String sql = "insert into xwcs_cxzt(rkrq,state) values(to_date('" + rkrq + "','yyyymm'),'0')";
			bs.insert(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
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

	@RequestMapping("SetDataState_query.do")
	@ResponseBody
	public String query(HttpServletRequest request, HttpServletResponse response) {
		try {
			String pageNo = getValue(request.getParameter("pageNo"));
			String pageSize = getValue(request.getParameter("pageSize"));
			String yf = getValue(request.getParameter("rkrq"));
			String sql = "select to_Char(rkrq,'yyyyMM') rkrq,state " + "from XWCS_CXZT ";
			if (!yf.equals("")) {
				sql += " where to_Char(rkrq,'yyyyMM')='" + yf + "'";
			}
			sql += " order by rkrq";
			List<Map<String, Object>> list = bs.query(sql, pageNo, pageSize);
			int count = bs.queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String GetNowDate() {
		String temp_str = "";
		Date dt = new Date();
		// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
		temp_str = sdf.format(dt);
		return temp_str;
	}

}
