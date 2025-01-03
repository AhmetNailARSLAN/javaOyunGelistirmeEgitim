package com.example.oyungelistirmeegitim;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Veritabanı Bilgileri
    private static final String DATABASE_NAME = "EgitimDB";
    private static final int DATABASE_VERSION = 1;

    // Tablo ve Sütun Adları
    public static final String TABLE_DERSLER = "dersler";
    public static final String COLUMN_DERS_ID = "dersId";
    public static final String COLUMN_DERS_AD = "dersAd";
    public static final String COLUMN_DERS_ACIKLAMA = "dersAciklama";
    public static final String COLUMN_DERS_VIDEO_URL = "dersVideoUrl";
    public static final String COLUMN_DERS_IMAGE_URL = "dersImageUrl";
    public static final String COLUMN_DERS_VIEW = "dersView";

    // Tablo Oluşturma SQL
    private static final String CREATE_TABLE_DERSLER =
            "CREATE TABLE " + TABLE_DERSLER + " (" +
                    COLUMN_DERS_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_DERS_AD + " TEXT, " +
                    COLUMN_DERS_ACIKLAMA + " TEXT, " +
                    COLUMN_DERS_VIDEO_URL + " TEXT, " +
                    COLUMN_DERS_IMAGE_URL + " TEXT, " +
                    COLUMN_DERS_VIEW + " INTEGER" +
                    ");";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tüm verileri alma metodu
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DERSLER, null);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Veritabanı oluşturulurken çağrılır
        db.execSQL(CREATE_TABLE_DERSLER); // Tabloyu oluştur
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Veritabanı sürümü güncellendiğinde çağrılır
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DERSLER); // Eski tabloyu sil
        onCreate(db); // Yeni tabloyu oluştur
    }

}