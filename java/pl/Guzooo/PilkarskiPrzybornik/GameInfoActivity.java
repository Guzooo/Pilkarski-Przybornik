package pl.Guzooo.PilkarskiPrzybornik;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameInfoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        ImageView image = findViewById(R.id.image);

        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        Game game = new Game();
        game.getOfId(id, this);

        title.setText(game.getName());
        description.setText(game.getDescription());
        image.setImageResource(game.getImage());
        CreateButtons(game);
    }

    private void CreateButtons(Game game) {
        LinearLayout linearLayout = findViewById(R.id.buttons_box);
        for (int i = 0; i < game.getButtonsName().size(); i++) {
            Button button = new Button(this);
            button.setText(game.getButtonsName().get(game.getButtonsOrder().get(i)));
            button.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorSecondary), PorterDuff.Mode.MULTIPLY);
            button.setId(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Games.currentGame.getGameInfo().Play(view.getId(), GameInfoActivity.this);
                }
            });
            linearLayout.addView(button);
        }
    }
}
