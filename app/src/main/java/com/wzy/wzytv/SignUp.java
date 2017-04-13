package com.wzy.wzytv;

import android.content.Context;
import android.os.Bundle;
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

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText sign_name;
    private EditText sign_password;
    private EditText sign_email;
    private EditText sign_qq;
    private Button submit;
    private Button cancel;
    private EditText sign_password_2;
    private String reUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        sign_name = (EditText) findViewById(R.id.sign_name);
        sign_password = (EditText) findViewById(R.id.sign_password);
        sign_email = (EditText) findViewById(R.id.sign_email);
        sign_qq = (EditText) findViewById(R.id.sign_qq);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        sign_password_2 = (EditText) findViewById(R.id.sign_password_2);
        sign_password_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submit();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        String name = sign_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入您的账号", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = sign_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入您的密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = sign_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入您的邮箱地址", Toast.LENGTH_SHORT).show();
            return;
        }
        String qq = sign_qq.getText().toString().trim();
        if (TextUtils.isEmpty(qq)) {
            Toast.makeText(this, "请输入您的绑定QQ", Toast.LENGTH_SHORT).show();
            return;
        }
        String password_2 = sign_password_2.getText().toString().trim();
        if (!password_2.equals(password)) {
            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        reUrl = "http://cnbeijing.xyz:8080/UserRegister?name=" + name + "&pass=" + password + "&email=" + email + "&device_id=" + DEVICE_ID+ "&qq=" + qq;
        // TODO validate success, do something
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(reUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("注册成功")) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "注册失败，请检查您的网络", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue.add(stringRequest);

    }


}
