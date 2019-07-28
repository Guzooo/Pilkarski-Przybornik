package pl.Guzooo.PilkarskiPrzybornik;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class PlayingFieldActivity extends AppCompatActivity {

    private final String PREFERENCE_BUTTONS_GATE_VISIBILITY = "buttonsgatevisibility";
    private final String BUNDLE_WIN = "win";

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
        onLoadInstanceState(savedInstanceState);

        RefreshInfo();
        SetButtonsGateVisibleOnStart();
    }

    private void onLoadInstanceState(Bundle save){
        if(save != null)
            win = save.getBoolean(BUNDLE_WIN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_WIN, win);
    }

    @Override
    public void onBackPressed() {
       if(UtilsForActivity.DoubleTab()) {
           super.onBackPressed();
       } else {
            UtilsForActivity.ToastDoubleTabExit(this);
       }
    }

    private void RefreshInfo() {
        if (win)
            LayoutForWinner();
        setGoalkeeper();
        setShooter();
        setLife();
        setOrder();
    }

    private void SetButtonsGateVisibleOnStart(){
        boolean visible = getPreferences(MODE_PRIVATE).getBoolean(PREFERENCE_BUTTONS_GATE_VISIBILITY, DEFAULT_BUTTONS_GATE_VISIBILITY);
        if(!visible){
            ClickHideButtonsGate(null);
        }
    }

    private void setGoalkeeper(){
        TextView textView = findViewById(R.id.goalkeeper);
        textView.setText(getString(R.string.goalkeeper_emoticon, listener.getGoalkeeper()));
    }

    private void setShooter(){
        TextView textView = findViewById(R.id.shooter);
        if(!win)
            textView.setText(getString(R.string.shooter_emoticon, listener.getShooter()));
        else
            textView.setText(getString(R.string.winner_emoticon, listener.getShooter()));
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
        ShowAction();
        listener.Crossbar(this);
        RefreshInfo();
    }

    public void ClickStake(View v){
        ShowAction();
        listener.Stake(this);
        RefreshInfo();
    }

    public void ClickBadShot(View v){
        ShowAction();
        listener.BadShot(this);
        RefreshInfo();
    }

    public void ClickMishit(View v){
        ShowAction();
        listener.Mishit(this);
        RefreshInfo();
    }

    public void ClickGoal(View v){
        ShowAction();
        win = listener.Gol(this);
        RefreshInfo();
    }

    public void ClickAction(View v) {
        if (win)
            finish();
        else
            HideAction();
    }

    private void LayoutForWinner(){
        Button action = findViewById(R.id.action);
        action.setText(R.string.back_to_menu);
        findViewById(R.id.goalkeeper).setVisibility(View.GONE);
        findViewById(R.id.scroll_view).setVisibility(View.GONE);
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

    private void ShowAction(){
        View actionBox = findViewById(R.id.action_box);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int x = actionBox.getWidth()/2;
            int y = actionBox.getHeight()/2;
            float radius = (float) Math.hypot(x, y);
            ViewAnimationUtils.createCircularReveal(actionBox, x, y, 0, radius).start();
        }
        actionBox.setVisibility(View.VISIBLE);
    }

    private void HideAction() {
        final View actionBox = findViewById(R.id.action_box);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int x = actionBox.getWidth() / 2;
            int y = actionBox.getHeight() / 2;
            float radius = (float) Math.hypot(x, y);
            Animator anim = ViewAnimationUtils.createCircularReveal(actionBox, x, y, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    actionBox.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            actionBox.setVisibility(View.INVISIBLE);
        }

    }
}
