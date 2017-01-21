package com.nenton.androidmiddle.mvp.models;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.androidmiddle.data.network.error.ApiError;
import com.nenton.androidmiddle.data.network.error.NetworkAvailableError;
import com.nenton.androidmiddle.data.network.res.ProductRes;
import com.nenton.androidmiddle.data.storage.dto.ProductDto;
import com.nenton.androidmiddle.data.storage.dto.ProductLocalInfo;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;

import java.util.List;

import rx.Observable;

public class CatalogModel extends AbstractModel{

    public CatalogModel() {
    }

//    public List<ProductDto> getProductList(){
//        return mDataManager.getListProduct();
//    }

    public boolean isUserAuth(){
        return false;//mDataManager.isUserAuth();
    }

//    public ProductDto getProductById(int productId){
//        return mDataManager.getProductById(productId);
//    }
//
//    public void updateProduct(ProductDto product){
//        mDataManager.updateProduct(product);
//    }

    public Observable<ProductRealm> getProductObs() {
        Observable<ProductRealm> disk = fromDisk();
        Observable<ProductRealm> network = fromNetwork();
//        Observable<ProductLocalInfo> local = network.flatMap(productRes -> mDataManager.getProductLocalInfoObs(productRes));
//
//        Observable<ProductDto> productFromNetwork = Observable.zip(network, local, ProductDto::new);
        return Observable.mergeDelayError(disk, network)
                .distinct(ProductRealm::getId);
    }

    @RxLogObservable
    public Observable<ProductRealm> fromNetwork(){
        return mDataManager.getProductObsFromNetwork();
    }

    @RxLogObservable
    public Observable<ProductRealm> fromDisk(){
        return mDataManager.getProductFromRealm();
    }

//    public void updateProductLocalInfo(ProductLocalInfo localInfo) {
//        mDataManager.getPreferencesManager(); // mMockDB.updateOrInsertLocalInfo(localInfo);
//    }
}
