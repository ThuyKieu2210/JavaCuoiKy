package Model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public String md5(String str){
		MessageDigest md;
		String result = "";
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			BigInteger bi = new BigInteger(1, md.digest());
			
			result = bi.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		MD5 lib = new MD5();
		
		String password = "123456";
		String strmd5 = lib.md5(password);
		
		System.out.println("Sau khi mã hóa: "+strmd5);
	}
}
