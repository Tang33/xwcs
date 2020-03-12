package fast.app;


import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.util.Super;

public class User extends Super{

	
	public String init(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			return "xtgl/User";
		}catch(Exception e){
			e.printStackTrace();
			return "xtgl/User";
		}
	}
	
	public String query(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String userName=getValue(this.getForm().get("userName"));
			String nickName=getValue(this.getForm().get("nickName"));
			String pageNo=getValue(this.getForm().get("pageNo"));
			String pageSize=getValue(this.getForm().get("pageSize"));
			String sql=getSql("user_query",new Object[]{userName,nickName});
			List<Map<String, Object>> list = this.getBs().query(sql,pageNo,pageSize);
			int count = this.getBs().queryCount(sql);
			return this.toJson("000", "查询成功！", list,count);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	public String querydw(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String sql="select * from xtgl_dw ";
			List<Map<String, Object>> list = this.getBs().query(sql);
			return this.toJson("000", "查询成功！", list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	public String del(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uuid=getValue(this.getForm().get("uuid"));
			this.getBs().delete(this.getSql("user_del", new Object[] {uuid}));
			System.out.println("打印一下:"+this.getSql("user_del", new Object[] {uuid}));
			return this.toJson("000", "查询成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	
	public String update(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uuid=getValue(this.getForm().get("uuid1"));
			String uno=getValue(this.getForm().get("uno1"));
			String uname=getValue(this.getForm().get("uname1"));
			String upwd=getValue(this.getForm().get("upwd1"));
			String yxbz=getValue(this.getForm().get("yxbz3"));
			String ssdw=getValue(this.getForm().get("ssdwedit"));
			String sql="select * from fast_user where uuid='"+uuid+"'";
			System.out.println(sql);
			List<Map<String, Object>> list= this.getBs().query(sql);
			if(list!=null&&list.size()>0) {
				String uupwd=(String) list.get(0).get("upwd");
				if(upwd==null||upwd.trim().equals("")) {
					upwd=uupwd;
				}else {
					if(uupwd!=null&&!uupwd.equals(upwd)) {
						upwd = DigestUtils.md5Hex(upwd); // 密码
						System.out.println("加密后" + upwd);
					}
				}
			}else {
				System.out.println("---");
			}
			
			this.getBs().update(this.getSql("user_update", new Object[]{uno,uname,upwd,yxbz,ssdw,uuid}));
			return this.toJson("000", "查询成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	public String add(Map<String, Object> rmap){
		try{
			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String uno=getValue(this.getForm().get("uno"));
			String uname=getValue(this.getForm().get("uname"));
			String upwd=getValue(this.getForm().get("upwd"));
			String yxbz=getValue(this.getForm().get("yxbz"));
			String ssdw=getValue(this.getForm().get("ssdw"));
			upwd = DigestUtils.md5Hex(upwd); // 密码
			System.out.println("加密后" + upwd);
			if(yxbz.equals("on")) {
				yxbz="Y";
			}else if(yxbz.equals("off")) {
				yxbz="N";
			}
			this.getBs().insert(this.getSql("user_add", new Object[]{uno,uname,upwd,yxbz,ssdw}));
			return this.toJson("000", "查询成功！");
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
}
