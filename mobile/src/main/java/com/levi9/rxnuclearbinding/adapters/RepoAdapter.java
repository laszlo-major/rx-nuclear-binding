package com.levi9.rxnuclearbinding.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.levi9.rxnuclearbinding.R;
import com.levi9.rxnuclearbinding.databinding.ListItemBinding;
import com.levi9.rxnuclearbinding.models.Repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {
    private final View empty;

    private List<Repo> repos = new ArrayList<>();

    public RepoAdapter(final View empty) {
        this.empty = empty;
    }

    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false);

        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Repo repo = this.repos.get(position);
        holder.bind(repo);
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();
        this.empty.setVisibility(this.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @UiThread
    public void addItem(final Repo repo) {
        this.repos.add(repo);
        this.dataSetChanged();
    }

    @UiThread
    public void addItems(final Repo[] repos) {
        this.repos.addAll(Arrays.asList(repos));
        this.dataSetChanged();
    }

    @UiThread
    public void clearItems() {
        this.repos.clear();
        this.dataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public ViewHolder(final View view, final ListItemBinding binding) {
            super(view);
            this.binding = binding;
        }

        @UiThread
        public void bind(final Repo repo) {
            this.binding.setRepo(repo);
        }
    }
}