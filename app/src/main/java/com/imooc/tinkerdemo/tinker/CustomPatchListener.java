package com.imooc.tinkerdemo.tinker;

import android.content.Context;

import com.imooc.tinkerdemo.util.Utils;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * 1.较验patch文件是否合法  2.启动Service去安装patch文件
 * Created by Administrator on 2017/7/11.
 */

public class CustomPatchListener extends DefaultPatchListener {

    private String currentMD5;

    public void setCurrentMD5(String md5Value){

        this.currentMD5 = md5Value;
    }

    public CustomPatchListener(Context context) {
        super(context);
    }

    @Override
    protected int patchCheck(String path) {

        //patch文件ms5较验

        if (!Utils.isFileMD5Matched(path,currentMD5)){

            return ShareConstants.ERROR_PATCH_DISABLE;
        }

        return super.patchCheck(path);

    }
}
