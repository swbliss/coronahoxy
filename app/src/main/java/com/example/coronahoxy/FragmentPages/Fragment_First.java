package com.example.coronahoxy.FragmentPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.coronahoxy.R;
import com.example.coronahoxy.Route.RouteCheckActivity;

import com.naver.maps.map.MapView;

public class Fragment_First extends Fragment {
	MapView mapView;
	TextView routeCheck;

	public Fragment_First(){

	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.first_page, container, false);
		return view;
	}

	private View.OnClickListener myListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
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
		mapView = view.findViewById(R.id.first_map_view);
		mapView.onCreate(savedInstanceState);
		routeCheck = view.findViewById(R.id.first_check_route);
		routeCheck.setOnClickListener(myListener);
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
