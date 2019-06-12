package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlayingFieldActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private Listener listener;

    public interface Listener{
        boolean onBackPressed();
        String getGoalkeeper();
        String getShooter();
        void BadShot();
        void Mishit();
        void Gol(Context context);
        String getPlayersSegregatedByLives();
        String getPlayersSegregatedByOrder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_field);

        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        //setListener(Games.getListenerPlayingField(id));

        RefreshInfo();
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
        setHeart();
        setOrder();
    }

    private void setGoalkeeper(){
        TextView textView = findViewById(R.id.goalkeeper);
        textView.setText(getString(R.string.goalkeeper, listener.getGoalkeeper()));
    }

    private void setShooter(){
        TextView textView = findViewById(R.id.shooter);
        textView.setText(getString(R.string.shooter, listener.getShooter()));
    }

    public void ClickBadShot(View v){
        listener.BadShot();
        RefreshInfo();
    }

    public void ClickMishit(View v){
        listener.Mishit();
        RefreshInfo();
    }

    public void ClickGoal(View v){
        listener.Gol(this);
        RefreshInfo();
    }

    private void setHeart(){
        TextView textView = findViewById(R.id.heart);
        textView.setText(listener.getPlayersSegregatedByLives());
    }

    private void setOrder(){
        TextView textView = findViewById(R.id.order);
        textView.setText(listener.getPlayersSegregatedByOrder());
    }

    private void setListener(Listener listener){
        this.listener = listener;
    }
}
