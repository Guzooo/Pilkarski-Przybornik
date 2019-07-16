package pl.Guzooo.PilkarskiPrzybornik.Powiadomienia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import pl.Guzooo.PilkarskiPrzybornik.R;
import pl.Guzooo.PilkarskiPrzybornik.ReadJSON;

public class NotificationsActivity extends AppCompatActivity implements ReadJSON.ReadJSONListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_NUMBER_NOTIFICATIONS = "numbernotifications";

    private static final String NOTIFICATIONS_PAGE = "https://raw.githubusercontent.com/Guzooo/Pilkarski-Przybornik/info/Notifications";
    private static final String NOTIFICATION_PAGE_END = ".txt";

    private static final String PREFERENCES_NAME = "notifications";
    private static final String PREFERENCES_NUMBER_NOTIFICATIONS = "numbernotifications";
    private static final String PREFERENCES_NEW_NOTIFICATIONS = "newnotifications";

    private AdapterNotifications adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private int appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setRefreshLayout();
        setRecyclerView();
        setAdapter();
        appVersion = getAppVersion(this);

        onRefresh();
    }

    private static int getAppVersion(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "APPLICATION VERSION IS UNKNOWN", Toast.LENGTH_SHORT).show();//TODO: nie ma stringow
        }
        return 0;
    }

    private static String getAppLanguage(){
        return Locale.getDefault().getLanguage();
    }

    private static boolean checkVersion(int appVersion, String version){
        if (version.equals("all"))
            return true;

        if(version.contains("<")){
            version = version.replace("<", "");
            int ver = Integer.valueOf(version);
            if (appVersion < ver){
                return true;
            }
        }

        if (version.contains(">")){
            version = version.replace(">", "");
            int ver = Integer.valueOf(version);
            if (appVersion > ver){
                return true;
            }
        }

        return false;
    }

    private Notification getNotification(JSONObject object){
        Notification notification = new Notification(object, this);
        if (checkVersion(appVersion, notification.getVersion())) {
            return notification;
        }
        return null;
    }

    private void setRefreshLayout(){
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorSecondary));
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setAdapter(){
        adapter = new AdapterNotifications();
        recyclerView.setAdapter(adapter);
    }

    private void addNotificationAdapter(Notification notification){
        if(notification != null)
            adapter.addNotification(notification);
    }

    @Override
    public void onRefresh() {
        GoneAllAlert();
        adapter.delAllNotifications();
        ReadJSON readJSON = new ReadJSON();
        readJSON.setReadJSONListener(this);
        readJSON.execute(NOTIFICATIONS_PAGE + getAppLanguage() + NOTIFICATION_PAGE_END);
    }

    @Override
    public void onPreRead() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onProgressRead(JSONObject object) {
        addNotificationAdapter(getNotification(object));
    }

    @Override
    public void onPostRead() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onPositivePostRead() {
        int extra = getIntent().getIntExtra(EXTRA_NUMBER_NOTIFICATIONS, 0);
        if (extra == 0) {
            getOnlineNotificationNumber(null, true, this);
        } else {
            SavePreferencesNotificationNumber(extra, this);
        }
        SavePreferencesNewNotification(false, this);
    }

    @Override
    public void onNegativePostRead() {
        VisibleDownloadFailedAlert();
    }

    public static ReadJSON getOnlineNotificationNumber(final ImageView imageView, final boolean save, final Context context){
        final String NOTIFICATION_INFO_PAGE = "https://raw.githubusercontent.com/Guzooo/Pilkarski-Przybornik/info/InfoNotifications.txt";

        final ReadJSON readJSON = new ReadJSON();
        readJSON.setReadJSONListener(new ReadJSON.ReadJSONListener() {

            private int appVersion;
            private String appLanguage;
            private JSONObject correctObject;

            private final String CODE_LANGUAGE = "languagecode";
            private final String CODE_VERSION_NUMBER = "versionnumber";
            private final String CODE_VERSION = "version";
            private final String CODE_NUMBER = "number";

            @Override
            public void onPreRead() {
                appVersion = getAppVersion(context);
                appLanguage = getAppLanguage();
                Log.d("pm", "zaczynamy");
            }

            @Override
            public void onProgressRead(JSONObject object) {
                try {
                    if (object.getString(CODE_LANGUAGE).equals(appLanguage)){
                        correctObject = object;
                        Log.d("pm", "juz chyba ogarniam");
                        readJSON.cancel(true);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPostRead() {

            }

            @Override
            public void onPositivePostRead() {
                try {
                    JSONArray array = correctObject.getJSONArray(CODE_VERSION_NUMBER);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        int onlineNotificationNumber = object.getInt(CODE_NUMBER);
                        Log.d("pm", onlineNotificationNumber + " ooonline, " + getPreferencesNotificationNumber(context));
                        if(checkVersion(appVersion, object.getString(CODE_VERSION)) && onlineNotificationNumber != getPreferencesNotificationNumber(context)) {
                            if(imageView != null)
                                DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(context, R.color.colorAlert));
                            readJSON.setFirstSaveInt(onlineNotificationNumber);
                            SavePreferencesNewNotification(true, context);
                            if(save)
                                SavePreferencesNotificationNumber(onlineNotificationNumber, context);
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNegativePostRead() {

            }
        });
        readJSON.execute(NOTIFICATION_INFO_PAGE);
        return readJSON;
    }

    public static int getPreferencesNotificationNumber(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(PREFERENCES_NUMBER_NOTIFICATIONS, 0);
    }

    private static void SavePreferencesNotificationNumber(int notificationsNumber, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFERENCES_NUMBER_NOTIFICATIONS, notificationsNumber);
        editor.apply();
    }

    public static boolean getPreferencesNewNotification(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(PREFERENCES_NEW_NOTIFICATIONS, false);
    }

    public static void SavePreferencesNewNotification(boolean newNotifications, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFERENCES_NEW_NOTIFICATIONS, newNotifications);
        editor.apply();
    }

    private void GoneAllAlert(){
        findViewById(R.id.alert_box).setVisibility(View.GONE);
        findViewById(R.id.cellular_data_alert).setVisibility(View.GONE);
        findViewById(R.id.internet_disconnect_alert).setVisibility(View.GONE);
        findViewById(R.id.download_failed_alert).setVisibility(View.GONE);
        findViewById(R.id.buttons_box).setVisibility(View.GONE);
    }

    private void VisibleCellularDataAlert(){
        findViewById(R.id.alert_box).setVisibility(View.VISIBLE);
        findViewById(R.id.cellular_data_alert).setVisibility(View.VISIBLE);
    }

    private void VisibleInternetDisconnectAlert(){
        findViewById(R.id.alert_box).setVisibility(View.VISIBLE);
        findViewById(R.id.internet_disconnect_alert).setVisibility(View.VISIBLE);
    }

    private void VisibleDownloadFailedAlert(){
        findViewById(R.id.alert_box).setVisibility(View.VISIBLE);
        findViewById(R.id.download_failed_alert).setVisibility(View.VISIBLE);
        findViewById(R.id.buttons_box).setVisibility(View.VISIBLE);
    }

    public void ClickMsg(View v){
        Uri uri = Uri.parse("https://www.messenger.com/t/GuzoooApps");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
