package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private Listener listener;

    public interface Listener{
        String getResult(Context context);
    }

    private void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setListener((Listener) Games.currentGame.getGameInfo());

        SetText();
    }

    @Override
    public void onBackPressed() {
        if(UtilsForActivity.DoubleTab()){
            super.onBackPressed();
        } else {
            UtilsForActivity.ToastDoubleTabExit(this);
        }
    }

    public void ClickBackToMenu(View v){
        finish();
    }

    private void SetText(){
        TextView textView = findViewById(R.id.text);
        textView.setText(listener.getResult(this));
    }
}
