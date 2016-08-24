package com.levi9.rxnuclearbinding.services;

import com.levi9.rxnuclearbinding.models.Repo;
import com.levi9.rxnuclearbinding.models.User;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GitHub {

    @GET("/users/{user}/repos")
    Observable<Repo[]> getRepos(@Path("user") final String user);
}
