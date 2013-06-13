package br.com.androidzin.pontopro;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import br.com.androidzin.pontopro.data.DatabaseManager;
import br.com.androidzin.pontopro.settings.SettingsActivity;

public class MainActivity extends SherlockFragmentActivity{

	private MainContent content;
	private DatabaseManager mDatabaseManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDatabaseManager = new DatabaseManager(getApplicationContext());
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
		mDatabaseManager.close();
	}
}
