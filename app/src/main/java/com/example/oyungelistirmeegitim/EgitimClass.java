package com.example.oyungelistirmeegitim;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class EgitimClass implements Parcelable {
    private String dersId;
    private String dersAd;
    private String dersAciklama;
    private String dersVideoUrl;
    private String dersImageUrl;
    private int dersView;

    // Boş yapıcı (Firestore için gereklidir)
    public EgitimClass() {}

    // Constructor
    public EgitimClass(String dersId, String dersAd, String dersAciklama, String dersVideoUrl, String dersImageUrl, int dersView) {
        this.dersId = dersId;
        this.dersAd = dersAd;
        this.dersAciklama = dersAciklama;
        this.dersVideoUrl = dersVideoUrl;
        this.dersImageUrl = dersImageUrl;
        this.dersView = dersView;
    }

    // Parcelable Methods
    protected EgitimClass(Parcel in) {
        dersId = in.readString();
        dersAd = in.readString();
        dersAciklama = in.readString();
        dersVideoUrl = in.readString();
        dersImageUrl = in.readString();
        dersView = in.readInt();
    }

    public static final Creator<EgitimClass> CREATOR = new Creator<EgitimClass>() {
        @Override
        public EgitimClass createFromParcel(Parcel in) {
            return new EgitimClass(in);
        }

        @Override
        public EgitimClass[] newArray(int size) {
            return new EgitimClass[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dersId);
        dest.writeString(dersAd);
        dest.writeString(dersAciklama);
        dest.writeString(dersVideoUrl);
        dest.writeString(dersImageUrl);
        dest.writeInt(dersView);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter ve Setter metotları
    public String getDersId() {
        return dersId;
    }

    public void setDersId(String dersId) {
        this.dersId = dersId;
    }

    public String getDersAd() {
        return dersAd;
    }

    public void setDersAd(String dersAd) {
        this.dersAd = dersAd;
    }

    public String getDersAciklama() {
        return dersAciklama;
    }

    public void setDersAciklama(String dersAciklama) {
        this.dersAciklama = dersAciklama;
    }

    public String getDersVideoUrl() {
        return dersVideoUrl;
    }

    public void setDersVideoUrl(String dersVideoUrl) {
        this.dersVideoUrl = dersVideoUrl;
    }

    public String getDersImageUrl() {
        return dersImageUrl;
    }

    public void setDersImageUrl(String dersImageUrl) {
        this.dersImageUrl = dersImageUrl;
    }

    public int getDersView() {
        return dersView;
    }

    public void setDersView(int dersView) {
        this.dersView = dersView;
    }

}