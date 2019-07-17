package pl.Guzooo.PilkarskiPrzybornik;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pl.Guzooo.PilkarskiPrzybornik.Powiadomienia.NotificationsActivity;

public class MainActivity extends AppCompatActivity {

    private int activePlayers;

    private RecyclerView recyclerView;

    private SQLiteDatabase db;
    private Cursor cursor;
    private AdapterGames adapter;

    private ReadJSON readJSONNumberNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNewNotifications();
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setCursor();
        setRecyclerViewLayout();
        setAdapter();

        setNumberActivePlayers();
        setActivePlayerText();
        setNotificationColor();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cursor.close();
        db.close();
    }

    private void setCursor(){
        db = Database.getRead(this);
        cursor = db.query(Game.databaseName,
                Game.onCursor,
                null, null, null, null, null);
    }

    private void setRecyclerViewLayout(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setAdapter(){
        adapter = new AdapterGames(cursor, this);
        recyclerView.setAdapter(adapter);
    }

    private void checkNewNotifications() {
        if (!NotificationsActivity.getPreferencesNewNotification(this))
            readJSONNumberNotification = NotificationsActivity.getOnlineNotificationNumber((ImageView) findViewById(R.id.notifications), false, this);
    }

    private void setNotificationColor(){
        ImageView notificationIcon = findViewById(R.id.notifications);
        if(NotificationsActivity.getPreferencesNewNotification(this)){
            DrawableCompat.setTint(notificationIcon.getDrawable(), ContextCompat.getColor(this, R.color.colorAlert));
            AnimLookMe(notificationIcon);
        } else {
            DrawableCompat.setTint(notificationIcon.getDrawable(), ContextCompat.getColor(this, R.color.primaryIcon));
        }
    }

    private void setActivePlayerText(){
        TextView descriptionManagePlayers = findViewById(R.id.description_active_players);
        if (activePlayers == 0){
            descriptionManagePlayers.setVisibility(View.GONE);
        } else {
            descriptionManagePlayers.setVisibility(View.VISIBLE);
            descriptionManagePlayers.setText(getString(R.string.number_active_players, activePlayers));
        }
    }

    public void ClickNotifications(View v){
        Intent intent = new Intent(this, NotificationsActivity.class);
        if(readJSONNumberNotification != null)
            intent.getIntExtra(NotificationsActivity.EXTRA_NUMBER_NOTIFICATIONS, readJSONNumberNotification.getFirstSaveInt());
        startActivity(intent);
    }

    public void ClickSetting(View v){

    }

    public void ClickPlayersControl(View v){
        Intent intent = new Intent(this, PlayersActivity.class);
        startActivity(intent);
    }

    private void setNumberActivePlayers(){
        SQLiteDatabase db = Database.getRead(this);
        Cursor cursor = db.query(Player.databaseName,
                null,
                "ACTIVE = ?",
                new String[]{Integer.toString(1)},
                null, null, null);
        activePlayers = cursor.getCount();
        cursor.close();
        db.close();
    }

    public static void AnimLookMe(View animView){
        ObjectAnimator animRotation = ObjectAnimator.ofFloat(animView, "rotation", 0, 0, 25, 0, -25, 25, 0, -25, 0, 0);
        ObjectAnimator animTransform = ObjectAnimator.ofFloat(animView, "translationY", 0, -75, -75, -75, 0);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(animView, "scaleX", 1, 2, 2, 2, 1);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(animView, "scaleY", 1, 2, 2, 2, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animRotation, animTransform, animScaleX,  animScaleY);
        animatorSet.setDuration(1000);
        animatorSet.setStartDelay(2000);
        animatorSet.start();
    }
}
