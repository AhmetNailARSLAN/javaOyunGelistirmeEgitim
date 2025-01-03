package com.example.oyungelistirmeegitim;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EgitimDetay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_egitim_detay);
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


        EgitimClass egitimClass = getIntent().getParcelableExtra("egitimdetayclass");

        // Xml bağla
        TextView basliktext = findViewById(R.id.icerik_dersad);
        TextView aciklamatext = findViewById(R.id.icerik_dersaciklama);
        Button youtubeButton = findViewById(R.id.videoyagit);
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // JavaScript'i etkinleştir

        // Veriyi göster
        basliktext.setText(egitimClass.getDersAd());
        aciklamatext.setText(egitimClass.getDersAciklama());
        webView.setWebViewClient(new WebViewClient());
        String videoUrl = "https://www.youtube.com/embed/" + egitimClass.getDersVideoUrl();
        webView.loadUrl(videoUrl);


        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = "https://www.youtube.com/watch?v=" + egitimClass.getDersVideoUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(youtubeUrl));
                intent.setPackage("com.google.android.youtube"); // YouTube uygulamasını tercih et
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // YouTube uygulaması yoksa tarayıcıda aç
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });

    }
}