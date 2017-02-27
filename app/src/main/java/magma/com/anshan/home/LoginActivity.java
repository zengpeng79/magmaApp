package magma.com.anshan.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import javax.security.auth.PrivateCredentialPermission;
import javax.security.auth.callback.Callback;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import magma.com.anshan.R;
import magma.com.anshan.dto.CustomerDTO;
import magma.com.anshan.service.CustomerService;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback {


    public static final String TAG = "magma";
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    public static final String USER_ISLOGIN = "USER_ISLOGIN";
    public static final String USER_ID="USER_ID";
    public static final String USER_TOKEN="USER_TOKEN";
    public static final String USER_MOBILE="USER_MOBILE";
    public static final String USER_LASTLOGINTIME="USER_LASTLOGINTIME";
    public static final String USER_TOKENVALIDTIME="USER_TOKENVALIDTIME";
    public static final String USER_NAME="USER_NAME";
    private static String APPKEY = "1b37491f0228c";
    private static String APPSECRET = "8ba15d1fc7672ee1d164087a0b9b9208";
    private static final String Result_OK = "1";
    private static final String Result_False = "2";
    private static final String Result_NoNet = "3";
    private static final String Result_noCallback = "4";
    private EditText prompt_phone;
    private Button getSMSCode;
    private EditText SMSCode;
    private View mProgressView;
    private View mLoginFormView;
    private Button loginBT;
    private boolean result = true;
    private int resultCode = 0;
    private int time = 60;
    private int count = 0;
    private LoginTask mLoginTask = null;
    private CustomerDTO customerDTO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        getSaveData();
        initView();
        initSDK();
    }

    private void initView() {
        prompt_phone = (EditText)findViewById(R.id.prompt_phone);
        getSMSCode = (Button) findViewById(R.id.getSMSCode);
        getSMSCode.setOnClickListener(this);
        SMSCode = (EditText)findViewById(R.id.SMSCode);
        loginBT = (Button)findViewById(R.id.loginBT);
        loginBT.setOnClickListener(this);
        mProgressView = (ProgressBar)findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
    }

    private void initSDK() {

        SMSSDK.initSDK(this,APPKEY,APPSECRET);
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    switch(event){
                        case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                            Log.d(TAG, "afterEvent: verifySuccess");
                            toast("验证成功");
                            attemptLogin(true);
                            break;
                        case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                toast("获取验证码成功");

                                //默认的智能验证是开启的,我已经在后台关闭
                            } else {
                                toast("获取验证码失败");
                            }
                            break;
                        case SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES:
                            break;
                    }

                }else{
                    ((Throwable)data).printStackTrace();
                    String Throwable = String.valueOf((Throwable)data);
                    if(Throwable.contains("\"")){
                        String[] detail = Throwable.split("\"");
                        toast(detail[detail.length - 2]);
                    }
                    else
                    {
                        toast("获取验证码失败");
                    }
                    attemptLogin(false);
                }

            }

        };
        SMSSDK.registerEventHandler(eh); //注册短信回调


    }

    private void attemptLogin(Boolean feedback) {
        if (mLoginTask != null) {
            return;
        }
        customerDTO = new CustomerDTO();
        customerDTO.setMobile(prompt_phone.getText().toString());
        mLoginTask = new LoginTask(customerDTO,feedback );
        mLoginTask.execute((Void) null);
    }


    private void toast(final String str) {

        runOnUiThread(new Runnable() {

            @Override

            public void run() {

                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();

            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getSMSCode:
                getSMS();
                Log.d(TAG, "onClick: 发送短信");
                break;
            case R.id.loginBT:
                verifySMS();
//                attemptLogin(true);
                showProgress(true);
                Log.d(TAG, "onClick: 登录按钮");
                break;
        }
    }



    private void getSMS(){
        String userPhone= prompt_phone.getText().toString();
        Boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(userPhone)) {
            toast(getString(R.string.error_phone_required));
            cancel = true;
        } else if (!isPhoneValid(userPhone)) {
            toast(getString(R.string.error_invalid_phone));
            cancel = true;
        }
        if(cancel){
            focusView = prompt_phone;
            focusView.requestFocus();
        }
        else{
            try {
                SMSSDK.getVerificationCode("86", userPhone);
                showTime();
            }
            catch (Exception e){
                toast(e.getMessage());
            }

            Log.d(TAG, "attemptSendSMS: to" + userPhone);
        }


    }
    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        Boolean isValid = false;
        if (phone.startsWith("1")&& (phone.length()==11)){
            isValid = true;
        }
        return isValid;
    }
    private void verifySMS() {
        String SMSVerifyCode = SMSCode.getText().toString();
        String userPhone= prompt_phone.getText().toString();
        SMSSDK.submitVerificationCode("86", userPhone, SMSVerifyCode);
    }

