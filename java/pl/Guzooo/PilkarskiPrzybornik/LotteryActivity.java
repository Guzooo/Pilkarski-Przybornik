package pl.Guzooo.PilkarskiPrzybornik;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class LotteryActivity extends AppCompatActivity {

    private final String BUNDLE_CURRENT_PLAYER = "currentplayer";
    private final String BUNDLE_BUTTON_LOTTERY_TEXT = "buttonlotterytext";
    private final String BUNDLE_RESULT = "result";

    private int currentPlayer;
    private boolean buttonLotteryText = true;
    private ArrayList<String> titles;

    private Listener listener;

    public interface Listener{
        ArrayList<String> getTitles(Context context);
        String ClickRandom(Context context);
        boolean ClickNextPlayer(int currentPlayer, int allPlayers);
        int setButtonText(int allPlayer);
        void ClickEnd(Context context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);

        setListener((Listener) Games.currentGame.getGameInfo());
        onLoadInstanceState(savedInstanceState);

        titles = listener.getTitles(this);
        setTitle();

        if(buttonLotteryText) {
            HideLottery();
        } else {
            setResultOfLottery(savedInstanceState.getString(BUNDLE_RESULT));
            setButtonText(listener.setButtonText(titles.size()));
        }
    }

    private void onLoadInstanceState(Bundle save){
        if(save != null){
            currentPlayer = save.getInt(BUNDLE_CURRENT_PLAYER);
            buttonLotteryText = save.getBoolean(BUNDLE_BUTTON_LOTTERY_TEXT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_PLAYER, currentPlayer);
        outState.putBoolean(BUNDLE_BUTTON_LOTTERY_TEXT, buttonLotteryText);
        outState.putString(BUNDLE_RESULT, ((TextView) findViewById(R.id.result_of_lottery)).getText().toString());
    }

    @Override
    public void onBackPressed() {
        if(UtilsForActivity.DoubleTab())
            super.onBackPressed();
        else
            UtilsForActivity.ToastDoubleTabExit(this);
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
            setResultOfLottery(listener.ClickRandom(this));
            ShowLottery();
            setButtonText(listener.setButtonText(titles.size()));
        } else {
            if(listener.ClickNextPlayer(currentPlayer, titles.size())){
                addCurrentPlayer();
                setTitle();
                HideLottery();
                setButtonText(R.string.random);
            } else {
                v.setClickable(false);
                listener.ClickEnd(this);
                finish();
            }
        }
        buttonLotteryText = !buttonLotteryText;
    }

    public static int getRandomResult(Context context){
        return new Random().nextInt(SettingsActivity.getPreferencesFinalRandomNumber(context));
    }

    private void setButtonText(int resource){
        Button button = findViewById(R.id.button);
        button.setText(resource);
    }

    private void addCurrentPlayer(){
        currentPlayer++;
        if(currentPlayer == titles.size()){
            currentPlayer = 0;
        }
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
