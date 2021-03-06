package com.nenton.androidmiddle.data.managers;


import android.content.SharedPreferences;
import android.util.Log;

import com.nenton.androidmiddle.data.network.RestCallTransformer;
import com.nenton.androidmiddle.data.network.RestService;
import com.nenton.androidmiddle.data.network.res.AvatarUrlRes;
import com.nenton.androidmiddle.data.network.res.Comment;
import com.nenton.androidmiddle.data.network.res.ProductRes;
import com.nenton.androidmiddle.data.storage.dto.UserAddressDto;
import com.nenton.androidmiddle.data.storage.realm.ProductRealm;
import com.nenton.androidmiddle.di.DaggerService;

import com.nenton.androidmiddle.di.components.DaggerDataManagerComponent;
import com.nenton.androidmiddle.di.components.DataManagerComponent;
import com.nenton.androidmiddle.di.modules.LocalModule;
import com.nenton.androidmiddle.di.modules.NetworkModule;
import com.nenton.androidmiddle.utils.App;
import com.nenton.androidmiddle.utils.AppConfig;
import com.nenton.androidmiddle.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.nenton.androidmiddle.data.managers.PreferencesManager.*;

public class DataManager {

    private static final String TAG = "DataManager";
    private static DataManager ourInstance = new DataManager();

    @Inject
    RestService mRestService;
    @Inject
    PreferencesManager mPreferencesManager;
    @Inject
    Retrofit mRetrofit;
    @Inject
    RealmManager mRealmManager;

//    private List<ProductDto> mProductDto;

    private boolean mUserAuth;


    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
        DataManagerComponent component = DaggerService.getComponent(DataManagerComponent.class);
        if (component == null) {
            component = DaggerDataManagerComponent.builder()
                    .appComponent(App.getAppComponent())
                    .localModule(new LocalModule())
                    .networkModule(new NetworkModule())
                    .build();
            DaggerService.registerComponent(DataManagerComponent.class, component);
        }
        component.inject(this);
//        mSharedPreferences = App.getSharedPreferences();
//        generateProducts();

        updateLocalDataWithTimer();// for example
    }

    private void updateLocalDataWithTimer() {
        Observable.interval(AppConfig.UPDATE_DATA_INTERVAL, TimeUnit.SECONDS)
                .flatMap(aLong -> NetworkStatusChecker.isInternetAvailible())
                .filter(aBoolean -> aBoolean)
                .flatMap(aBoolean -> getProductObsFromNetwork())
                .subscribe(productRealm -> {
                    Log.e(TAG, "LOCAL UPDATE complete: ");

                }, throwable -> {
                    Log.e(TAG, "LOCAL UPDATE error: " + throwable.getMessage());
                });

    }

    public Observable<ProductRealm> getProductObsFromNetwork() {
        return mRestService.getProductResObs(mPreferencesManager.getLastProductUpdate())
                .compose(new RestCallTransformer<List<ProductRes>>())
                // трансформируем response выбрасываем ApiError в случае ошибки, проверяем статус сети перед запросом, обрабатываем коды ответа
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(productRes -> {
                    if (!productRes.isActive()) {
                        mRealmManager.deleteFromRealm(ProductRealm.class, productRes.getId()); // удалить из базы запись о продукте если он больше не активен
                    }
                })
                .filter(ProductRes::isActive)
                .doOnNext(productRes -> mRealmManager.saveProductResponseToRealm(productRes)
                )
                .retryWhen(errorObservable ->
                errorObservable
                .zipWith(Observable.range(1, AppConfig.RETRY_REQUEST_COUNT), (throwable, retryCount) -> retryCount)
                .doOnNext(retryCount -> Log.e(TAG, "LOCAL UPDATE request retry count: " + retryCount + " " + new Date()))
                .map(retryCount -> ((long) (AppConfig.RETRY_REQUEST_BASE_DELAY * Math.pow(Math.E, retryCount))))
                .doOnNext(delay -> Log.e(TAG, "LOCAL UPDATE delay: " + delay))
                .flatMap(delay -> Observable.timer(delay, TimeUnit.MILLISECONDS)))
                .flatMap(productRes -> Observable.empty());
    }

//    public String getAuthToken() {
//        return mPreferencesManager.getSharedPreferences().getString(ConstantsManager.USER_AUTH_TOKEN, "");
//    }
//
//    public void setAuthToken(String token) {
//        SharedPreferences.Editor editor = mPreferencesManager.getSharedPreferences().edit();
//        editor.putString(ConstantsManager.USER_AUTH_TOKEN, token);
//        editor.apply();
//    }
//
//    public boolean equalsToken(String token) {
//        return token.equals(mPreferencesManager.getSharedPreferences().getString(ConstantsManager.USER_AUTH_TOKEN, ""));
//    }