//     显示时间在梯减的文本框
    public void showTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (result) {

                    time--;
                    try {
                        Thread.sleep(1000);
                        //                                     tvShowTime.setText(time + "s后从新获取");
                        getSMSCode.post(new Runnable() {
                            @Override
                            public void run() {
                                getSMSCode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this,R.color.bg_Gray));
                                getSMSCode.setText(time + "s后重新获取");
                                getSMSCode.setEnabled(false);
                            }
                        });
                        if (time <= 1) {
                            count=0;
                            result = false;
                            getSMSCode.post(new Runnable() {
                                @Override
                                public void run() {
                                    getSMSCode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this,R.color.green));
                                    getSMSCode.setText("获取验证码");
                                    getSMSCode.setEnabled(true);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                result = true;
                time = 60;
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();    }

    public class LoginTask extends AsyncTask<Void, Void, String> {

        private final String mPhone;
        private final String mPassword;
        private final String mUserName;
        private Boolean mFeedback = false;

        LoginTask(CustomerDTO customer, Boolean feedback) {
            mPhone = customer.getMobile();
            mPassword = customer.getPassword();
            mUserName = customer.getUserName();
            this.mFeedback = feedback;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if(mFeedback){
                CustomerDTO newCustomer = new CustomerDTO() ;
                newCustomer.setMobile(mPhone);
                CustomerService loginCustomer = new CustomerService();
                CustomerDTO callbackCust = loginCustomer.loginCust(newCustomer);
                if(callbackCust == null){
                    return Result_NoNet;
                }
                else {
                    saveUserInfo(callbackCust);
                    Log.d(TAG, "doInBackground: Result_OK");
                    return Result_OK;
                }

                //                CustomerDTO getCustomerFromIS = loginCustomer.loginCustomer(customerDTO);
//                Log.d(TAG, "doInBackground: returnCustomer" + getCustomerFromIS.getToken());
//                Log.d(TAG, "returnCustomer: mobile"+getCustomerFromIS.getMobile());
            }
            Log.d(TAG, "doInBackground: Result_noCallback");
            return Result_noCallback;
        }


        @Override
        protected void onPostExecute(String result) {
            mLoginTask = null;
            showProgress(false);
            switch(result){
                case Result_OK:
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case Result_False:
                    SMSCode.setError(getString(R.string.error_incorrect_password));
                    SMSCode.requestFocus();
                    break;
                case Result_NoNet:
                    toast("网络不给力");
                    break;
                case Result_noCallback:
                    Log.d(TAG, "onPostExecute: sdhfsdhfjshfkjshfkjsdf");
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
            showProgress(false);
        }
    }

    private void saveUserInfo(CustomerDTO returnCust) {

        SharedPreferences sharedPreferences= getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putBoolean(USER_ISLOGIN,true);
        editor.putString(USER_ID, returnCust.getUid());
        editor.putString(USER_NAME, returnCust.getUserName());
        editor.putString(USER_TOKEN, returnCust.getToken());
        editor.putString(USER_MOBILE,returnCust.getMobile());
        editor.putString(USER_LASTLOGINTIME,returnCust.getLastLoginTime());
        editor.putString(USER_TOKENVALIDTIME,returnCust.getTokenValidTime());
        //提交当前数据
        editor.apply();
        Log.d(TAG, "saveUserInfo: OK");

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    int shortAnimTime=getResources().getInteger(android.R.integer.config_shortAnimTime);

                    loginBT.setVisibility(show ? View.GONE : View.VISIBLE);
                    loginBT.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginBT.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mProgressView.animate().setDuration(shortAnimTime).alpha(
                            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
                } else {
                    // The ViewPropertyAnimator APIs are not available, so simply show
                    // and hide the relevant UI components.
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    loginBT.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            }

}
