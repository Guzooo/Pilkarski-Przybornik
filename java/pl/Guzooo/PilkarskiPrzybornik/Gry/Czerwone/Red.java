package pl.Guzooo.PilkarskiPrzybornik.Gry.Czerwone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.Gry.Settings;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.PlayersActivity;
import pl.Guzooo.PilkarskiPrzybornik.R;
import pl.Guzooo.PilkarskiPrzybornik.SettingsActivity;

public class Red extends GameInfo implements LotteryActivity.Listener{

    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<Integer> order = new ArrayList<>();
    private String goalkeeper = "";
    private ArrayList<ArrayList<String>> teams = new ArrayList<>();

    @Override
    public String getName(Context context) {
        return context.getString(R.string.game_red);
    }

    @Override
    public int getIcon(Context context) {
        return R.drawable.goalkeeper_and_teams;
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.game_red_description, SettingsActivity.getPreferencesFinalRandomNumber(context));
    }

    @Override
    public String getShortDescription(Context context) {
        return context.getString(R.string.game_red_short_description);
    }

    @Override
    public ArrayList<String> getButtons(Context context) {
        ArrayList<String> buttons = new ArrayList<>();
        buttons.add(context.getString(R.string.random_teams));
        return buttons;
    }

    @Override
    public void Play(int buttonId, final Context context) {
        if(PlayersActivity.getNumberActivePlayers(context) > 4){
            switch (buttonId){
                case 0:
                    int teamNumber = (PlayersActivity.getNumberActivePlayers(context) - 1) / 2;
                    CharSequence[] items = new CharSequence[teamNumber-1];
                    for(int i = 2; i <= teamNumber; i++){
                        items[i-2] = i+"";
                    }
                    /*if(teamNumber == 2){ TODO: dwie drużyny to goł odrazu
                        for(int j = 0; j < 2; j++){
                            teams.add(new ArrayList<String>());
                        }
                        Intent intent = new Intent(context, LotteryActivity.class);
                        context.startActivity(intent);
                    }*/
                    new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                            .setTitle(R.string.set_number_team)
                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    for(int j = 0; j < i+2; j++){
                                        teams.add(new ArrayList<String>());
                                    }
                                    Intent intent = new Intent(context, LotteryActivity.class);
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.need_more_players, 5), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Reset(Context context) {
        players.clear();
        order.clear();
        goalkeeper = "";
        teams.clear();
    }

    @Override
    public Settings getSettings() {
        return null;
    }

    @Override
    public boolean getSpecialView() {
        return false;
    }

    //LOTTERY

    @Override
    public ArrayList<String> getTitles(Context context) {
        if(players.size() == 0) {
            SQLiteDatabase db = Database.getRead(context);
            Cursor cursor = db.query(Player.databaseName,
                    Player.onCursor,
                    "ACTIVE = ?",
                    new String[]{Integer.toString(1)},
                    null, null,
                    "NAME");
            if (cursor.moveToFirst()) {
                do {
                    Player player = new Player();
                    player.getOfCursor(cursor, context);
                    players.add(player.getName());
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return players;
    }

    @Override
    public String ClickRandom(Context context) {
        int result = LotteryActivity.getRandomResult(context);
        for(int i = 0; i < order.size(); i++){
            if(result == order.get(i)){
                return ClickRandom(context);
            }
        }
        order.add(result);
        return result+1 + "";
    }

    @Override
    public boolean ClickNextPlayer(int currentPlayer, int allPlayers) {
        if(currentPlayer+1 < allPlayers)
            return true;
        return false;
    }

    @Override
    public int setButtonText(int allPlayer) {
        if(allPlayer == order.size())
            return R.string.see_teams;
        return R.string.next_player;
    }

    @Override
    public void ClickEnd(Context context) {
        SegregatePlayersByOrder(context);
        AddPlayersToTeams();
        DisplayTeams(context);
    }

    private void SegregatePlayersByOrder(Context context){
        ArrayList<String> playersNoOrder = new ArrayList<>();
        playersNoOrder.addAll(players);
        players.removeAll(players);
        for(int i = 0; i < SettingsActivity.getPreferencesFinalRandomNumber(context); i++){
            for(int j = 0; j < playersNoOrder.size(); j++){
                if(order.get(j) == i){
                    if(goalkeeper.equals(""))
                        goalkeeper = playersNoOrder.get(j);
                    else
                        players.add(playersNoOrder.get(j));
                    break;
                }
            }
        }
    }

    private void AddPlayersToTeams(){
        int j = 0;
        for(int i = 0; i < players.size(); i++){
            teams.get(j).add(players.get(i));
            j++;
            if(j >= teams.size())
                j = 0;
        }
    }

    private void DisplayTeams(Context context){
        String message = goalkeeper + " \uD83E\uDDE4\uD83E\uDD45\n";
        for(int i = 0; i < teams.size(); i++){
            message += "\n" + context.getString(R.string.team, i) + "\n";
            for(int j = 0; j < teams.get(i).size(); j++){
                message += teams.get(i).get(j) + "\n";
            }
        }

        new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
