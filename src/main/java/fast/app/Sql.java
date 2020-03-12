package fast.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fast.main.conf.ApiBody;
import fast.main.conf.ApiHead;
import fast.main.util.Super;
@ApiHead(value="Sql操作类")
public class Sql extends Super {
	@ApiBody(name = "初始化Sql页面", context = "初始化sql下拉框", type = "Post")
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			
			return "xtgl/Sql";
		} catch (Exception e) {
			e.printStackTrace();
			return "xtgl/Sql";
		}
	}
    @ApiBody(name = "初始化Sql页面", context = "初始化sql下拉框", type = "Post")
	public String csh(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			List<String> list = new ArrayList<String>();
			String sql = getSql("sql_key");
			List<Map<String, Object>> resultList = this.getBs().query(sql);
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> map = resultList.get(i);
					String key = getValue(map.get("TYPE"));
					list.add(key);
				}
			}
			return this.toJson("000", "查询成功！", list, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name = "分页查询Sql语句", context = "根据类型和key分页查询.type,key", type = "Post")
	public String query(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			Map mm= this.getForm();
			String tj_type = getValue(this.getForm().get("type"));
			String tj_key = getValue(this.getForm().get("key"));
			String pageNo = getValue(this.getForm().get("pageNo"));
			String pageSize = getValue(this.getForm().get("pageSize"));
			String sql = getSql("sql_query", new Object[] { tj_type, tj_key });
			List<Map<String, Object>> list = this.getBs().query(sql, pageNo, pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name = "删除Sql语句", context = "根据key删除sql", type = "Post")
	public String del(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String key = getValue(this.getForm().get("key"));
			String sql = this.getSql("sql_del", new Object[] { key });
			int i = this.getBs().delete(sql);

			return this.toJson("000", "查询成功！", i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name = "修改Sql语句", context = "根据key修改sql", type = "Post")
	public String update(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String key = getValue(this.getForm().get("key"));
			String value = getValue(this.getForm().get("value"));
			value = value.replaceAll("'", "''");
			value = value.replaceAll("[?]", "------");
			String sql = this.getSql("sql_update", new Object[] { value, key });
			sql = sql.replaceAll("------", "?");
			int i = this.getBs().update(sql);
			return this.toJson("000", "查询成功！", i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name = "新增改Sql", context = "新增改sql语句", type = "Post")
	public String add(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String type = getValue(this.getForm().get("type"));
			String key = getValue(this.getForm().get("key"));
			String value = getValue(this.getForm().get("value"));
			value = value.replaceAll("'", "''");
			String sql = this.getSql("sql_add", new Object[] { type, key, value });
			int i = this.getBs().insert(sql);
			return this.toJson("000", "查询成功！", i, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

	/**
	 * @param rmap
	 * @return
	 */
	@ApiBody(name = "测试Sql", context = "在线执行sql语句", type = "Post")
	public String test(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			int size = 0;
			Map<String, Object> map = new HashMap<String, Object>();
			String parSize = getValue(this.getForm().get("parSize"));
			String value2 = getValue(this.getForm().get("value2"));
			String pageNo = getValue(this.getForm().get("pageNo2"));
			String pageSize = getValue(this.getForm().get("pageSize2"));

			if (parSize != null && !parSize.equals("")) {
				size = Integer.parseInt(parSize);
				Object[] o = new Object[size];
				for (int i = 1; i <= size; i++) {
					String par = getValue(this.getForm().get("par" + i));
					o[i - 1] = par;
					// value2=value2.replaceFirst("\\?", "'"+par+"'");
				}
				value2 = this.getSql2(value2, o);
			}
			System.out.println(value2);
			List<Map<String, Object>> list = null;
			int count=-1;
			List<String> hlist = new ArrayList<String>();
			List<List<String>> blist = new ArrayList<List<String>>();
			String msg="";
			if(value2.toLowerCase().lastIndexOf("update")>=0) {
				this.getBs().update(value2);
				msg="修改成功";
			}else if(value2.toLowerCase().lastIndexOf("delete")>=0) {
				this.getBs().delete(value2);
				msg="删除成功";
			}else if(value2.toLowerCase().lastIndexOf("insert")>=0) {
				this.getBs().insert(value2);
				msg="插入成功";
			}else {
				list = this.getBs().query(value2, pageNo, pageSize);
				count= this.getBs().queryCount(value2);
				if (list != null && list.size() > 0) {
					Map<String, Object> hmap = list.get(0);
					Set<String> set = hmap.keySet();
					for (String str : set) {
						hlist.add(str);
					}
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> bmap = list.get(i);
						List<String> child = new ArrayList<String>();
						for (String str : set) {
							String o = getValue(bmap.get(str));
							child.add(o);
						}
						blist.add(child);
					}
				}
				msg="查询成功";
			}
			
			map.put("blist", blist);
			map.put("hlist", hlist);
			return this.toJson("000",msg, map, count);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}

}

