package com.nenton.androidmiddle.data.network;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.nenton.androidmiddle.data.network.error.ErrorUtils;
import com.nenton.androidmiddle.data.network.error.NetworkAvailableError;
import com.nenton.androidmiddle.utils.ConstantsManager;
import com.nenton.androidmiddle.utils.NetworkStatusChecker;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by serge on 03.01.2017.
 */

public class RestCallTransformer<R> implements Observable.Transformer<Response<R>, R> {
    @Override
    @RxLogObservable
    public Observable<R> call(Observable<Response<R>> responseObservable) {
        return NetworkStatusChecker.isInternetAvailible()
                .flatMap(aBoolean -> aBoolean ? responseObservable : Observable.error(new NetworkAvailableError()))
                .flatMap(rResponse -> {
                    switch (rResponse.code()){
                        case 200:
                            String lastModified = rResponse.headers().get(ConstantsManager.LAST_MODIFIED_HEADER);
                            if (lastModified != null){
//                                DataManager.getInstance().getPreferencesManager().saveLastProductUpdate(lastModified);
                            }
                            return Observable.just(rResponse.body());
                        case 304:
                            return Observable.empty();
                            default:
                                return Observable.error(ErrorUtils.parseError(rResponse));
                    }
                });
    }
}
