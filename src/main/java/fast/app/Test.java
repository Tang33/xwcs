package fast.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test {
	public void doGet(HttpServletRequest request, HttpServletResponse response,File filenames)
			throws ServletException, IOException {


		filenames =new  File("E:\\ceshiliu\\Export_10070082(5001_10000).xls");


		//设置文件ContentType类型，这样设置，会自动判断下载文件类型  
		response.setContentType("multipart/form-data");
		//设置编码格式
		response.setCharacterEncoding("UTF-8");
		//设置可以识别Html文件  
		response.setContentType("txt/html");
		// 2.设置文件头：最后一个参数是设置下载文件名  
		//response.setHeader("Content-Disposition", "attachment;filename="+filenames+".xls");
		response.setHeader("Content-Disposition", "attachment;fileName=" + "filenames" + ".xls");
		//可以设置成.pdf格式 ：response.setHeader("Content-Disposition", "attachment;fileName=" + “文件名” + ".pdf");
		OutputStream out = new FileOutputStream("E:\\ceshiliu\\new.xls");


		FileInputStream fileinput = new FileInputStream(filenames);
		try {
			
			int b = 0;
			byte[] buffer = new byte[1024];
			while ((b = fileinput.read(buffer)) != -1) {
				// 4.写到输出流(out)中  
				out.write(buffer, 0, b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			fileinput.close();
			out.flush();
			out.close();
		}
	}
}