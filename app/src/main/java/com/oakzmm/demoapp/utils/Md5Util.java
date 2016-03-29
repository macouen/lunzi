package com.oakzmm.demoapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	public static String getStringMD5(String sourceStr) {
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger bigInt = new BigInteger(1, md.digest(sourceStr.getBytes()));
			s = String.format("%032x", bigInt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static String getFileMD5(File file) {
		String s = null;
		
		if (!file.exists()) {
			return null;
		}
		
		FileInputStream in = null;
	    byte buffer[] = new byte[1024];
	    int len;

		try {  
			MessageDigest md = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				md.update(buffer, 0, len);
		    }
			in.close();

			BigInteger bigInt = new BigInteger(1, md.digest());
			s = String.format("%032x", bigInt);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return s;
	}
}
