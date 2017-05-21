package cn.buildworld.com.humiture2;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MiChong on 2017/5/21.
 */

public class JsonHelp{

    public String result;
    public String unit ;
    public String id ;
    public String value;
    public String time;

    public JsonHelp(String result) throws JSONException {
        this.result = result;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}
