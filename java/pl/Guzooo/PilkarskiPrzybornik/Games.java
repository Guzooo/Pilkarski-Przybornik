package pl.Guzooo.PilkarskiPrzybornik;

import android.database.sqlite.SQLiteDatabase;

import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;
import pl.Guzooo.PilkarskiPrzybornik.Gry.Krol.King;
import pl.Guzooo.PilkarskiPrzybornik.Gry.WyborBramkarza.SelectGoalkeeper;

public class Games {

    public static Game currentGame;

    public static final GameInfo[] gamesInfo = new GameInfo[] {new King(), new SelectGoalkeeper()};

    public static void setCurrentGame(Game game){
        currentGame = game;
    }

    public static GameInfo getGameInfo(int id){
        return gamesInfo[id];
    }

    public static void createGamesInDatabase(SQLiteDatabase db, int oldVersion){
        if(oldVersion < 1){
            insert(0,4, db);
        }
    }

    private static void insert(int from, int to, SQLiteDatabase db){
        for(int i = from; i < to; i++){
            Game game = new Game();
            game.Empty();
            game.setId(i);
            db.insert(Game.databaseName, null, game.getContentValues());
        }
    }
}