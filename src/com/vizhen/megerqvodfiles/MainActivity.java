package com.vizhen.megerqvodfiles;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity implements TabListener {

	//暂时先不单独处理合并成功的文件列表
	private static final int TAB_INDEX_COUNT = 1;
	
	private static final int TAB_INDEX_SEARCHFILE = 0;
	private static final int TAB_INDEX_RESULT = 1;
	
	private ViewPager viewPager;
	private ViewPageAdpater viewPageAdpater;
	private FileListFragment fileListFragment;
	private ResultListFragment resultListFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		fileListFragment = new FileListFragment();
		resultListFragment = new ResultListFragment();
		
		setUpActionBar();
		setUpViewPager();
		setUpTabs();
	}

	private void setUpTabs() {
		ActionBar actionBar = getActionBar();
		for (int i = 0; i < viewPageAdpater.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(viewPageAdpater.getPageTitle(i))
					.setTabListener(this));
		}
		
	}

	private void setUpViewPager() {
		viewPageAdpater = new ViewPageAdpater(getSupportFragmentManager());
		
		viewPager = (ViewPager)findViewById(R.id.page);
		viewPager.setAdapter(viewPageAdpater);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			public void onPageSelected(int postion)
			{
				ActionBar actionBar = getActionBar();
				actionBar.setSelectedNavigationItem(postion);
			}
		});
	}

	private void setUpActionBar() {
		ActionBar actionBar = getActionBar();
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public class ViewPageAdpater extends FragmentPagerAdapter
	{

		public ViewPageAdpater(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {

			switch (arg0) {
			case TAB_INDEX_SEARCHFILE:
				return fileListFragment;

			case TAB_INDEX_RESULT:
				return resultListFragment;

			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TAB_INDEX_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String tablLable = null;
			switch (position) {
			case TAB_INDEX_SEARCHFILE:
				 tablLable = getString(R.string.tasks);
				break;
			case TAB_INDEX_RESULT:
				tablLable = getString(R.string.result);
				break;

			default:
				break;
			}
			return tablLable;
		}
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
