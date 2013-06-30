package com.vizhen.megerqvodfiles;

public class QvodFileInfo {

	public String fileDir;
	public String fileName;
	public long fileSize;
	public long megreSize = 0;
	public FileType fileType = FileType.MOVIE;
	public boolean isMegered = false;
	
	
	public enum FileType
	{
		MOVIE,
		MUSIC
	}
}
