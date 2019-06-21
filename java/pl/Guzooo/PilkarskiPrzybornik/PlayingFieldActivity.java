package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlayingFieldActivity extends AppCompatActivity {

    private final String PREFERENCE_BUTTONS_GATE_VISIBILITY = "buttonsgatevisibility";

    private final Boolean DEFAULT_BUTTONS_GATE_VISIBILITY = true;

    private boolean win;

    private Listener listener;

    public interface Listener{
        boolean onBackPressed();
        String getGoalkeeper();
        String getShooter();
        void Crossbar(Context context);
        void Stake(Context context);
        void BadShot(Context context);
        void Mishit(Context context);
        boolean Gol(Context context);
        String getPlayersSegregatedByLives(Context context);
        String getPlayersSegregatedByOrder(Context context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_field);

        setListener((Listener) Games.currentGame.getGameInfo());

        RefreshInfo();
        SetButtonsGateVisible();
    }

    @Override
    public void onBackPressed() {
        if(listener.onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void RefreshInfo(){
        setGoalkeeper();
        setShooter();
        setLife();
        setOrder();
    }

    private void SetButtonsGateVisible(){
        boolean visible = getPreferences(MODE_PRIVATE).getBoolean(PREFERENCE_BUTTONS_GATE_VISIBILITY, DEFAULT_BUTTONS_GATE_VISIBILITY);
        if(!visible){
            ClickHideButtonsGate(null);
        }
    }

    private void setGoalkeeper(){
        TextView textView = findViewById(R.id.goalkeeper);
        textView.setText(getString(R.string.goalkeeper, listener.getGoalkeeper()));
    }

    private void setShooter(){
        TextView textView = findViewById(R.id.shooter);
        if(!win)
            textView.setText(getString(R.string.shooter, listener.getShooter()));
        else
            textView.setText(getString(R.string.winner, listener.getShooter()));
    }

    public void ClickShowButtonsGate(View v){
        findViewById(R.id.buttons_gate).setVisibility(View.VISIBLE);
        findViewById(R.id.buttons_gate_show).setVisibility(View.GONE);
        SaveButtonsGateVisible(true);
    }

    public void ClickHideButtonsGate(View v){
        findViewById(R.id.buttons_gate).setVisibility(View.GONE);
        findViewById(R.id.buttons_gate_show).setVisibility(View.VISIBLE);
        SaveButtonsGateVisible(false);
    }

    public void ClickCrossbar(View v){
        listener.Crossbar(this);
        RefreshInfo();
    }

    public void ClickStake(View v){
        listener.Stake(this);
        RefreshInfo();
    }

    public void ClickBadShot(View v){
        listener.BadShot(this);
        RefreshInfo();
    }

    public void ClickMishit(View v){
        listener.Mishit(this);
        RefreshInfo();
    }

    public void ClickGoal(View v){
        win = listener.Gol(this);
        RefreshInfo();
        if(win){
            findViewById(R.id.buttons_gate).setVisibility(View.GONE);
            findViewById(R.id.buttons).setVisibility(View.GONE);
            findViewById(R.id.goalkeeper).setVisibility(View.GONE);
            findViewById(R.id.scroll_view).setVisibility(View.GONE);
        }
    }

    private void setLife(){
        TextView textView = findViewById(R.id.life);
        textView.setText(listener.getPlayersSegregatedByLives(this));
    }

    private void setOrder(){
        TextView textView = findViewById(R.id.order);
        textView.setText(listener.getPlayersSegregatedByOrder(this));
    }

    private void setListener(Listener listener){
        this.listener = listener;
    }

    private void SaveButtonsGateVisible(boolean visible){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean(PREFERENCE_BUTTONS_GATE_VISIBILITY, visible);
        editor.apply();
    }
}
