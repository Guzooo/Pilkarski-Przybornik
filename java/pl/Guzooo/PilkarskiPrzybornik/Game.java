package pl.Guzooo.PilkarskiPrzybornik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import pl.Guzooo.PilkarskiPrzybornik.Gry.GameInfo;

public class Game extends Model{

    private String name;
    private int image;
    private String description;
    private String shortDescription;
    private int numberGame;
    private String lastGame;
    private GameInfo gameInfo;
    private ArrayList<String> buttonsName;
    private String buttonsOrder = "";

    public static final String databaseName = "GAMES";
    public static final String[] onCursor = {"_id", "NUMBER_GAME", "LAST_GAME", "BUTTONS"};

    @Override
    public String[] onCursor() {
        return onCursor;
    }

    @Override
    public String databaseName() {
        return databaseName;
    }

    private void Template(int id, int numberGame, String lastGame, String buttons){
        setId(id);
        setNumberGame(numberGame);
        setLastGame(lastGame);
        setButtonsOrder(buttons);
    }

    @Override
    public void Empty() {
        Template(0, 0, "", null);
    }

    @Override
    public void getOfCursor(Cursor cursor, Context context) {
        Template(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getString(3));
        setGameInfo(Games.getGameInfo(getId()));
        setName(gameInfo.getName(context));
        setDescription(gameInfo.getDescription(context));
        setShortDescription(gameInfo.getShortDescription(context));
        setImage(gameInfo.getIcon(context));
        setButtonsName(gameInfo.getButtons(context));
        CheckButtonsOrder(context);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", getId());
        contentValues.put("NUMBER_GAME", numberGame);
        contentValues.put("LAST_GAME", lastGame);
        contentValues.put("BUTTONS", buttonsOrder);
        return contentValues;
    }

    public String getName(){
        return name;
    }

    private void setName(String name){
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    private void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getNumberGame() {
        return numberGame;
    }

    private void setNumberGame(int numberGame) {
        this.numberGame = numberGame;
    }

    public void addNumberGame(){
        numberGame++;
    }

    public String getLastGame() {
        return lastGame;
    }

    private void setLastGame(String lastGame) {
        this.lastGame = lastGame;
    }

    public void setLastGame() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String data = hour + ":";
        if(minute < 10)
            data += "0" + minute;
        else
            data += minute;
        data += " " + day + "/" + month + "/" + year;
        lastGame = data;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public ArrayList<String> getButtonsName() {
        return buttonsName;
    }

    public void setButtonsName(ArrayList<String> buttonsName) {
        this.buttonsName = buttonsName;
    }

    public ArrayList<Integer> getButtonsOrder() {
        String[] buttonsS = buttonsOrder.split(";");
        ArrayList<Integer> buttonsI = new ArrayList<>();
        for(String string : buttonsS)
            buttonsI.add(Integer.valueOf(string));

        return buttonsI;
    }

    public void setButtonsOrder(String buttons) {
        this.buttonsOrder = buttons;
        Log.d("GAME", "tu ustawiamy: " + buttonsOrder);
    }

    public void CheckButtonsOrder(Context context) {
        int count = gameInfo.getButtons(context).size();
        int buttonsLength = 0;
        if (buttonsOrder != null) {
            String[] buttons = buttonsOrder.split(";");
            buttonsLength = buttons.length;
        } else {
            buttonsOrder = "";
        }
        if(count > buttonsLength){
            for (int i = buttonsLength; i < count; i++){
                buttonsOrder += i + ";";
            }
            Log.d("GAME", "tu sprawdzamy powiekszyło się");
        } else if (count < buttonsLength){
            for (int i = count; i < buttonsLength; i++) {
                buttonsOrder = buttonsOrder.replace(i + ";", "");
            }
            Log.d("GAME", "tu sprawdzamy zmniejszyło się");
        }
        Log.d("GAME", "tu sprawdzamy: " + buttonsOrder);
    }

    public void setLastButton(int i){
        buttonsOrder = buttonsOrder.replace(i + ";", "");
        buttonsOrder = i + ";" + buttonsOrder;
        Log.d("GAME", "a tu robie takie cyk cyk z ostatnim: " + buttonsOrder);
    }
}