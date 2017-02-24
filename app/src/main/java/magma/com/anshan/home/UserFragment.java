package magma.com.anshan.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;

import magma.com.anshan.R;
import magma.com.anshan.util.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static magma.com.anshan.home.LoginActivity.TAG;
import static magma.com.anshan.home.LoginActivity.USER_ISLOGIN;
import static magma.com.anshan.home.LoginActivity.USER_MOBILE;
import static magma.com.anshan.home.LoginActivity.USER_NAME;
import static magma.com.anshan.home.LoginActivity.USER_TOKENVALIDTIME;

/**
 * Created by Administrator on 2017/2/6 0006.
 */

public class UserFragment extends Fragment {

    private CircleImageView userCircleImage;
    private View userLinearLayout ;
    private int requestCode = 0;
    private TextView tx_userNmae;
    private TextView tx_mobile;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        userCircleImage = (CircleImageView)view.findViewById(R.id.user_circleImage);
        userLinearLayout = (View)view.findViewById(R.id.user_LinearLayout);
        tx_userNmae = (TextView)view.findViewById(R.id.tx_userNmae);
        tx_mobile = (TextView)view.findViewById(R.id.tx_mobile);
        if(isLoginValid()!= true) {
            tx_userNmae.setText(R.string.loginnow);
            tx_mobile.setText(R.string.login_tip);
            Log.d(TAG, "onActivityResult: RESULT_CANCELED");
        }
        else{
            SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userInfo",
                    Activity.MODE_PRIVATE);
            String userName = sharedPreferences.getString(USER_NAME,"");
            String mobile = sharedPreferences.getString(USER_MOBILE,"");
            tx_userNmae.setText(userName);
            tx_mobile.setText(mobile);
        }

        userLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isLoginValid()!= true) {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, requestCode);
                    getActivity().overridePendingTransition(R.anim.cu_push_right_in, 0);
                }
                else{
                    Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                    startActivityForResult(intent, requestCode);
                    getActivity().overridePendingTransition(R.anim.cu_push_right_in, 0);
                }
            }

        });
    }
    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Log.d(TAG, "onActivityResult: RESULT_OK");
                SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userInfo",
                        Activity.MODE_PRIVATE);
                String userName = sharedPreferences.getString(USER_NAME,"");
                String mobile = sharedPreferences.getString(USER_MOBILE,"");
                tx_userNmae.setText(userName);
                tx_mobile.setText(mobile);
                break;
            case RESULT_CANCELED:
                tx_userNmae.setText(R.string.loginnow);
                tx_mobile.setText(R.string.login_tip);
                Log.d(TAG, "onActivityResult: RESULT_CANCELED");
                break;
            default:
                break;
        }
    }

    private Boolean isLoginValid(){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        Boolean isLogin = sharedPreferences.getBoolean(USER_ISLOGIN,false);
        Log.d(TAG, "isLoginValid: isLogin" + isLogin);
        String tokenValidTime = sharedPreferences.getString(USER_TOKENVALIDTIME,"");
        Log.d(TAG, "isLoginValid: tokenValidTime" + tokenValidTime);
        Timestamp loginTimestamp  = new Timestamp(System.currentTimeMillis());
        Long mLoginTimeStamp = loginTimestamp.getTime();
//        mLoginTimeStamp = mLoginTimeStamp * 10;
        Log.d(TAG, "isLoginValid: mLoginTimeStamp" + mLoginTimeStamp);
        Long mTokenValidTime =Long.valueOf(tokenValidTime);
        Log.d(TAG, "isLoginValid: mTokenValidTime" + mTokenValidTime);
        if(isLogin != true){
            Log.d(TAG, "isLoginValid: isLogin != \"true\"");
            return false;
        }
        else if(mLoginTimeStamp > mTokenValidTime ){
            Log.d(TAG, "isLoginValid: mLoginTimeStamp > mTokenValidTime");
            return false;
        }
        else {
            Log.d(TAG, "isLoginValid: return true");
            return true;
        }       
    }

    }
