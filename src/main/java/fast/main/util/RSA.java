package fast.main.util;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
 
public class RSA {
	public static String pbKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsjMZIpK4m4mHf1bcY72A+/K35MANrcMprldOXUM4eqLW0Mnx6LK0MDOHrsVsgLZYb7fwNS/qZBWlhPnZmfNC6WNtrC/ZlmFcSRQX3Yr+AG7lxlaUg+Yn2us8PImd30VuHYuz8WBBz3AYXrCIDWTRpU4SoUpeaGnXGG2ePnjGTIOmrVpdoXEr5l+cm/LxmjbJpdUZ9FUQOdf/9pQ1j4BImeVzEe8EHycePa1lhqVxftjlPm9JOmp93BbNY1Y+r7MGgPNWLXU748/2O343hDahrP7ASPgB5nfa9C2G7BBfN6gk0T7gYqU3pELYi0o301QMblW7snKQBgzudK8dHrUFFQIDAQAB";
	public static String prkey="MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDI+RdsKCU3yibDAMFKzx9jmS8SO5kQeC8G/bO7rHlm7xK9X7vpkqm3KexrYqfw/blO6A5ItvoFsTz85u5/y3xHUS+3ONHgNL37HoLrmXvxZffrPEqEZFTJRHCNpV4tgiDBSht8HbAaP8WNBJh0eDlQocn6mipw9SftJkWIus6GFGVe5IISEHuQKbT169BACRGurSSubvmWA1DKh2zuyUwpLWieSEtpZvkEvcKpiznWNU64Yp2mJccHNN+WTXWJvrfcy1VqGuAJoEijYQkmSUlWywNEua1z388p5O/v56DG4sk8FLkY3bJB+OvDnnhRXuQTds0VDhnhyOBgXMLIrib7AgMBAAECggEAXfTONXtjQvzYOExvEXlZtGeWMRD501BMtBkB4RiWPUBB45URNfaiL4aDCAU8fJcx/ijRHL+mjei5mYahM8+/D1xuUVtYC1i9BNzT2i+MWWQRjsFGGTUKynmboS0RpXbzt0ndJZ5/iRYNVosydykJqp+o3OccNmj2vJEq6uxixx2H06u2APcn/+vQGDtSLof6E4oI0TWlGLmZ9VgALqTTspRhOTP2z0ahtkJNTUQmfJrpe3Nmt8UX3EEzHDHctTlGz/bN3tXVhzHBULSOl/NJC9VTJWt/4l5NT/7isndKuDoZD5Zcg2dghTAZs6ji8JUWsfKDTmTb+0+v2+q3HVjjYQKBgQDm/e66urHecfMqC1HlUKrCRGlLnM0DqYtJuG+NjVTKnfIOLikXcWY4rkTZkdnzUNbO/yvYz58u3VCqMBExfhjrFykWGOCUKTIY1jX1//KXiFkRSXY7GP/U8nybAMYSW9L6bk7N0eGDz5pBj18phMqWZez//vHxB2NpvZkUr/UfEQKBgQDeuysfPg9SVjzCR1ehk3/15ldc8qnv/QGznAHaD7B+pCN3iOtGOSVhn05PaFfREDT2s2qSHHQQtkCw6dUnL3NVS/DsPjA1W9Y3ERcPq3DL91ECMWrjy/8jIVKaHzde4VVdI9rbVC/+YcmdTAv78YSkpgiO6BIQK4EvYnLGWgc9SwKBgCmZ1BPnKb+RMCUDUuWEyKwiHACVJcsiDsbhI54PtRSkq4JoR9hEAQ/2OsogXWtXxMgU5rWudtG+5BcyfvY16pD0Y97p7hBg9vfIJIHAbdVipf/aBiQQlYqpfU2BLysBRJb7pM+mnAHcMsDZ7F6S3tWFV9Cm/rXVCc9yaQ5zviaxAoGANG7TY/N0H+n1O7cXxg23HegvwM3Pe5OQBvw+41q2LdnuD4HIASwKxMR/5umlYq3yCfGKsQLJv/VnnT4u0560A+xwtuyc5LLN5wC68RFm6F08UMvZYe7wDREXJPkcS9W4BskeCGv1MGlM0R7J43GjzC7Z7Ops9imHq+P9KPygn6kCgYBtqF+ydDGz9TrO61LLSYcF7g3qoGifS+zrrrbnSl+LcynFyf1+y1Nd9jz7g/mEjOZzfXCxHpblFWlAC8ENmFzdNBklvXiaVNdnHx6if+vW6vApnv+4CGZrn9qLGSoaa5UN/7O3mtlUfi2ugSG9qAoRufYE7n/dbAdpEShi+bzteA==";
    
	
	public static void main(String[] args) throws Exception {
		String rr="jLGFK8LfQdZpu5V53oi5UedINTVrBcK9gsqTlG3KoPlWZRLD7%2fIUjNzwJlYj09%2bGVbLN5eah1C%2fiy8IoW6%2bTttdfGMA4MFrm1KpdfzjriQvEPaWSTq3DUy0qBlFxs2qm6MMjZFt88APh72GVcGO7iPhdYT1XSqER5TFzYzDkxYlzb89v0rAOpWShJPm4H7%2fXwnrB9ovVJtgv1OL%2fjaB8dLVqv6pYkrSPlHFssxBEEM2GmPWcWNINH10wOPwYoVxax0nqRtqs%2fOL48q%2fczAQY%2fcUfFqBPNRICgKfZ0z9vitpbo3AGYnhHOBvgzgH2F8%2fW50g2jBDwS%2f%2fKauvreDSGuQ%3d%3d";
		String ss=URLDecoder.decode(rr, "utf-8");
		System.out.println(ss);
		String rrr=ss.replaceAll(" ", "+");
		System.out.println(rrr);
		String re=decrypt(rrr,"utf-8");
		System.out.println(re);
		
	}
	
	public static String encrypt( String str,String charSet) throws Exception{
		byte[] decoded = Base64.decodeBase64(pbKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(charSet)));
		return outStr;
	}
	
	public static String decrypt(String str,String charSet) throws Exception{
		//生成密钥对
		byte[] inputByte = Base64.decodeBase64(str.getBytes(charSet));
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(prkey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte),charSet);
		return outStr;
	}
	
	
	
	
}