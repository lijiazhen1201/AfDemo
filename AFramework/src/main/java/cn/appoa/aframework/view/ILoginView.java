package cn.appoa.aframework.view;

import android.content.Intent;

public interface ILoginView extends IBaseView {

    boolean isLogin();

    void toLoginActivity();

    void toLoginSuccess(Intent data);
}
