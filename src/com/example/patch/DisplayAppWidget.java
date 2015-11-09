package com.example.patch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.MainActivity;
import com.example.demo.R;

public class DisplayAppWidget extends AppWidgetProvider {

	public static final String	TAG	= "DisplayAppWidget";

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Log.i(TAG, "onEnabled");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_main);
		remoteViews.setOnClickPendingIntent(R.id.container, pendingIntent);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.ic_launcher);
		remoteViews.setImageViewResource(R.id.imageView2, R.drawable.btn_fangda);
		remoteViews.setImageViewResource(R.id.imageView3, R.drawable.btn_suoxiao);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName componentName = new ComponentName(context, DisplayAppWidget.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
		Log.i(TAG, "onReceive");
	}

	Paint	paint	= new Paint();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		for (int i = 0; i < appWidgetIds.length; i++) {
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_main);
			remoteViews.setOnClickPendingIntent(R.id.container, pendingIntent);
			remoteViews.setImageViewResource(R.id.imageView1, R.drawable.ic_launcher);
			remoteViews.setImageViewResource(R.id.imageView2, R.drawable.btn_fangda);
			remoteViews.setImageViewResource(R.id.imageView3, R.drawable.btn_suoxiao);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		Log.i(TAG, "onUpdate");
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.i(TAG, "onDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Log.i(TAG, "onDisabled");
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
		// TODO Auto-generated method stub
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
		Log.i(TAG, "onAppWidgetOptionsChanged-->appWidgetId:" + appWidgetId);
	}
}
