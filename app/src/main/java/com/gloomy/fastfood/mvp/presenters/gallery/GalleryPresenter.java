package com.gloomy.fastfood.mvp.presenters.gallery;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.gloomy.fastfood.R;
import com.gloomy.fastfood.api.ApiRequest;
import com.gloomy.fastfood.api.responses.ImageResponse;
import com.gloomy.fastfood.mvp.models.GalleryImage;
import com.gloomy.fastfood.mvp.models.Image;
import com.gloomy.fastfood.mvp.presenters.BasePresenter;
import com.gloomy.fastfood.mvp.presenters.EndlessScrollListener;
import com.gloomy.fastfood.mvp.views.gallery.GalleryRecyclerAdapter;
import com.gloomy.fastfood.mvp.views.gallery.IGalleryView;
import com.gloomy.fastfood.utils.ScreenUtil;
import com.gloomy.fastfood.widgets.HeaderBar;
import com.gloomy.fastfood.widgets.SpacesItemDecoration;

import org.androidannotations.annotations.EBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright © 2017 Gloomy
 * Created by HungTQB on 21-Apr-17.
 */
@EBean
public class GalleryPresenter extends BasePresenter implements GalleryRecyclerAdapter.OnGalleryDialogListener {
    /**
     * GalleryType
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GalleryType.FOOD_TYPE, GalleryType.TOPIC_TYPE})
    public @interface GalleryType {
        int FOOD_TYPE = 1;
        int TOPIC_TYPE = 2;
        int STORE_TYPE = 3;
    }

    private static final int LOAD_MORE_THRESHOLD = 15;
    private static final int NUM_COLUMN = 2;

    private List<GalleryImage> mGalleryImages = new ArrayList<>();
    private int mCurrentPage;
    private boolean mIsLastPage;
    private Callback<ImageResponse> mCallbackLoadData = new Callback<ImageResponse>() {
        @Override
        public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
            mView.onDismissProgressDialog();
            if (response == null || response.body() == null) {
                return;
            }
            ImageResponse imageResponse = response.body();
            parseDataToImage(imageResponse);
            if (!mGalleryImages.isEmpty()) {
                mCurrentPage = imageResponse.getCurrentPage();
                mIsLastPage = imageResponse.isLast();
                mView.onLoadDataComplete();
            } else {
                mView.onEmptyData();
            }
        }

        @Override
        public void onFailure(Call<ImageResponse> call, Throwable t) {
            Log.d("TAG", "onFailure: ");
            mView.onDismissProgressDialog();
            mView.onLoadDataFailure();
        }
    };

    private Callback<ImageResponse> mCallbackLoadMore = new Callback<ImageResponse>() {
        @Override
        public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
            if (response == null || response.body() == null) {
                return;
            }
            ImageResponse imageResponse = response.body();
            parseDataToImage(imageResponse);
            mCurrentPage = imageResponse.getCurrentPage();
            mIsLastPage = imageResponse.isLast();
            mView.onLoadMoreComplete();
        }

        @Override
        public void onFailure(Call<ImageResponse> call, Throwable t) {
            // No-op
        }
    };

    @Setter
    @Accessors(prefix = "m")
    private IGalleryView mView;

    @Setter
    @Accessors(prefix = "m")
    private int mGalleryId;

    @Setter
    @Accessors(prefix = "m")
    private int mGalleryType;

    public void initRecyclerView(RecyclerView mRecyclerView) {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(NUM_COLUMN, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) ScreenUtil.convertDpiToPixel(getContext(), 2)));
        mRecyclerView.setAdapter(new GalleryRecyclerAdapter(getContext(), mGalleryImages, this));
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(LOAD_MORE_THRESHOLD) {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    public void getGalleryData() {
        mView.onShowProgressDialog();
        switch (mGalleryType) {
            case GalleryType.FOOD_TYPE:
                getGalleryFoodData();
                break;
            case GalleryType.TOPIC_TYPE:
                getGalleryTopicData();
                break;
            case GalleryType.STORE_TYPE:
                getGalleryStoreData();
                break;
        }
    }

    private void loadMoreData() {
        if (mIsLastPage) {
            return;
        }
        mCurrentPage++;
        switch (mGalleryType) {
            case GalleryType.FOOD_TYPE:
                loadMoreFoodData();
                break;
            case GalleryType.TOPIC_TYPE:
                loadMoreTopicData();
                break;
            case GalleryType.STORE_TYPE:
                loadMoreStoreData();
                break;
        }
    }

    private void parseDataToImage(ImageResponse imageResponse) {
        for (Image image : imageResponse.getImages()) {
            mGalleryImages.add(GalleryImage.builder().imagePath(image.getImagePath()).build());
        }
    }

    @Override
    public void onItemGalleryClick(int position) {
        mView.onItemGalleryClick(mGalleryImages.get(position));
    }

    public void initHeaderBar(HeaderBar headerBar, String foodName) {
        headerBar.setTitle(getContext().getString(R.string.title_image_gallery, foodName));
        headerBar.setOnHeaderBarListener(new HeaderBar.OnHeaderBarListener() {
            @Override
            public void onLeftButtonClick() {
                mView.onBackButtonPressed();
            }

            @Override
            public void onRightButtonClick() {
                // No-op
            }
        });
    }

    private void getGalleryFoodData() {
        ApiRequest.getInstance().getFoodImages(null, null, mGalleryId, mCallbackLoadData);
    }

    private void loadMoreFoodData() {
        ApiRequest.getInstance().getFoodImages(mCurrentPage, null, mGalleryId, mCallbackLoadMore);
    }

    private void getGalleryTopicData() {
        ApiRequest.getInstance().getTopicImages(null, null, mGalleryId, mCallbackLoadData);
    }

    private void loadMoreTopicData() {
        ApiRequest.getInstance().getTopicImages(mCurrentPage, null, mGalleryId, mCallbackLoadMore);
    }

    private void getGalleryStoreData() {
        ApiRequest.getInstance().getStoreImages(null, null, mGalleryId, mCallbackLoadData);
    }

    private void loadMoreStoreData() {
        ApiRequest.getInstance().getStoreImages(mCurrentPage, null, mGalleryId, mCallbackLoadMore);
    }
}
