package br.com.androidzin.pontopro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import br.com.androidzin.pontopro.database.DatabaseManager;
import br.com.androidzin.pontopro.settings.SettingsActivity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity{

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private DatabaseManager mDatabaseManager;
	private boolean isTablet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDatabaseManager = new DatabaseManager(getApplicationContext());
		isTablet = getResources().getBoolean(R.bool.is_tablet);
		if(isTablet == false)
		{
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mPagerAdapter = new CheckinHistoryAdapter(getSupportFragmentManager());
			mViewPager.setAdapter(mPagerAdapter);
		}
	}
	
	public boolean isTablet()
	{
		return isTablet;
	}
	
	@Override
    public void onBackPressed() {
		if(isTablet == false)
		{
	        if (mViewPager.getCurrentItem() == 0) {
	            super.onBackPressed();
	        } else {
	            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
	        }
		} else {
			if(getSupportFragmentManager().getBackStackEntryCount() == 0)
			{
				super.onBackPressed();
			}
			getSupportFragmentManager().popBackStack();			
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_settings){
			startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDatabaseManager.close();
	}
	
	private class CheckinHistoryAdapter extends FragmentPagerAdapter implements OnPageChangeListener
	{
		Fragment screens[];
		public CheckinHistoryAdapter(FragmentManager fm) {
			super(fm);
			screens = new Fragment[2];
			screens[0] = new CheckinFragment();
			screens[1] = new HistoryFragment();
		}

		@Override
		public Fragment getItem(int index) {
			if(index <= screens.length)
			{
				return screens[index];
			}
			return null;
		}

		@Override
		public int getCount() {
			return screens.length;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int currentItem) {
			//if(currentItem == 1)  screens[0].
		}
	}
	
}
