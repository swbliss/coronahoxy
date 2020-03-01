package com.example.coronahoxy.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.example.coronahoxy.API_and_Tools.RouteStorageService;
import com.example.coronahoxy.FragmentPages.CustomViewPager;
import com.example.coronahoxy.FragmentPages.Fragment_First;
import com.example.coronahoxy.FragmentPages.Fragment_Second;
import com.example.coronahoxy.FragmentPages.Fragment_Third;
import com.example.coronahoxy.FragmentPages.SectionPageAdapter;
import com.example.coronahoxy.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
	private final String TAG = "sungchul";
	private static final int GPS_ENABLE_REQUEST_CODE = 2001;
	private static final int PERMISSIONS_REQUEST_CODE = 100;

	String[] REQUIRED_PERMISSIONS = {
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.ACCESS_BACKGROUND_LOCATION
	};

	private ImageView location;
	private CustomViewPager viewPager;
	private TabLayout tabLayout;
	SectionPageAdapter adapter;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	public static int status;
	public static int backgroundStatus;

	private Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkPermision();
		location = findViewById(R.id.location_check_button);
		location.setOnClickListener(myListener);

		init();
		preferences = getSharedPreferences("Status", MODE_PRIVATE);
		editor = preferences.edit();
		status = preferences.getInt("status", 0);
		backgroundStatus = preferences.getInt("backgroundStatus", 0);


		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
		boolean isWhiteListing = false;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
		}
		if (!isWhiteListing) {
			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
			intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
			startActivity(intent);
		}
		if (RouteStorageService.serviceIntent==null) {
			Log.d(TAG, "onCreate: Service null");
			serviceIntent = new Intent(this, RouteStorageService.class);
			startService(serviceIntent);
		} else {
			Log.d(TAG, "onCreate: Service not null");

			serviceIntent = RouteStorageService.serviceIntent;//getInstance().getApplication();
			Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (serviceIntent!=null) {
			stopService(serviceIntent);
			serviceIntent = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(status>0) editor.putInt("status", status);
		else editor.putInt("status", status+1);
		editor.commit();
	}

	private void init() {
		viewPager = findViewById(R.id.main_pager);
		viewPager.setOffscreenPageLimit(3);
		setupViewPager(viewPager);
		tabLayout = findViewById(R.id.main_bottom_tab);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
		tabLayout.getTabAt(1).setIcon(R.drawable.note_icon);
		tabLayout.getTabAt(2).setIcon(R.drawable.alarm_icon);
	}

	private View.OnClickListener myListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.location_check_button:
					checkPermision();
					break;
			}
		}
	};

	private void setupViewPager(ViewPager viewPager) {
		adapter = new SectionPageAdapter(getSupportFragmentManager());
		adapter.addFragment(new Fragment_First());
		adapter.addFragment(new Fragment_Second());
		adapter.addFragment(new Fragment_Third());
		viewPager.setAdapter(adapter);
	}

	public void checkPermision() {
		if (!checkLocationServicesStatus()) {

			showDialogForLocationServiceSetting();
		} else {

			checkRunTimePermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int permsRequestCode,
		@NonNull String[] permissions,
		@NonNull int[] grandResults) {

		if (permsRequestCode == PERMISSIONS_REQUEST_CODE
			&& grandResults.length == REQUIRED_PERMISSIONS.length) {

			// 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

			boolean check_result = true;
			// 모든 퍼미션을 허용했는지 체크합니다.

			for (int result : grandResults) {
				if (result != PackageManager.PERMISSION_GRANTED) {
					check_result = false;
					break;
				}
			}

			if (check_result) {
				//Todo 초록색
				location.setBackgroundColor(getResources().getColor(R.color.permission_green));
				//위치 값을 가져올 수 있음
				;
			} else {
				// 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

				if (ActivityCompat
					.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
					|| ActivityCompat
					.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
					//Todo 빨강색
					location.setBackgroundColor(Color.RED);
				} else if (ActivityCompat
					.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])) {
					//Todo 노랑색
					location.setBackgroundColor(getResources().getColor(R.color.warning));
				} else {
					location.setBackgroundColor(getResources().getColor(R.color.permission_green));
				}
			}

		}
	}

	void checkRunTimePermission() {

		//런타임 퍼미션 처리
		// 1. 위치 퍼미션을 가지고 있는지 체크합니다.
		int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_FINE_LOCATION);
		int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_COARSE_LOCATION);
		int hasBackLocationPermission = ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_BACKGROUND_LOCATION);

		if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
			hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
			hasBackLocationPermission == PackageManager.PERMISSION_GRANTED) {

			// 2. 이미 퍼미션을 가지고 있다면
			// ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)

			// 3.  위치 값을 가져올 수 있음

		} else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

			// 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
			if (ActivityCompat
				.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

				// 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
				Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
				// 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
				ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
					PERMISSIONS_REQUEST_CODE);

			} else {
				// 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
				// 요청 결과는 onRequestPermissionResult에서 수신됩니다.
				ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
					PERMISSIONS_REQUEST_CODE);
			}

		}

	}

	public String getCurrentAddress(double latitude, double longitude) {

		//지오코더... GPS를 주소로 변환
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());

		List<Address> addresses;

		try {

			addresses = geocoder.getFromLocation(
				latitude,
				longitude,
				7);
		} catch (IOException ioException) {
			//네트워크 문제
			Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
			return "지오코더 서비스 사용불가";
		} catch (IllegalArgumentException illegalArgumentException) {
			Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
			return "잘못된 GPS 좌표";

		}

		if (addresses == null || addresses.size() == 0) {
			Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
			return "주소 미발견";

		}

		Address address = addresses.get(0);
		return address.getAddressLine(0).toString() + "\n";

	}

	//여기부터는 GPS 활성화를 위한 메소드들
	private void showDialogForLocationServiceSetting() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("위치 서비스 비활성화");
		builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
			+ "위치 설정을 수정하실래요?");
		builder.setCancelable(true);
		builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent callGPSSettingIntent
					= new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
			}
		});
		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

			case GPS_ENABLE_REQUEST_CODE:

				//사용자가 GPS 활성 시켰는지 검사
				if (checkLocationServicesStatus()) {
					if (checkLocationServicesStatus()) {

						Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
						checkRunTimePermission();
						return;
					}
				}

				break;
		}
	}

	public boolean checkLocationServicesStatus() {
		LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
			|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
}
