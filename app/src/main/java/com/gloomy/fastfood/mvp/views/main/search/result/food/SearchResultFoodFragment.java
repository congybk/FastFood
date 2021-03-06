package com.gloomy.fastfood.mvp.views.main.search.result.food;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gloomy.fastfood.R;
import com.gloomy.fastfood.api.responses.SearchResultResponse;
import com.gloomy.fastfood.mvp.BaseFragment;
import com.gloomy.fastfood.mvp.models.Food;
import com.gloomy.fastfood.mvp.presenters.main.search.result.food.SearchResultFoodPresenter;
import com.gloomy.fastfood.mvp.views.detail.food.FoodDetailActivity_;
import com.gloomy.fastfood.observer.SearchResultObserver;
import com.gloomy.fastfood.observer.listener.OnSearchResultObserverListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

/**
 * Copyright © 2017 Gloomy
 * Created by HungTQB on 30-Mar-17.
 */
@EFragment(R.layout.fragment_search_result_food)
public class SearchResultFoodFragment extends BaseFragment implements OnSearchResultObserverListener, ISearchResultFoodView {

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.tvEmptyMessage)
    TextView mTvEmptyMessage;

    @Bean
    SearchResultFoodPresenter mPresenter;

    @AfterViews
    void afterViews() {
        mPresenter.setView(this);
        mPresenter.initRecyclerView(mRecyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        SearchResultObserver.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        SearchResultObserver.unregister(this);
    }

    @Override
    public void onSearchResponse(SearchResultResponse response) {
        mPresenter.onReceiveSearchResponse(response);
    }

    @Override
    public void onUpdateData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mTvEmptyMessage.setVisibility(View.GONE);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onFoodClick(Food food) {
        FoodDetailActivity_.intent(getActivity()).mFoodParcelable(Parcels.wrap(food)).start();
    }

    @Override
    public void onEmptyData() {
        mRecyclerView.setVisibility(View.GONE);
        mTvEmptyMessage.setVisibility(View.VISIBLE);
        mTvEmptyMessage.setText(R.string.food_is_nothing);
    }
}
