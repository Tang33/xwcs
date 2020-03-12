package fast.app;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
 

public class TestIo {

	 

	
	 
		/**
		 * 读文件的方法
		 * @param fName文件绝对路径
		 */
		public void readFile(String fName){
			try {
				FileInputStream  fis = new FileInputStream(fName);
				int n=fis.read();//读取下一个字节
				//循环读写
				while(n!=-1){
					System.out.println("读到的字节是"+n);
				    n = fis.read();
				}
				fis.close();//关闭输入流
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * 写文件的方法
		 * @param msg写入的内容
		 * @param s文件绝对路径
		 * @throws Exception抛出异常
		 */
		public void writeFile(String msg,String s) throws Exception{
			try {
				FileOutputStream fos = new FileOutputStream(s,false);
				byte[] b = msg.getBytes();//得到组成字符串的字节
				fos.write(b);
				fos.close();//关闭输出流 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args) {
			TestIo tjm = new TestIo();
			tjm.readFile("E://类与对象总结.txt");
			try {//此语句运行两次必须改写入文件的文件名或删除上次写入的文件，否则报错（已有该文件）
				tjm.writeFile("hello! 你好！~~~~", "E://写入的文件1.txt");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
	
	
}
