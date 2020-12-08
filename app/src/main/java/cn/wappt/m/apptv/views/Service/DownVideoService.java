package cn.wappt.m.apptv.views.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author: wsq
 * @date: 2020/10/13
 * Description:
 */
public class DownVideoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
