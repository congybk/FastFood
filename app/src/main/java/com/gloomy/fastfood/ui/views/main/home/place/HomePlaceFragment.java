package com.gloomy.fastfood.ui.views.main.home.place;

import android.support.v7.widget.RecyclerView;

import com.gloomy.fastfood.R;
import com.gloomy.fastfood.models.City;
import com.gloomy.fastfood.models.Province;
import com.gloomy.fastfood.ui.BaseFragment;
import com.gloomy.fastfood.ui.presenters.main.home.place.HomePlacePresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright © 2017 Gloomy
 * Created by HungTQB on 29-Mar-17.
 */
@EFragment(R.layout.fragment_home_place)
public class HomePlaceFragment extends BaseFragment implements IHomePlaceView {

    @Bean
    HomePlacePresenter mPresenter;

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @AfterViews
    void afterView() {
        mPresenter.setView(this);
        mPresenter.getHomePlaceData();
    }

    @Override
    public void onShowProgressDialog() {

    }

    @Override
    public void onDismissProgressDialog() {

    }

    @Override
    public void onNoInternetConnection() {

    }

    @Override
    public void onLoadDataComplete() {
        mPresenter.initRecyclerView(mRecyclerView);
    }

    @Override
    public void onLoadMoreComplete() {

    }

    @Override
    public void onLoadDataFailure() {

    }

    @Override
    public void onItemHomeCityClick(City city) {

    }

    @Override
    public void onItemProvinceClick(Province province) {

    }
}
