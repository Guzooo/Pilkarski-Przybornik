package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "pilkarskiprzybornik";
    private static final int DB_VERSION = 1;

    Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        update(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        update(db, oldVersion, newVersion);
    }

    private void update(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE PLAYERS (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "NAME TEXT,"
                    + "ACTIVE INTEGER,"
                    + "SHOTS INTEGER,"
                    + "GOOD_SHOTS INTEGER,"
                    + "GOAL INTEGER,"
                    + "DEFENDED_GOAL INTEGER, "
                    + "UNDEFENDED_GOAL INTEGER,"
                    + "GAME_OF_KING INTEGER,"
                    + "WIN_GAME_OF_KING INTEGER,"
                    + "LOST_GAME_OF_KING INTEGER)");

            db.execSQL("CREATE TABLE GAMES (_id INTEGER PRIMARY KEY,"
                    + "NUMBER_GAME INTEGER,"
                    + "LAST_GAME TEXT)");
        }
    }

    public static SQLiteDatabase getWrite(Context context){
        SQLiteOpenHelper helper = new Database(context);
        return helper.getWritableDatabase();
    }

    public static SQLiteDatabase getRead(Context context){
        SQLiteOpenHelper helper = new Database(context);
        return helper.getReadableDatabase();
    }

    public static void ShowError(Context context){
        Toast.makeText(context, R.string.error_database,Toast.LENGTH_SHORT).show();
    }
}