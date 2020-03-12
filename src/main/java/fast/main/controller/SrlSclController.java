package fast.main.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fast.main.service.BaseService;

@Controller
public class SrlSclController {
	@Autowired 
	BaseService bs;
	
	
		
		public static String getJsonString(String fileName,HttpServletRequest request) {
			
			fileName="C:\\Users\\WN\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp3\\wtpwebapps\\xwcs\\upload\\rksj\\1573544364093.xls";
			
			StringBuffer sb = new StringBuffer();

	    	InputStream inputStream;
	        FileReader fr = null;
	        try {
	        	inputStream = request.getInputStream();

	     		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
	            fr = new FileReader(fileName);
	            BufferedReader br = new BufferedReader(fr);
	            String s="";
	    
	            while ((s = br.readLine()) != null) {
	                sb.append(s).append("\n");;
	             
	            }
	            reader.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	 //写入文件
	            	        String targetPath = "C:\\Users\\WN\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp3\\wtpwebapps\\xwcs\\upload\\rksj\\cshi.txt";//目标文件路径
	            	        File f = new File(targetPath);//新建文件
	            	        try {
	            	             BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	            	             bw.write(sb.toString());
	            	             bw.flush();
	            	             bw.close();
	            	         }
	            	         catch(Exception e) {
	            	             System.out.println(e.getMessage());
	            	         }
	                fr.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
		  
		public static void byteOutStream(String msg,String idno) throws Exception {

	        //1:使用File类创建一个要操作的文件路径
	        File file = new File("C:" + File.separator + "demo" + File.separator + idno+".xsl");
	        if(!file.getParentFile().exists()){ //如果文件的目录不存在
	            file.getParentFile().mkdirs(); //创建目录

	        }
	        byte[] buf = new byte[1024];

	        //2: 实例化OutputString 对象
	        OutputStream output = new FileOutputStream(file);

	        //3: 准备好实现内容的输出
	        //将字符串变为字节数组d
	        byte data[] = msg.getBytes();
	        output.write(data);
	        //4: 资源操作的最后必须关闭
	        output.close();
	    }


	}

