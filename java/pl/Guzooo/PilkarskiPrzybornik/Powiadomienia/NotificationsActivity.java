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

    private static final String NOTIFICATIONS_PAGE = "https://docs.google.com/document/d/1dikNs0eGZm5rIk-SI23VFKlEaKBhD1zU_xblYzWzED0/edit?usp=sharing";
    private final String CODE_NOTIFICATION = "news";
    private static final String EXTRA_NEWS = "news";

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

        if(getIntent().getStringExtra(EXTRA_NEWS) == null){
            onRefresh();
        } else {
            try {
                JSONObject object = new JSONObject(getIntent().getStringExtra(EXTRA_NEWS));
                JSONArray array = object.getJSONArray(CODE_NOTIFICATION);
                refreshAdapter(getNotification(array));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
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

    private ArrayList<Notification> getNotification(JSONArray array){
        ArrayList<Notification> notifications = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                Notification notification = new Notification(array.getJSONObject(i), this);
                if (checkLanguage(notification.getLanguage()) && checkVersion(notification.getVersion())) {
                    notifications.add(notification);
                    Log.d("noActi", "dodaje");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return notifications;
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

    private void refreshAdapter(ArrayList<Notification> notifications){
        adapter.addNotifications(notifications);
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
    public void onPositivePostRead(ArrayList<String> strings) {
        try {
            JSONObject object = new JSONObject(strings.get(1));
            JSONArray array = object.getJSONArray(CODE_NOTIFICATION);
            refreshAdapter(getNotification(array));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onNegativePostRead() {
        Toast.makeText(this, "NOT FIND NEWS, PLIS TRY AGAIN", Toast.LENGTH_SHORT).show();
    }
}
