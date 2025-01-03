package com.example.oyungelistirmeegitim;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private DatabaseReference db;
    List<EgitimClass> dersler;

    String dersisim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        db = database.getReference("Egitimler");

        ImageView loadingimage = findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.loadinganim);


        loadingimage.startAnimation(animation);
        dbHelper = new DatabaseHelper(this);

        // internet kontrol
        if (isInternetAvailable()) {
            syncFirebaseToSQLite(); // İnternet varsa işlemleri başlat
        } else {
            showNoInternetDialog(); // İnternet yoksa uyarı göster
        }
    }

    private void saveDataToFirebase() {
        // EgitimClass nesnesi oluşturuluyor
        EgitimClass egitimClass = new EgitimClass(
                "2",
                "Unity ile S.O.L.I.D Prensipleri",
                "Yazılımla ilgilenen biriyseniz S.O.L.I.D prensiplerini mutlaka duymuşsunuzdur. Bu videomda S.O.L.I.D prensipleri nedir, nerelerde kullanılır, ne işe yararlar ve Unity'de nasıl uygulanırlar göstermeye çalıştım. "+
                        "Ayrıca, animasyonlu anlatım yaparak teorik kısmını, canlı kod refactor'ü yaparak pratik kısmını anlamaya çalışacağız",
                "https://www.youtube.com/watch?v=S_uaNa-tyis&t=1012s",
                "aaa",
                3412
        );

        // Benzersiz bir anahtar oluştur
        String key = db.push().getKey(); // push() otomatik bir ID üretir
        if (key != null) {
            egitimClass.setDersId(key); // Anahtarı nesneye atıyoruz

            // Veriyi Firebase'e yükle
            db.child(key).setValue(egitimClass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Veri başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Veri kaydedilemedi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(MainActivity.this, "Anahtar oluşturulamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    private void syncFirebaseToSQLite() {
        // SQLite Verilerini Çek
        dersler = getSQLiteData();

        // Firebase Verilerini Çek
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<EgitimClass> firebaseDersler = new ArrayList<>();

                for (DataSnapshot dersSnapshot : dataSnapshot.getChildren()) {
                    EgitimClass ders = dersSnapshot.getValue(EgitimClass.class);
                    firebaseDersler.add(ders);
                }
                // Firebase ve SQLite Verilerini Karşılaştır
                if (isDataEqual(dersler, firebaseDersler)) {

                    // Eşit ise diğer sayfayı aç
                    openNextActivity();
                } else {
                    // Eşit değilse Firebase verilerini SQLite'a yaz
                    saveDataToSQLite(firebaseDersler);
                    openNextActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Veri çekme hatası: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<EgitimClass> getSQLiteData() {
        List<EgitimClass> dersler = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("dersler", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") EgitimClass ders = new EgitimClass(
                    cursor.getString(cursor.getColumnIndex("dersId")),
                    cursor.getString(cursor.getColumnIndex("dersAd")),
                    cursor.getString(cursor.getColumnIndex("dersAciklama")),
                    cursor.getString(cursor.getColumnIndex("dersVideoUrl")),
                    cursor.getString(cursor.getColumnIndex("dersImageUrl")),
                    cursor.getInt(cursor.getColumnIndex("dersView"))
            );
            dersler.add(ders);
        }
        cursor.close();
        return dersler;
    }
    private boolean isDataEqual(List<EgitimClass> sqliteDersler, List<EgitimClass> firebaseDersler) {
        if (sqliteDersler == null || firebaseDersler == null) return false;
        if (sqliteDersler.size() != firebaseDersler.size()) return false;

        // Listeleri sıralayın
        Collections.sort(sqliteDersler, Comparator.comparing(EgitimClass::getDersId));
        Collections.sort(firebaseDersler, Comparator.comparing(EgitimClass::getDersId));

        for (int i = 0; i < sqliteDersler.size(); i++) {
            EgitimClass sqliteDers = sqliteDersler.get(i);
            EgitimClass firebaseDers = firebaseDersler.get(i);

            if (!Objects.equals(sqliteDers.getDersId(), firebaseDers.getDersId()) ||
                    !Objects.equals(sqliteDers.getDersAd(), firebaseDers.getDersAd()) ||
                    !Objects.equals(sqliteDers.getDersAciklama(), firebaseDers.getDersAciklama()) ||
                    !Objects.equals(sqliteDers.getDersVideoUrl(), firebaseDers.getDersVideoUrl()) ||
                    sqliteDers.getDersView() != firebaseDers.getDersView()) {
                return false;
            }
        }
        return true;
    }

    //Datayı sql e kaydet
    private void saveDataToSQLite(List<EgitimClass> _dersler) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM dersler"); // Eski verileri temizle

        for (EgitimClass ders : _dersler) {
            ContentValues values = new ContentValues();
            values.put("dersId", ders.getDersId());
            values.put("dersAd", ders.getDersAd());
            values.put("dersAciklama", ders.getDersAciklama());
            values.put("dersVideoUrl", ders.getDersVideoUrl());
            values.put("dersImageUrl", ders.getDersImageUrl());
            values.put("dersView", ders.getDersView());
            db.insert("dersler", null, values);
        }
        db.close();
    }

    // Anasayfayı açma metodu
    private void openNextActivity() {
        Intent intent = new Intent(MainActivity.this, Anasayfa.class);
        startActivity(intent);
        finish();
    }

    // İnternet kontrolü yapan metod
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }


    // Uyarı ekranı gösteren metod
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("İnternet Bağlantısı Yok");
        builder.setMessage("Lütfen internet bağlantınızı kontrol edin ve tekrar deneyin.");
        builder.setCancelable(false); // Kullanıcı bağlantı olmadan ekrandan çıkamasın

        // "Tekrar Dene" butonu
        builder.setPositiveButton("Tekrar Dene", (dialog, which) -> {
            if (isInternetAvailable()) {
                syncFirebaseToSQLite(); // İnternet varsa işlemleri başlat
            } else {
                showNoInternetDialog(); // İnternet yoksa uyarıyı tekrar göster
            }
        });

        // İsteğe bağlı "Çıkış" butonu
        builder.setNegativeButton("Çıkış", (dialog, which) -> finish());

        // Dialogu göster
        builder.show();
    }
}