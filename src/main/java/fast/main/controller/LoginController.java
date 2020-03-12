package fast.main.controller;

import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页控制
 * 
 * @author tenviy
 * 
 */

public class LoginController implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// 执行完毕，返回前拦截
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// 在处理过程中，执行拦截
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// 在拦截点执行前拦截，如果返回true则不执行拦截点后的操作（拦截成功）
		// 返回false则不执行拦截
		HttpSession session = request.getSession();
		 String uri = request.getRequestURI(); // 获取登录的uri，这个是不进行拦截的
		String params = request.getParameter("ctrl");
		if(params!=null  ) {
		if (params.contains("Login")) {
			// 登录成功不拦截
			return true;
		}
		if (params.contains("index")) {
			// 登录成功不拦截
			return true;
		}}
		// if(session.getAttribute("LOGIN_USER")!=null ||
		// uri.indexOf("system/login")!=-1) {// 说明登录成功 或者 执行登录功能
		if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){ //如果是ajax请求响应头会有x-requested-with  
			
            if(session.getAttribute("user") == null) {
            	ServletOutputStream out = response.getOutputStream();  
            	 out.print("loseSession");//session失效
                 out.flush();
                 return false;
            }
           
        }

		if (session.getAttribute("user") != null) {
			// 登录成功不拦截
			return true;
		} else {
			// 拦截后进入登录页面
			request.getRequestDispatcher("login/unLogin.do").forward(request, response);
			return false;
		}
	}
}
