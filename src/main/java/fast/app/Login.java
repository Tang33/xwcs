package fast.app;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import fast.main.conf.ApiBody;
import fast.main.conf.ApiHead;
import fast.main.util.Super;

@ApiHead(value="登录操作类")
public class Login extends Super {

	@ApiBody(name = "登录功能", context = "登录，存session", type = "Post")
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			String activeUser = getValue(this.getForm().get("activeUser"));
			String activePwd = getValue(this.getForm().get("activePwd"));
			activePwd = DigestUtils.md5Hex(activePwd); // 密码
			System.out.println("加密后" + activePwd);
			String sql=this.getSql("login_query",new Object[] {activeUser,activePwd});
			List<Map<String, Object>> list = this.getBs().query(sql);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				resultMap = list.get(0);
				this.getRequest().getSession().setAttribute("user", resultMap);
				HttpSession session = this.getRequest().getSession();
				// 把用户数据保存在session域对象中
				session.setAttribute("id", resultMap.get("UUID"));
				session.setAttribute("uno", resultMap.get("UNO"));
				session.setAttribute("uname", resultMap.get("UNAME"));
				session.setAttribute("dwid", resultMap.get("DWID"));
				return this.toJson("000", "查询成功！", resultMap, 0);
			} else {
				return this.toJson("002", "查询成功！", resultMap, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	@ApiBody(name = "登出功能", context = "登出，删session", type = "Post")
	public String LoginOut(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);
			this.getRequest().getSession().removeAttribute("user");
			this.getRequest().getSession().removeAttribute("uuid");
			this.getRequest().getSession().removeAttribute("uno");
			this.getRequest().getSession().removeAttribute("uname");
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}


}
