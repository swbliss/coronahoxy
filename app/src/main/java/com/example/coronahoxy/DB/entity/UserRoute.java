package com.example.coronahoxy.DB.entity;


import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserRoute {
	@PrimaryKey(autoGenerate = true)
	public int num;

	@ColumnInfo(name = "time")
	@Nullable
	public String time;

	@ColumnInfo(name = "latitude")
	@Nullable
	public double latitude;

	@ColumnInfo(name = "longitude")
	@Nullable
	public double longitude;
}
