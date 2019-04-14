package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

public class LinksManagement {

    public static String START_LINK = "https://Guzooo-PiłkarskiPrzybornik/";
    public static int VERSION = 1;
    public static String SEPARATOR = "/";

    public static int NUMBER_SEGMENT_ON_PLAYER = 9;

    public static ArrayList<Player> getPlayers (Uri link, Context context){
        String strLink = link.toString();
        strLink = strLink.replaceFirst(START_LINK, "");

        if(!isGoodCode(strLink))
            return null; //Błąd zły link

        String[] strings = strLink.split(SEPARATOR);

        strings[1] = decodeV4(strings[1]);
        if(!isGoodDecode(strings[1]))
            return null; //Błąd zły link

        int numberPlayers = Integer.valueOf(strings[1]);

        if(numberPlayers%4 == 0) {
            strings[0] = decodeV1(strings[0]);
        } else if(numberPlayers%4 == 1){
            strings[0] = decodeV2(strings[0]);
        } else if(numberPlayers%4 == 2){
            strings[0] = decodeV3(strings[0]);
        } else {
            strings[0] = decodeV4(strings[0]);
        }
        if(!isGoodDecode(strings[0]))
            return null; //Błąd zły link

        int version = Integer.valueOf(strings[0]);

        if(version > VERSION) {
            new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                    .setTitle("Aktualizacja zalecana")
                    .setPositiveButton("Aktualizuj", null)
                    .setNeutralButton("Pomiń", null)
                     .setNegativeButton(R.string.cancel, null)
                    .show();    //TODO:literki ze stringa

        }

        String[] stringsOfPlayers = new String[numberPlayers * NUMBER_SEGMENT_ON_PLAYER];

        int j = 2;
        for(int i = 0; i < stringsOfPlayers.length && i+2 < strings.length; i++, j++){
            stringsOfPlayers[i] = strings[j];
        }

        String end = null;
        for(int i = numberPlayers; i < stringsOfPlayers.length; i++) {
            if(end != null && stringsOfPlayers[i] == null){
                stringsOfPlayers[i] = end;
            } else if (i % 3 == 0) {
                stringsOfPlayers[i] = decodeV1(stringsOfPlayers[i]);
            } else if (i % 3 == 1) {
                stringsOfPlayers[i] = decodeV2(stringsOfPlayers[i]);
            } else {
                stringsOfPlayers[i] = decodeV3(stringsOfPlayers[i]);
            }
            if(!isGoodDecode(stringsOfPlayers[i]))
                return null; //Błąd zły link
            end = stringsOfPlayers[i];
        }

        for(int i = 0; i < numberPlayers; i++){
            Player player = new Player();
            player.setName(stringsOfPlayers[i]);
            player.setShots(Integer.valueOf(stringsOfPlayers[i + numberPlayers]));
            player.setGoodShots(Integer.valueOf(stringsOfPlayers[i + 2*numberPlayers]));
            player.setGoal(Integer.valueOf(stringsOfPlayers[i + 3*numberPlayers]));
            player.setDefendedGoal(Integer.valueOf(stringsOfPlayers[i + 4*numberPlayers]));
            player.setUndefendedGoal(Integer.valueOf(stringsOfPlayers[i + 5*numberPlayers]));
            player.setGameOfKing(Integer.valueOf(stringsOfPlayers[i + 6*numberPlayers]));
            player.setWinGameOfKing(Integer.valueOf(stringsOfPlayers[i + 7*numberPlayers]));
            player.setLostGameOfKing(Integer.valueOf(stringsOfPlayers[i + 8*numberPlayers]));
            //Dialog czy chcesz dodać gracza jako nowy czy jako ktoś;
        }

        AlertDialog a = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle("Gracze")
                //.setView(recyclerView)
                .setPositiveButton("Potwierdź", null)
                .setNeutralButton("Gracze", null)
                .setNegativeButton(R.string.cancel, null)
                .show();

        return null;
    }

    public static void sharePlayers (Cursor cursor, Context context){
        ArrayList<Player> players = getList(cursor);

        String link = START_LINK;
        String linkPlayers = getLink(players);
        link += encode(linkPlayers);

        startIntent(link , context);
    }

    private static ArrayList<Player> getList(Cursor cursor){
        ArrayList<Player> players = new ArrayList<>();

        if(cursor.moveToFirst()) { //TODO: Jeśli w nazwie będzie separator pomiń;
            do {
                Player player = new Player();
                player.getOfCursor(cursor);
                players.add(player);
            }while (cursor.moveToNext());
        }

        return players;
    }

    private static String getLink(ArrayList<Player> players){
        String link = "";

        for (Player p : players) {
            link += p.getName() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getShots() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getGoodShots() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getGoal() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getDefendedGoal() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getUndefendedGoal() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getGameOfKing() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getWinGameOfKing() + SEPARATOR;
        }
        for (Player p : players) {
            link += p.getLostGameOfKing() + SEPARATOR;
        }

        return link;
    }

    private static String encode (String link){
        String[] strings = link.split(SEPARATOR);
        int numberPlayers = strings.length/9;
        boolean sameEnd = false;
        for(int i = numberPlayers; i < strings.length; i++){

            if(!sameEnd) {
                for (int j = i; j < strings.length; j++) {
                    if (!strings[i].equals(strings[j])) {
                        sameEnd = false;
                        break;
                    } else {
                        sameEnd = true;
                    }
                }
            }

            if (i % 3 == 0) {
                strings[i] = encodeV1(strings[i]);
            } else if (i % 3 == 1) {
                strings[i] = encodeV2(strings[i]);
            } else {
                strings[i] = encodeV3(strings[i]);
            }

            if(sameEnd){
                for(int j = i+1; j < strings.length; j++){
                    strings[j] = "";
                }
                break;
            }
        }

        String version;
        if(numberPlayers%4 == 0) {
            version = encodeV1(Integer.toString(VERSION));
        } else if(numberPlayers%4 == 1){
            version = encodeV2(Integer.toString(VERSION));
        } else if(numberPlayers%4 == 2){
            version = encodeV3(Integer.toString(VERSION));
        } else {
            version = encodeV4(Integer.toString(VERSION));
        }

        String encode = version + SEPARATOR + encodeV4(Integer.toString(numberPlayers)) + SEPARATOR;
        for(String s : strings) {
            if (!s.equals("")) {
                encode += s + SEPARATOR;
            }
        }
        return encode;
    }

