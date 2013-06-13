package br.com.androidzin.pontopro;


import android.support.v4.app.FragmentManager;

public class TabletMainContent extends MainContent{

    public TabletMainContent(MainActivity activity) {
        super(activity);
    }

    @Override
    public boolean onBackPressed() {

        FragmentManager fragmentManager = mMainActivity.getSupportFragmentManager();
        if(hasPreviousFragment(fragmentManager))
        {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    private boolean hasPreviousFragment(FragmentManager fragmentManager) {
        return fragmentManager.getBackStackEntryCount() > 0;
    }
}
