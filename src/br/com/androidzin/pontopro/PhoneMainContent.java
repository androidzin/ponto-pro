package br.com.androidzin.pontopro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class PhoneMainContent extends MainContent{

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    public PhoneMainContent(MainActivity activity) {
        super(activity);
        mViewPager = (ViewPager) mMainActivity.findViewById(R.id.pager);
        mPagerAdapter = new CheckinHistoryAdapter(mMainActivity.getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected boolean onBackPressed() {

        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            return true;
        }
        return false;
    }

    private class CheckinHistoryAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
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
        }
    }
}