//    public List<ProductDto> getListProduct() {
//        if (mProductDto == null){
//            generateProducts();
//        }
//        return mProductDto;
//    }


//    private void generateProducts() {
//        mProductDto = new ArrayList<>();
//        mProductDto.add(new ProductDto(1, "Name_1", "http://cdn-4.nikon-cdn.com/en_INC/o/PressRelease/J_4uHt00U-sY95iCnB8fzhvkokM/S2_11_27.5_YW_front.high.jpg", "Description_1", 100, 1));
//        mProductDto.add(new ProductDto(2, "Name_2", "http://veleks.ru/wp-content/uploads/2016/01/IMG_8295-1.png", "Description_1", 200, 1));
//        mProductDto.add(new ProductDto(3, "Name_3", "http://ocodeco.ru/d/687051/d/диван.jpg", "Description_1", 300, 1));
//        mProductDto.add(new ProductDto(4, "Name_4", "http://s019.radikal.ru/i601/1205/02/143ebfe4fbad.jpg", "Description_1", 400, 1));
//        mProductDto.add(new ProductDto(5, "Name_5", "http://www.ixbt.com/mobile/cat-b100/cat-b100-box.jpg", "Description_1", 500, 1));
//        mProductDto.add(new ProductDto(6, "Name_6", "http://img2.korablik.ru/upload/catalog/25912/25912_1000.jpg", "Description_1", 600, 1));
//    }

//    public boolean isUserAuth() {
//        // TODO: 30.10.2016 auth user
//        return mUserAuth;
//    }

    public Observable<Comment> sendComment(String productId, Comment comment) {
        return mRestService.sendComment(productId, comment);

    }

    public Map<String, String> getUserProfileInfo() {
        Map<String, String> profileInfo = new HashMap<>();
        profileInfo.put(PROFILE_FULL_NAME_KEY, getPreferencesManager().getSharedPreferences().getString(PROFILE_FULL_NAME_KEY, "No name"));
        profileInfo.put(PROFILE_PHONE_KEY, getPreferencesManager().getSharedPreferences().getString(PROFILE_PHONE_KEY, ""));
        return profileInfo;
    }

    public ArrayList<UserAddressDto> getUserAddresses() {
        ArrayList<UserAddressDto> mUserAddresses = new ArrayList<>();
        mUserAddresses.add(new UserAddressDto(1, "Дом", "Жукова", "58", "1234", 4, "После 17.00"));
        mUserAddresses.add(new UserAddressDto(2, "Работа", "Жукова", "58", "1234", 4, "После 17.00"));
        return mUserAddresses;
    }

    public Map<String, Boolean> getUserSettings() {
        Map<String, Boolean> userSettings = new HashMap<>();
        userSettings.put(NOTIFICATION_PROMO_KEY, getPreferencesManager().getSharedPreferences().getBoolean(NOTIFICATION_PROMO_KEY, false));
        userSettings.put(NOTIFICATION_ORDER_KEY, getPreferencesManager().getSharedPreferences().getBoolean(NOTIFICATION_ORDER_KEY, false));
        return userSettings;
    }

    public void saveProfileInfo(String name, String phone, String avatar) {
        SharedPreferences.Editor editor = getPreferencesManager().getSharedPreferences().edit();
        editor.putString(PROFILE_FULL_NAME_KEY, name);
        editor.putString(PROFILE_PHONE_KEY, phone);
        editor.putString(PROFILE_AVATAR_KEY, avatar);
        editor.apply();
    }

    public void saveSetting(String notificationKey, boolean isChecked) {
        SharedPreferences.Editor editor = getPreferencesManager().getSharedPreferences().edit();
        editor.putBoolean(notificationKey, isChecked);
        editor.apply();
    }
//
//    public void addAddress(UserAddressDto userAddressDto) {
//
//    }

    public void updateOrInsertAddress(UserAddressDto address) {
//        SharedPreferences.Editor editor = getPreferencesManager().getSharedPreferences().edit();
//        editor.putBoolean(NOTIFICATION_ORDER_KEY, add);
//        editor.apply();
        // TODO: 18.12.2016 implement me
    }

    public void removeAddress(UserAddressDto address) {
        // TODO: 18.12.2016 implement me
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }


    public Observable<ProductRealm> getProductFromRealm() {
        return mRealmManager.getAllProductFromRealm();
    }

    public Observable<AvatarUrlRes> uploadUserPhoto(MultipartBody.Part body) {
        return mRestService.uploadUserAvatar(body);
    }

    public boolean isUserAuth(String token) {
        // TODO: 06.02.2017 rest запрос к серверу о валидности токена
        return true;
    }
}
