package com.gloomy.fastfood.mvp.views.login;

import com.gloomy.fastfood.mvp.IBaseView;

/**
 * Copyright © 2017 AsianTech inc.
 * Created by HungTQB on 18/04/2017.
 */
public interface ILoginFragmentView extends IBaseView {
    void onLoginFailure(String message);

    void onRequestFailure();

    void onLoginSuccessful();
}
