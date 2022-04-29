package com.growfxtrade.prefrence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceManager {
    private static final String MY_PREFS_NAME = "Orionfx";
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    public static String currency = "currency";
    public static String allcurrency = "allcurrency";
    public static String USERID = "userid";
    public static String USERNAME = "username";
    public static String EMAIL = "email";
    public static String MOBILE = "mono";
    public static String STATUS = "status";
    public static String COUNTRY = "country";
    public static String LOGIN_STATUS = "loginstatus";
    public static String Accountno = "accountno";
    public static String Bankname = "bankname";
    public static String IFCI = "ifci";
    public static String UPI = "upi";
    public static String user_balence = "amountt";


    public static void setString(Context activity, String key, String value) {
        sharedPref = activity.getSharedPreferences(MY_PREFS_NAME, 0);
        editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getString(Activity activity, String key) {
        sharedPref = activity.getSharedPreferences(MY_PREFS_NAME, 0);
        return sharedPref.getString(key, "");

    }
    public static void setuser_balence(Context activity, String key, String value) {
        sharedPref = activity.getSharedPreferences(MY_PREFS_NAME, 0);
        editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getuser_balence(Activity activity, String key) {
        sharedPref = activity.getSharedPreferences(MY_PREFS_NAME, 0);
        return sharedPref.getString(key, "");

    }

    public static void setCurrencyString(Context activity, String value) {
        String old = "";
        String newstring = "";
        old = getString((Activity) activity, allcurrency);
        newstring = old + "," + value;
        setString(activity, allcurrency, newstring);
    }

    public static void removeCurrencyString(Context activity, String value) {
        String old = "";
        String newstring = "";
        old = getString((Activity) activity, allcurrency);
        newstring = old.replace(value, "");
        setString(activity, allcurrency, newstring);
    }

    public static void removeAllString(Context activity, String value) {
        setString(activity, allcurrency, value);
    }


    public static void clearPrefs(Activity activity) {
        sharedPref = activity.getSharedPreferences(MY_PREFS_NAME, 0);
        editor = sharedPref.edit();
        editor.clear().apply();
    }


}