    private static void startIntent(String link, Context context){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link);
        Intent intentChoose = Intent.createChooser(intent, context.getString(R.string.share));
        context.startActivity(intentChoose);
    }

    private static String encodeV1(String string){
        string = string.replaceAll("0", "ydf");
        string = string.replaceAll("1", "suw");
        string = string.replaceAll("2", "eig");
        string = string.replaceAll("3", "oar");
        string = string.replaceAll("4", "tpq");
        string = string.replaceAll("5", "hm");
        string = string.replaceAll("6", "bv");
        string = string.replaceAll("7", "cj");
        string = string.replaceAll("8", "zk");
        string = string.replaceAll("9", "xn");
        return string;
    }

    private static String encodeV2(String string){
        string = string.replaceAll("0", "h");
        string = string.replaceAll("1", "l");
        string = string.replaceAll("2", "j");
        string = string.replaceAll("3", "c");
        string = string.replaceAll("4", "k");
        string = string.replaceAll("5", "q");
        string = string.replaceAll("6", "t");
        string = string.replaceAll("7", "u");
        string = string.replaceAll("8", "o");
        string = string.replaceAll("9", "a");
        return string;
    }

    private static String encodeV3(String string){
        string = string.replaceAll("0", "suw");
        string = string.replaceAll("1", "oar");
        string = string.replaceAll("2", "l");
        string = string.replaceAll("3", "tpq");
        string = string.replaceAll("4", "c");
        string = string.replaceAll("5", "ydf");
        string = string.replaceAll("6", "k");
        string = string.replaceAll("7", "h");
        string = string.replaceAll("8", "eig");
        string = string.replaceAll("9", "j");
        return string;
    }

    private static String encodeV4(String string){
        string = string.replaceAll("0", "hej");
        string = string.replaceAll("1", "ju");
        string = string.replaceAll("2", "hu");
        string = string.replaceAll("3", "ba");
        string = string.replaceAll("4", "meh");
        string = string.replaceAll("5", "off");
        string = string.replaceAll("6", "rrr");
        string = string.replaceAll("7", "lol");
        string = string.replaceAll("8", "kop");
        string = string.replaceAll("9", "zoo");
        return string;
    }

    private static String decodeV1(String string){
        string = string.replaceAll("ydf", "0" );
        string = string.replaceAll("suw", "1");
        string = string.replaceAll("eig", "2");
        string = string.replaceAll("oar", "3");
        string = string.replaceAll("tpq", "4");
        string = string.replaceAll("hm", "5");
        string = string.replaceAll("bv", "6");
        string = string.replaceAll("cj", "7");
        string = string.replaceAll("zxk", "8");
        string = string.replaceAll("xn", "9");
        return string;
    }

    private static String decodeV2(String string){
        string = string.replaceAll("h", "0");
        string = string.replaceAll("l", "1");
        string = string.replaceAll("j", "2");
        string = string.replaceAll("c", "3");
        string = string.replaceAll("k", "4");
        string = string.replaceAll("q", "5");
        string = string.replaceAll("t", "6");
        string = string.replaceAll("u", "7");
        string = string.replaceAll("o", "8");
        string = string.replaceAll("a", "9");
        return string;
    }

    private static String decodeV3(String string){
        string = string.replaceAll("suw", "0");
        string = string.replaceAll("oar", "1");
        string = string.replaceAll("l", "2");
        string = string.replaceAll("tpq", "3");
        string = string.replaceAll("c", "4");
        string = string.replaceAll("ydf", "5");
        string = string.replaceAll("k", "6");
        string = string.replaceAll("h", "7");
        string = string.replaceAll("eig", "8");
        string = string.replaceAll("j", "9");
        return string;
    }

    private static String decodeV4(String string){
        string = string.replaceAll("hej", "0");
        string = string.replaceAll("ju", "1");
        string = string.replaceAll("hu", "2");
        string = string.replaceAll("ba", "3");
        string = string.replaceAll("meh", "4");
        string = string.replaceAll("off", "5");
        string = string.replaceAll("rrr", "6");
        string = string.replaceAll("lol", "7");
        string = string.replaceAll("kop", "8");
        string = string.replaceAll("zoo", "9");
        return string;
    }

    private static boolean isGoodCode(String string){
        if      (string.contains("0")
                || string.contains("1")
                || string.contains("2")
                || string.contains("3")
                || string.contains("4")
                || string.contains("5")
                || string.contains("6")
                || string.contains("7")
                || string.contains("8")
                || string.contains("9"))
            return false;
        return true;
    }

    private static boolean isGoodDecode(String string){
        string = string.replaceAll("0", "");
        string = string.replaceAll("1", "");
        string = string.replaceAll("2", "");
        string = string.replaceAll("3", "");
        string = string.replaceAll("4", "");
        string = string.replaceAll("5", "");
        string = string.replaceAll("6", "");
        string = string.replaceAll("7", "");
        string = string.replaceAll("8", "");
        string = string.replaceAll("9", "");
        if(string.equals(""))
            return true;
        return false;
    }
}
