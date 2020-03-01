package com.example.coronahoxy.DB.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.coronahoxy.DB.entity.UserRoute;

@Dao
public interface UserRouteDao {
	@Query("SELECT num, time, latitude, longitude FROM userroute")
	public List<UserRoute> getAll();

	@Insert
	public void insertAll(UserRoute... userRoutes);

	@Insert
	public void insertRoute(UserRoute userRoute);

}
