package com.levi9.rxnuclearbinding.presenters;

import com.levi9.rxnuclearbinding.activities.MainActivity;
import com.levi9.rxnuclearbinding.models.Repo;
import com.levi9.rxnuclearbinding.services.GitHub;

import android.os.Bundle;

import icepick.Icepick;
import icepick.Icicle;
import nucleus.presenter.RxPresenter;
import retrofit.RestAdapter;
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
        if (this.mService == null) {
            this.mService = ActivityMainPresenter.getRestAdapter().create(GitHub.class);
        }
        deliverLatestCache();
    }


    private static RestAdapter getRestAdapter() {
        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(GITHUB_API)
                .build();
    }

    public void getRepos(final String user) {
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
        getView().showLoading(false);
        mRepos = repos;
        getView().clearItems();
        getView().addItems(mRepos);
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
