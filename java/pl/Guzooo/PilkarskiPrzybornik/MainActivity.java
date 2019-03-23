package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void ClickPlayersControl(View v){
        Intent intent = new Intent(this, PlayersActivity.class);
        startActivity(intent);
    }
}
