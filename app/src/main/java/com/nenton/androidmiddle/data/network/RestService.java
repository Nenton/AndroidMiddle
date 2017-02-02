package com.nenton.androidmiddle.data.network;

import com.nenton.androidmiddle.data.network.req.ProductAddReq;
import com.nenton.androidmiddle.data.network.req.ProductEditReq;
import com.nenton.androidmiddle.data.network.res.AvatarUrlRes;
import com.nenton.androidmiddle.data.network.res.Comment;
import com.nenton.androidmiddle.data.network.res.ProductAddRes;
import com.nenton.androidmiddle.data.network.res.ProductEditRes;
import com.nenton.androidmiddle.data.network.res.ProductRes;
import com.nenton.androidmiddle.utils.ConstantsManager;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface RestService {

    @GET("products")
    Observable<Response<List<ProductRes>>> getProductResObs (@Header(ConstantsManager.IF_MODIFIED_SINCE_HEADER)String lastEntityUpdate);

    @POST("products")
    Observable<Response<ProductAddRes>> addProductResObs (@Body ProductAddReq product);

    @PUT("products/{productId}")
    Observable<Response<ProductEditRes>> editProductResObs (@Path("productId")String productId, @Body ProductEditReq product);

    @GET("products/{productId}/comments")
    Observable<List<Comment>> getCommentsResObs (@Path("productId")String productId);

    @POST("products/{productId}/comments")
    Observable<Comment> sendComment (@Path("productId")String productId, @Body Comment comment);

    @Multipart
    @POST("avatar")
    Observable<AvatarUrlRes> uploadUserAvatar(@Part MultipartBody.Part file);
}
