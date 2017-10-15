// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DES.java

package com.ailk.biapp.ci.localization.shanxi.utils;

import com.sun.crypto.provider.SunJCE;
import java.io.PrintStream;
import java.security.Security;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class DES
{

	public static int _DES = 1;
	public static int _DESede = 2;
	public static int _Blowfish = 3;
	private Cipher p_Cipher;
	private SecretKey p_Key;
	private String p_Algorithm;
	private static DES _instance;
	private static String hexKey = "B5584A5D9B61C23BE52CA1168C9110894C4FE9ABC8E9F251";

	private void selectAlgorithm(int al)
	{
		switch (al)
		{
		case 1: // '\001'
		default:
			p_Algorithm = "DES";
			break;

		case 2: // '\002'
			p_Algorithm = "DESede";
			break;

		case 3: // '\003'
			p_Algorithm = "Blowfish";
			break;
		}
	}

	public DES(int algorithm)
		throws Exception
	{
		selectAlgorithm(algorithm);
		Security.addProvider(new SunJCE());
		p_Cipher = Cipher.getInstance(p_Algorithm);
	}

	private SecretKey checkKey()
	{
		try
		{
			if (p_Key == null)
			{
				KeyGenerator keygen = KeyGenerator.getInstance(p_Algorithm);
				p_Key = keygen.generateKey();
			}
		}
		catch (Exception nsae) { }
		return p_Key;
	}

	private void setKey(byte enckey[])
	{
		p_Key = new SecretKeySpec(enckey, p_Algorithm);
	}

	private byte[] encode(byte data[])
		throws Exception
	{
		p_Cipher.init(1, checkKey());
		return p_Cipher.doFinal(data);
	}

	private byte[] decode(byte encdata[], byte enckey[])
		throws Exception
	{
		setKey(enckey);
		p_Cipher.init(2, p_Key);
		return p_Cipher.doFinal(encdata);
	}

	private String byte2hex(byte b[])
	{
		String hs = "";
		String stmp = "";
		for (int i = 0; i < b.length; i++)
		{
			stmp = Integer.toHexString(b[i] & 0xff);
			if (stmp.length() == 1)
				hs = (new StringBuilder()).append(hs).append("0").append(stmp).toString();
			else
				hs = (new StringBuilder()).append(hs).append(stmp).toString();
		}

		return hs.toUpperCase();
	}

	private byte[] hex2byte(String hex)
		throws IllegalArgumentException
	{
		if (hex.length() % 2 != 0)
		{
			System.out.println((new StringBuilder()).append("hex:").append(hex).append("\nlength:").append(hex.length()).toString());
			throw new IllegalArgumentException();
		}
		char arr[] = hex.toCharArray();
		byte b[] = new byte[hex.length() / 2];
		int i = 0;
		int j = 0;
		for (int l = hex.length(); i < l;)
		{
			String swap = (new StringBuilder()).append("").append(arr[i++]).append(arr[i]).toString();
			int byteint = Integer.parseInt(swap, 16) & 0xff;
			b[j] = (new Integer(byteint)).byteValue();
			i++;
			j++;
		}

		return b;
	}

	public static String encrypt(String s)
		throws Exception
	{
		if (null == _instance)
			_instance = new DES(_DESede);
		_instance.setKey(_instance.hex2byte(_instance.hexKey));
		byte enc[] = _instance.encode(s.getBytes());
		String hexenc = _instance.byte2hex(enc);
		return hexenc;
	}

	public static String decrypt(String s)
		throws Exception
	{
		if (null == _instance)
			_instance = new DES(_DESede);
		return new String(_instance.decode(_instance.hex2byte(s), _instance.hex2byte(_instance.hexKey)));
	}

	public static void main(String args[])
		throws Exception
	{
		String pwd = "ED2705F227C3C80B";
		System.out.println(decrypt(pwd));
	}

}
