package pl.Guzooo.PilkarskiPrzybornik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class Game extends Model{

    private int name;
    private int image;
    private int description;
    private int numberGame;
    private String lastGame;

    private Listener listener;

    public static final String databaseName = "GAMES";
    public static final String[] onCursor = {"_id", "NUMBER_GAME", "LAST_GAME"};

    public interface Listener{
        void ClickPlay(int id, Context context);
    }

    @Override
    public String[] onCursor() {
        return onCursor;
    }

    @Override
    public String databaseName() {
        return databaseName;
    }

    private void Template(int id, int numberGame, String lastGame){
        setId(id);
        setNumberGame(numberGame);
        setLastGame(lastGame);
    }

    @Override
    public void Empty() {
        Template(0, 0, "");
    }

    @Override
    public void getOfCursor(Cursor cursor) {
        Template(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2));
        Games.setCurrentGame(this);
        setName(Games.getName(getId()));
        setImage(Games.getImage(getId()));
        setDescription(Games.getDescription(getId()));
        setListener(Games.getListenerGeneral(getId()));
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", getId());
        contentValues.put("NUMBER_GAME", numberGame);
        contentValues.put("LAST_GAME", lastGame);
        return contentValues;
    }

    public int getName(){
        return name;
    }

    private void setName(int name){
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    private void setImage(int image) {
        this.image = image;
    }

    public int getDescription() {
        return description;
    }

    private void setDescription(int description) {
        this.description = description;
    }

    public int getNumberGame() {
        return numberGame;
    }

    private void setNumberGame(int numberGame) {
        this.numberGame = numberGame;
    }

    public String getLastGame() {
        return lastGame;
    }

    private void setLastGame(String lastGame) {
        this.lastGame = lastGame;
    }

    private void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener(){
        return listener;
    }
}