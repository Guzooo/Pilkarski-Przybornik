package pl.Guzooo.PilkarskiPrzybornik.Gry.Krol;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Games;
import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.MainActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.PlayersActivity;
import pl.Guzooo.PilkarskiPrzybornik.PlayingFieldActivity;
import pl.Guzooo.PilkarskiPrzybornik.R;
import pl.Guzooo.PilkarskiPrzybornik.SettingsActivity;

public class King extends GameInfo implements LotteryActivity.Listener, PlayingFieldActivity.Listener {

    private static int shooter;
    private static int goalkeeper;

    private static boolean firstDeath;

    private static int currentStake;

    private static ArrayList<Integer> order = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<Integer> lives = new ArrayList<>();

    //GENERAL
    @Override
    public String getName(Context context) {
        return context.getString(R.string.game_king);
    }

    @Override
    public int getIcon(Context context) {
        return R.drawable.crown;
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.game_king_description, SettingsActivity.getPreferencesFinalRandomNumber(context), getSettings().getLive(context), getSettings().getStake(context));
    }

    @Override
    public String getShortDescription(Context context) {
        return context.getString(R.string.game_king_short_description);
    }

    @Override
    public ArrayList<String> getButtons(Context context) {
        ArrayList<String> buttons = new ArrayList<>();
        buttons.add(context.getString(R.string.normal_mode));
        //buttons.add(context.getString(R.string.randomizing_mode));
        return buttons;
    }

    @Override
    public void Play(int buttonId, Context context) {
        if (PlayersActivity.getNumberActivePlayers(context) > 1) {
            switch (buttonId) {
                case 0:
                    Intent intent = new Intent(context, LotteryActivity.class);
                    context.startActivity(intent);
                    break;
            }
            Games.currentGame.setLastButton(buttonId);
        } else {
            Toast.makeText(context, context.getString(R.string.need_more_players, 2), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Reset(Context context) {
        goalkeeper = -1;
        shooter = -1;

        firstDeath = false;

        currentStake = getSettings().getStake(context);

        order.clear();
        players.clear();
        lives.clear();
    }

    @Override
    public KingSettings getSettings() {
        return new KingSettings();
    }

    @Override
    public boolean getSpecialView() {
        if (numberAlivePlayers() > 1)
            return true;
       return false;
    }

    //LOTTERY
    @Override
    public ArrayList<String> getTitles(Context context) {
        ArrayList<String> titles = new ArrayList<>();
        if(players.size() == 0) {
            SQLiteDatabase db = Database.getWrite(context);
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
                    titles.add(player.getName());
                    players.add(player);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } else {
            for(Player player : players){
                titles.add(player.getName());
            }
        }
        return titles;
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
        return result+1 +"";
    }

    @Override
    public boolean ClickNextPlayer(int currentPlayer, int allPlayers) {
        if(currentPlayer+1 < allPlayers)
            return true;
        else
            return false;
    }

    @Override
    public int setButtonText(int allPlayer) {
        if(allPlayer == order.size())
            return R.string.start_game;
        return R.string.next_player;
    }

    @Override
    public void ClickEnd(Context context) {
        SegregatePlayersByOrder(context);
        SetPlayersLife(context);
        Games.currentGame.setLastGame();
        Games.currentGame.addNumberGame();
        Games.currentGame.update(context);
        Intent intent = new Intent(context, PlayingFieldActivity.class);
        context.startActivity(intent);
    }

    //TO IDZIE NA DWA NA RAZIE // odydwie metody 100% pewne
    @Override
    public boolean onBackPressed() {
//TODO: wyskakujące oknoooooooooo z pytaniem czy wychodzimy czy zostajemy
        return true;
    }

    //PLAYING FIELD
    @Override
    public String getGoalkeeper() {
        if(goalkeeper == -1)
            goalkeeper = players.size()-1;
        return players.get(goalkeeper).getName();
    }

    @Override
    public String getShooter() {
        if(shooter == -1)
            shooter = 0;
        return players.get(shooter).getName();
    }

    @Override
    public void Crossbar(Context context) {
        NormalStake(context);
    }

    @Override
    public void Stake(Context context) {
        NormalStake(context);
    }

    @Override
    public void BadShot(Context context) {
        players.get(shooter).addShots();
        goalkeeper = shooter;
        setShooter(context);
    }

    @Override
    public void Mishit(Context context) {
        players.get(shooter).addShots();
        players.get(shooter).addGoodShots();
        players.get(goalkeeper).addDefendedGoal();
        goalkeeper = shooter;
        setShooter(context);
    }

    @Override
    public boolean Gol(Context context) {
        players.get(shooter).addShots();
        players.get(shooter).addGoal();
        players.get(goalkeeper).addUndefendedGoal();
        lives.set(goalkeeper, lives.get(goalkeeper) -1);
        if(lives.get(goalkeeper) == 0 && GoalkeeperLiquidated(context))
            return true;
        setShooter(context);
        return false;
    }

    private boolean GoalkeeperLiquidated(Context context){
        if(!firstDeath){
            FirstDeath();
        }
        if(numberAlivePlayers() == 1){
            players.get(shooter).addWinGameOfKing();
            SavePlayers(context);
            return true;
        }
        Toast.makeText(context, context.getString(R.string.player_elimination, players.get(goalkeeper).getName()), Toast.LENGTH_SHORT).show();
        goalkeeper = shooter;
        return false;
    }

    private void FirstDeath(){
        players.get(goalkeeper).addLostGameOfKing();
        firstDeath = true;
    }

    private void SavePlayers(Context context){
        for(Player player : players){
            player.addGameOfKing();
            player.update(context);
        }
    }

    private void setShooter(Context context){
        currentStake = getSettings().getStake(context);
        for(int i = shooter + 1; i < players.size(); i++){
            if(i != goalkeeper && lives.get(i) > 0){
                shooter = i;
                return;
            }
        }
        for (int i = 0; i < shooter; i++){
            if(i != goalkeeper && lives.get(i) > 0){
                shooter = i;
                return;
            }
        }
    }

    private void SegregatePlayersByOrder(Context context){
        ArrayList<Player> playersNoOrder = new ArrayList<>();
        playersNoOrder.addAll(players);
        players.removeAll(players);
        for(int i = 0; i < SettingsActivity.getPreferencesFinalRandomNumber(context); i++){
            for(int j = 0; j < playersNoOrder.size(); j++){
                if(order.get(j) == i){
                    players.add(playersNoOrder.get(j));
                    break;
                }
            }
        }
    }

    private void SetPlayersLife(Context context){
        for(Player player : players)
            lives.add(getSettings().getLive(context));
    }

    private int numberAlivePlayers(){
        int players = 0;
        for(int i : lives){
            if(i > 0){
                players++;
            }
        }
        return players;
    }

    private void NormalStake(Context context){
        currentStake--;
        if(currentStake == 0){
            goalkeeper = shooter;
            setShooter(context);
        }
    }

    @Override
    public String getPlayersSegregatedByLives(Context context) {
        String string = context.getString(R.string.life);
        for(int i = getSettings().getLive(context); i > 0; i--){
            for(int j = 0; j < players.size(); j++){
                if(lives.get(j) == i){
                    string += "\n" + players.get(j).getName() + " " + lives.get(j);
                }
            }
        }
        return string;
    }

    @Override
    public String getPlayersSegregatedByOrder(Context context) {
        String string = context.getString(R.string.order);
        for(int i = shooter; i < players.size(); i++) {
            if (lives.get(i) > 0) {
                string += "\n" + players.get(i).getName() + MarkOfOrder(i);
            }
        }
        for(int i = 0; i < shooter; i++) {
            if (lives.get(i) > 0) {
                string += "\n" + players.get(i).getName() + MarkOfOrder(i);
            }
        }
        return string;
    }

    private String MarkOfOrder(int player){
        if(player == shooter)
            return " ⚽";
        else if (player == goalkeeper)
            return " 🥅";
        else
            return " ⬆";
    }
}
