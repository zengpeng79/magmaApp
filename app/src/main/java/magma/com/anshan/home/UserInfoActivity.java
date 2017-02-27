package magma.com.anshan.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import magma.com.anshan.R;

import static magma.com.anshan.home.LoginActivity.USER_ID;
import static magma.com.anshan.home.LoginActivity.USER_ISLOGIN;
import static magma.com.anshan.home.LoginActivity.USER_LASTLOGINTIME;
import static magma.com.anshan.home.LoginActivity.USER_MOBILE;
import static magma.com.anshan.home.LoginActivity.USER_NAME;
import static magma.com.anshan.home.LoginActivity.USER_TOKEN;
import static magma.com.anshan.home.LoginActivity.USER_TOKENVALIDTIME;
import static magma.com.anshan.home.UserFragment.RESULT_LOGOUT;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "magma";
    private Button logoutBT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
//        getSaveData();
        initView();
//        initSDK();
    }

    private void initView() {
        Button logoutBT = (Button) findViewById(R.id.logoutBT);
        logoutBT.setOnClickListener(this);
    }
/*
    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        super.onBackPressed();
        overridePendingTransition(R.anim.cu_push_left_in, 0);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutBT:
                logout();
                Log.d(TAG, "onClick: logout");
                Intent intent = new Intent();
                setResult(RESULT_LOGOUT,intent);
                finish();
                break;
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences= getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putBoolean(USER_ISLOGIN,false);
        editor.putString(USER_ID, "0");
        editor.putString(USER_NAME, "0");
        editor.putString(USER_TOKEN, "0");
        editor.putString(USER_LASTLOGINTIME,"0");
        editor.putString(USER_TOKENVALIDTIME,"0");
        //提交当前数据
        editor.apply();
        Log.d(TAG, "logout: OK");

    }


}
