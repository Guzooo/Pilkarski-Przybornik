package pl.Guzooo.PilkarskiPrzybornik;

import android.content.ContentValues;
import android.database.Cursor;

public class Game extends Model{

    private int name;
    private int image;
    private int description;
    private int numberGame;
    private String lastGame;

    public static final String databaseName = "GAMES";
    public static final String[] onCursor = {"_id", "NUMBER_GAME", "LAST_GAME"};

    @Override
    public String[] onCursor() {
        return onCursor;
    }

    @Override
    public String databaseName() {
        return databaseName;
    }

    @Override
    public void Empty() {
        //W chwili obecnej niemożliwe jest utworzenie gry bez parametrów;
    }

    @Override
    public void getOfCursor(Cursor cursor) {
        setId(cursor.getInt(0));
        setNumberGame(cursor.getInt(1));
        setLastGame(cursor.getString(2));
        setName(Games.getName(getId()));
        setImage(Games.getImage(getId()));
        setDescription(Games.getDescription(getId()));
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NUMBER_GAME", numberGame);
        contentValues.put("LAST_GAME", lastGame);
        return contentValues;
    }

    public int getName(){
        return name;
    }

    private void setName(int name){
        this.name = name;
        Player player = new Player();
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
}