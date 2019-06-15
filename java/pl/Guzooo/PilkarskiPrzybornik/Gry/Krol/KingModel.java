/*
package pl.Guzooo.PilkarskiPrzybornik.Gry.Krol;

import android.content.ContentValues;
import android.database.Cursor;

public class KingModel extends pl.Guzooo.PilkarskiPrzybornik.Model {

    public static final String databaseName = "KING";
    public static final String[] onCursor = {"_id", "NAME", "SHOOTER", "GOALKEEPER", "PLAYERS", "STATS", "DATA"};

    private String name;
    private int shooter;
    private int goalkeeper;
    private String players;
    private String stats;
    private String data;

    @Override
    public String[] onCursor() {
        return onCursor;
    }

    @Override
    public String databaseName() {
        return databaseName;
    }

    private void Template(int id, String name, int shooter, int goalkeeper, String players, String stats, String data){
        setId(id);
        setName(name);
        setShooter(shooter);
        setGoalkeeper(goalkeeper);
        setPlayers(players);
        setStats(stats);
        setData(data);
    }

    @Override
    public void Empty() {
        Template(0, "", -1, -1, "", "", "");
    }

    @Override
    public void getOfCursor(Cursor cursor) {
        Template(cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", getName());
        contentValues.put("SHOOTER", getShooter());
        contentValues.put("GOALKEEPER", getGoalkeeper());
        contentValues.put("PLAYERS", getPlayers());
        contentValues.put("STATS", getStats());
        contentValues.put("DATA", getData());
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShooter() {
        return shooter;
    }

    public void setShooter(int shooter) {
        this.shooter = shooter;
    }

    public int getGoalkeeper() {
        return goalkeeper;
    }

    public void setGoalkeeper(int goalkeeper) {
        this.goalkeeper = goalkeeper;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
*/
