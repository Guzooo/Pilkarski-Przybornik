package pl.Guzooo.PilkarskiPrzybornik.Gry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import pl.Guzooo.PilkarskiPrzybornik.Database;
import pl.Guzooo.PilkarskiPrzybornik.Player;

public abstract class GameInfo {
    public abstract String getName(Context context);
    public abstract int getIcon(Context context);
    public abstract String getDescription(Context context);
    public abstract String getShortDescription(Context context);
    public abstract ArrayList<String> getButtons(Context context);
    public abstract void Play(int buttonId, Context context);
    public abstract void Reset();
    public abstract Settings getSettings();

    public int getNumberActivePlayers(Context context){
        SQLiteDatabase db = Database.getRead(context);
        Cursor cursor = db.query(Player.databaseName,
                null,
                "ACTIVE = ?",
                new String[]{Integer.toString(1)},
                null, null, null);
        int activePlayers = cursor.getCount();
        cursor.close();
        db.close();
        return activePlayers;
    }
}
