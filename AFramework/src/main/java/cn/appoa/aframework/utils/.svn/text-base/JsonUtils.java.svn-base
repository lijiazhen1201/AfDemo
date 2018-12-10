package cn.appoa.aframework.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static int TYPE = 1;
    public static String CODE = "code";
    public static String MESSAGE = "message";
    public static String DATA = "data";
    public static String STATE = "state";
    public static String RESULT = "result";

    /**
     * 初始化
     *
     * @param type
     */
    public static void init(int type) {
        TYPE = type;
    }

    /**
     * 初始化
     *
     * @param code
     * @param message
     * @param data
     */
    public static void init(String code, String message, String data) {
        TYPE = 1;
        CODE = code;
        MESSAGE = message;
        DATA = data;
    }

    /**
     * 初始化
     *
     * @param state
     * @param result
     */
    public static void init(String state, String result) {
        TYPE = 2;
        STATE = state;
        RESULT = result;
    }

    /**
     * 过滤json数据
     *
     * @param json
     * @return
     */
    public static boolean filterJson(String json) {
        boolean b = false;
        try {
            if (!TextUtils.isEmpty(json)) {
                JSONObject obj = new JSONObject(json);
                if (TYPE == 1) {
                    int code = obj.getInt(CODE);
                    if (code == 200) {
                        b = true;
                    }
                } else if (TYPE == 2) {
                    String state = obj.getString(STATE);
                    if (TextUtils.equals(state, "true")) {
                        b = true;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 解析json数据
     *
     * @param json
     * @param t
     * @return
     */
    public static <T> List<T> parseJson(String json, Class<T> t) {
        List<T> list = new ArrayList<>();
        if (filterJson(json)) {
            try {
                JSONObject obj = new JSONObject(json);
                JSONArray array = null;
                if (TYPE == 1) {
                    array = obj.getJSONArray(DATA);
                } else if (TYPE == 2) {
                    array = obj.getJSONArray(RESULT);
                }
                if (array != null && array.length() > 0) {
                    list = JSON.parseArray(array.toString(), t);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 弹出信息
     *
     * @param context
     * @param json
     */
    public static void showMsg(Context context, String json) {
        String message = null;
        try {
            if (!TextUtils.isEmpty(json)) {
                JSONObject obj = new JSONObject(json);
                if (TYPE == 1) {
                    message = obj.getString(MESSAGE);
                } else if (TYPE == 2) {
                    message = obj.getString(RESULT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (!TextUtils.isEmpty(message)) {
                AtyUtils.showShort(context, message, false);
            }
        }
    }

    /**
     * 弹出正确信息
     *
     * @param context
     * @param json
     */
    public static void showSuccessMsg(Context context, String json) {
        if (filterJson(json)) {
            showMsg(context, json);
        }
    }

    /**
     * 弹出错误信息
     *
     * @param context
     * @param json
     */
    public static void showErrorMsg(Context context, String json) {
        if (!filterJson(json)) {
            showMsg(context, json);
        }
    }
}
