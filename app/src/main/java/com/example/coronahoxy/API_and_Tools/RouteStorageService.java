package com.example.coronahoxy.API_and_Tools;

import static com.example.coronahoxy.FragmentPages.Fragment_First.*;
import static com.example.coronahoxy.MainActivity.MainActivity.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.coronahoxy.DB.entity.UserRoute;
import com.example.coronahoxy.MainActivity.MainActivity;
import com.example.coronahoxy.R;

public class RouteStorageService extends Service {

	private Thread mainThread;
	private Timer timer;
	private TimerTask timerTask;
	public static Intent serviceIntent = null;
	private GpsTracker gpsTracker;
	private SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");

	public RouteStorageService() {
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		gpsTracker = new GpsTracker(this);
		serviceIntent = intent;

		Log.d(TAG, "onStartCommand: is timer null?" + (timer == null));
//		setTimer();
		Log.d(TAG, "onStartCommand: here");
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean run = true;
				while (run) {
					try {
						Thread.sleep(1000 * 1 * 5); // 10 minute
						//addLocationData();
						Log.d(TAG, "run: ");
						sendNotification("asdf");
					} catch (InterruptedException e) {
						run = false;
						e.printStackTrace();
					}
				}
			}
		});
		mainThread.start();
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		serviceIntent = null;
		setAlarmTimer();
		Thread.currentThread().interrupt();

		if (mainThread != null) {
			mainThread.interrupt();
			mainThread = null;
		}
	}

	protected void setAlarmTimer() {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.SECOND, 1);
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

		AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+5000, sender);
	}

	private void sendNotification(String messageBody) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent
			.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

		String channelId =
			"fcm_default_channel";//getString(R.string.default_notification_channel_id);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder =
			new NotificationCompat.Builder(this, channelId)
				.setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
				.setContentTitle("Service test")
				.setContentText(messageBody)
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setPriority(Notification.PRIORITY_HIGH)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager =
			(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel =
				new NotificationChannel(channelId, "Channel human readable title",
					NotificationManager.IMPORTANCE_DEFAULT);
			notificationManager.createNotificationChannel(channel);
		}

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
	}

	private void setTimer() {
		Log.d(TAG, "setTimer: status : " + status);
		if (status == 0) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					addLocationData();
				}
			};
			timer = new Timer();
			timer.schedule(timerTask, 0, 1000 * 5);
			status++;
		}
	}

	public void addLocationData() {
		double latitude, longitude;
		latitude = gpsTracker.getLatitude();
		longitude = gpsTracker.getLongitude();

		UserRoute data = new UserRoute();
		data.latitude = latitude;
		data.longitude = longitude;
		data.time = dateToString(new Date(System.currentTimeMillis()));

		Log.d(TAG, "addLocationData: current time : " + data.time + ", latitude : " + latitude
			+ ", longitude : " + longitude);

		//		database.userRouteDao().insertRoute(data);
	}

	private String dateToString(Date date) {
		return format.format(date);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
	}
}
