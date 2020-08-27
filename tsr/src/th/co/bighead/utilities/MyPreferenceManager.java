package th.co.bighead.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by teera-s on 5/19/2016 AD.
 */
public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref2;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;
    // Context
    Context _context;
    Context _context2;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Settings";

    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_NOTIFICATIONS2 = "notifications2";
    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();



    }

    public void setPreferrence(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferrence(String key) {
        return pref.getString(key, null);
    }


    public  void set_nontification_id(String key,int id){
        editor.putInt(key, id);
        editor.commit();
    }


public int get_nontification_id(String key){
    return pref.getInt(key, 0);
}
    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }







    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }


    public  void clearnontification(){
        editor.remove(KEY_NOTIFICATIONS);
        editor.apply();
    }


    public void clear() {
        editor.clear();
        editor.commit();
    }



}
