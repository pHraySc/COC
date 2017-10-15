package com.ailk.biapp.ci.util;

import java.io.Serializable;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CiIdentifierGenerator implements IdentifierGenerator {
	public static final char[] CHARMAP = new char[64];

	public CiIdentifierGenerator() {
	}

	public static String hexTo64(String hex) {
		StringBuffer r = new StringBuffer();
		boolean index = false;
		int[] buff = new int[3];
		int l = hex.length();

		for(int i = 0; i < l; ++i) {
			int var6 = i % 3;
			buff[var6] = Integer.parseInt("" + hex.charAt(i), 16);
			if(var6 == 2) {
				r.append(CHARMAP[buff[0] << 2 | buff[1] >>> 2]);
				r.append(CHARMAP[(buff[1] & 3) << 4 | buff[2]]);
			}
		}

		return r.toString();
	}

	public Serializable generate(SessionImplementor sessionimplementor, Object obj) throws HibernateException {
		return randomUUID();
	}

	public static String randomUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "").toUpperCase();
		uuid = "0" + uuid;
		uuid = hexTo64(uuid);
		return uuid;
	}

	static {
		int i;
		for(i = 0; i < 10; ++i) {
			CHARMAP[i] = (char)(48 + i);
		}

		for(i = 10; i < 36; ++i) {
			CHARMAP[i] = (char)(97 + i - 10);
		}

		for(i = 36; i < 62; ++i) {
			CHARMAP[i] = (char)(65 + i - 36);
		}

		CHARMAP[62] = 95;
		CHARMAP[63] = 45;
	}
}
