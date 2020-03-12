package fast.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import fast.main.conf.ApiBody;
import fast.main.conf.ApiHead;
import fast.main.util.Super;
@ApiHead(value="菜单操作类")
public class Cd  extends Super{
	

	@ApiBody(name="菜单初始化",context="用于获取菜单的名称和代码返回list-json")
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/Cd";
		}catch(Exception e){
			e.printStackTrace();
			return "xtgl/Cd";
		}
	}
	@ApiBody(name="菜单初始化",context="用于获取菜单的名称和代码返回list-json")
	public String csh(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			List<Object> list=new ArrayList<Object>();
			String sql=getSql("cd_query");
			List<Map<String, Object>> resultList = this.getBs().query(sql);
			if(resultList!=null&&resultList.size()>0){
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> resultmap=resultList.get(i);
					String gnmc=getValue(resultmap.get("GNMC"));
					String gndm=getValue(resultmap.get("GNDM"));
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("gnmc",gnmc);
					map.put("gndm",gndm);
					list.add(map);
				}
			}
			return this.toJson("000", "查询成功！", list, 0);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	@ApiBody(name="菜单分页查询",context="根据gndm分页查询菜单信息.pageNo,pageSize",type="Post")
	public String query(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String pageNo=this.getRequest().getParameter("pageNo");
			String pageSize=this.getRequest().getParameter("pageSize");
			System.out.println(pageNo+","+pageSize);
			String gndmpar=this.getRequest().getParameter("gndm");
			String sql="";
			if(gndmpar!=null&&!gndmpar.equals("")) {
				sql = this.getSql("gncd_query",new Object[] {gndmpar});
			}else {
				sql = this.getSql("gncd_query",new Object[] {""});
			}
			List<Map<String, Object>> list = this.getBs().query(sql,pageNo,pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list, count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
		
	}
	
	@ApiBody(name="菜单删除",context="根据gndm删除菜单",type="Post")
	public String del(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String GNDM=getValue(this.getForm().get("gndm"));
			String sql=this.getSql("cd_del", new Object[]{GNDM});
			this.getBs().delete(sql);
			
			return this.toJson("000", "查询成功！", GNDM, 0);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	@ApiBody(name="菜单信息修改",context="根据uuid修改菜单信息",type="Post")
	public String update(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String gndm=getValue(this.getForm().get("gndm2"));
			String uuid=getValue(this.getForm().get("uuid2"));
			String gnmc=getValue(this.getForm().get("gnmc2"));
			String sj_gndm=getValue(this.getForm().get("sj_gndm2"));
			String px=getValue(this.getForm().get("px2"));
			String url=getValue(this.getForm().get("url2"));
			String sql="UPDATE fast_gncd SET  gndm='"+gndm+"', gnmc='"+gnmc+"', sjgndm='"+sj_gndm+"', px='"+px+"', url='"+url+"' WHERE (uuid='"+uuid+"')";
			int i=this.getBs().update(sql);
			return this.toJson("000", "查询成功！", i, 0);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name="子菜单新增",context="新增子菜单",type="Post")
	public String add(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String type3=getValue(this.getForm().get("type3"));
			String gndm3=getValue(this.getForm().get("gndm3"));
			String gnmc3=getValue(this.getForm().get("gnmc3"));
			String px3=getValue(this.getForm().get("px3"));
			String url3=getValue(this.getForm().get("url3"));
			String yxbz3=getValue(this.getForm().get("yxbz3"));
			int i=this.getBs().insert(this.getSql("cd_addmain",new Object[]{gndm3,gnmc3,type3,px3,url3,yxbz3}));
			return this.toJson("000", "查询成功！", i, 0);		
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	/**
	 * @param rmap
	 * @return
	 */
	@ApiBody(name="菜单启用禁用",context="禁用菜单",type="Post")
	public String test(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String yxbz=getValue(this.getForm().get("yxbz"));
			String gndm=getValue(this.getForm().get("gndm"));
			if(yxbz.equals("Y")){
				yxbz="N";
			}else{
				yxbz="Y";
			}
			String sql=this.getSql("cd_test", new Object[]{yxbz,gndm});
			this.getBs().update(sql);
			return this.toJson("000", "查询成功！",yxbz);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name="主菜单新增",context="新增主菜单",type="Post")
	public String addmain(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String cdm=getValue(this.getForm().get("cdm"));
			String sql=this.getSql("cd_query");
			int count = this.getBs().queryCount(sql);
			int i=this.getBs().insert(this.getSql("cd_addmain",new Object[]{count+1,cdm,0,count+1,"","Y"}));
			return this.toJson("000", "查询成功！",i);
		}catch(Exception e){
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
