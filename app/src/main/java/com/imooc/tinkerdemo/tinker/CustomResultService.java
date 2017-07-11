package com.imooc.tinkerdemo.tinker;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * Created by Administrator on 2017/7/11.
 */

public class CustomResultService extends DefaultTinkerResultService{

    private static final String TAG = "Tinker.SampleResultService";

    //返回patch文件的最终安装结果
    @Override
    public void onPatchResult(PatchResult result) {

        if (result == null){

            TinkerLog.e(TAG, "DefaultTinkerResultService received null result!!!!");
            return;
        }

        TinkerLog.i(TAG, "DefaultTinkerResultService received a result:%s",result.toString());

        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        if (result.isSuccess){
            deleteRawPatchFile(new File(result.rawPatchFilePath));
        }

    }
}
