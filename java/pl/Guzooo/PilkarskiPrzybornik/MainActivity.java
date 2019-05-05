package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView descriptionManagePlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, GameInfoActivity.class);
        startActivity(intent);

        descriptionManagePlayers = findViewById(R.id.description_manage_players);
        setNumberActivePlayers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setNumberActivePlayers();
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
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count == 0){
            descriptionManagePlayers.setText(R.string.manage_players);
        } else {
            descriptionManagePlayers.setText(getString(R.string.number_active_players, count));
        }
    }
}
