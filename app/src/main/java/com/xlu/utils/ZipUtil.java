package com.xlu.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

	public static void unZip(String unZipfileName) {
		FileOutputStream fileOut;
		File file;
		ZipInputStream zipIn;
		ZipEntry zipEntry;
		int readedBytes;
		byte[] buf = new byte[512];

		String f = unZipfileName.substring(0,unZipfileName.length() - 4);
		File ff = new File(f);
		if (!ff.exists()) {
			ff.mkdirs();
		}

		try {
			zipIn = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(unZipfileName)));
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				file = new File(ff.getAbsolutePath() + "/" + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					if(!file.exists()){
						file.mkdirs();
					}
				} else {
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					fileOut = new FileOutputStream(file);

					while ((readedBytes = zipIn.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();
				}

				zipIn.closeEntry();
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}
}
