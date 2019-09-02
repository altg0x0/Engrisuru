package com.example.lord.engrisuru.japanese;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.lord.engrisuru.db.WeightedStringArray;

@Entity
public class Kanji {
    @PrimaryKey
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    public char character; // All kanji are supposedly 'non-astral' unicode

    public int grade = 11; // 11 == ungraded
    public String[] onyomiReadings = new String[]{"ソンザイシテイナイ"};
    public String[] kunyomiReadings = new String[]{"そんざいしていない"};
    public String[] englishMeanings;

    public double weight;

//    public WeightedStringArray onyomiReadings = new WeightedStringArray(new String[]{"ソンザイシテイナイ"}, 1);
//    public WeightedStringArray kunyomiReadings = new WeightedStringArray(new String[]{"そんざいしていない"}, 1);
//    public WeightedStringArray englishMeanings;
}
