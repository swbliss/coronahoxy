package com.example.coronahoxy.Route;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coronahoxy.R;

public class RouteAdapter extends RecyclerView.Adapter<CommonViewHolder> {
	private ArrayList<RouteData> routeList;

	@NonNull
	@Override
	public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(viewType == RouteData.ROUTE){
			return new RouteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.route_view_holder, parent, false));
		}
		else{
			return new RouteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.date_view_holder, parent, false));
		}
	}

	@Override
	public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
		if(getItemViewType(position)==RouteData.ROUTE){
			RouteHolder rHolder = (RouteHolder) holder;
			rHolder.time.setText(routeList.get(position).hourMin);
			rHolder.commonTime.setText(routeList.get(position).commonTime);
			rHolder.location.setText(routeList.get(position).description);
			rHolder.order.setText(routeList.get(position).order+"번째 확진자");
		}
		else{
			DateHolder dHolder = (DateHolder) holder;
			dHolder.textView.setText(routeList.get(position).monthDay);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return routeList.get(position).type;
	}

	@Override
	public int getItemCount() {
		return 0;
	}
}

abstract class CommonViewHolder extends RecyclerView.ViewHolder{
	public CommonViewHolder(@NonNull View itemView) {
		super(itemView);
	}
}

class RouteHolder extends CommonViewHolder {
	TextView time, location, commonTime, order;

	public RouteHolder(@NonNull View itemView) {
		super(itemView);
		time = itemView.findViewById(R.id.route_time);
		location = itemView.findViewById(R.id.route_location);
		commonTime = itemView.findViewById(R.id.route_common_time);
		order = itemView.findViewById(R.id.route_order);
	}
}

class DateHolder extends CommonViewHolder {
	TextView textView;
	public DateHolder(@NonNull View itemView) {
		super(itemView);
		textView = itemView.findViewById(R.id.date_date);
	}
}
