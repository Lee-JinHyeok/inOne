package com.example.inone.prefs;
import android.content.Context;
import android.content.SharedPreferences;



public class SharedPreferencesManager {

    public static final String PREFERENCES_NAME = "rebuild_preference";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * float 값 저장
     * @param context
     * @param key
     * @param value
     */

    public static void setFloat(Context context, String key, float value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * float 값 로드
     * @param context
     * @param key
     * @return
     */

    public static float getFloat(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        float value = prefs.getFloat(key, 0);
        return value;
    }

    /**
     * 키 값 삭제
     * @param context
     * @param key
     */

    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    /**
     * 모든 저장 데이터 삭제
     * @param context
     */

    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();

    }
}