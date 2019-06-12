package pl.Guzooo.PilkarskiPrzybornik.Gry.SelectGoalkeeper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Game;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.R;

public class SelectGoalkeeper implements LotteryActivity.Listener {

    private static boolean goalkeeper;

    /*//GENERAL
    @Override
    public void ClickPlay(int id, Context context) {
        Log.d("tu jest", "bramkarz " + goalkeeper);
        Intent intent = new Intent(context, LotteryActivity.class);
        intent.putExtra(LotteryActivity.EXTRA_ID, id);
        context.startActivity(intent);
    }*/

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
    public void ClickEnd(int id, Context context) {
        goalkeeper = false;
    }

    @Override
    public boolean onBackPressed() {
        goalkeeper = false;
        return true;
    }
}
