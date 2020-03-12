package fast.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.conf.ApiBody;
import fast.main.service.BaseService;
import fast.main.util.Super;

/**
 * 系统登录
 *
 */
@Controller
@RequestMapping("login")
public class LoginsController extends Super {
	
	@Autowired
	BaseService bs;
	
	/**
	 * 系统账号密码验证登录
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name = "登录功能", context = "登录，存session", type = "Post")
	public String init(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			
			String activeUser = getValue(rmap.get("activeUser"));
			String activePwd = getValue(rmap.get("activePwd"));
			activePwd = DigestUtils.md5Hex(activePwd); // 密码
			System.out.println("加密后" + activePwd);
			String sql = "select * from fast_user where 1=1 and uno=? and upwd=? and YXBZ = 'Y'";
			sql=this.getSql2(sql,new Object[] {activeUser,activePwd});
			List<Map<String, Object>> list = bs.query(sql);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				resultMap = list.get(0);
				request.getSession().setAttribute("user", resultMap);
				HttpSession session = request.getSession();
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
	
	/**
	 * 退出系统登录
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/LoginOut.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	@ApiBody(name = "登出功能", context = "登出，删session", type = "Post")
	public String LoginOut(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			
			request.getSession().removeAttribute("user");
			request.getSession().removeAttribute("uuid");
			request.getSession().removeAttribute("uno");
			request.getSession().removeAttribute("uname");
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
}
