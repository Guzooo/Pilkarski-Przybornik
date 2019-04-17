package pl.Guzooo.PilkarskiPrzybornik;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class LinksManagement {

    public static String APP_PAGE = "https://play.google.com/store/apps/details?id=pl.Guzooo.PilkarskiPrzybornik";

    public static String START_LINK = "https://Guzooo-PiłkarskiPrzybornik/";
    public static int VERSION = 1;
    public static String SEPARATOR = "/";

    public static int NUMBER_SEGMENT_ON_PLAYER = 9;

    public static void getPlayers (Uri link, Activity activity, Context context){
        if(link == null){
            activity.finish();
            return;
        }

        String strLink = link.toString();
        strLink = strLink.replaceFirst(START_LINK, "");
        String[] strings = strLink.split(SEPARATOR);

        //decode count of players
        strings[1] = decodeV4(strings[1]);
        if(!isGoodDecode(strings[1], activity, context))
            return;
        int numberPlayers = Integer.valueOf(strings[1]);

        //decode version
        strings[0] = decodeVersion(strings[0], numberPlayers);
        if(!isGoodDecode(strings[0], activity, context))
            return; //Bad URL
        int version = Integer.valueOf(strings[0]);

        String[] stringsOfPlayers = decode(numberPlayers, strings, activity, context);
        if(stringsOfPlayers == null)
            return; //Bad URL

        if(NewVersion(version, activity, context, numberPlayers, stringsOfPlayers))
            return; //Update

        ArrayList<Player> players = getListPlayers(numberPlayers, stringsOfPlayers);

        ShowPlayers(players, activity, context);
    }

    public static void sharePlayers (Cursor cursor, Context context){
        ArrayList<Player> players = getList(cursor);

        String linkPlayers = getLink(players);
        String link = START_LINK + encode(linkPlayers) + " 🎮" + "\n\n" + APP_PAGE + " 📲";

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

    private static AlertDialog ErrorLink(final Activity activity, Context context){
        return new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle(R.string.bad_url)
                .setMessage(R.string.bad_url_description)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                })
                .show();

    }

    private static String decodeVersion(String version, int numberPlayers){
        if(numberPlayers%4 == 0) {
            version = decodeV1(version);
        } else if(numberPlayers%4 == 1){
            version = decodeV2(version);
        } else if(numberPlayers%4 == 2){
            version = decodeV3(version);
        } else {
            version = decodeV4(version);
        }
        return version;
    }

    private static boolean NewVersion(int version, final Activity activity, final Context context, final int numberPlayers, final String[] stringsOfPlayers){
        if(version > VERSION) {
            new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                    .setTitle(R.string.update_recommended)
                    .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                            Uri uri = Uri.parse(APP_PAGE);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }
                    })
                    .setNeutralButton(R.string.skip, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<Player> players = getListPlayers(numberPlayers, stringsOfPlayers);

                            ShowPlayers(players, activity, context);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            activity.finish();
                        }
                    })
                    .show();
            return true;
        }
        return false;
    }

    private static String[] decode (int numberPlayers, String[] strings, Activity activity, Context context){
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
            if(!isGoodDecode(stringsOfPlayers[i], activity, context)) {
                return null; //Bad URL
            }
            end = stringsOfPlayers[i];
        }
        return stringsOfPlayers;
    }

    private static ArrayList<Player> getListPlayers(int numberPlayers, String[] stringsOfPlayers){
        ArrayList<Player> players = new ArrayList<>();
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
            players.add(player);
        }
        return players;
    }

    private static void ShowPlayers(ArrayList<Player> players, final Activity activity, final Context context){
        RecyclerView recyclerView = new RecyclerView(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        final AdapterAddSharePlayers adapter = new AdapterAddSharePlayers(players, context);
        recyclerView.setAdapter(adapter);

        new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setView(recyclerView)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.SaveChanges(context);
                    }
                })
                .setNeutralButton(R.string.player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                        Intent intent = new Intent(context, PlayersActivity.class);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                })
                .show();
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

    private static boolean isGoodDecode(String string, Activity activity, Context context){
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
        ErrorLink(activity, context);
        return false;
    }
}
