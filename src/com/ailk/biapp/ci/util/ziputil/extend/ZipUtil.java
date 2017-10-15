package com.ailk.biapp.ci.util.ziputil.extend;

import com.ailk.biapp.ci.util.ziputil.zip.EncryptZipEntry;
import com.ailk.biapp.ci.util.ziputil.zip.EncryptZipInput;
import com.ailk.biapp.ci.util.ziputil.zip.EncryptZipOutput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ZipUtil {
	public ZipUtil() {
	}

	public static byte[] getEncryptZipByte(File[] srcfile, String password, String encode) {
		ByteArrayOutputStream tempOStream = new ByteArrayOutputStream(1024);
		byte[] tempBytes = null;
		byte[] buf = new byte[1024];

		try {
			EncryptZipOutput e = new EncryptZipOutput(tempOStream, password);
			e.setEncoding(encode);

			for(int i = 0; i < srcfile.length; ++i) {
				FileInputStream in = new FileInputStream(srcfile[i]);
				e.putNextEntry(new EncryptZipEntry(srcfile[i].getName()));

				int len;
				while((len = in.read(buf)) > 0) {
					e.write(buf, 0, len);
				}

				e.closeEntry();
				in.close();
			}

			tempOStream.flush();
			e.close();
			tempBytes = tempOStream.toByteArray();
			tempOStream.close();
		} catch (IOException var10) {
			var10.printStackTrace();
		}

		return tempBytes;
	}

	public static void unzipFiles(byte[] zipBytes, String password, String dir) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
		EncryptZipInput zin = new EncryptZipInput(bais, password);

		EncryptZipEntry ze;
		while((ze = zin.getNextEntry()) != null) {
			ByteArrayOutputStream toScan = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];

			int len;
			while((len = zin.read(buf)) > 0) {
				toScan.write(buf, 0, len);
			}

			byte[] fileOut = toScan.toByteArray();
			toScan.close();
			writeByteFile(fileOut, new File(dir + File.separator + ze.getName()));
		}

		zin.close();
		bais.close();
	}

	private static boolean writeByteFile(byte[] bytes, File file) {
		FileOutputStream fos = null;
		boolean flag = true;

		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (FileNotFoundException var16) {
			flag = false;
			var16.printStackTrace();
		} catch (IOException var17) {
			flag = false;
			var17.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException var15) {
					var15.printStackTrace();
				}
			}

		}

		return flag;
	}
}
