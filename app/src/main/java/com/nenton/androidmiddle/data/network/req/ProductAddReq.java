package com.nenton.androidmiddle.data.network.req;

import com.nenton.androidmiddle.data.storage.realm.ProductRealm;

import java.util.List;

/**
 * Created by serge on 01.02.2017.
 */

public class ProductAddReq {

    private int remoteId;
    private String productName;
    private String imageUrl;
    private String description;
    private int price;
    private float raiting;
    private List<?> comments = null;

    public ProductAddReq(int remoteId, ProductRealm productRealm) {
        this.remoteId = remoteId;
        this.productName = productRealm.getProductName();
        this.imageUrl = productRealm.getImageUrl();
        this.description = productRealm.getDescription();
        this.price = productRealm.getPrice();
        this.raiting = productRealm.getRating();
    }
}

//{
//        "remoteId": 1,
//        "productName": "test 1",
//        "imageUrl": "http://skill-branch.ru/img/app/product-01.jpg",
//        "description": "description 1 description 1 description 1 description 1 description 1",
//        "price": 100,
//        "raiting": 4.2,
//        "comments": []
//        }
