package com.gloomy.fastfood.mvp.views.login;

import android.content.Intent;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.gloomy.fastfood.R;
import com.gloomy.fastfood.mvp.BaseFragment;
import com.gloomy.fastfood.mvp.presenters.login.LoginFragmentPresenter;
import com.gloomy.fastfood.mvp.views.login.register.RegisterFragment_;
import com.gloomy.fastfood.mvp.views.main.MainActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright © 2017 AsianTech inc.
 * Created by HungTQB on 18/04/2017.
 */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment implements ILoginFragmentView {

    @ViewById(R.id.edtUsername)
    EditText mEdtUsername;

    @ViewById(R.id.edtPassword)
    EditText mEdtPassword;

    @ViewById(R.id.btnFacebookLogin)
    LoginButton mBtnFacebookLogin;

    @Bean
    LoginFragmentPresenter mPresenter;

    private CallbackManager mFacebookCallbackManager;

    @AfterViews
    void afterViews() {
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mPresenter.setView(this);
        mBtnFacebookLogin.setFragment(this);
        mPresenter.initButtonLoginFacebook(mBtnFacebookLogin, mFacebookCallbackManager);
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
        showNoInternetConnectionMessage();
    }

    @Click(R.id.btnFacebookLoginInterface)
    void onFacebookLoginInterfaceClick() {
        mBtnFacebookLogin.performClick();
    }

    @Click(R.id.btnSignIn)
    void onSignInClick() {
        mPresenter.signIn(mEdtUsername.getText().toString(), mEdtPassword.getText().toString());
    }

    @Override
    public void onLoginFailure(String message) {
        showMessageDialog(message, getString(R.string.button_close));
    }

    @Override
    public void onRequestFailure() {
        showLoadDataFailure();
    }

    @Override
    public void onLoginSuccessful() {
        MainActivity_.intent(getActivity())
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.tvCreateNewAccount)
    void onCreateNewAccountClick() {
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).replaceFragment(RegisterFragment_.builder().build(), true);
        }
    }
}
