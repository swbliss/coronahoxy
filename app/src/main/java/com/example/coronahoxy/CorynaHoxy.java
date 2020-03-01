package com.example.coronahoxy;

import android.app.Application;
import com.facebook.stetho.Stetho;

import com.naver.maps.map.NaverMapSdk;

public class CorynaHoxy extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
		NaverMapSdk.getInstance(this).setClient(
			new NaverMapSdk.NaverCloudPlatformClient("mlknmjo0k2"));
	}
}
