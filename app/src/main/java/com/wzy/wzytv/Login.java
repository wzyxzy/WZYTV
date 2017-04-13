package com.wzy.wzytv;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private EditText name;
    private EditText password;
    private Button sign_in;
    private Button sign_up;
    private String logUrl;
    private String download_url = "http://cnbeijing.xyz/tv/WZYTV.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update();
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPref = this.getSharedPreferences("infos",
                MODE_PRIVATE);
        if (sharedPref.getBoolean("signup", false)) {
            signIn(sharedPref.getString("name", "wzy"), sharedPref.getString("password", "tv1.m"));
        }
        initView();
    }

    private void update() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://cnbeijing.xyz/tv/version", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                check(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络加载异常，请检查您的网络是否连接正常", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    private void check(String response) {
        try {

            if (!response.equals(getVersionName())) {
                Toast.makeText(this, "发现新版本", Toast.LENGTH_SHORT).show();
                dialog();

//                Uri uri = Uri.parse("http://cnbeijing.xyz/tv/WZYTV.apk");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("发现新版本，是否要下载？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                intent.putExtra("download_url", download_url);
                startService(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private String getVersionName() throws Exception {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    private void initView() {

        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.sign_in);
        sign_in.setOnClickListener(this);
        sign_up = (Button) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);
    }

    private void gotonewlayout() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                submit();
                break;
            case R.id.sign_up:
                signup();
                break;
        }
    }

    private void signup() {
        Intent intent = new Intent();
        intent.setClass(this, SignUp.class);
        startActivity(intent);
    }

    private void submit() {
        // validate
        String nameString = name.getText().toString().trim();
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, "请输入您的账号", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordString = password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "请输入您的密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        signIn(nameString, passwordString);
        SharedPreferences sharedPref = this.getSharedPreferences("infos",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", nameString);
        editor.putString("password", passwordString);
        editor.putBoolean("signup", true);
        editor.commit();

    }

    private void signIn(String nameString, String passwordString) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        logUrl = "http://cnbeijing.xyz:8080/UserLogin?name=" + nameString + "&pass=" + passwordString + "&device_id=" + DEVICE_ID;
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(logUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("登录成功")) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    gotonewlayout();
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "登录失败，请检查您的网络", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(stringRequest);
    }
}
