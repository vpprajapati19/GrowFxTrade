package com.growfxtrade.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.growfxtrade.fragment.FragmentHome;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.google.android.material.navigation.NavigationView;
import com.growfxtrade.R;

import java.util.Locale;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private AppCompatSpinner ImageView;
    private Locale myLocale;
    private ImageView iv_main_drawer, iv_main_filter;
    private TextView tv_user,tv_email, drawer_item_resetpwd,drawer_item_privacypolicy, drawer_item_aboutus,drawer_item_termsandcondition,drawer_item_refundpolicy;
    private LinearLayout drawer_item_profile,  drawer_item_chatwithus, drawer_item_chart, drawer_item_calaneder,   drawer_item_fxrobo,
            drawer_item_withdrawal, drawer_item_yourwishlist, drawer_orderhistory, drawer_item_portfolio, drawer_item_traction, drawer_item_logout;
    DrawerLayout drawer_layout;

    public static ImageView img_search = null;
    public static ImageView img_search_back = null;
    public static EditText edt_serch;
    public static TextView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        replaceFragment(new FragmentHome(), R.id.frame, FragmentHome.class.getName());

    }

    private void initComponent() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_main_drawer = (ImageView) findViewById(R.id.iv_main_drawer);
        iv_main_filter = (ImageView) findViewById(R.id.iv_main_filter);
        drawer_item_chatwithus = (LinearLayout) findViewById(R.id.drawer_item_chatwithus);
        drawer_item_chart = (LinearLayout) findViewById(R.id.drawer_item_chart);
        drawer_item_calaneder = (LinearLayout) findViewById(R.id.drawer_item_calaneder);
        drawer_item_resetpwd = (TextView) findViewById(R.id.drawer_item_resetpwd);
        drawer_item_privacypolicy = (TextView) findViewById(R.id.drawer_item_privacypolicy);
        tv_email = findViewById(R.id.tv_email);
        drawer_item_profile = findViewById(R.id.drawer_item_profile);
        tv_user = findViewById(R.id.tv_user);
        drawer_item_fxrobo = findViewById(R.id.drawer_item_fxrobo);
      //  drawer_item_fxrobo.setBackgroundColor(getResources().getColor(R.color.gray_333));
        drawer_item_yourwishlist = findViewById(R.id.drawer_item_yourwishlist);
        drawer_item_aboutus = findViewById(R.id.drawer_item_aboutus);
        drawer_item_withdrawal = findViewById(R.id.drawer_item_withdrawal);
        drawer_orderhistory = findViewById(R.id.drawer_orderhistory);
        drawer_item_termsandcondition = findViewById(R.id.drawer_item_termsandcondition);
        drawer_item_refundpolicy = findViewById(R.id.drawer_item_refundpolicy);
        drawer_item_portfolio = findViewById(R.id.drawer_item_portfolio);
        drawer_item_logout = findViewById(R.id.drawer_item_logout);
        drawer_item_traction = findViewById(R.id.drawer_item_traction);

        img_search = findViewById(R.id.img_search);
        edt_serch = findViewById(R.id.edt_serch);
        iv_icon = findViewById(R.id.iv_icon);
        img_search_back = findViewById(R.id.img_search_back);

        try {
            tv_email.setText(PrefrenceManager.getString(this, PrefrenceManager.EMAIL));
        } catch (Exception e) {
        }

        tv_user.setText(PrefrenceManager.getString(this, PrefrenceManager.USERNAME));


        iv_main_drawer.setOnClickListener(this);
        iv_main_filter.setOnClickListener(this);
        drawer_item_profile.setOnClickListener(this);
        drawer_item_yourwishlist.setOnClickListener(this);
        drawer_item_fxrobo.setOnClickListener(this);
        drawer_item_withdrawal.setOnClickListener(this);
        drawer_item_refundpolicy.setOnClickListener(this);
        drawer_item_chatwithus.setOnClickListener(this);
        drawer_item_chart.setOnClickListener(this);
        drawer_orderhistory.setOnClickListener(this);
        drawer_item_calaneder.setOnClickListener(this);
        drawer_item_privacypolicy.setOnClickListener(this);
        drawer_item_aboutus.setOnClickListener(this);
        drawer_item_resetpwd.setOnClickListener(this);
        drawer_item_termsandcondition.setOnClickListener(this);
        drawer_item_portfolio.setOnClickListener(this);
        drawer_item_logout.setOnClickListener(this);
        drawer_item_traction.setOnClickListener(this);


        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteCurrencyActivity.class);
                startActivity(intent);
               overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            Intent ii = new Intent(getApplicationContext(), ExitActivity.class);
