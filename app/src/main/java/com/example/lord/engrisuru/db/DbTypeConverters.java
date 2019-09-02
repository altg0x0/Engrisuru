package com.example.lord.engrisuru.db;

import android.text.TextUtils;

import androidx.room.TypeConverter;

public class DbTypeConverters {
    @TypeConverter
    public String[] stringToStringArray (String str)
    {
        return str.split(",");
    }

    @TypeConverter
    public String stringToStringArray (String[] strArr)
    {
        return TextUtils.join(",", strArr);
    }

    @TypeConverter
    public char StringToChar(String str)
    {
        return str.charAt(0);
    }

    @TypeConverter
    public String StringToChar(char ch)
    {
        return Character.toString(ch);
    }
}
