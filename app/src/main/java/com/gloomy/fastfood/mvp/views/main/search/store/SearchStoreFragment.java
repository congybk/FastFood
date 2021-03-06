package com.gloomy.fastfood.mvp.views.main.search.store;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gloomy.fastfood.R;
import com.gloomy.fastfood.api.responses.SearchStoreResponse;
import com.gloomy.fastfood.mvp.BaseFragment;
import com.gloomy.fastfood.mvp.models.Store;
import com.gloomy.fastfood.mvp.presenters.main.search.store.SearchStorePresenter;
import com.gloomy.fastfood.mvp.views.detail.store.StoreDetailActivity_;
import com.gloomy.fastfood.utils.PermissionUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

/**
 * Copyright © 2017 Gloomy
 * Created by HungTQB on 30-Mar-17.
 */
@EFragment(R.layout.fragment_search_store)
public class SearchStoreFragment extends BaseFragment implements ISearchStoreView {

    @Bean
    SearchStorePresenter mPresenter;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewById(R.id.disableView)
    View mDisableView;

    private SearchStoreResponse mSearchStoreResponse;

    @AfterViews
    void afterViews() {
        mPresenter.setView(this);
        mPresenter.getDataSearchStore(mSearchStoreResponse);
        mPresenter.initSwipeRefresh(mSwipeRefreshLayout, mDisableView);
    }

    @Override
    public void onShowProgressDialog() {
        showProgressDialog();
    }

    @Override
    public void onDismissProgressDialog() {
        dismissProgressDialog();
    }

    @Override
    public void onNoInternetConnection() {
        mSwipeRefreshLayout.setRefreshing(false);
        showNoInternetConnectionMessage();
    }

    @Override
    public void onLoadDataComplete(SearchStoreResponse response) {
        mSearchStoreResponse = response;
        mPresenter.initRecyclerView(mRecyclerView);
    }

    @Override
    public void onLoadMoreComplete() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoadDataFailure() {
        showLoadDataFailure();
    }

    @Override
    public void onRefreshComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
        mDisableView.setVisibility(View.GONE);
        mPresenter.refreshData(mRecyclerView);
    }

    @Override
    public void notifyDataSetChanged() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemStoreClick(Store store) {
        StoreDetailActivity_.intent(getActivity()).mStoreParcel(Parcels.wrap(store)).start();
    }

    @Override
    public void requestPermission(String... permissions) {
        PermissionUtil.requestPermissions(this, SearchStorePresenter.PERMISSION_REQUEST_CODE, permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
