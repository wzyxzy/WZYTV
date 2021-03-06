package com.wzy.wzytv;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wzy.wzytv.adapters.TVAdapter;
import com.wzy.wzytv.model.TVModel;
import com.wzy.wzytv.model.TokenInfo;
import com.wzy.wzytv.tools.AESTools;
import com.wzy.wzytv.tools.TextTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.wzy.wzytv.R.id.feedback;
import static com.wzy.wzytv.R.id.signout;
import static com.wzy.wzytv.R.id.tv1;
import static com.wzy.wzytv.R.id.tv4;
import static com.wzy.wzytv.R.id.tv5;
import static com.wzy.wzytv.R.id.tv6;
import static com.wzy.wzytv.R.id.tv7;
import static com.wzy.wzytv.R.id.tv8;
import static com.wzy.wzytv.R.id.tv9;
import static com.wzy.wzytv.R.id.update;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private String url;
    private ListView lv;
    private TVAdapter adapter;
    //    private int level;
    private String text;
    private List<TVModel.TvListEntity> tvListEntities;
    private static final String getUrl = "http://cnxa.top/tv/";
    private static final String pass = "wzy2008";
    private File file;
    private String userText;
    private String download_url = "http://cnxa.top/tv/WZYTV.apk";
    private String tv_sources;
    private String up_token;
    private static final int VITAMIO_PLAYER_FOR_VIDEO = 0;
    private static final int EXO_PLAYER_FOR_VIDEO = 1;
    private static final int IJK_PLAYER_FOR_VIDEO = 2;
    private int choose_player = VITAMIO_PLAYER_FOR_VIDEO;
    private String device_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
//        Intent intent = getIntent();
        SharedPreferences sharedPref = this.getSharedPreferences("infos",
                MODE_PRIVATE);
        text = sharedPref.getString("text", "tv4.m");
        tv_sources = "&name=" + sharedPref.getString("name", "wzy") + "&pass=" + sharedPref.getString("password", "wzy");
        up_token = "user=" + sharedPref.getString("name", "wzy") + "&pass=" + sharedPref.getString("password", "wzy");
        initView();
        initData();
    }

    private void initData() {
        SharedPreferences sharedPref = this.getSharedPreferences("infos",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("text", text);
        editor.commit();

        device_id = getIntent().getStringExtra("device_id");
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(getUrl + text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                initGson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络加载异常，请检查您的网络是否连接正常", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);

    }

    private void initGson(String response) {
//        Log.d("s",response);
        String s = AESTools.decrypt(response, pass);

        gotoGson(s);

    }

    private void gotoGson(String s) {
        Gson gson = new Gson();
        TVModel tvModel = gson.fromJson(s, TVModel.class);
        tvListEntities = tvModel.getTv_list();
        adapter.updateRes(tvListEntities);
    }


    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        tvListEntities = new ArrayList<>();
        adapter = new TVAdapter(tvListEntities, this, R.layout.item);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        lv.setLayoutManager(llm);
//        lv.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        lv.setAdapter(adapter);

//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                url = tvListEntities.get(position).getUrl();
//                if (TextUtils.isEmpty(url)) {
//
//                    return;
//                } else {
//                    intentgo(position);
//                }
//            }
//        });
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tv.txt");
        if (file.exists()) {
            CustomThread customThread = new CustomThread();
            customThread.start();
        } else {
            Toast.makeText(this, "您的内存卡主目录中未发现\"tv.txt\"文件，请先将自定义文件放入内存卡主目录中", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tvsource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case tv1:
                text = "tv4.m";
                tokenup();
                break;

            case tv4:
                text = "tv4.m";
                initData();
                break;
            case tv5:
                text = "tv5.m";
                initData();
                break;
            case tv6:
                text = "tv6.m";
                initData();
                break;
            case tv7:
                text = "tv7.m";
                initData();
                break;
            case tv8:
                text = "tv8.m";
                initData();
                break;
            case tv9:
                custom();
                break;
            case signout:
                signOut();
                break;
            case feedback:
                feedback();
                break;
            case update:
                update();
                break;
        }
        return true;
    }

    private void tokenup() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://cnxa.top:8080/LoginToken?" + up_token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                get_token(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络加载异常，请检查您的网络是否连接正常", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    private void get_token(final String response) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://access.pygdzhcs.com/account/login?accounttype=2&accesstoken=null&deviceno=" + device_id + "&isforce=1&pwd=abvc&devicetype=3&account=usersawbcde" + response, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                upload_token(res, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络加载异常，请检查您的网络是否连接正常", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    private void upload_token(String r, String line) {
        Gson gson = new Gson();
        TokenInfo info = gson.fromJson(r, TokenInfo.class);
        String token = info.getAccess_token();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://cnxa.top:8080/UpToken?line=" + line + "&playtoken=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络加载异常，请检查您的网络是否连接正常", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(stringRequest);
    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    String b = bundle.getString("a");
                    userText = b;
                    break;
            }
        }
    };

    private void custom() {
        text = "tv9.m";
        if (TextUtils.isEmpty(userText)) {
            Toast.makeText(this, "由于数据太多,加载较慢,请稍后．．．", Toast.LENGTH_SHORT).show();
        } else {
            gotoGson(userText);

        }


    }

    class CustomThread extends Thread {
        @Override
        public void run() {
            String a = TextTools.fileToJson(file);
            Message msg = new Message();
            msg.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("a", a);
            msg.setData(bundle);
            uiHandler.sendMessage(msg);
        }
    }

    private void update() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://cnxa.top/tv/version", new Response.Listener<String>() {
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

            if (response.equals(getVersionName())) {
                Toast.makeText(this, "此版本为最新版本，无需更新", Toast.LENGTH_SHORT).show();
            } else {
                dialog();
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
//                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
//                intent.putExtra("download_url", download_url);
//                startService(intent);
                Uri uri = Uri.parse(download_url);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);
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

    private void feedback() {
        Intent intent = new Intent();
        intent.setClass(this, Feedback.class);
        startActivity(intent);
    }

    private void signOut() {
        SharedPreferences sharedPref = this.getSharedPreferences("infos",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent();
        intent.setClass(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void intentgo(int position) {
        Intent intent = new Intent();
        switch (choose_player) {
            case VITAMIO_PLAYER_FOR_VIDEO:
                intent.setClass(this, VideoTV.class);
                break;
            case EXO_PLAYER_FOR_VIDEO:
//                intent.setClass(this, VideoViewExo.class);
                break;
            case IJK_PLAYER_FOR_VIDEO:
                intent.setClass(this, VideoTV.class);
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("tv_sources", tv_sources);
        bundle.putString("url", url);
        bundle.putInt("position", position);
        bundle.putString("getUrl", getUrl + text);
        bundle.putString("name", tvListEntities.get(position).getTitle());
        if (text.equalsIgnoreCase("tv9.m")) {
            bundle.putString("userText", userText);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        url = tvListEntities.get(position).getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        } else {
            intentgo(position);
        }
    }
}
