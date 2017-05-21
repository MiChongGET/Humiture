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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private String TAG = "数据获取";
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

    //获取参数信息
    public void sendRequest(View view){
        String url = input_url.getText().toString().trim();

        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder builder = httpUrl.newBuilder(url);
        url = builder.toString();
        OneNetApi.get(url, mCallback);

        Toast.makeText(this, "发送请求", Toast.LENGTH_SHORT).show();
        //OneNetApi.get(url, mCallback);

    }


    //获取温湿度信息

    //获取所有数据的接口
    interface Function1<T> {
        void apply(T t);
    }

    //获取制定数据接口
    interface Function2<T>{
        void apply(T t1,T t2);
    }
    private void displayLog(String response) throws JSONException {
        if ((response.startsWith("{") && response.endsWith("}")) || (response.startsWith("[") && response.endsWith("]"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            response = gson.toJson(jsonParser.parse(response));
            Log.i(TAG, response);
            showjson.setText(response);

            //将取得数据进行解析
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            JSONObject humidity = dataArray.getJSONObject(9);
            JSONObject temp = dataArray.getJSONObject(4);


            String h_value = humidity.getString("current_value");
            String h_id = humidity.getString("id");
            String h_time = humidity.getString("update_at");
            String h_symbol = humidity.getString("unit_symbol");

            String t_value = temp.getString("current_value");
            String t_id = temp.getString("id");
            String t_time = temp.getString("update_at");
            String t_symbol = temp.getString("unit_symbol");

            Log.i(TAG, "湿度："+h_id+"------"+h_time+"------"+h_value+h_symbol);
            Log.i(TAG, "温度："+t_id+"------"+t_time+"------"+t_value+t_symbol);

//            showjson.setText(unit+value+time);

        }
        //DisplayApiRespActivity.actionDisplayApiResp(getActivity(), response);
    }

    private class Callback implements OneNetApiCallback {
        @Override
        public void onSuccess(String response) {
            try {
                displayLog(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "数据获取成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Exception e) {
            Toast.makeText(MainActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
        }
    }


    //获取指定数据
    private Function2<String> mQuerySingleDatastreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.querySingleDataStream(deviceId, dataStreamId, new Callback());
        }
    };

    //获取所有的数据
    private Function1<String> mQueryMultiDataStreamFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryMultiDataStreams(deviceId, new Callback());
        }
    };
    public void getHumiture(View view){

//        mQuerySingleDatastreamFunction.apply("6193404","temperature");
        mQueryMultiDataStreamFunction.apply("6193404");

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
