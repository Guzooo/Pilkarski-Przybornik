package pl.Guzooo.PilkarskiPrzybornik.Powiadomienia;

import android.content.pm.PackageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import pl.Guzooo.PilkarskiPrzybornik.R;
import pl.Guzooo.PilkarskiPrzybornik.ReadJSON;

public class NotificationsActivity extends AppCompatActivity implements ReadJSON.ReadJSONListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String NOTIFICATIONS_PAGE = "https://raw.githubusercontent.com/Guzooo/Pilkarski-Przybornik/info/Notificationspl.txt";

    private static final String PREFERENCES_NUMBER_NOTIFICATIONS = "numbernotifications";

    private AdapterNotifications adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private int appVersion;
    private String appLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        setRefreshLayout();
        setRecyclerView();
        setAdapter();
        getAppInfo();

        onRefresh();
    }

    private void getAppInfo(){
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "APPLICATION VERSION IS UNKNOWN", Toast.LENGTH_SHORT).show();//TODO: nie ma stringow
        }
        appLanguage = Locale.getDefault().getLanguage();
        Log.d("sa",appLanguage + " jezyk, " + appVersion + " versia");
    }

    private boolean checkLanguage(String language){
        return language.equals(appLanguage);
    }

    private boolean checkVersion(String version){
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
        if (checkVersion(notification.getVersion())) {
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
        adapter.delAllNotifications();
        ReadJSON readJSON = new ReadJSON();
        readJSON.setReadJSONListener(this);
        readJSON.execute(NOTIFICATIONS_PAGE);
    }

    @Override
    public void onPreRead() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onPostRead() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onProgressRead(JSONObject object) {
        addNotificationAdapter(getNotification(object));
    }

    @Override
    public void onPositivePostRead() {

    }

    @Override
    public void onNegativePostRead() {
        Toast.makeText(this, "NOT FIND NEWS, PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
    }
}
