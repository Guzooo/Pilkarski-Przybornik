package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView descriptionManagePlayers;
    int activePlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        descriptionManagePlayers = findViewById(R.id.description_manage_players);
        setNumberActivePlayers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setNumberActivePlayers();
    }

    public void ClickCrow(View v){
        if(activePlayers > 1) {
            Intent intent = new Intent(this, GameInfoActivity.class);
            intent.putExtra(GameInfoActivity.EXTRA_ID, 0);
            startActivity(intent);
        } else {
            Toast.makeText(this, "MIN 2 PLAYERS", Toast.LENGTH_SHORT).show(); //TODO:string
        }
    }

    public void ClickPlayersControl(View v){
        Intent intent = new Intent(this, PlayersActivity.class);
        startActivity(intent);
    }

    private void setNumberActivePlayers(){
        SQLiteDatabase db = Database.getRead(this);
        Cursor cursor = db.query(Player.databaseName,
                null,
                "ACTIVE = ?",
                new String[]{Integer.toString(1)},
                null, null, null);
        activePlayers = cursor.getCount();
        cursor.close();
        db.close();

        if (activePlayers == 0){
            descriptionManagePlayers.setText(R.string.manage_players);
        } else {
            descriptionManagePlayers.setText(getString(R.string.number_active_players, activePlayers));
        }
    }
}
