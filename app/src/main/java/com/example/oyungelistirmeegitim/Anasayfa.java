package com.example.oyungelistirmeegitim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Anasayfa extends AppCompatActivity {
    ArrayList<EgitimClass> egitimList = new ArrayList<>();
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anasayfa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tam ekran modu
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        dbHelper = new DatabaseHelper(Anasayfa.this);

        readDataFromDatabase();

        ListView lw = findViewById(R.id.listView);

          CustomAdapter customAdapter = new CustomAdapter(Anasayfa.this, egitimList);
          lw.setAdapter(customAdapter);

          lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                  Intent intent = new Intent(Anasayfa.this, EgitimDetay.class);
                  intent.putExtra("egitimdetayclass", egitimList.get(i));
                  startActivity(intent);
              }
          });

    }
    public void readDataFromDatabase() {
        // Egitimlist verisini al
        Cursor cursor = dbHelper.getAllData();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String dersId = cursor.getString(cursor.getColumnIndexOrThrow("dersId"));
                String dersAd = cursor.getString(cursor.getColumnIndexOrThrow("dersAd"));
                String dersAciklama = cursor.getString(cursor.getColumnIndexOrThrow("dersAciklama"));
                String dersVideoUrl = cursor.getString(cursor.getColumnIndexOrThrow("dersVideoUrl"));
                String dersImageUrl = cursor.getString(cursor.getColumnIndexOrThrow("dersImageUrl"));
                int dersView = cursor.getInt(cursor.getColumnIndexOrThrow("dersView"));

                EgitimClass egitim = new EgitimClass(dersId, dersAd, dersAciklama,dersVideoUrl,dersImageUrl, dersView);
                egitimList.add(egitim);

                // Verileri işleyin
                System.out.println("Ders Adı: " + dersAd + ", İzlenme: " + dersView);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close(); // Kaynakları serbest bırakın
        }
    }

}