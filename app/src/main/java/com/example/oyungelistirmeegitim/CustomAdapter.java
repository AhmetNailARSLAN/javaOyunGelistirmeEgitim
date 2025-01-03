package com.example.oyungelistirmeegitim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter  extends BaseAdapter {

    Context context;
    ArrayList<EgitimClass> egitimler;

    public CustomAdapter(Context context, ArrayList<EgitimClass> egitimler) {
        this.context = context;
        this.egitimler = egitimler;
    }

    @Override
    public int getCount() {
        return egitimler.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View yenigorunum = inflater.inflate(R.layout.video_layout, null);

        TextView egitimadtext = yenigorunum.findViewById(R.id.egitimadtext);
        TextView egitimviewtext = yenigorunum.findViewById(R.id.egitimviewtext);
        ImageView egitimImage = yenigorunum.findViewById(R.id.egitimImage);

        EgitimClass egt = egitimler.get(i);

        egitimadtext.setText(egt.getDersAd());
        egitimviewtext.setText("Görüntülenme: " + String.valueOf(egt.getDersView()));

        // Picasso ile resmi yükleme
        Picasso.get()
                .load(egt.getDersImageUrl()) // Veritabanından gelen URL
                .placeholder(R.drawable.pngegg) // Yüklenirken gösterilecek varsayılan görsel
                .error(R.drawable.ic_launcher_background) // Yükleme başarısız olursa gösterilecek görsel
                .into(egitimImage); // Hedef ImageView

        return yenigorunum;
    }
}
