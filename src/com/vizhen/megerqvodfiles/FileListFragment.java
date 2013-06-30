package com.vizhen.megerqvodfiles;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vizen.common.FileUtil;
import com.vizhen.megerqvodfiles.MegerAdapter.MegerListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * TODO：将数据放到Application 中去
 * 
 * @author Administrator
 * 
 */
public class FileListFragment extends ListFragment implements MegerListener {

	private static final String TAG = "FileListFragment";

	public static int MAX_MEGERING_NUM = 3;

	private String outFileDir;

	private ArrayList<QvodFileInfo> qvodFileInfos;
	private ArrayList<QvodFileInfo> taskList;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private static final int SHOW_MEGER_PROGRESS = 0;
	private static final int SHOW_MEGER_COMPLTE = 1;
	private static final int EXIT_MEGER_TASK = 2;

	private boolean canMeger = true;

	private Handler mainHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == SHOW_MEGER_COMPLTE) {
				// TODO

			} else if (msg.what == SHOW_MEGER_PROGRESS) {
				// TODO 更新数据
				Log.d(TAG, "SHOW_MEGER_PROGRESS");
				MegerAdapter myAdapter = (MegerAdapter) FileListFragment.this
						.getListView().getAdapter();
				myAdapter.notifyDataSetChanged();
			}else if(msg.what == EXIT_MEGER_TASK)
			{
				//TODO 退出合并
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		if (Environment.getExternalStorageDirectory() == null) {

			// TODO 没有存储设备
			Log.e(TAG, "No Files!");
		} else {

			outFileDir = Environment.getExternalStorageDirectory()
					+ File.separator + "QvodPlus";
			File outfile = new File(outFileDir);
			if (!outfile.exists()) {
				if (!outfile.mkdir()) {
					// TODO 无法创建输出路径
					Log.e(TAG, "Can not create output file");
					canMeger = false;
				}
			}

			qvodFileInfos = new ArrayList<QvodFileInfo>();
			taskList = new ArrayList<QvodFileInfo>();

			String filePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "p2pcache/";

			ScanFileTask scanFileTask = new ScanFileTask();
			scanFileTask.execute(filePath);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.filelistview, container, false);
	}

	class ScanFileTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {
				MegerAdapter myAdapter = new MegerAdapter(
						FileListFragment.this.getActivity(), qvodFileInfos);
				myAdapter.setMegerListener(FileListFragment.this);
				getListView().setAdapter(myAdapter);

			} else {
				// TODO 没有文件
				getListView().setAdapter(null);
			}
		}

		@Override
		protected Boolean doInBackground(String... params) {

			qvodFileInfos.clear();
			File rootFile = new File(params[0]);
			Log.d(TAG, "FilePath:" + rootFile.getAbsolutePath());

			ArrayList<String> movieList = FileUtil.fileDirSubList(rootFile);

			if (movieList == null || movieList.isEmpty()) {
				Log.d(TAG, "movieList is null");
				return false;
			}

			//扫描子文件
			for (String path : movieList) {
				Log.d(TAG, "path:" + path);
				File file = new File(path);
				File[] subFiles = file.listFiles();
				if (subFiles != null && subFiles.length > 0) {
					QvodFileInfo qvodFile = new QvodFileInfo();

					long size = 0;

					for (int i = 0; i < subFiles.length; i++) {
						size += subFiles[i].length();
					}

					String name = subFiles[0].getName();
					int endIndex = name.lastIndexOf("_0.!mv");

					if (endIndex > -1) {
						Log.d(TAG, "name:" + name + " endIndex:" + endIndex);

						qvodFile.fileName = name.substring(0, endIndex);
						qvodFile.fileDir = path;
						qvodFile.fileSize = size;
						qvodFile.isMegered = false;
						qvodFile.megreSize = 0;

						Log.d(TAG, "fileName:" + qvodFile.fileName);
						qvodFileInfos.add(qvodFile);
					} else {
						// TODO 格式不正确
						continue;
					}
				}
			}

			if (qvodFileInfos.size() > 0) {
				return true;
			}
			return false;
		}

	}

	class MergerFileRunable implements Runnable {
		QvodFileInfo qvodFile;
		String outPath;
		int postion;

		public MergerFileRunable(QvodFileInfo qvodFile, String outFilePath,
				int postion) {
			this.qvodFile = qvodFile;
			this.outPath = outFilePath;
			this.postion = postion;
		}

		public MergerFileRunable(QvodFileInfo qvodFile, String outFilePath) {
			this.qvodFile = qvodFile;
			this.outPath = outFilePath;
		}

		@Override
		public void run() {

			File file = new File(qvodFile.fileDir);
			if (file == null || file.list().length == 0) {
				// TODO 没有文件
			} else {
				ArrayList<String> subFiles = FileUtil.fileDirSubList(file);
				if (subFiles != null) {
					try {

						File outFile = new File(outFileDir + File.separator
								+ qvodFile.fileName);
						if (outFile.length() != 0) {
							
							if(outFile.length() == qvodFile.fileSize)
							{
								//已经合并好了，无需继续合并
								qvodFile.megreSize = qvodFile.fileSize;
								qvodFile.isMegered = false;
								return;
							}
							//FIXME 现在还无法做断点合并，所以删除之前的数据
							outFile.delete();
						}
						RandomAccessFile writer = new RandomAccessFile(outFile,
								"rw");
						
						//合并子文件
						for (String child : subFiles) {
							Log.d("MergeFiles", "child:" + child);

							RandomAccessFile reader = new RandomAccessFile(
									child, "r");
							byte[] buffer = new byte[1024 * 1024];

							int readbyte = 0;
							while ((readbyte = reader.read(buffer)) != -1) {

								writer.write(buffer);

								qvodFile.megreSize += readbyte;

								Message msg = new Message();
								msg.what = SHOW_MEGER_PROGRESS;
								mainHandler.sendMessage(msg);
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

			}

		}

	}

	@Override
	public void megerQvodFile(QvodFileInfo qvodFileInfo, View view) {

		Log.d(TAG, "qvoidFileinfo:" + qvodFileInfo.fileName + " isMegerd:"
				+ qvodFileInfo.isMegered);
		if (!qvodFileInfo.isMegered) {
			qvodFileInfo.isMegered = true;
			((ImageView) view).setImageResource(R.drawable.pause);
			executorService.execute(new MergerFileRunable(qvodFileInfo,
					outFileDir));
		}
	}

	@Override
	public void onDestroyView() {
		Log.d(TAG, "onDestroyView");
		//停止合并
		executorService.shutdown();
		super.onDestroyView();
	}

}
