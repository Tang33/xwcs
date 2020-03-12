package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.Super;

public class gwgl extends Super {
	private Map<String, Object> user = null;

	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");

			return "xtgl/gwgl";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/gwgl";
		}
	}

	public String querycd(Map<String, Object> rmap) {
		try {
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String sql = getSql("gncd_query_fcd");
			List<Map<String, Object>> resultlist = this.getBs().query(sql);
			if (resultlist != null && resultlist.size() > 0) {
				for (int i = 0; i < resultlist.size(); i++) {
					Map<String, Object> map = resultlist.get(i);
					String gndm = String.valueOf(map.get("GNDM"));
					if (gndm != null && !gndm.trim().equals("")) {
						sql = getSql("gncd_query_zcd", new Object[] { gndm });
						List<Map<String, Object>> gnlist = this.getBs().query(sql);
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

	public String queryry(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String name = getValue(this.getForm().get("findtext")).toUpperCase();
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			String sql = " select t.*,(select ssdw_mc from xtgl_dw where ssdw_dm=t.sjdw_dm) as sjdwmc from XTGL_DW t where ssdw_mc like '%"
					+ name + "%' order by ssdw_dm ";
			List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！", new ArrayList<Map<String, Object>>(), 0);
		}
	}

	public String checkzgw(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("id")).toUpperCase();
			String sql = "select * from xtgl_dw where sjdw_dm='" + id + "'";
			List<Map<String, Object>> list = this.getBs().query(sql);
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

	public String checkgwry(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("id")).toUpperCase();
			String sql = "select * from fast_user where dwid='" + id + "'";
			List<Map<String, Object>> list = this.getBs().query(sql);
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

	public List<Map<String, Object>> gw() {
		String sqllevel = getSql("fast_gwgl_querylevel");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> levels = this.getBs().query(sqllevel);
		List<Map<String, Object>> addlist = new ArrayList<Map<String, Object>>();
		Map<String, Object> all = new HashMap<String, Object>();
		if (levels != null) {
			for (int i = 0; i < levels.size(); i++) {
				String level = levels.get(i).get("LEL").toString();
				String sqllevellist = getSql("fast_gwgl_querylevelgw", new Object[] { level });
				List<Map<String, Object>> levellist = this.getBs().query(sqllevellist);
				all.put(level, levellist);
			}
			for (int i = 0; i < all.size(); i++) {
				String level = levels.get(i).get("LEL").toString();
				List<Map<String, Object>> levellist = (List<Map<String, Object>>) all.get(level);
				if (level.equals("0")) {
					for (int j = 0; j < levellist.size(); j++) {
						list.add(levellist.get(j));
					}
				} else {
					String nextlevel = levels.get(i + 1).get("LEL").toString();
					List<Map<String, Object>> nextlevellist = (List<Map<String, Object>>) all.get(nextlevel);
					for (int j = 0; j < nextlevellist.size(); j++) {
						int index = -1;
						for (int j2 = 0; j2 < levellist.size(); j2++) {
							if (levellist.get(j2).get("SJGW").toString()
									.equals(nextlevellist.get(j).get("ID").toString())) {
								addlist.add(levellist.get(j2));
								index = j;
							}
						}
						if (index >= 0) {
							((List<Map<String, Object>>) all.get(nextlevel)).get(j).put("children", addlist);
							addlist = new ArrayList<Map<String, Object>>();
						}
					}

				}

			}
		}
		return list;
	}

	public String querygw(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String sql = " select t.*,(select ssdw_mc from xtgl_dw where ssdw_dm=t.sjdw_dm) as sjdwmc from XTGL_DW t ";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String querytree(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			List<Map<String, Object>> list = gw();
			/*
			 * if (all != null) { for (int i = 0; i < all.size(); i++) { String id =
			 * all.get(i).get("id").toString(); String sql =
			 * getSql("fast_gwgl_queryxjgw",new Object[] {id}); List<Map<String, Object>>
			 * gwlist= this.getBs().query(sql); all.get(i).put("children", gwlist); } }
			 */
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String del(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("id")).toUpperCase();
			String sql = "delete from xtgl_dw where ssdw_dm='" + id + "'";
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String sjdw(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String sql = " select t.* from XTGL_DW t where t.sjdw_dm is null ";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String update(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("idedit")).toUpperCase();
			String dwdm = getValue(this.getForm().get("dwdmedit")).toString();
			String gwmc = getValue(this.getForm().get("gwmcedit")).toString();
			String sjdw = getValue(this.getForm().get("sjdwedit")).toString();
			String dwlx = getValue(this.getForm().get("dwlxedit")).toString();
			String sql = "Update xtgl_dw set ssdw_dm='"+dwdm+"',ssdw_mc='" + gwmc + "',sjdw_dm='" + sjdw + "',ssdw_lx='" + dwlx
					+ "' where ssdw_dm='" + id + "'";
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String queryids(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("id")).toUpperCase();
			String sqlids = "select * from fast_qxgl where dwid='" + id + "'";
			List<Map<String, Object>> ids = this.getBs().query(sqlids);
			return this.toJson("000", "查询成功！", ids);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String updateqx(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String id = getValue(this.getForm().get("ids")).toUpperCase();
			String gwid = getValue(this.getForm().get("gwidqx")).toUpperCase();
			String sqldel = "delete from fast_qxgl where dwid='" + gwid + "'";
			this.getBs().update(sqldel);
			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (!ids[i].equals("")) {
					String sql = "insert into fast_qxgl(uuid,dwid,cdid) VALUES(seq_fast_qxgl.nextval,'" + gwid + "','"
							+ ids[i] + "')";
					this.getBs().update(sql);
				}

			}
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	public String add(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			user = (Map<String, Object>) this.getRequest().getSession().getAttribute("user");
			String dwdm = getValue(this.getForm().get("dwdm")).toString();
			String gwmc = getValue(this.getForm().get("gwmc")).toString();
			String sjdw = getValue(this.getForm().get("sjdw")).toString();
			String dwlx = getValue(this.getForm().get("dwlx")).toString();
			String sql = "insert into xtgl_dw(ssdw_dm,ssdw_mc,sjdw_dm,ssdw_lx) values('"+dwdm+"','"+gwmc+"','"+sjdw+"','"+dwlx+"')";
			this.getBs().insert(sql);
			return this.toJson("000", "查询成功！");
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
