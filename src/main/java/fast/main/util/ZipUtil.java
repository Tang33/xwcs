package fast.main.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {
	/**
	 * 
	 * zip解压
	 * 
	 * @param srcFile
	 *            zip源文件
	 * 
	 * @param destDirPath
	 *            解压后的目标文件夹
	 * 
	 * @throws RuntimeException
	 *             解压失败会抛出运行时异常
	 * 
	 */

	public static void unZip(File srcFile, String destDirPath) throws RuntimeException {

		long start = System.currentTimeMillis();

		// 判断源文件是否存在

		if (!srcFile.exists()) {

			throw new RuntimeException(srcFile.getPath() + "所指文件不存在");

		}

		// 开始解压

		ZipFile zipFile = null;

		try {

			zipFile = new ZipFile(srcFile);

			Enumeration<?> entries = zipFile.entries();

			while (entries.hasMoreElements()) {

				ZipEntry entry = (ZipEntry) entries.nextElement();

				System.out.println("解压" + entry.getName());

				// 如果是文件夹，就创建个文件夹

				if (entry.isDirectory()) {

					String dirPath = destDirPath + "/" + entry.getName();

					File dir = new File(dirPath);

					dir.mkdirs();

				} else {

					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去

					File targetFile = new File(destDirPath + "/" + entry.getName());

					// 保证这个文件的父文件夹必须要存在

					if (!targetFile.getParentFile().exists()) {

						targetFile.getParentFile().mkdirs();

					}

					targetFile.createNewFile();

					// 将压缩文件内容写入到这个文件中

					InputStream is = zipFile.getInputStream(entry);

					FileOutputStream fos = new FileOutputStream(targetFile);

					int len;

					byte[] buf = new byte[2048];

					while ((len = is.read(buf)) != -1) {

						fos.write(buf, 0, len);

					}

					// 关流顺序，先打开的后关闭

					fos.close();

					is.close();

				}

			}

			long end = System.currentTimeMillis();

			System.out.println("解压完成，耗时：" + (end - start) + " ms");

		} catch (Exception e) {

			throw new RuntimeException("unzip error from ZipUtils", e);

		} finally {

			if (zipFile != null) {

				try {

					zipFile.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

		}

	}

	public static String unZipnew(String descDir, String zipFilePath) {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		// 解决zip文件中有中文目录或者中文文件
		ZipFile zip;
		try {
			File zipFile = new File(zipFilePath);
			zip = new ZipFile(zipFile, Charset.forName("GBK"));
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				// 输出文件路径信息
				System.out.println(outPath);
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			zip.close();
			zipFile.delete();
			File[] fileList = new File(descDir).listFiles();
			for (int j = 0; j < fileList.length; ++j) {
				if (fileList[j].isDirectory()) {
					onlyDeleteDir(fileList[j], descDir);
				}
			}
			String fileNames = "";
			File[] fileList_final = new File(descDir).listFiles();
			for (int m = 0; m < fileList_final.length; m++) {
				if (fileList_final[m].getPath().replaceAll(".*(/|\\\\)", "").equals("ChromeSetup.exe")) {
					continue;
				}

				if (fileNames.equals("")) {
					fileNames = fileList_final[m].getPath();
				} else {
					fileNames = fileNames + "$_$" + fileList_final[m].getPath();
				}
			}
			System.out.println("******************解压完毕********************");
			return fileNames;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static boolean onlyDeleteDir(File dir, String target) throws IOException {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = onlyDeleteDir(new File(dir, children[i]), target);
				if (!success) {
					return false;
				}
			}
		} else {
			System.out.println(dir.getPath().replaceAll(".*(/|\\\\)", ""));
			copyFile(dir, new File(target + dir.getPath().replaceAll(".*(/|\\\\)", "")));
		}
		return dir.delete();
	}

	private static void copyFile(File sourceFile, File targetFile) {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input;
		try {
			input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);
			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			try {
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
			// 关闭流
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
