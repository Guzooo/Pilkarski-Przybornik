package pl.Guzooo.PilkarskiPrzybornik.Gry.Krol;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Games;
import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.PlayingFieldActivity;
import pl.Guzooo.PilkarskiPrzybornik.R;

public class King extends GameInfo implements LotteryActivity.Listener, PlayingFieldActivity.Listener {

    private static int shooter;
    private static int goalkeeper;

    private static boolean firstDeath;

    private static final int MAX_STAKE = 3;
    private static int currentStake;

    private static ArrayList<Integer> order = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<Player> playersOrganic = new ArrayList<>();
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
        return context.getString(R.string.game_king_description, getNumberActivePlayers(context), getSettings().getLive(context), getSettings().getStake(context));
    }

    @Override
    public String getShortDescription(Context context) {
        return context.getString(R.string.game_king_short_description);
    }

    @Override
    public ArrayList<String> getButtons(Context context) {
        ArrayList<String> buttons = new ArrayList<>();
        buttons.add(context.getString(R.string.normal_game));
        buttons.add("NEW MOOD");
        buttons.add("NEW MOOD");
        return buttons;
    }

    @Override
    public void Play(int buttonId, Context context) {
        Reset();
        switch (buttonId){
            case 0:
                if(getNumberActivePlayers(context) > 1) {
                    Intent intent = new Intent(context, LotteryActivity.class);
                    context.startActivity(intent);
                    Games.currentGame.setLastButton(buttonId);
                } else {
                    Toast.makeText(context, "MIN 2 PLAYERS", Toast.LENGTH_LONG).show(); //TODO: STRINg
                }
                break;
            case 1:
                Toast.makeText(context, "COMING SOON", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(context, "COMING SOON", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void Reset() {
        goalkeeper = -1;
        shooter = -1;

        firstDeath = false;

        currentStake = MAX_STAKE;

        order.clear();
        players.clear();
        lives.clear();
    }

    @Override
    public KingSettings getSettings() {
        return new KingSettings();
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
                players.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return titles;
    }

    @Override
    public String ClickRandom(int allPlayers) {
        int result = new Random().nextInt(allPlayers);
        for(int i = 0; i < order.size(); i++){
            if(result == order.get(i)){
                return ClickRandom(allPlayers);
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
        ArrayList<Player> playersNoOrder = new ArrayList<>();
        playersNoOrder.addAll(players);
        players.removeAll(players);
        for(int i = 0; i < order.size(); i++){
            for(int j = 0; j < playersNoOrder.size(); j++){
                if(order.get(j) == i){
                    players.add(playersNoOrder.get(j));
                    lives.add(getSettings().getLive(context));
                    break;
                }
            }
        }
        playersOrganic.addAll(players);

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
    public void Crossbar() {
        NormalStake();
    }

    @Override
    public void Stake() {
        NormalStake();
    }

    @Override
    public void BadShot() {
        players.get(shooter).addShots();
        goalkeeper = shooter;
        setShooter();
    }

    @Override
    public void Mishit() {
        players.get(shooter).addShots();
        players.get(shooter).addGoodShots();
        players.get(goalkeeper).addDefendedGoal();
        goalkeeper = shooter;
        setShooter();
    }

    @Override
    public boolean Gol(Context context) {
        players.get(shooter).addShots();
        players.get(shooter).addGoal();
        players.get(goalkeeper).addUndefendedGoal();
        lives.set(goalkeeper, lives.get(goalkeeper) -1);
        if(lives.get(goalkeeper) == 0){
            Toast.makeText(context, context.getString(R.string.player_elimination, players.get(goalkeeper).getName()), Toast.LENGTH_SHORT).show();//TODO: stringi
            if(!firstDeath){
                players.get(goalkeeper).addLostGameOfKing();
                firstDeath = true;
            }
            if(numberAlivePlayers() == 1){
                players.get(shooter).addWinGameOfKing();
                Toast.makeText(context, context.getString(R.string.player_win, players.get(shooter).getName()), Toast.LENGTH_LONG).show();//TODO: stringi
                for(Player player : players){
                    player.addGameOfKing();
                    player.update(context);
                }
                return true;
            }
            goalkeeper = shooter;
        }
        setShooter();
        return false;
    }

    private void setShooter(){
        currentStake = MAX_STAKE;
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

    private int numberAlivePlayers(){
        int players = 0;
        for(int i : lives){
            if(i > 0){
                players++;
            }
        }
        return players;
    }

    private void NormalStake(){
        currentStake--;
        if(currentStake == 0){
            goalkeeper = shooter;
            setShooter();
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
