package com.nenton.androidmiddle.data.storage.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.nenton.androidmiddle.data.network.res.ProductRes;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;

public class ProductDto implements Parcelable{
   private int id;
   private String productName;
   private String urlProduct;
   private String description;
   private int price;
   private int count;
    private boolean favorite;


    public ProductDto(int id, String productName, String urlProduct, String description, int price, int count) {
        this.id = id;
        this.productName = productName;
        this.urlProduct = urlProduct;
        this.description = description;
        this.price = price;
        this.count = count;
    }

    public ProductDto(ProductRealm productRealm) {
        this.productName = productRealm.getProductName();
        this.urlProduct = productRealm.getImageUrl();
        this.description = productRealm.getDescription();
        this.price = productRealm.getPrice();
        this.count = productRealm.getCount();
        this.favorite = productRealm.isFavorite();
    }

    //region ========================= Parcelable =========================

    protected ProductDto(Parcel in) {
        id = in.readInt();
        productName = in.readString();
        urlProduct = in.readString();
        description = in.readString();
        price = in.readInt();
        count = in.readInt();
    }

    public static final Creator<ProductDto> CREATOR = new Creator<ProductDto>() {
        @Override
        public ProductDto createFromParcel(Parcel in) {
            return new ProductDto(in);
        }

        @Override
        public ProductDto[] newArray(int size) {
            return new ProductDto[size];
        }
    };

    public ProductDto(ProductRes productRes, ProductLocalInfo productLocalInfo) {
        this.id = productRes.getRemoteId();
        this.productName = productRes.getProductName();
        this.urlProduct = productRes.getImageUrl();
        this.description = productRes.getDescription();
        this.price = productRes.getPrice();
        this.count = productLocalInfo.getCount();
        this.favorite = productLocalInfo.isFavorite();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(productName);
        dest.writeString(urlProduct);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeInt(count);
    }

    //endregion

    //region ========================= Getters =========================

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void deleteCountProduct() {
        count--;
    }

    public void addCountProduct(){
        count++;
    }

    public boolean isFavorite() {
        return favorite;
    }

    //endregion
}
