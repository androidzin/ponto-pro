package br.com.androidzin.pontopro;

import android.content.Intent;
import android.os.Bundle;
import br.com.androidzin.pontopro.settings.SettingsActivity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity{

	private MainContent content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        content = MainActivityContentFactory.getContent(this);
    }

	@Override
    public void onBackPressed() {
	    if(!content.onBackPressed()){
           super.onBackPressed();
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
	}
}
