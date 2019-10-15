package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AsyncRun;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * 主线程和子线程切换
 */
public class AsyncRunActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("AsyncRun")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_async_run);
    }

    private EditText et_login_phone;
    private EditText et_login_pwd;
    private Button btn_login;

    @Override
    public void initView() {
        super.initView();
        et_login_phone = (EditText) findViewById(R.id.et_login_phone);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if (AtyUtils.isTextEmpty(et_login_phone)) {
            AtyUtils.showShort(mActivity, et_login_phone.getHint(), false);
            return;
        }
        String phone = AtyUtils.getText(et_login_phone);
        if (!AtyUtils.isMobile(phone)) {
            AtyUtils.showShort(mActivity, "请输入正确的手机号", false);
            return;
        }
        if (AtyUtils.isTextEmpty(et_login_pwd)) {
            AtyUtils.showShort(mActivity, et_login_pwd.getHint(), false);
            return;
        }
        showLoading("正在登录...");
        AsyncRun.runInBack(new Runnable() {//切换子线程
            @Override
            public void run() {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AsyncRun.runInMain(new Runnable() {//切换主线程
                    @Override
                    public void run() {
                        dismissLoading();
                        AtyUtils.showShort(mActivity, "登录成功", false);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void initData() {

    }
}
