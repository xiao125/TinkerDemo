package com.imooc.tinkerdemo.network;


import com.imooc.tinkerdemo.network.listener.DisposeDataHandle;
import com.imooc.tinkerdemo.network.listener.DisposeDataListener;
import com.imooc.tinkerdemo.network.listener.DisposeDownloadListener;
import com.imooc.tinkerdemo.network.request.CommonRequest;
import com.imooc.tinkerdemo.network.request.RequestParams;
import com.imooc.tinkerdemo.tinker.module.BasePatch;

/**
 * Created by renzhiqiang on 17/4/24.
 *
 * @function 请求发送中心
 */
public class RequestCenter {

    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }


    /**
     * 询问是否有patch可更新
     *
     * @param listener
     */
    public static void requestPatchUpdateInfo(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstant.UPDATE_PATCH_URL, null, listener,
                BasePatch.class);

    }

    /**
     * 文件下载
     *
     * @param url
     * @param path
     * @param listener
     */
    public static void downloadFile(String url, String path, DisposeDownloadListener listener) {
        CommonOkHttpClient.downloadFile(CommonRequest.createGetRequest(url, null),
                new DisposeDataHandle(listener, path));
    }
}