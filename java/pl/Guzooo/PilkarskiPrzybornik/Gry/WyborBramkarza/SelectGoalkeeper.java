package pl.Guzooo.PilkarskiPrzybornik.Gry.WyborBramkarza;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.Gry.Settings;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.R;

public class SelectGoalkeeper extends GameInfo implements LotteryActivity.Listener {

    private static boolean goalkeeper;

    @Override
    public String getName(Context context) {
        return context.getString(R.string.game_select_goalkeeper);
    }

    @Override
    public int getIcon(Context context) {
        return R.drawable.goalkeeper;
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.game_select_goalkeeper_description);
    }

    @Override
    public String getShortDescription(Context context) {
        return context.getString(R.string.game_select_goalkeeper_short_description);
    }

    @Override
    public ArrayList<String> getButtons(Context context) {
        ArrayList<String> buttons = new ArrayList<>();
        buttons.add(context.getString(R.string.random_goalkeeper));
        return buttons;
    }

    @Override
    public void Play(int buttonId, Context context) {
        Reset();
        if(getNumberActivePlayers(context) > 1) {
            Intent intent = new Intent(context, LotteryActivity.class);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "MIN 2 PLAYERS", Toast.LENGTH_LONG).show(); //TODO: STRINg
        }
    }

    @Override
    public void Reset() {
        goalkeeper = false;
    }

    @Override
    public Settings getSettings() {
        return null;
    }

    //LOTTERY
    @Override
    public ArrayList<String> setTitles(Context context) {
        SQLiteDatabase db = Database.getWrite(context);
        Cursor cursor = db.query(Player.databaseName,
                Player.onCursor,
                "ACTIVE = ?",
                new String[]{Integer.toString(1)},
                null, null,
                "NAME");
        ArrayList<String> titles = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                Player player = new Player();
                player.getOfCursor(cursor, context);
                titles.add(player.getName());
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return titles;
    }

    @Override
    public String ClickRandom(int allPlayers) {
        int result = new Random().nextInt(allPlayers);
        if(result > 0){
            return "0";
        }
        goalkeeper = true;
        return "1";
    }

    @Override
    public boolean ClickNextPlayer(int currentPlayer, int allPlayers) {
        if(!goalkeeper){
            return true;
        }
        return false;
    }

    @Override
    public int setButtonText(int allPlayer) {
        if(goalkeeper)
            return R.string.back_to_menu;
        return R.string.next_player;
    }

    @Override
    public void ClickEnd(Context context) {

    }

    @Override
    public boolean onBackPressed() {
        goalkeeper = false;
        return true;
    }
}
