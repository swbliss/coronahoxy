package com.example.coronahoxy;

import android.app.Application;

import com.naver.maps.map.NaverMapSdk;

public class CorynaHoxy extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		NaverMapSdk.getInstance(this).setClient(
			new NaverMapSdk.NaverCloudPlatformClient("YOUR_CLIENT_ID_HERE"));
	}
}
