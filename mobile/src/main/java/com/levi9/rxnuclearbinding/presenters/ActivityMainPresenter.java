package com.levi9.rxnuclearbinding.presenters;

import com.levi9.rxnuclearbinding.BuildConfig;
import com.levi9.rxnuclearbinding.activities.MainActivity;
import com.levi9.rxnuclearbinding.models.Repo;
import com.levi9.rxnuclearbinding.services.GitHub;
import com.levi9.rxnuclearbinding.services.MockGithubService;

import android.os.Bundle;

import icepick.Icepick;
import icepick.Icicle;
import nucleus.presenter.RxPresenter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityMainPresenter extends RxPresenter<MainActivity> implements Observer<Repo[]> {
    private static final String GITHUB_API = "https://api.github.com";

    GitHub mService;
    @Icicle Repo[] mRepos;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
        deliverLatestCache();
    }

    private void mockIt() {
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(getRestAdapter())
                .networkBehavior(NetworkBehavior.create()).build();
        BehaviorDelegate<GitHub> delegate = mockRetrofit.create(GitHub.class);
        this.mService = new MockGithubService(delegate, this.getView().getBaseContext());
    }

    private static Retrofit getRestAdapter() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging).build();
        return new Retrofit.Builder()
                .baseUrl(GITHUB_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public void getRepos(final String user) {
        if (this.mService == null) {
            if (BuildConfig.FLAVOR.equals("mock")) {
                mockIt();
            } else {
                this.mService = ActivityMainPresenter.getRestAdapter().create(GitHub.class);
            }
        }
        getView().showLoading(true);
        mService.getRepos(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(final Throwable exception) {
        getView().showLoading(false);
        getView().toast(exception.getMessage());
    }

    @Override
    public void onNext(final Repo[] repos) {
        if (repos != null) {
            getView().showLoading(false);
            mRepos = repos;
            getView().clearItems();
            getView().addItems(mRepos);
        }
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }

    @Override
    protected void onTakeView(MainActivity view) {
        super.onTakeView(view);
        if (mRepos != null) {
            onNext(mRepos);
        }
    }
}
