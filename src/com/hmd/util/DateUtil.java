package com.hmd.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	
	public static String getCurrentYear(){
		return new SimpleDateFormat("yyyy").format(new Date());
	}

}
