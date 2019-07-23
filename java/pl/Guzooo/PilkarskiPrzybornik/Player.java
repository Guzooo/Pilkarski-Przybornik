package pl.Guzooo.PilkarskiPrzybornik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;

public class Player extends Model{

    private String name;
    private boolean active;
    private int shots;
    private int goodShots;
    private int goal;
    private int defendedGoal;
    private int undefendedGoal;
    private int gameOfKing;
    private int winGameOfKing;
    private int lostGameOfKing;

    public static final String databaseName = "PLAYERS";
    public static final String[] onCursor = {"_id", "NAME", "ACTIVE", "SHOTS", "GOOD_SHOTS", "GOAL", "DEFENDED_GOAL", "UNDEFENDED_GOAL", "GAME_OF_KING", "WIN_GAME_OF_KING", "LOST_GAME_OF_KING"};

    @Override
    public String[] onCursor() {
        return onCursor;
    }

    @Override
    public String databaseName() {
        return databaseName;
    }

    private void Template(int id, String name, int active, int shots, int goodShots, int goal, int defendedGoal, int undefendedGoal, int gameOfKing, int winGameOfKing, int lostGameOfKing){
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
        Template(0, "", 1, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public void getOfCursor(Cursor cursor, Context context) {
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

    public void getOfPlayer(Player player){
        setShots(player.shots);
        setGoodShots(player.goodShots);
        setGoal(player.goal);
        setDefendedGoal(player.defendedGoal);
        setUndefendedGoal(player.undefendedGoal);
        setGameOfKing(player.gameOfKing);
        setWinGameOfKing(player.winGameOfKing);
        setLostGameOfKing(player.lostGameOfKing);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
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

    public String getDescription(Context context){
        String string;
        string = context.getString(R.string.player_shots, getShots()) + "\n";
        string += context.getString(R.string.player_good_shots, getGoodShots()) + "\n";
        string += context.getString(R.string.player_goal, getGoal()) + "\n";
        string += context.getString(R.string.player_defended_goal, getDefendedGoal()) + "\n";
        string += context.getString(R.string.player_undefended_goal, getUndefendedGoal()) + "\n";
        string += context.getString(R.string.player_game_of_king, getGameOfKing()) + "\n";
        string += context.getString(R.string.player_win_game_of_king, getWinGameOfKing()) + "\n";
        string += context.getString(R.string.player_lost_game_of_king, getLostGameOfKing());
        return string;
    }

    public String getDescriptionPercent(Context context){
        String string;
        string = context.getString(R.string.player_good_shots_percent, getGoodShotsPercent()) + "\n";
        string += context.getString(R.string.player_goal_percent, getGoalPercent()) + "\n";
        string += context.getString(R.string.player_defended_goal_percent, getDefendedGoalPercent()) + "\n";
        string += context.getString(R.string.player_win_game_of_king_percent, getWinGameOfKingPercent()) + "\n";
        string += context.getString(R.string.player_lost_game_of_king_percent, getLostGameOfKingPercent());
        return string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active){
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

    public void addShots(){
        shots++;
    }

    public int getGoodShots() {
        return goodShots;
    }

    public void setGoodShots(int goodShots) {
        this.goodShots = goodShots;
    }

    public void addGoodShots(){
        goodShots++;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void addGoal(){
        goal++;
    }

    public int getDefendedGoal() {
        return defendedGoal;
    }

    public void setDefendedGoal(int defendedGoal) {
        this.defendedGoal = defendedGoal;
    }

    public void addDefendedGoal(){
        defendedGoal++;
    }

    public int getUndefendedGoal() {
        return undefendedGoal;
    }

    public void setUndefendedGoal(int undefendedGoal) {
        this.undefendedGoal = undefendedGoal;
    }

    public void addUndefendedGoal(){
        undefendedGoal++;
    }

    public int getGameOfKing() {
        return gameOfKing;
    }

    public void setGameOfKing(int gameOfKing) {
        this.gameOfKing = gameOfKing;
    }

    public void addGameOfKing(){
        gameOfKing++;
    }

    public int getWinGameOfKing() {
        return winGameOfKing;
    }

    public void setWinGameOfKing(int winGameOfKing) {
        this.winGameOfKing = winGameOfKing;
    }

    public void addWinGameOfKing(){
        winGameOfKing++;
    }

    public int getLostGameOfKing() {
        return lostGameOfKing;
    }

    public void setLostGameOfKing(int lostGameOfKing) {
        this.lostGameOfKing = lostGameOfKing;
    }

    public void addLostGameOfKing(){
        lostGameOfKing++;
    }

    public float getGoodShotsPercent(){
        return getPercentIntOfInt((getGoodShots() + getGoal()), getShots());
    }

    public float getGoalPercent(){
        return getPercentIntOfInt(getGoal(), getGoodShots());
    }

    public float getDefendedGoalPercent(){
        return getPercentIntOfInt(getDefendedGoal(), (getDefendedGoal() + getUndefendedGoal()));
    }

    public float getWinGameOfKingPercent(){
        return getPercentIntOfInt(getWinGameOfKing(), getGameOfKing());
    }

    public float getLostGameOfKingPercent(){
        return getPercentIntOfInt(getLostGameOfKing(), getGameOfKing());
    }

    private float getPercentIntOfInt(int part, int whole){
        if(whole == 0)
            return 0;
        return (float) part * 100 / (float) whole;
    }
}