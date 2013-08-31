package br.com.androidzin.pontopro.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.androidzin.pontopro.MainActivity;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.model.Checkin.CheckinListener;
import br.com.androidzin.pontopro.model.Checkin.CheckinType;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;

public class CheckinNotificationManager extends BroadcastReceiver implements CheckinListener{

	private Context mContext;
	public static final String WORKDAY_COMPLETE = 
			"br.com.androidzin.pontopro.notification.CheckinNotificationManager.WORKDAY_COMPLETE";
	
	private SharedPreferences sharedPreferences;
	private AlarmManager alarmManager; 
	
	public CheckinNotificationManager(){
		
	}
	
	public CheckinNotificationManager(Context context){
		mContext = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		alarmManager =	(AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Notification notification = null;
		
		if(intent.getAction().equals(WORKDAY_COMPLETE)){
			notification = getWorkdayCompleteNotification(context);
		}

		NotificationManager notificationManager = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.notify(0, notification);
	}
	
	private Notification getWorkdayCompleteNotification(Context context){
		return getNotification(context, context.getString(R.string.workday_complete_title),
				context.getString(R.string.workday_complete_message));
	}

	private Notification getNotification(Context context, String title, String message) {
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    context,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		Notification notification = builder
				.setContentTitle(title)
				.setContentText(message)
				.setSmallIcon(android.R.drawable.sym_action_chat)
				.setContentIntent(resultPendingIntent)
				.getNotification();
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		return notification;
	}
	
	//TODO: public for test propose
	public Intent getWorkdayCompleteIntent(){
		return getIntentNofitication(WORKDAY_COMPLETE);
	}
	
	private Intent getIntentNofitication(String action){
		Intent intent = new Intent(mContext, CheckinNotificationManager.class);
		intent.setAction(action);
		return intent;
	}

	@Override
	public void onCheckinDone(CheckinType checkin, long when, long workedHours) {

		Intent intent = null;
		PendingIntent notifier = null;
		
		switch(checkin){
			case AFTER_LUNCH:
				scheduleWorkdayCompleteNotification(when, workedHours);
			break;
			
			case LEAVING:
				cancelWorkdayCompleteNotification(workedHours);
			break;
		}
		
		
		
		
		
		
	}

	private void cancelWorkdayCompleteNotification(long workedHours) {
		if(workedHours < BusinessHourCommom.getWorkingTime(sharedPreferences))
		{
			PendingIntent notifier = getWorkdayCompletePendingIntent();
			notifier.cancel();
			alarmManager.cancel(notifier);
		}
	}

	private void scheduleWorkdayCompleteNotification(long when,
			long workedHours) {
		
		
		PendingIntent notifier = getWorkdayCompletePendingIntent();
		
		long notificationHour = when + 
				(BusinessHourCommom.getWorkingTime(sharedPreferences) - workedHours);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, notificationHour, notifier);
	}

	private PendingIntent getWorkdayCompletePendingIntent() {
		Intent intent = getWorkdayCompleteIntent();
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return notifier;
	}
}
