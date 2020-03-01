package com.example.coronahoxy.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.coronahoxy.DB.dao.UserRouteDao;
import com.example.coronahoxy.DB.entity.UserRoute;

@Database(entities = {UserRoute.class}, version = 1)
public abstract class HoxyDataBase extends RoomDatabase{
	public abstract UserRouteDao userRouteDao();
}
