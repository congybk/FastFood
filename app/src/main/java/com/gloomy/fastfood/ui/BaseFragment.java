package com.gloomy.fastfood.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.gloomy.fastfood.listener.OnBaseActivityListener;
import com.gloomy.fastfood.widgets.dialog.CustomMessageDialog;

/**
 * Copyright © 2017 Gloomy
 * Created by HungTQB on 27-Mar-17.
 */
public abstract class BaseFragment extends Fragment {
    protected OnBaseActivityListener mOnBaseActivityListener;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnBaseActivityListener = (OnBaseActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnBaseActivityListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnBaseActivityListener = (OnBaseActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnBaseActivityListener");
        }
    }

    public void replaceFragment(Fragment fragment, boolean isAddToBackStack) {
        ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, isAddToBackStack);
    }

    public void hideKeyboard() {
        mOnBaseActivityListener.hideKeyboard();
    }

    public void showProgressDialog() {
        mOnBaseActivityListener.showProgressDialog();
    }

    public void dismissProgressDialog() {
        mOnBaseActivityListener.dismissProgressDialog();
    }

    public void showMessageDialog() {
        mOnBaseActivityListener.showMessageDialog();
    }

    public void showMessageDialog(CustomMessageDialog.OnCustomMessageDialogListener listener) {
        mOnBaseActivityListener.showMessageDialog(listener);
    }

    public void dismissMessageDialog() {
        mOnBaseActivityListener.dismissMessageDialog();
    }

    public void setMessageDialogText(String text) {
        mOnBaseActivityListener.setMessageDialogText(text);
    }

    public void setMessageDialogButtonText(String text) {
        mOnBaseActivityListener.setButtonMessageDialogText(text);
    }

    public void showNoInternetConnectionMessage() {
        mOnBaseActivityListener.showNoInternetConnection();
    }

    public void showLoadDataFailure() {
        mOnBaseActivityListener.showLoadDataFailure();
    }

    public void showLoginDialog() {
        mOnBaseActivityListener.showLoginDialog();
    }
}
