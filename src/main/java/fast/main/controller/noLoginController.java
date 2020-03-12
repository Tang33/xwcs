package fast.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fast.main.service.BaseService;
import fast.main.util.Super;
@Controller
@RequestMapping("login")
public class noLoginController extends Super{
	
	@Autowired
	BaseService bs;
	
	@RequestMapping("unLogin.do")
	public String unLogin(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("请求:unLogin....");
		request.getSession().removeAttribute("UserType");
		return "redirect:jsp/login.jsp";
	}

}
