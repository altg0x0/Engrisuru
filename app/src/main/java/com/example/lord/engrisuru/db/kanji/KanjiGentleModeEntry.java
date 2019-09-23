package com.example.lord.engrisuru.db.kanji;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.lord.engrisuru.db.EngrisuruDatabase;
import com.example.lord.engrisuru.japanese.Kanji;

@Entity
public class KanjiGentleModeEntry {
    private static final double learnWeightThreshold = 1;
    private static final double unlearnWeightThreshold = 10;

    @PrimaryKey
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    public char character; // All kanji are supposedly 'non-astral' unicode

    public boolean learnt = false;

    private KanjiGentleModeEntry(Kanji kanji) {
        this.character = kanji.character;
        this.learnt = kanji.weight < learnWeightThreshold;
    }

    public KanjiGentleModeEntry() {}

    public static void updateForKanji(Kanji kanji) {
        KanjiGentleModeEntry entry = EngrisuruDatabase.getInstance().kanjiGentleModeEntryDao().getEntryByCharacter(kanji.character);
        if (entry == null) entry = new KanjiGentleModeEntry(kanji);
        else {
            entry.learnt = kanji.weight < learnWeightThreshold || (entry.learnt && kanji.weight < unlearnWeightThreshold);
        }
        EngrisuruDatabase.getInstance().kanjiGentleModeEntryDao().update(entry);
    }
}
