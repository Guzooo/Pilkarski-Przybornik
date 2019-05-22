package pl.Guzooo.PilkarskiPrzybornik;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LotteryActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private int currentPlayer;
    private boolean buttonLotteryText = true;
    private ArrayList<String> titles;

    private Listener listener;

    public interface Listener{
        boolean onBackPressed();
        ArrayList<String> setTitles(Context context);
        String ClickRandom(int allPlayers);
        boolean ClickNextPlayer(int currentPlayer, int allPlayers);
        int setButtonText(int allPlayer);
        void ClickEnd(int id, Context context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);

        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        setListener(Games.getListenerLottery(id));
        HideLottery();

        titles = listener.setTitles(this);
        setTitle();
    }

    @Override
    public void onBackPressed() {
        if(listener.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void setTitle(){
        TextView textTitle = findViewById(R.id.title);
        textTitle.setText(titles.get(currentPlayer));
    }

    public void setResultOfLottery(String result){
        TextView textView = findViewById(R.id.result_of_lottery);
        textView.setText(result);
    }

    public void ClickButton(View v){
        if(buttonLotteryText) {
            setResultOfLottery(listener.ClickRandom(titles.size()));
            ShowLottery();
            setButtonText(listener.setButtonText(titles.size()));
        } else {
            if(listener.ClickNextPlayer(currentPlayer, titles.size())){
                currentPlayer++;
                setTitle();
                HideLottery();
                setButtonText(R.string.random);
            } else {
                v.setClickable(false);
                int id = getIntent().getIntExtra(EXTRA_ID, 0);
                listener.ClickEnd(id, this);
                finish();
            }
        }
        buttonLotteryText = !buttonLotteryText;
    }

    private void setButtonText(int resource){
        Button button = findViewById(R.id.button);
        button.setText(resource);
    }

    public void HideLottery(){
        TextView textView = findViewById(R.id.result_of_lottery);
        ObjectAnimator animY = ObjectAnimator.ofFloat(textView, "scaleY", 0);
        ObjectAnimator animX = ObjectAnimator.ofFloat(textView, "scaleX", 0);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animY, animX);
        animSetXY.setDuration(250);
        animSetXY.start();
    }

    public void ShowLottery(){
        TextView textView = findViewById(R.id.result_of_lottery);
        ObjectAnimator animY = ObjectAnimator.ofFloat(textView, "scaleY", 1);
        ObjectAnimator animX = ObjectAnimator.ofFloat(textView, "scaleX", 1);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animY, animX);
        animSetXY.setDuration(250);
        animSetXY.start();
    }

    private void setListener(Listener listener){
        this.listener = listener;
    }
}
