package com.example.coronahoxy.FragmentPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import com.example.coronahoxy.API_and_Tools.GpsTracker;
import com.example.coronahoxy.DB.HoxyDataBase;
import com.example.coronahoxy.R;
import com.example.coronahoxy.Route.RouteCheckActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;

public class Fragment_First extends Fragment implements OnMapReadyCallback {
	public static final String TAG = "sungchul";
	private MapView mapView;
	private MapFragment mapFragment;
	private TextView routeCheck;
	private FragmentManager fm;

	private NaverMapOptions options;
	public static NaverMap NMap;
	private GpsTracker gpsTracker;

	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
	private FusedLocationSource locationSource;

	public static HoxyDataBase database;

	void initDatabase(){
		database = Room.databaseBuilder(getContext(),
			HoxyDataBase.class, "searchat.db")
			.fallbackToDestructiveMigration()
			.allowMainThreadQueries()
			.build();
	}

	public Fragment_First() {
	}

	public void setFirstOptions() { //1-8. 초기옵션 설정

		double latitude = gpsTracker.getLatitude();
		double longitude = gpsTracker.getLongitude();

		options = new NaverMapOptions()
			.camera(new CameraPosition(new LatLng(latitude, longitude), 14));
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {

		initDatabase();
		gpsTracker = new GpsTracker(getActivity());

		fm = getChildFragmentManager();
		mapFragment = (MapFragment)fm.findFragmentById(R.id.first_map_view);
		setFirstOptions();
		if (mapFragment == null) { //맵프래그먼트 생성된 적 없으면
			mapFragment = MapFragment.newInstance(options); //새로 만들어주고   // 1-8. 초기옵션 추가
			Toast.makeText(getContext(), "맵 생성 완료", Toast.LENGTH_SHORT).show();
			fm.beginTransaction().add(R.id.first_map_view, mapFragment)
				.commit(); // 프래그매니저에게 명령 map 레이아웃에 생성된 맵 객체를 add
		}
		//1-3. 맵 프래그먼트에 NaverMap 객체 가져옴 : MapFragment 및 MapView는 지도에 대한 뷰 역할만을 담당하므로 API를 호출하려면 인터페이스 역할을 하는 NaverMap 객체가 필요
		locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
		mapFragment.getMapAsync(this); //이거 만들면 onMapReady 사용 가능
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.first_page, container, false);
		return viewGroup;
	}

	private View.OnClickListener myListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.first_check_route:
					Intent intent = new Intent(getActivity(), RouteCheckActivity.class);
					startActivity(intent);
					break;
			}
		}
	};

	@Override
	public void onViewCreated(@NonNull View view,
		@Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		routeCheck = view.findViewById(R.id.first_check_route);
		routeCheck.setOnClickListener(myListener);
		mapView = view.findViewById(R.id.first_map_view);
		mapView.onCreate(savedInstanceState);

	}

	/**
	 *
	 */
	@Override
	public void onMapReady(@NonNull NaverMap naverMap) {
		naverMap.setLocationSource(locationSource);
		setMapUI(naverMap);
		LocationOverlay locationOverlay = naverMap.getLocationOverlay();
		locationOverlay.setVisible(true);
	}

	/**
	 *
	 */
	public void setMapUI(NaverMap naverMap) {
		UiSettings uiSettings = naverMap.getUiSettings();
		uiSettings.setZoomControlEnabled(true);
		uiSettings.setLocationButtonEnabled(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		mapView.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		mapView.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
}
