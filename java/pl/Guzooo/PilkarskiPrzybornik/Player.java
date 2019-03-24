package pl.Guzooo.PilkarskiPrzybornik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Player extends Model{

    private boolean active;
    private int shots;
    private int goodShots;
    private int goal;
    private int defendedGoal;
    private int undefendedGoal;
    private int gameOfKing;
    private int winGameOfKing;
    private int lostGameOfKing;

    @Override
    public String[] onCursor() {
        return new String[] {"_id", "NAME", "ACTIVE", "SHOTS", "GOOD_SHOTS", "GOAL", "DEFENDED_GOAL", "UNDEFENDED_GOAL", "GAME_OF_KING", "WIN_GAME_OF_KING", "LOST_GAME_OF_KING"};
    }

    @Override
    public String databaseName() {
        return "PLAYERS";
    }

    private void Template(int id, String name, int active, int shots, int goodShots, int goal, int defendedGoal, int undefendedGoal, int gameOfKing, int winGameOfKing, int lostGameOfKing){
        Log.d("Player","ziarno");
        setId(id);
        setName(name);
        setActive(active);
        setShots(shots);
        setGoodShots(goodShots);
        setGoal(goal);
        setDefendedGoal(defendedGoal);
        setUndefendedGoal(undefendedGoal);
        setGameOfKing(gameOfKing);
        setLostGameOfKing(lostGameOfKing);
        setWinGameOfKing(winGameOfKing);
    }

    @Override
    public void Empty() {
        Log.d("Player"," of empty");
        Template(0, "", 1, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public void getOfCursor(Cursor cursor) {
        Log.d("Player"," of cursor");
        Template(cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getInt(8),
                cursor.getInt(9),
                cursor.getInt(10));
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("ACTIVE", active);
        contentValues.put("SHOTS", shots);
        contentValues.put("GOOD_SHOTS", goodShots);
        contentValues.put("GOAL", goal);
        contentValues.put("DEFENDED_GOAL", defendedGoal);
        contentValues.put("UNDEFENDED_GOAL", undefendedGoal);
        contentValues.put("GAME_OF_KING", gameOfKing);
        contentValues.put("WIN_GAME_OF_KING", winGameOfKing);
        contentValues.put("LOST_GAME_OF_KING", lostGameOfKing);
        return contentValues;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){  //TODO: może zbędne 🤷‍♂️
        this.active = active;
    }

    public void setActive(int active) {
        this.active = (active == 1);
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public int getGoodShots() {
        return goodShots;
    }

    public void setGoodShots(int goodShots) {
        this.goodShots = goodShots;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getDefendedGoal() {
        return defendedGoal;
    }

    public void setDefendedGoal(int defendedGoal) {
        this.defendedGoal = defendedGoal;
    }

    public int getUndefendedGoal() {
        return undefendedGoal;
    }

    public void setUndefendedGoal(int undefendedGoal) {
        this.undefendedGoal = undefendedGoal;
    }

    public int getGameOfKing() {
        return gameOfKing;
    }

    public void setGameOfKing(int gameOfKing) {
        this.gameOfKing = gameOfKing;
    }

    public int getWinGameOfKing() {
        return winGameOfKing;
    }

    public void setWinGameOfKing(int winGameOfKing) {
        this.winGameOfKing = winGameOfKing;
    }

    public int getLostGameOfKing() {
        return lostGameOfKing;
    }

    public void setLostGameOfKing(int lostGameOfKing) {
        this.lostGameOfKing = lostGameOfKing;
    }
}