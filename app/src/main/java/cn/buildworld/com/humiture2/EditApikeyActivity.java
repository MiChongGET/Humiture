package cn.buildworld.com.humiture2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;

public class EditApikeyActivity extends AppCompatActivity {

    private EditText edit_apikey ;
    private String apikey;
    private Preferences preferences;
    private String TAG = "设置API";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_apikey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        toolbar.setTitle("设置API_KEY");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_apikey = (EditText) findViewById(R.id.edit_apikey);
        preferences = Preferences.getInstance(this);

        apikey = preferences.getString(Preferences.API_KEY,null);
        if (TextUtils.isEmpty(apikey)){
        }else {
            edit_apikey.setText(apikey);
        }
    }

    public void saveApi(View view){

        String key = edit_apikey.getText().toString().trim();
        Log.i(TAG, "saveApi: "+key);
        if (TextUtils.isEmpty(key)){
            Toast.makeText(this, "您的API_KEY为空！！！", Toast.LENGTH_SHORT).show();
        }else {
            preferences.putString(Preferences.API_KEY,key);
            OneNetApi.setAppKey(key);
            finish();
        }
    }
}
