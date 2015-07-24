package com.levi9.rxnuclearbinding.views;

public interface ListingsView<T> extends BaseView {
    void addItem(T item);

    void addItems(T[] items);

    void clearItems();
}
