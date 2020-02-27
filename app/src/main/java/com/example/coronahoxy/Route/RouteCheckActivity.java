package com.example.coronahoxy.Route;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coronahoxy.R;

public class RouteCheckActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	private RouteAdapter routeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_check);
		init();
	}

	private void init(){
		recyclerView = findViewById(R.id.route_recycler);
		routeAdapter = new RouteAdapter();
		recyclerView.setAdapter(routeAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
	}
}
