package fast.main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;
@Controller
@RequestMapping("gwgl")
public class DwglController extends Super{
@Autowired
BaseService bs;
private Map<String, Object> user = null;
/**
 * 查询权限管理
 * @param request
 * @param response
 * @param form
 * @return
 */
@RequestMapping("/queryids.do")
@ResponseBody
public String queryids(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {

	try {
		String id = getValue(form.get("id"));
		String sqlids = "select * from fast_qxgl where dwid='" + id + "'";
		List<Map<String, Object>> ids = bs.query(sqlids);
		return this.toJson("000", "查询成功！", ids);
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}

@RequestMapping("querycd.do")
@ResponseBody
public String querycd(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from fast_gncd where 1=1 and sjgndm='0' order by px";
		List<Map<String, Object>> resultlist = bs.query(sql);
		if (resultlist != null && resultlist.size() > 0) {
			for (int i = 0; i < resultlist.size(); i++) {
				Map<String, Object> map = resultlist.get(i);
				String gndm = String.valueOf(map.get("GNDM"));
				if (gndm != null && !gndm.trim().equals("")) {
					sql = " select * from fast_gncd where 1=1 and (-sjgndm=?-)  order by px";
					sql =this.getSql2(sql, gndm);
					List<Map<String, Object>> gnlist = bs.query(sql);
					map.put("gnlist", gnlist);
				}
				list.add(map);
			}
		}
		return this.toJson("000", "查询成功！", list);
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}

@RequestMapping("sjdw.do")
@ResponseBody
public String sjdw(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		String sql = " select t.* from XTGL_DW t where t.sjdw_dm is null ";
		List<Map<String, Object>> list = bs.query(sql);
		return this.toJson("000", "查询成功！", list);
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}

@RequestMapping("add.do")
@ResponseBody
public String add(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		String dwdm = getValue(form.get("dwdm")).toString();
		String gwmc = getValue(form.get("gwmc")).toString();
		String sjdw = getValue(form.get("sjdw")).toString();
		String dwlx = getValue(form.get("dwlx")).toString();
		String sql = "insert into xtgl_dw(ssdw_dm,ssdw_mc,sjdw_dm,ssdw_lx) values('"+dwdm+"','"+gwmc+"','"+sjdw+"','"+dwlx+"')";
		System.out.println(sql);
		bs.insert(sql);
		return this.toJson("000", "查询成功！");
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}
	
@RequestMapping("updateqx.do")
@ResponseBody
public String updateqx(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {

		String id = getValue(form.get("ids")).toUpperCase();
		String gwid = getValue(form.get("gwid"));
		String sqldel = "delete from fast_qxgl where dwid='" + gwid + "'";
		bs.update(sqldel);
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			if (!ids[i].equals("")) {
				String sql = "insert into fast_qxgl(uuid,dwid,cdid) VALUES(seq_fast_qxgl.nextval,'" + gwid + "','"
+ ids[i] + "')";
				
				bs.update(sql);
				System.out.println(sql);
			}

		}
		return this.toJson("000", "查询成功！");
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}

@RequestMapping("update.do")
@ResponseBody
public String update(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		String id = getValue(form.get("idedit")).toUpperCase();
		String dwdm = getValue(form.get("dwdmedit")).toString();
		String gwmc = getValue(form.get("gwmcedit")).toString();
		String sjdw = getValue(form.get("sjdwedit")).toString();
		String dwlx = getValue(form.get("dwlxedit")).toString();
		String sql = "Update xtgl_dw set ssdw_dm='"+dwdm+"',ssdw_mc='" + gwmc + "',sjdw_dm='" + sjdw + "',ssdw_lx='" + dwlx
				+ "' where ssdw_dm='" + id + "'";
		bs.update(sql);
		return this.toJson("000", "查询成功！");
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}

@RequestMapping("queryry.do")
@ResponseBody
public String queryry(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		String name = getValue(form.get("findtext"));
		String pageNo = getValue(form.get("page"));
		String pageSize = getValue(form.get("limit"));
		String sql = " select t.ssdw_dm, t.ssdw_mc, decode(t.SJDW_DM,'null','',t.SJDW_DM) SJDW_DM, decode(t.ssdw_lx,'0','区','街道') SSDW_LX,(select ssdw_mc from xtgl_dw where ssdw_dm=t.sjdw_dm) as sjdwmc1 from XTGL_DW t where ssdw_mc like '%"
				+ name + "%'  order by ssdw_dm ";
		List<Map<String, Object>> list = bs.query(sql, pageNo, pageSize);
		int count = bs.queryCount(sql);
		return this.toJson("000", "查询成功！", list, count);
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！", new ArrayList<Map<String, Object>>(), 0);
	}
}
@RequestMapping("checkzgw.do")
@ResponseBody
public String checkzgw(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		String id = getValue(form.get("SSDW_DM"));
		String sql = "select * from xtgl_dw where sjdw_dm='" + id + "'";
		List<Map<String, Object>> list = bs.query(sql);
		if (list != null && list.size() > 0) {
			return this.toJson("002", "有下级！");
		} else {
			return this.toJson("000", "查询成功！");
		}
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}
@RequestMapping("checkgwry.do")
@ResponseBody
public String checkgwry(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try {
		String id = getValue(form.get("id")).toUpperCase();
		String sql = "select * from fast_user where dwid='" + id + "'";
		List<Map<String, Object>> list = bs.query(sql);
		if (list != null && list.size() > 0) {
			return this.toJson("002", "有人员！");
		} else {
			return this.toJson("000", "查询成功！");
		}
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}
@RequestMapping("del.do")
@ResponseBody
public String del(HttpServletRequest request , HttpServletResponse response,
		@RequestParam Map<String, Object> form) {
	try{
		String id = getValue(form.get("SSDW_DM"));
		String sql = "delete from xtgl_dw where ssdw_dm='" + id + "'";
		bs.update(sql);
		return this.toJson("000", "查询成功！");
	} catch (Exception e) {
		e.printStackTrace();
		return this.toJson("009", "查询失败！");
	}
}

}
