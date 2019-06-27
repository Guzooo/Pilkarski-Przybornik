package pl.Guzooo.PilkarskiPrzybornik;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.Guzooo.PilkarskiPrzybornik.Gry.Settings;

public class GameInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Game game;

    private TextView title;
    private TextView description;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        Initialization();
        SetInfoGame();
        CreateButtons();

        IfSetting();
    }

    private void CreateButtons() {
        LinearLayout linearLayout = findViewById(R.id.buttons_box);
        for (int i = 0; i < game.getButtonsName(this).size(); i++) {
            Button button = new Button(this);
            button.setText(game.getButtonsName(this).get(game.getButtonsOrder().get(i)));
            button.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorSecondary), PorterDuff.Mode.MULTIPLY);
            button.setId(i);
            button.setOnClickListener(this);
            linearLayout.addView(button);
        }
    }

    private void Initialization(){
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        image = findViewById(R.id.image);
        game = Games.currentGame;
    }

    private void SetInfoGame(){
        title.setText(game.getName(this));
        description.setText(game.getDescription(this));
        image.setImageResource(game.getImage(this));
    }

    private void IfSetting(){
        if(game.getSettings() == null) {
            View button = findViewById(R.id.setting);
            button.setVisibility(View.GONE);
        } else {
            game.getSettings().setDialogListener(new Settings.DialogListener() {
                @Override
                public void Dismiss() {
                    SetInfoGame();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        Games.currentGame.getGameInfo().Reset(this);
        Games.currentGame.getGameInfo().Play(view.getId(), this);
        Games.currentGame.update(this);
    }

    public void ClickSettings(View view){
        Games.currentGame.getSettings().show(getSupportFragmentManager(), "settings");
    }
}
