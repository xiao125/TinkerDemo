package com.imooc.tinkerdemo.tinker;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.imooc.tinkerdemo.network.RequestCenter;
import com.imooc.tinkerdemo.network.listener.DisposeDataListener;
import com.imooc.tinkerdemo.network.listener.DisposeDownloadListener;
import com.imooc.tinkerdemo.tinker.module.BasePatch;

import java.io.File;

/**
 * Created by Administrator on 2017/7/11.
 */

public class TinkerService extends Service {

    private static final String FILE_END = ".apk"; //文件后缀名
    private static final int DOWNLOAD_PATCH = 0x01; //下载patch文件信息
    private static final int UPDATE_PATCH = 0x02; //检查是否有patch更新

    private String mPatchFileDir; //patch要保存的文件夹
    private String mFilePtch; //patch文件保存路径
    private BasePatch mBasePatchInfo; //服务器patch信息

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }


    //初始化变量
    private void init() {

        mPatchFileDir = getExternalCacheDir().getAbsolutePath()+"/tpatch/";
        File patchFileDir = new File(mPatchFileDir);
        try {

            if (patchFileDir == null || !patchFileDir.exists()){

                patchFileDir.mkdir();//文件夹不存在则创建
            }
        }catch (Exception e){

            e.printStackTrace();
            stopSelf();//无法正常创建文件，则终止服务
        }


    }


    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case UPDATE_PATCH:

                    checkPatchInfo();

                    break;

                case DOWNLOAD_PATCH:

                    downloadPatch();

                    break;
            }

        }
    };


    //用来与被启动者通信的接口
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

       //检查是否有patch更新

        mHandler.sendEmptyMessage(UPDATE_PATCH);

        return START_NOT_STICKY;//被系统回收不再重启
    }

    private void checkPatchInfo(){

        RequestCenter.requestPatchUpdateInfo(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                //获取到服务器返回的状态
                mBasePatchInfo = (BasePatch) responseObj;
                mHandler.sendEmptyMessage(DOWNLOAD_PATCH);

            }

            @Override
            public void onFailure(Object reasonObj) {

                stopSelf();
            }
        });


    }


    private void downloadPatch(){

       mFilePtch = mPatchFileDir.concat(String.valueOf(System.currentTimeMillis()))
               .concat(FILE_END);

        //开始下载差异包
        RequestCenter.downloadFile(mBasePatchInfo.data.downloadUrl, mFilePtch,
                new DisposeDownloadListener() {
            @Override
            public void onProgress(int progrss) {
                //可以打印文件下载进度
            }

            @Override
            public void onSuccess(Object responseObj) {

                //加载path
                TinkerManager.loadPatch(mFilePtch,mBasePatchInfo.data.md5);

            }

            @Override
            public void onFailure(Object reasonObj) {

                stopSelf();
            }
        });


    }


}
