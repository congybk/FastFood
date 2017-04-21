package com.gloomy.fastfood.ui.views.gallery;

import android.support.v7.widget.RecyclerView;

import com.gloomy.fastfood.R;
import com.gloomy.fastfood.models.GalleryImage;
import com.gloomy.fastfood.ui.BaseActivity;
import com.gloomy.fastfood.ui.presenters.gallery.GalleryPresenter;
import com.gloomy.fastfood.widgets.HeaderBar;
import com.gloomy.fastfood.widgets.dialog.GalleryDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright © 2017 Gloomy
 * Created by HungTQB on 21-Apr-17.
 */
@EActivity(R.layout.activity_gallery)
public class GalleryActivity extends BaseActivity implements IGalleryView {

    @ViewById(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.headerBar)
    HeaderBar mHeaderBar;

    @Extra
    int mFoodId;

    @Extra
    String mFoodName;

    @Bean
    GalleryPresenter mPresenter;

    @AfterViews
    void afterViews() {
        mPresenter.setView(this);
        mPresenter.setFoodId(mFoodId);
        mPresenter.initRecyclerView(mRecyclerView);
        mPresenter.initHeaderBar(mHeaderBar, mFoodName);
        mPresenter.getGalleryData();
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
        showNoInternetConnection();
    }

    @Override
    public void onLoadMoreComplete() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemGalleryClick(GalleryImage galleryImage) {
        GalleryDialog dialog = new GalleryDialog();
        dialog.setImagePath(galleryImage.getImagePath());
        dialog.show(getFragmentManager(), GalleryDialog.class.getSimpleName());
    }

    @Override
    public void onLoadDataFailure() {
        showLoadDataFailure();
    }

    @Override
    public void onLoadDataComplete() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onBackButtonPressed() {
        finish();
    }
}
