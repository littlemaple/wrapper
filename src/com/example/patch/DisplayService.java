package com.example.patch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DisplayService extends Service {

	private Thread	thread	= new Thread(new Runnable() {

								@Override
								public void run() {

									while(true){
										
									}
								}
							});

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
