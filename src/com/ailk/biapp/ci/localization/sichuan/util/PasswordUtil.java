// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PasswordUtil.java

package com.ailk.biapp.ci.localization.sichuan.util;

import java.io.PrintStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class PasswordUtil
{

	public static String key = "asia123?";

	public PasswordUtil()
	{
	}

	public static String decrypt(String message)
		throws Exception
	{
		byte bytesrc[] = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(2, secretKey, iv);
		byte retByte[] = cipher.doFinal(bytesrc);
		return URLDecoder.decode(new String(retByte), "utf-8");
	}

	public static String encrypt(String message)
		throws Exception
	{
		message = URLEncoder.encode(message, "utf-8").toLowerCase();
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(1, secretKey, iv);
		return toHexString(cipher.doFinal(message.getBytes("UTF-8"))).toUpperCase();
	}

	public static byte[] convertHexString(String ss)
	{
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++)
		{
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte)byteValue;
		}

		return digest;
	}

	public static void main(String args[])
		throws Exception
	{
		String a = encrypt("my_zengjie");
		System.out.println((new StringBuilder()).append("加密后的数据为:").append(a).toString());
		String b = decrypt("AC3A4D84888EA4D55CF4096A41D17FB9");
		System.out.println((new StringBuilder()).append("解密后的数据:").append(b).toString());
		String c = decrypt("5AB34A6350BB7488");
		System.out.println((new StringBuilder()).append("解密后的数据:").append(c).toString());
	}

	public static String toHexString(byte b[])
	{
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = (new StringBuilder()).append("0").append(plainText).toString();
			hexString.append(plainText);
		}

		return hexString.toString();
	}

}
