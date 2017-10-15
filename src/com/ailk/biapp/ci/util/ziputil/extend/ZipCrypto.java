
package com.ailk.biapp.ci.util.ziputil.extend;

import com.ailk.biapp.ci.util.ziputil.zip.Crc32;

public class ZipCrypto {
	private static long[] keys = new long[]{305419896L, 591751049L, 878082192L};

	public ZipCrypto() {
	}

	private static int magicByte() {
		int t = (int)(keys[2] & 65535L | 2L);
		t = t * (t ^ 1) >> 8;
		return t;
	}

	private static void updateKeys(int byteValue) {
		keys[0] = Crc32.update(keys[0], byteValue);
		int key0val = (byte)((int)keys[0]);
		if((byte)((int)keys[0]) < 0) {
			key0val += 256;
		}

		keys[1] += (long)key0val;
		keys[1] *= 134775813L;
		++keys[1];
		keys[2] = Crc32.update(keys[2], (byte)((int)(keys[1] >> 24)));
	}

	public static void initCipher(String passphrase) {
		keys[0] = 305419896L;
		keys[1] = 591751049L;
		keys[2] = 878082192L;

		for(int i = 0; i < passphrase.length(); ++i) {
			updateKeys((byte)passphrase.charAt(i));
		}

	}

	public static byte[] decryptMessage(byte[] cipherText, int length) {
		byte[] plainText = new byte[length];

		for(int i = 0; i < length; ++i) {
			int m = magicByte();
			byte C = (byte)(cipherText[i] ^ m);
			if(C < 0) {
				updateKeys(C + 256);
				plainText[i] = (byte)(C + 256);
			} else {
				updateKeys(C);
				plainText[i] = C;
			}
		}

		return plainText;
	}

	public static byte[] encryptMessage(byte[] plaintext, int length) {
		byte[] cipherText = new byte[length];

		for(int i = 0; i < length; ++i) {
			byte C = plaintext[i];
			cipherText[i] = (byte)(plaintext[i] ^ magicByte());
			updateKeys(C);
		}

		return cipherText;
	}
}
