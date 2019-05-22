package pl.Guzooo.PilkarskiPrzybornik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    }

    public void ClickPlay(View v){ //TODO: listener żeby dało się więcej przycisków w zależności od gry
        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        Games.currentGame.getListener().ClickPlay(id,this);
    }
}
