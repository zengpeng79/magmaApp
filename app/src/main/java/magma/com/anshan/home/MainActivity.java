package magma.com.anshan.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import magma.com.anshan.R;
import magma.com.anshan.data.Data;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /** 主界面 */
    private MainActivityFragment home_F;
    /** 用户信息界面 */
    private UserFragment user_F;
    // 界面底部的菜单按钮
    private ImageView[] bt_menu = new ImageView[3];
    // 界面底部的菜单按钮id
//	private int[] bt_menu_id = { R.id.iv_menu_0, R.id.iv_menu_1, R.id.iv_menu_2, R.id.iv_menu_3, R.id.iv_menu_4 };
    private int[] bt_menu_id = { R.id.iv_menu_0, R.id.iv_menu_1, R.id.iv_menu_2 };

    // 界面底部的选中菜单按钮资源
//	private int[] select_on = { R.drawable.guide_home_on, R.drawable.guide_tfaccount_on, R.drawable.guide_discover_on, R.drawable.guide_cart_on, R.drawable.guide_account_on };
    private int[] select_on = { R.mipmap.guide_home_on, R.mipmap.guide_cart_on, R.mipmap.guide_account_on };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSaveData();
        initView();
    }
    /** 得到保存的购物车数据 */
    private void getSaveData() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        SharedPreferences sp = getSharedPreferences("SAVE_CART", Context.MODE_PRIVATE);
        int size = sp.getInt("ArrayCart_size", 0);
        for (int i = 0; i < size; i++) {
            hashMap.put("type", sp.getString("ArrayCart_type_" + i, ""));
            hashMap.put("color", sp.getString("ArrayCart_color_" + i, ""));
            hashMap.put("num", sp.getString("ArrayCart_num_" + i, ""));
            Data.arrayList_cart.add(hashMap);
        }
    }

    // 初始化组件
    private void initView() {
        // 找到底部菜单的按钮并设置监听
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
            bt_menu[i].setOnClickListener(this);
        }

        // 初始化默认显示的界面
        if (home_F == null) {
            home_F = new MainActivityFragment();
            addFragment(home_F);
            showFragment(home_F);
        } else {
            showFragment(home_F);
        }
        // 设置默认首页为点击时的图片
//        bt_menu[0].setImageResource(select_on[0]);

    }

    /** 添加Fragment **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.show_layout, fragment);
        ft.commit();
    }

    /** 删除Fragment **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /** 显示Fragment **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        // 设置Fragment的切换动画
        ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (home_F != null) {
            ft.hide(home_F);
        }
        if (user_F != null) {
            ft.hide(user_F);
        }
/*        if (cart_F != null) {
            ft.hide(cart_F);
        }
*/

        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu_0:
                // 主界面
                if (home_F == null) {
                    home_F=new MainActivityFragment();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(home_F);
                    showFragment(home_F);
                } else {
                    if (home_F.isHidden()) {
                        showFragment(home_F);
                    }
                }

                break;

            case R.id.iv_menu_2:
                // 我的淘宝界面
                if (user_F == null) {
                    user_F = new UserFragment();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    if (!user_F.isHidden()) {
                        addFragment(user_F);
                        showFragment(user_F);
                    }
                } else {
                    if (user_F.isHidden()) {
                        showFragment(user_F);
                    }
                }

                break;

/*            case R.id.iv_menu_1:
                // 购物车界面
                if (cart_F != null) {
                    removeFragment(cart_F);
                    cart_F = null;
                }
                cart_F = new Cart_F();
                // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                addFragment(cart_F);
                showFragment(cart_F);

                break;
*/
        }

    }

}
