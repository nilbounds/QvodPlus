package com.vizhen.megerqvodfiles;

import java.util.ArrayList;

import android.app.Application;

public class MainApplication extends Application {

	private ArrayList<QvodFileInfo> qvodFilesList;
	private ArrayList<QvodFileInfo> taskList;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public ArrayList<QvodFileInfo> getQvodFilesList() {
		return qvodFilesList;
	}

	public void setQvodFilesList(ArrayList<QvodFileInfo> qvodFilesList) {
		this.qvodFilesList = qvodFilesList;
	}

	public ArrayList<QvodFileInfo> getTaskList() {
		return taskList;
	}

	public void setTaskList(ArrayList<QvodFileInfo> taskList) {
		this.taskList = taskList;
	}

	
}
