package br.com.androidzin.pontopro.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
	public static final String WORKING_TIME_VIOLATION = 
			"br.com.androidzin.pontopro.notification.CheckinNotificationManager.WORKDING_TIME_VIOLATION";
	public static final String GOBACK_TO_WORK =
			"br.com.androidzin.pontopro.notification.CheckinNotificationManager.GOBACK_TO_WORK";
	
	private static final long TEN_HOURS = 36000000;
	
	private static final String KEY_NOTIFICATION_ENABLED = "pref_key_notification_enabled";
	private static final String KEY_WORKDAY_COMPLETE_NOTIFICATION = "pref_key_notification_bussineshour_enabled";
	private static final String KEY_WORKING_TIME_VIOLATION_NOTIFICATION = "pref_key_notification_maxhour_enabled";
	
	
	private SharedPreferences sharedPreferences;
	private AlarmManager alarmManager; 
	
	public CheckinNotificationManager(){
		
	}
	
	public CheckinNotificationManager(Context context){
		mContext = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		alarmManager =	(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(isNotificationEnabled(context)){
			
			Notification notification = null;
			
			if(intent.getAction().equals(WORKDAY_COMPLETE) && shouldNotify(context, WORKDAY_COMPLETE)){
				notification = getWorkdayCompleteNotification(context);
			} else if(intent.getAction().equals(WORKING_TIME_VIOLATION) && shouldNotify(context, WORKING_TIME_VIOLATION)){
				notification = getWorkingTimeViolationNotification(context);
			} else if(intent.getAction().equals(GOBACK_TO_WORK)){
				notification = getGoBackToWorkNotification(context);
			}
	
			if(notification != null){
				NotificationManager notificationManager = 
						(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
				notificationManager.notify(0, notification);
			}
		}
	}
	
	private Notification getWorkdayCompleteNotification(Context context){
		return getNotification(context, context.getString(R.string.workday_complete_title),
				context.getString(R.string.workday_complete_message),
				R.drawable.workday_complete);
	}
	
	private Notification getWorkingTimeViolationNotification(Context context){
		return getNotification(context,
				context.getString(R.string.workingtime_violation_title),
				context.getString(R.string.workingtime_violation_message),
				R.drawable.warning);
	}
	
	private Notification getGoBackToWorkNotification(Context context){
		return getNotification(context,
				context.getString(R.string.gobacktowork_title),
				context.getString(R.string.gobacktowork_message),
				R.drawable.goback_to_work);
	}

	private Notification getNotification(Context context, String title, String message, int icon) {
		
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
				.setSmallIcon(icon)
				.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
				.setContentIntent(resultPendingIntent)
				.getNotification();
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		return notification;
	}
	
	public Intent getWorkdayCompleteIntent(){
		return getIntentNofitication(WORKDAY_COMPLETE);
	}
	
	public Intent getWorkingTimeViolationIntent(){
		return getIntentNofitication(WORKING_TIME_VIOLATION);
	}
	
	public Intent getGobackToWorkIntent(){
		return getIntentNofitication(GOBACK_TO_WORK);
	}
	
	private Intent getIntentNofitication(String action){
		Intent intent = new Intent(mContext, CheckinNotificationManager.class);
		intent.setAction(action);
		return intent;
	}

	private boolean shouldNotify(Context context, String action) {
		if(sharedPreferences == null){
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		
		if(action.equals(WORKDAY_COMPLETE)){
			return sharedPreferences.getBoolean(KEY_WORKDAY_COMPLETE_NOTIFICATION, true);
		} else if (action.equals(WORKING_TIME_VIOLATION)) {
			return sharedPreferences.getBoolean(KEY_WORKING_TIME_VIOLATION_NOTIFICATION, true);
		} 
		return false;
	}
	
	@Override
	public void onCheckinDone(CheckinType checkin, long when, long workedHours) {

		switch(checkin){
			case ENTERED:
			break;
			
			case LUNCH:
				scheduleGobackToWorkNotification(when);
			break;
		
			case AFTER_LUNCH:
				scheduleWorkdayCompleteNotification(when, workedHours);
				scheduleWorkingTimeViolationNotification(when, workedHours);
			break;
			
			case LEAVING:
				cancelWorkdayCompleteNotification(workedHours);
				cancelWorkingTimeViolationNotification();
			break;
			
			case ANY_ENTRANCE:
			break;
			
			case ANY_LEAVING:
			break;
			
			default:
			break;
		}
		
	}
	
	private PendingIntent getPendingIntent(Intent intent) {
		PendingIntent notifier = PendingIntent.getBroadcast(mContext, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return notifier;
	}
	
	private PendingIntent getWorkdayCompletePendingIntent() {
		Intent intent = getWorkdayCompleteIntent();
		return getPendingIntent(intent);
	}
	
	private PendingIntent getWorkingTimeViolationPendingIntent() {
		Intent intent = getWorkingTimeViolationIntent();
		return getPendingIntent(intent);
	}
	
	private PendingIntent getGobackToWorkPendingIntent() {
		Intent intent = getGobackToWorkIntent();
		return getPendingIntent(intent);
	}

	private void cancelAlarm(PendingIntent notifier) {
		notifier.cancel();
		alarmManager.cancel(notifier);
	}
	
	private void cancelWorkdayCompleteNotification(long workedHours) {
		if(workedHours < BusinessHourCommom.getWorkingTime(sharedPreferences))
		{
			PendingIntent notifier = getWorkdayCompletePendingIntent();
			cancelAlarm(notifier);
		}
	}

	private void cancelWorkingTimeViolationNotification() {
		PendingIntent notifier = getWorkingTimeViolationPendingIntent();
		cancelAlarm(notifier);
	}
	
	private void cancelGobackToWorkNotification() {
		PendingIntent notifier = getGobackToWorkPendingIntent();
		cancelAlarm(notifier);
	}

	
	private void scheduleWorkdayCompleteNotification(long when,
			long workedHours) {
		if(isNotificationEnabled(mContext) && shouldNotify(mContext, WORKDAY_COMPLETE)){
		
			PendingIntent notifier = getWorkdayCompletePendingIntent();
			long notificationHour = when + 
					(BusinessHourCommom.getWorkingTime(sharedPreferences) - workedHours);
			
			alarmManager.set(AlarmManager.RTC_WAKEUP, notificationHour, notifier);
		}
	}
	

	private void scheduleWorkingTimeViolationNotification(long when,
			long workedHours) {
		if(isNotificationEnabled(mContext) && shouldNotify(mContext, WORKING_TIME_VIOLATION)){
		
			PendingIntent notifier = getWorkingTimeViolationPendingIntent();
			long notificationHour = when + (TEN_HOURS - workedHours);
			alarmManager.set(AlarmManager.RTC_WAKEUP, notificationHour, notifier);
		}
	}
	
	private void scheduleGobackToWorkNotification(long when) {
		if(isNotificationEnabled(mContext)){
		
			PendingIntent notifier = getGobackToWorkPendingIntent();
			long notificationHour = when + BusinessHourCommom.getEatingTimeInterval(sharedPreferences);
			alarmManager.set(AlarmManager.RTC_WAKEUP, notificationHour, notifier);
		}
	}
	
	private boolean isNotificationEnabled(Context context){
		if(sharedPreferences == null){
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
		return sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true);
	}
}

	

