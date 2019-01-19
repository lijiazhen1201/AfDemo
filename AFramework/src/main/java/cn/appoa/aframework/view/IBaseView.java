package cn.appoa.aframework.view;

public interface IBaseView {

    void showLoading(CharSequence message);

    void dismissLoading();

    void showSoftKeyboard();

    void hideSoftKeyboard();

    String getRequestTag();

    void onErrorCodeResponse(String message);
}
