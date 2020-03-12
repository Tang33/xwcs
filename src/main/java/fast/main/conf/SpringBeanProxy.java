package fast.main.conf;

import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SpringBeanProxy {
	public static List<HashMap<String, Object>> codeList = new ArrayList<HashMap<String, Object>>();

	public synchronized static void setApplicationContext(ApplicationContext arg0) {
		
		try {
			String packageName = "fast/app"; 
			List<String> list=getClassName(packageName,true);
			for (int i = 0; i < list.size(); i++) {
				HashMap<String,Object> map=new HashMap<String,Object>();
				//类名，包含包名
				String claName=list.get(i);
				//反射类
				Class<?> cla= Class.forName(claName);
				ApiHead anno = (ApiHead) cla.getAnnotation(ApiHead.class);
				String vString ="";
				if(anno!=null) {
					vString=anno.value();
				}
				//获取类中的方法
				Method[] name = cla.getDeclaredMethods();
				List<HashMap<String, String>> metList=new ArrayList<HashMap<String,String>>();
				//循环方法，判断方法是否包含注解
				for (Method str : name) {
					HashMap<String, String> metMap=new HashMap<String, String>();
					// 判断是否方法上存在注解
					boolean annotationPresent = str.isAnnotationPresent(ApiBody.class);
					if (annotationPresent) {
						// 获取自定义注解对象
						ApiBody methodAnno = str.getAnnotation(ApiBody.class);
						// 根据对象获取注解值
						String value = methodAnno.name();
						String descript = methodAnno.context();
						String type = methodAnno.type();
						String key=str.getName();
						metMap.put("key", key);
						metMap.put("value", value);
						metMap.put("descript", descript);
						metMap.put("type", type);
						metList.add(metMap);
					}
				}
				map.put("claName", claName);
				map.put("value", vString);
				map.put("metList", metList);
				codeList.add(map);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(codeList);


	}


	public static List<HashMap<String, Object>> getList(){
		return codeList;
	}



	/** 
	 * 获取某包下所有类 
	 * @param packageName 包名 
	 * @param childPackage 是否遍历子包 
	 * @return 类的完整名称 
	 */  
	public static List<String> getClassName(String packageName, boolean childPackage) {  
		List<String> fileNames = null;  
		ClassLoader loader = Thread.currentThread().getContextClassLoader();  
		String packagePath = packageName.replace(".", "/");  
		URL url = loader.getResource(packagePath);  
		if (url != null) {  
			String type = url.getProtocol();  
			if (type.equals("file")) {  
				fileNames = getClassNameByFile(url.getPath(), null, childPackage);  
			} else if (type.equals("jar")) {  
				fileNames = getClassNameByJar(url.getPath(), childPackage);  
			}  
		} else {  
			fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);  
		}  
		return fileNames;  
	}  
	/** 
	 * 从项目文件获取某包下所有类 
	 * @param filePath 文件路径 
	 * @param className 类名集合 
	 * @param childPackage 是否遍历子包 
	 * @return 类的完整名称 
	 */  
	private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {  
		List<String> myClassName = new ArrayList<String>();  
		File file = new File(filePath);  
		File[] childFiles = file.listFiles();  
		for (File childFile : childFiles) {  
			if (childFile.isDirectory()) {  
				if (childPackage) {  
					myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));  
				}  
			} else {  
				String childFilePath = childFile.getPath();  
				if (childFilePath.endsWith(".class")) {  
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
					childFilePath = childFilePath.replace("\\", ".");  
					myClassName.add(childFilePath);  
				}  
			}  
		}  

		return myClassName;  
	}  
	/** 
	 * 从jar获取某包下所有类 
	 * @param jarPath jar文件路径 
	 * @param childPackage 是否遍历子包 
	 * @return 类的完整名称 
	 */  
	private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {  
		List<String> myClassName = new ArrayList<String>();  
		String[] jarInfo = jarPath.split("!");  
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));  
		String packagePath = jarInfo[1].substring(1);  
		try {  
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(jarFilePath);  
			Enumeration<JarEntry> entrys = jarFile.entries();  
			while (entrys.hasMoreElements()) {  
				JarEntry jarEntry = entrys.nextElement();  
				String entryName = jarEntry.getName();  
				if (entryName.endsWith(".class")) {  
					if (childPackage) {  
						if (entryName.startsWith(packagePath)) {  
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
							myClassName.add(entryName);  
						}  
					} else {  
						int index = entryName.lastIndexOf("/");  
						String myPackagePath;  
						if (index != -1) {  
							myPackagePath = entryName.substring(0, index);  
						} else {  
							myPackagePath = entryName;  
						}  
						if (myPackagePath.equals(packagePath)) {  
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
							myClassName.add(entryName);  
						}  
					}  
				}  
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return myClassName;  
	}  

	/** 
	 * 从所有jar中搜索该包，并获取该包下所有类 
	 * @param urls URL集合 
	 * @param packagePath 包路径 
	 * @param childPackage 是否遍历子包 
	 * @return 类的完整名称 
	 */  
	private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {  
		List<String> myClassName = new ArrayList<String>();  
		if (urls != null) {  
			for (int i = 0; i < urls.length; i++) {  
				URL url = urls[i];  
				String urlPath = url.getPath();  
				// 不必搜索classes文件夹  
				if (urlPath.endsWith("classes/")) {  
					continue;  
				}  
				String jarPath = urlPath + "!/" + packagePath;  
				myClassName.addAll(getClassNameByJar(jarPath, childPackage));  
			}  
		}  
		return myClassName;  
	}  

}
