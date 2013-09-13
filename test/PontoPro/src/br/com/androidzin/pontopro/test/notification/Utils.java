package br.com.androidzin.pontopro.test.notification;

import br.com.androidzin.pontopro.MainActivity;
import junit.framework.TestCase;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Utils extends TestCase{
	
	

	private static void cancelAlarm(AlarmManager alarmManager,
			PendingIntent notifier) {
		if(notifier != null){
			notifier.cancel();
			alarmManager.cancel(notifier);
		}
	}
	
	public static PendingIntent cancelAlarm(Context context, Intent intent, AlarmManager alarmManager) {
		PendingIntent notifier = getPendingIntentFromBroadcast(context, intent);
		
		cancelAlarm(alarmManager, notifier);
		
		return notifier;
	}
	
	private static PendingIntent getPendingIntentFromBroadcast(
			Context context, Intent intent) {
		PendingIntent notifier = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		return notifier;
	}
	
	public static void assertAlarmIsScheduled(Context context, Intent intent, AlarmManager alarmManager) {
		PendingIntent notifier = getPendingIntentFromBroadcast(context, intent);
		
		assertNotNull("Schedule did not work", notifier);
		
		cancelAlarm(alarmManager, notifier);
	}
	
	public static void assertAlarmIsNotScheduled(Context context, Intent intent, AlarmManager alarmManager) {
		PendingIntent notifier = getPendingIntentFromBroadcast(context, intent);
		
		assertNull("Notification was scheduled", notifier);

		
		cancelAlarm(alarmManager, notifier);
	}

	
	public static void assertAlarmNotificationWasCancelled(Context context, Intent intent) {
		PendingIntent notifier = getPendingIntentFromBroadcast(context, intent);
		
		assertNull("Cancel notification did not work", notifier);
	}
	
	public static void assertNotificationIsScheduled(Context context) {
		PendingIntent resultPendingIntent = getNotificationResultPendingIntent(context);
		
		
		assertNotNull("Notification is not scheduled", resultPendingIntent);
	}
	
	public static void assertNotificationIsNotScheduled(Context context) {
		PendingIntent resultPendingIntent = getNotificationResultPendingIntent(context);
		
		
		assertNull("Notification is scheduled", resultPendingIntent);
	}
	
	public static void cancelScheduledNotifications(Context context) {
		PendingIntent resultPendingIntent = getNotificationResultPendingIntent(context);
		
		if(resultPendingIntent != null){
			resultPendingIntent.cancel();
		}
	}

	private static PendingIntent getNotificationResultPendingIntent(
			Context context) {
		Intent resultIntent = new Intent(context, MainActivity.class);
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    context,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_NO_CREATE
		);
		return resultPendingIntent;
	}
}
