package com.wzy.wzytv;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wzy.wzytv.tools.NotifyUtil;

import java.io.File;

public class DownloadService extends Service {
    String download_url;
    String savePath= Environment.getExternalStorageDirectory()+"/WZYTV.apk";
    private int requestCode = (int) SystemClock.uptimeMillis();
    private NotifyUtil currentNotify;
    File mFile;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFile=new File(savePath);
        download_url=intent.getStringExtra("download_url");
        Log.e("test","执行onStartCommand");
        //设置想要展示的数据内容
        Intent intent_noti = new Intent();
        intent_noti.setAction(Intent.ACTION_VIEW);
        //文件的类型，从tomcat里面找
        intent_noti.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                requestCode, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.mipmap.ic36;
        String ticker = "正在更新WZYTV";
        //实例化工具类，并且调用接口
        NotifyUtil notify7 = new NotifyUtil(this, 7);
        notify7.notify_progress(rightPendIntent, smallIcon, ticker, "WZYTV升级程序", "正在下载中",
                false, false, false, download_url, savePath, new NotifyUtil.DownLoadListener() {
                    @Override
                    public void OnSuccess(File file) {
                        mFile=file;
                        DownloadService.this.stopSelf();
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {

                    }
                });
        currentNotify = notify7;
        return super.onStartCommand(intent, flags, startId);

    }
}
