package com.vizhen.megerqvodfiles;

import java.util.List;

import com.vizen.common.FileUtil;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MegerAdapter extends BaseAdapter {

	public class ViewHolder {
		
		public TextView movieTitle;
		public TextView megresize_fileSize;
		public ProgressBar progressBar;
		public ImageView  playFun;
	}
  
	private List<QvodFileInfo> megQvodFileInfos;
	private LayoutInflater mInflater;
	private MegerListener mergerListener;
	
	public MegerAdapter(Context context, List<QvodFileInfo> megerList) {
		
		mInflater = LayoutInflater.from(context);
		megQvodFileInfos = megerList;
	}

	@Override
	public int getCount() {
		
		return megQvodFileInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return megQvodFileInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setMegerListener(MegerListener megerListener)
	{
		this.mergerListener = megerListener;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.megerfileitem, null);
			viewHolder.movieTitle = (TextView) convertView
					.findViewById(R.id.movie_title);
			viewHolder.megresize_fileSize = (TextView) convertView
					.findViewById(R.id.movie_size);
			viewHolder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.meger_progress);
			viewHolder.playFun = (ImageView) convertView.findViewById(R.id.play);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final QvodFileInfo qvodFileInfo = megQvodFileInfos.get(position);
		if (qvodFileInfo != null) 
		{
			Log.d("FileListFragment", "getView");
			viewHolder.movieTitle.setText(qvodFileInfo.fileName);
			viewHolder.megresize_fileSize.setText(""
					+ FileUtil.FormetFileSize(qvodFileInfo.megreSize) + "/"
					+ FileUtil.FormetFileSize(qvodFileInfo.fileSize));
			
			long progess = ((qvodFileInfo.megreSize * 1000) / qvodFileInfo.fileSize);
			Log.d("FileListFragment", "megreSize:" + qvodFileInfo.megreSize);	
			Log.d("FileListFragment", "progress:" + progess);	
			viewHolder.progressBar.setProgress((int) progess);
			viewHolder.progressBar.setMax(1000);
			viewHolder.playFun.setEnabled(true);
			viewHolder.playFun.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Log.i("FileListFragment", "playFun onClick");
					if(mergerListener != null)
					{
						mergerListener.megerQvodFile(qvodFileInfo, v);
					}
				}
			});
            if(qvodFileInfo.isMegered)
            {
            	viewHolder.playFun.setImageResource(R.drawable.pause);
            }
            else 
            {
            	viewHolder.playFun.setImageResource(R.drawable.play);
			}
            
            if(qvodFileInfo.megreSize == qvodFileInfo.fileSize)
            {
            	//合并已经结束
            	viewHolder.playFun.setImageResource(R.drawable.ok);
            }
		}
		return convertView;
	}
	
	public interface MegerListener
	{
		public void megerQvodFile(QvodFileInfo qvodFileInfo, View view);
	}

}
