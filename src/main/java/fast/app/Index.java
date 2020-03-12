package fast.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import fast.main.conf.ApiBody;
import fast.main.conf.ApiHead;
import fast.main.conf.SpringBeanProxy;
import fast.main.util.Super;

@ApiHead(value="主页面操作类")
public class Index extends Super {
	@ApiBody(name="初始化主页面",context="根据session初始化整个系统页面",type="Post")
	public String init(Map<String, Object> rmap) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
			initMap(rmap);

			if (this.getRequest().getSession().getAttribute("user") != null) {
				return "index";
			} else {
				return "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "index";
		}
	}

	
				
}
