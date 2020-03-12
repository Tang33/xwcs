package fast.main.util;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.codec.binary.Base64;
 
public class SignUtil {
	
	private static final String ENCODING = "UTF-8";
	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	
	/**
	 * SHA256WithRSA签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] sign256(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, 
	  																		 SignatureException, UnsupportedEncodingException {
	    
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
	       
		signature.initSign(privateKey);
	       
		signature.update(data.getBytes(ENCODING));
	         
		return signature.sign();
	}
	
	public static boolean verify256(String data, byte[] sign, PublicKey publicKey){
		if(data == null || sign == null || publicKey == null){
			return false;
		}
		
		try {
			Signature signetcheck = Signature.getInstance(SIGNATURE_ALGORITHM);
			signetcheck.initVerify(publicKey);
			signetcheck.update(data.getBytes("UTF-8"));
			return signetcheck.verify(sign);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 二进制数据编码为BASE64字符串 
	 * @param data
	 * @return
	 */
	public static String encodeBase64(byte[] bytes){
		return new String(Base64.encodeBase64(bytes));
	}
	
	/**
	 * BASE64解码
	 * @param bytes
	 * @return
	 */
	public static byte[] decodeBase64(byte[] bytes) { 
		byte[] result = null;
		try {
			result = Base64.decodeBase64(bytes);
		} catch (Exception e) {
			return null;
		}
        return result;  
    }
}