package cn.buildworld.com.humiture2;

import android.app.Application;

import com.chinamobile.iot.onenet.OneNetApi;

/**
 * 作者：MiChong on 2017/5/15 0015 19:17
 * 邮箱：1564666023@qq.com
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {

        //初始化APP,获取并设置API_KEY
        super.onCreate();
        OneNetApi.init(this,true);

        String savedApiKey = Preferences.getInstance(this).getString(Preferences.API_KEY,null);

        if(savedApiKey != null){
            OneNetApi.setAppKey(savedApiKey);
        }
    }
}
