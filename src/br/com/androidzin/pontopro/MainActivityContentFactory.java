package br.com.androidzin.pontopro;


public class MainActivityContentFactory {

    public static MainContent getContent(MainActivity activity){

        boolean isTablet = activity.getResources().getBoolean(R.bool.is_tablet);

        if(isTablet){
            return new TabletMainContent(activity);
        } else {
            return new PhoneMainContent(activity);
        }
    }
}
