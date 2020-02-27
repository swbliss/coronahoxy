package com.example.coronahoxy.MainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.example.coronahoxy.FragmentPages.SectionPageAdapter;
import com.example.coronahoxy.FragmentPages.Fragment_First;
import com.example.coronahoxy.FragmentPages.Fragment_Second;
import com.example.coronahoxy.FragmentPages.Fragment_Third;
import com.example.coronahoxy.R;
import com.google.android.material.tabs.TabLayout;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
	private final String TAG = "Hoxy";

	private ImageView location;
	private ViewPager viewPager;
	private TabLayout tabLayout;
	SectionPageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		location = findViewById(R.id.location_check_button);
		location.setOnClickListener(myListener);

		init();

	}
	private void init(){
		viewPager = findViewById(R.id.main_pager);
		setupViewPager(viewPager);
		tabLayout = findViewById(R.id.main_bottom_tab);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
		tabLayout.getTabAt(1).setIcon(R.drawable.note_icon);
		tabLayout.getTabAt(2).setIcon(R.drawable.alarm_icon);
	}

	private void setupViewPager(ViewPager viewPager){
		adapter = new SectionPageAdapter(getSupportFragmentManager());
		adapter.addFragment(new Fragment_First());
		adapter.addFragment(new Fragment_Second());
		adapter.addFragment(new Fragment_Third());
		viewPager.setAdapter(adapter);
	}

	private void checkPermissionLocation() {
		boolean permissionAccessCoarseLocationApproved =
			ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				== PackageManager.PERMISSION_GRANTED;

		if (permissionAccessCoarseLocationApproved) {
			Log.d(TAG, "checkPermissionLocation: aaaa");
			boolean backgroundLocationPermissionApproved =
				ActivityCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_BACKGROUND_LOCATION)
					== PackageManager.PERMISSION_GRANTED;
			if (backgroundLocationPermissionApproved) {
				Log.d(TAG, "checkPermissionLocation: bbbb");
				// App can access location both in the foreground and in the background.
				// Start your service that doesn't have a foreground service type
				// defined.
			} else {
				Log.d(TAG, "checkPermissionLocation: cccc");
				/** 사용자가 don't ask again 을 체크한 상황
				 *  설정에 들어가서 사용자 위치 권한 허가? 를 수동으로 설정해야함
				 *  이를 설명하는 다이얼로그 알람 띄우기
				 */
				location.setBackgroundColor(ContextCompat.getColor(this, R.color.warning));

				// App can only access location in the foreground. Display a dialog
				// warning the user that your app must have all-the-time access to
				// location in order to function properly. Then, request background
				// location.
				ActivityCompat.requestPermissions(this, new String[] {
						Manifest.permission.ACCESS_BACKGROUND_LOCATION},
					1234);
			}
		} else {
			Log.d(TAG, "checkPermissionLocation: dddd");
			location.setBackgroundColor(Color.RED);
			// App doesn't have access to the device's location at all. Make full request
			// for permission.
			ActivityCompat.requestPermissions(this, new String[] {
					Manifest.permission.ACCESS_BACKGROUND_LOCATION
				},
				1234);
		}
	}
	private void popup(){
		Intent intent = new Intent(this, LocationRecommendActivity.class);
		startActivity(intent);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
		@NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private View.OnClickListener myListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.location_check_button:
					checkPermissionLocation();
					break;
			}
		}
	};

	@Override
	public void onMapReady(@NonNull NaverMap naverMap) {

	}
}
