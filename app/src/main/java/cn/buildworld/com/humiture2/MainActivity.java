package cn.buildworld.com.humiture2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private String TAG = "数据获取：";
    private EditText input_url;
    private TextView showjson;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.api_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==R.id.set_apikey){
            startActivity(new Intent(MainActivity.this,EditApikeyActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        input_url = (EditText) findViewById(R.id.input_url);
        showjson = (TextView) findViewById(R.id.showjson);



        String apikey = OneNetApi.getAppKey();

        if(TextUtils.isEmpty(apikey)){
         apikey = Preferences.getInstance(this).getString(Preferences.API_KEY,null);
        }

        if(TextUtils.isEmpty(apikey)){
            OneNetApi.setAppKey("");
            startActivity(new Intent(MainActivity.this,EditApikeyActivity.class));

        }else {
            OneNetApi.setAppKey(apikey);
        }
    }

    public void sendRequest(View view){
        String url = input_url.getText().toString().trim();

        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder builder = httpUrl.newBuilder(url);
        url = builder.toString();
        OneNetApi.get(url, mCallback);

        Toast.makeText(this, "发送请求", Toast.LENGTH_SHORT).show();
        //OneNetApi.get(url, mCallback);

    }


    //设置GET方式获取设备数据

    private OneNetApiCallback mCallback = new OneNetApiCallback() {
        @Override
        public void onSuccess(final String response) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonParser jsonParser = new JsonParser();
                        String result = gson.toJson(jsonParser.parse(response));
                        Log.i(TAG, "数据: "+result);
                        showjson.setText(result);
//                        mResponseLogTextView.setText(gson.toJson(jsonParser.parse(response)));
                    } catch (Exception e) {

//                        mResponseLogTextView.setText(response);
                    }
                }
            });
        }

        @Override
        public void onFailed(final Exception e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //mResponseLogTextView.setText(e.toString());
                }
            });
        }
    };
}
