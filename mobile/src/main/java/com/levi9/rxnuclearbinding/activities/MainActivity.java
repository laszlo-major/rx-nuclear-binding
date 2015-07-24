package com.levi9.rxnuclearbinding.activities;

import com.levi9.rxnuclearbinding.R;
import com.levi9.rxnuclearbinding.adapters.RepoAdapter;
import com.levi9.rxnuclearbinding.models.Repo;
import com.levi9.rxnuclearbinding.presenters.ActivityMainPresenter;
import com.levi9.rxnuclearbinding.views.ListingsView;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.Icicle;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ActivityMainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<ActivityMainPresenter> implements ListingsView<Repo>, SearchView.OnQueryTextListener {

    private RepoAdapter mRepoAdapter;

    private SearchView mSearchView;

    @Icicle
    String mQuery = "";

    @Bind(R.id.progressbar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        final int layoutId = this.onCreateViewId();
        this.setContentView(layoutId);

        final int toolbarId = this.onCreateViewToolbarId();
        this.setSupportActionBar((Toolbar) this.findViewById(toolbarId));

        ButterKnife.bind(this);

        this.mRepoAdapter = new RepoAdapter(this.findViewById(R.id.empty));

        final RecyclerView recycler = (RecyclerView) this.findViewById(R.id.repos);
        recycler.setAdapter(this.mRepoAdapter);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    protected int onCreateViewId() {
        return R.layout.activity_main;
    }

    protected int onCreateViewToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        this.mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        this.mSearchView.setOnQueryTextListener(this);
        this.mSearchView.onActionViewExpanded();
        this.mSearchView.setQuery(mQuery, false);
        this.mSearchView.clearFocus();
        return true;
    }

    @Override
    public void toast(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addItem(final Repo repo) {
        this.mRepoAdapter.addItem(repo);
    }

    @Override
    public void addItems(final Repo[] repos) {
        this.mRepoAdapter.addItems(repos);
    }

    @Override
    public void clearItems() {
        this.mRepoAdapter.clearItems();
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        getPresenter().getRepos(query);
        this.mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String query) {
        if (!"".equalsIgnoreCase(query)) {
            mQuery = query;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void showLoading(boolean choice) {
        if (choice) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @OnClick (R.id.fab)
    public void fabClicked(View view) {
        if (mQuery != null && !"".equalsIgnoreCase(mQuery)) {
            mSearchView.setQuery(mQuery, true);
        }
    }

}
