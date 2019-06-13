package pl.Guzooo.PilkarskiPrzybornik.Gry.Krol;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.LotteryActivity;
import pl.Guzooo.PilkarskiPrzybornik.Player;
import pl.Guzooo.PilkarskiPrzybornik.PlayingFieldActivity;
import pl.Guzooo.PilkarskiPrzybornik.R;

public class King extends GameInfo implements LotteryActivity.Listener, PlayingFieldActivity.Listener {

    private static int shooter = -1;
    private static int goalkeeper = -1;

    private static boolean firstDeath;

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
        return context.getString(R.string.game_king_description); //TODO: zmienne
    }

    @Override
    public ArrayList<String> getButtons(Context context) {
        ArrayList<String> buttons = new ArrayList<>();
        buttons.add(context.getString(R.string.play));
        buttons.add("dwa");
        buttons.add("trzy");;
        buttons.add("elo bencz");
        return buttons;
    }

    @Override
    public void restartGame() {


    }
   /*
    @Override
    public void ClickPlay(int id, Context context) {
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
    public void ClickEnd(int id, Context context) {
        ArrayList<Player> playersNoOrder = new ArrayList<>();
        playersNoOrder.addAll(players);
        players.removeAll(players);
        for(int i = 0; i < order.size(); i++){
            for(int j = 0; j < playersNoOrder.size(); j++){
                if(order.get(j) == i){
                    players.add(playersNoOrder.get(j));
                    lives.add(5); //TODO:liczba żyć
                    break;
                }
            }
        }
        playersOrganic.addAll(players);

        Intent intent = new Intent(context, PlayingFieldActivity.class);
        intent.putExtra(PlayingFieldActivity.EXTRA_ID, id);
        context.startActivity(intent);
    }

    //TO IDZIE NA DWA NA RAZIE
    @Override
    public boolean onBackPressed() {
        goalkeeper = -1;
        shooter = -1;

        firstDeath = false;

        order.clear();
        players.clear();
        lives.clear();
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
    public void Gol(Context context) {
        players.get(shooter).addShots();
        players.get(shooter).addGoal();
        players.get(goalkeeper).addUndefendedGoal();
        lives.set(goalkeeper, lives.get(goalkeeper) -1);
        if(lives.get(goalkeeper) == 0){
            Toast.makeText(context, players.get(goalkeeper).getName() + " został skończony 🥅⚽", Toast.LENGTH_SHORT).show();//TODO: stringi
            if(!firstDeath){
                players.get(goalkeeper).addLostGameOfKing();
                firstDeath = true;
            }
            goalkeeper = shooter;
            if(numberAlivePlayers() == 1){
                players.get(shooter).addWinGameOfKing();
                Toast.makeText(context, players.get(shooter).getName() + " wygrał 🏆\nCofnij, aby wyjść z gry", Toast.LENGTH_LONG).show();//TODO: stringi
                for(Player player : players){
                    player.addGameOfKing();
                    player.update(context);
                }
            }
        }
        setShooter();
    }

    private void setShooter(){
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

    @Override
    public String getPlayersSegregatedByLives() {
        String string = "Życia\n"; //TODO: żyćka
        for(int i = 5; i > 0; i--){
            for(int j = 0; j < players.size(); j++){
                if(lives.get(j) == i){
                    string += "\n" + players.get(j).getName() + " " + lives.get(j);
                }
            }
        }
        return string;
    }

    @Override
    public String getPlayersSegregatedByOrder() {
        String string = "Kolejność\n"; //TODO: kolejność
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
