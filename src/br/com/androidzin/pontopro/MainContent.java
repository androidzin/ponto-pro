package br.com.androidzin.pontopro;

public abstract class MainContent {

    protected MainActivity mMainActivity;

    public MainContent(MainActivity activity){
        mMainActivity = activity;
    }

    protected abstract boolean onBackPressed();
}
