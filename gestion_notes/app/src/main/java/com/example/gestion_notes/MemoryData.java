package com.example.gestion_notes;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class MemoryData {

    // Nom du fichier SharedPreferences
    private static final String PREF_NAME = "memory_data";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_LAST_MSG_TS_PREFIX = "last_msg_ts_";

    // Méthode pour enregistrer le numéro de mobile de l'utilisateur
    public static void saveData(Context context, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_MOBILE, data);
        editor.apply();
    }
    // Méthode pour stocker le numéro de mobile
    // Méthode pour stocker le numéro de mobile
    public static void saveUserMobile(Context context, String userMobile) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_MOBILE, userMobile);
        editor.apply();
    }
    public static String getUserMobile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userMobile = sharedPreferences.getString(KEY_USER_MOBILE, "");
        Log.d("MemoryData", "User mobile from SharedPreferences: " + userMobile); // Vérification
        return userMobile;
    }


    // Méthode pour enregistrer le timestamp du dernier message
    public static void saveLastMsgTS(String timestamp, String chatKey, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_MSG_TS_PREFIX + chatKey, timestamp);
        editor.apply();
    }

    // Méthode pour récupérer le timestamp du dernier message
    public static String getLastMsgTS(Context context ,String chatKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_MSG_TS_PREFIX + chatKey, "0"); // Retourne "0" si la valeur n'est pas trouvée
    }
}
