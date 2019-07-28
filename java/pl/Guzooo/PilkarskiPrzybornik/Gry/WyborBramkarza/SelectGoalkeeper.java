package pl.Guzooo.PilkarskiPrzybornik.Gry.WyborBramkarza;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Games;
import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.Gry.Settings;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.PlayersActivity;
import pl.Guzooo.PilkarskiPrzybornik.R;
import pl.Guzooo.PilkarskiPrzybornik.SettingsActivity;

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
        return context.getString(R.string.game_select_goalkeeper_description, SettingsActivity.getPreferencesFinalRandomNumber(context));
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
        if(PlayersActivity.getNumberActivePlayers(context) > 1) {
            switch (buttonId) {
                case 0:
                    Intent intent = new Intent(context, LotteryActivity.class);
                    context.startActivity(intent);
                    break;
            }
            Games.currentGame.setLastButton(buttonId);
            Games.currentGame.setLastGame();
            Games.currentGame.addNumberGame();
            Games.currentGame.update(context);
        } else {
            Toast.makeText(context, context.getString(R.string.need_more_players, 2), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Reset(Context context) {
        goalkeeper = false;
    }

    @Override
    public Settings getSettings() {
        return null;
    }

    @Override
    public View getSpecialView(Context context) {
        return null;
    }

    //LOTTERY
    @Override
    public ArrayList<String> getTitles(Context context) {
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
    public String ClickRandom(Context context) {
        int result = LotteryActivity.getRandomResult(context);
        if(result > 0){
            return getEmoticon();
        }
        goalkeeper = true;
        return context.getString(R.string.goalkeeper) + " \uD83E\uDDE4\uD83E\uDD45";
    }

    private String getEmoticon(){
        int emoticon = new Random().nextInt(15);
        switch (emoticon){
            case 0:
                return "😎";
            case 1:
                return "🏆";
            case 2:
                return "⚽";
            case 3:
                return "🐳";
            case 4:
                return "💖";
            case 5:
                return "🙃";
            case 6:
                return "😉";
            case 7:
                return "😁";
            case 8:
                return "😘";
            case 9:
                return "😍";
            case 10:
                return "👌";
            case 11:
                return "🎉";
            case 12:
                return "😇";
            case 13:
                return "😜";
        }
        return "😻";
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
}