//            startActivity(ii);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == iv_main_drawer) {
            openDrawer();
        }
        if (view == iv_main_filter) {
            Intent intent = new Intent(this, AddCurrencyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_yourwishlist) {
            if (drawer_item_yourwishlist.isSelected()) {
              //  drawer_item_yourwishlist.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_yourwishlist.setSelected(false);
            } else {
                drawer_item_yourwishlist.setSelected(true);
            }
            Intent intent = new Intent(this, InvestMoneyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        if (view == drawer_item_fxrobo) {
//            if (drawer_item_fxrobo.isSelected()) {
//                drawer_item_fxrobo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                drawer_item_fxrobo.setSelected(false);
//            } else {
//                drawer_item_fxrobo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                drawer_item_fxrobo.setSelected(true);
//            }
           /* Intent intent = new Intent(this, FxRoboTradeActivity.class);
            startActivity(intent);*/

            /*overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);*/


        }
        if (view == drawer_item_profile) {
            if (drawer_item_profile.isSelected()) {
              //  drawer_item_profile.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_profile.setSelected(false);
            } else {
                drawer_item_profile.setSelected(true);
            }
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_withdrawal) {
            if (drawer_item_withdrawal.isSelected()) {
             //   drawer_item_withdrawal.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_withdrawal.setSelected(false);
            } else {
                drawer_item_withdrawal.setSelected(true);
            }
            Intent intent = new Intent(this, WithdrawMoneyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_refundpolicy) {
            if (drawer_item_refundpolicy.isSelected()) {
               // drawer_item_refundpolicy.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_refundpolicy.setSelected(false);
            } else {
                drawer_item_refundpolicy.setSelected(true);
            }
            Intent intent = new Intent(this, RefundPolicyActivity.class);
            intent.putExtra("url", "http://orionfxrobo.com/refund_policy.php");
            intent.putExtra("title", "Refund Policy");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_termsandcondition) {
            if (drawer_item_termsandcondition.isSelected()) {
                //drawer_item_termsandcondition.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_termsandcondition.setSelected(false);
            } else {
                drawer_item_termsandcondition.setSelected(true);
            }
            Intent intent = new Intent(this, RefundPolicyActivity.class);
            intent.putExtra("url", "http://orionfxrobo.com/term_condition.php");
            intent.putExtra("title", "Terms & Policy");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        if (view == drawer_item_aboutus) {
            if (drawer_item_aboutus.isSelected()) {
               // drawer_item_aboutus.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_aboutus.setSelected(false);
            } else {
                drawer_item_aboutus.setSelected(true);
            }
            Intent intent = new Intent(this, RefundPolicyActivity.class);
            intent.putExtra("url", "http://orionfxrobo.com/about.php");
            intent.putExtra("title", "About Us");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_privacypolicy) {
            if (drawer_item_privacypolicy.isSelected()) {
             //   drawer_item_privacypolicy.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_privacypolicy.setSelected(false);
            } else {
                drawer_item_privacypolicy.setSelected(true);
            }
            Intent intent = new Intent(this, RefundPolicyActivity.class);
            intent.putExtra("url", "http://orionfxrobo.com/privacy_policy.php");
            intent.putExtra("title", "Privacy & Policy");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_calaneder) {
            if (drawer_item_calaneder.isSelected()) {
              //  drawer_item_calaneder.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_calaneder.setSelected(false);
            } else {
                drawer_item_calaneder.setSelected(true);
            }
            Intent intent = new Intent(this, RefundPolicyActivity.class);
            intent.putExtra("url", "https://orionfxrobo.com/economic_calender.php");
            intent.putExtra("title", "Economic Calender");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_chart) {
            if (drawer_item_chart.isSelected()) {
              //  drawer_item_chart.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_chart.setSelected(false);
            } else {
                drawer_item_chart.setSelected(true);
            }
            Intent intent = new Intent(this, RefundPolicyActivity.class);
            intent.putExtra("url", "https://in.tradingview.com/chart/");
            intent.putExtra("title", "Chart");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_orderhistory) {
            if (drawer_orderhistory.isSelected()) {
              //  drawer_orderhistory.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_orderhistory.setSelected(false);
            } else {
                drawer_orderhistory.setSelected(true);
            }
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_chatwithus) {
            if (drawer_item_chatwithus.isSelected()) {
              //  drawer_item_chatwithus.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_chatwithus.setSelected(false);
            } else {
              //  drawer_item_chatwithus.setBackgroundColor(getResources().getColor(R.color.gray_333));
                drawer_item_chatwithus.setSelected(true);
            }
            Intent intent = new Intent(this, ChatWithUsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_resetpwd) {
            if (drawer_item_resetpwd.isSelected()) {
              //  drawer_item_resetpwd.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_resetpwd.setSelected(false);
            } else {
                drawer_item_resetpwd.setSelected(true);
            }
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_portfolio) {
            if (drawer_item_portfolio.isSelected()) {
               // drawer_item_portfolio.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_portfolio.setSelected(false);
            } else {
                drawer_item_portfolio.setSelected(true);
            }
            Intent intent = new Intent(this, MyPortfolioActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if (view == drawer_item_traction) {
            if (drawer_item_traction.isSelected()) {
                drawer_item_traction.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_traction.setSelected(false);
            } else {
                drawer_item_traction.setSelected(true);
            }
            Intent intent = new Intent(this, TransactionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (view == drawer_item_logout) {
            if (drawer_item_logout.isSelected()) {
               // drawer_item_logout.setBackgroundColor(getResources().getColor(R.color.light_blue));
                drawer_item_logout.setSelected(false);
            } else {
              //  drawer_item_logout.setBackgroundColor(getResources().getColor(R.color.gray_333));
                drawer_item_logout.setSelected(true);
            }
            PrefrenceManager.setString(MainActivity.this, PrefrenceManager.LOGIN_STATUS, "");
            PrefrenceManager.removeAllString(MainActivity.this, "");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

}
