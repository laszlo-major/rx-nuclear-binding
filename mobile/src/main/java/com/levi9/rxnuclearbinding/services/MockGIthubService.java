package com.levi9.rxnuclearbinding.services;

import android.content.Context;

import com.google.gson.Gson;
import com.levi9.rxnuclearbinding.models.Repo;

import java.nio.charset.Charset;

import okio.BufferedSource;
import okio.Okio;
import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;
import rx.Observable;

/**
 * Created by levi9 on 24/08/16.
 */

public class MockGithubService implements GitHub {

    private final BehaviorDelegate<GitHub> delegate;
    private Context context;

    public MockGithubService(BehaviorDelegate<GitHub> delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public Observable<Repo[]> getRepos(@Path("user") String user) {
        String filename = "major-laslo.json";
        Repo[] thatWeek = null;
        try {
            BufferedSource source = Okio.buffer(Okio.source(context.getAssets().open(filename)));
            String languageJson = source.readString(Charset.forName("UTF-8"));
            thatWeek = new Gson().fromJson(languageJson, Repo[].class);
        } catch (Exception ex) {


        }


        return delegate.returningResponse(thatWeek).getRepos(user);
    }


}
