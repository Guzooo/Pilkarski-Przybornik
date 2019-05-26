package pl.Guzooo.PilkarskiPrzybornik;

import android.database.sqlite.SQLiteDatabase;

import pl.Guzooo.PilkarskiPrzybornik.Gry.SelectGoalkeeper;
import pl.Guzooo.PilkarskiPrzybornik.Gry.Krol.King;

public class Games {

    public static Game currentGame;

    public static final int[] names = new int[] {R.string.game_king, R.string.game_select_goalkeeper};
    public static final int[] descriptions = new int[] {R.string.game_king_description, R.string.game_select_goalkeeper_description};
    public static final int[] images = new int[] {R.drawable.crown, R.drawable.goalkeeper};
    public static final LotteryActivity.Listener[] listenerLotteries = new LotteryActivity.Listener[] {new King(), new SelectGoalkeeper()};
    public static final Game.Listener[] listenerGenerals = new Game.Listener[] {new King(), new SelectGoalkeeper()};
    public static final PlayingFieldActivity.Listener[] listenerPlayingFields = new PlayingFieldActivity.Listener[] {new King(), null};

    public static void setCurrentGame(Game game){
        currentGame = game;
    }

    public static int getName(int id){
        return names[id];
    }

    public static int getDescription(int id){
        return descriptions[id];
    }

    public static int getImage(int id){
        return images[id];
    }

    public static LotteryActivity.Listener getListenerLottery(int id){
        return listenerLotteries[id];
    }

    public static Game.Listener getListenerGeneral(int id){
        return listenerGenerals[id];
    }

    public static PlayingFieldActivity.Listener getListenerPlayingField(int id){
        return listenerPlayingFields[id];
    }

    public static void createGamesInDatabase(SQLiteDatabase db, int oldVersion){
        int from = 0;
        int to = 0;

        if(oldVersion < 1){
            from = 0;
            to = 4;
        }

        for(int i = from; i < to; i++){
            Game game = new Game();
            game.Empty();
            game.setId(i);
            db.insert(Game.databaseName, null, game.getContentValues());
        }
    }
}