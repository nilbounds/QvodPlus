package com.vizen.common;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class FileUtil {

	
	/**
	 * 获取子文件
	 * @param dir
	 * @return
	 */
	public static ArrayList<String> fileDirSubList(File dir)
	{
		if(dir == null || !dir.exists() || dir.list() == null)
		{
			Log.e("FileListFragment", "[ fileDirSubList]Dir is null");
			return null;
		}
		else 
		{
			ArrayList<String> subArrayList = new ArrayList<String>();
			for(File file:dir.listFiles())
			{
				Log.d("FileListFragment", "file:" + file);
				if (!file.isHidden()) 
				{
					subArrayList.add(file.getAbsolutePath());
					Log.d("FileListFragment", "dir:" + dir.getAbsolutePath()
							+ File.separator + file);
				}
			}
			Collections.sort(subArrayList);
			return subArrayList;
		}
	}
	
	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
