package fast.main.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class Test1{
	public static String getJsonString(String fileName) {
		
		fileName="D:\\测试文件";
		StringBuffer sb = new StringBuffer();
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	  
	public static void byteOutStream(String msg,String idno) throws Exception {

        //1:使用File类创建一个要操作的文件路径
        File file = new File("C:" + File.separator + "demo" + File.separator + idno+".txt");
        if(!file.getParentFile().exists()){ //如果文件的目录不存在
            file.getParentFile().mkdirs(); //创建目录

        }

        //2: 实例化OutputString 对象
        OutputStream output = new FileOutputStream(file);

        //3: 准备好实现内容的输出
        //将字符串变为字节数组d
        byte data[] = msg.getBytes();
        output.write(data);
        //4: 资源操作的最后必须关闭
        output.close();
    }

	/*
	 * public static void main(String fileName) { //String name =
	 * Test.getJsonString(fileName); System.out.println("aaaa"); }
	 */
	
}
