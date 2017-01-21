package com.nenton.androidmiddle.data.network;

import com.nenton.androidmiddle.data.network.res.ProductRes;
import com.nenton.androidmiddle.utils.Constants;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

public interface RestService {

    @GET("products")
    Observable<Response<List<ProductRes>>> getProductResObs (@Header(Constants.IF_MODIFIED_SINCE_HEADER)String lastEntityUpdate);
}
