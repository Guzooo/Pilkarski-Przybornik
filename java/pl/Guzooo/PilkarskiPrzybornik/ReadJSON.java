package pl.Guzooo.PilkarskiPrzybornik;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ReadJSON extends AsyncTask<String, Void, ArrayList<String>> {

    private final String PARENTHESIS = "GUZOOOJSON";
    private final String SEPARATOR = "GUZOOOSEP";

    private ReadJSONListener readJSONListener;

    public interface ReadJSONListener{
        void onPreRead();
        void onPostRead();
        void onPositivePostRead(ArrayList<String> strings);
        void onNegativePostRead();
    }

    public void setReadJSONListener(ReadJSONListener readJSONListener){
        this.readJSONListener = readJSONListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(readJSONListener != null)
            readJSONListener.onPreRead();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        try {
            String pageLine;
            ArrayList<String> stringsReturn = new ArrayList<>();
            for (int i = 0; i < strings.length; i++) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(strings[i]).openStream()));
                while ((pageLine = reader.readLine()) != null) {
                    if (pageLine.contains(PARENTHESIS)) {
                        String[] allText = pageLine.split(PARENTHESIS);
                        String correctJSON = allText[1].replaceAll("”", "\"");
                        correctJSON = correctJSON.replaceAll("</span><span>", "");
                        String[] onlyJSON = correctJSON.split(SEPARATOR);
                        for(int j = 0; j < onlyJSON.length; j++){
                            stringsReturn.add(onlyJSON[j]);
                        }
                        break;
                    }
                }
                reader.close();
            }
            return stringsReturn;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);

        if(readJSONListener != null){
            readJSONListener.onPostRead();
            if(strings != null && strings.size() > 0)
                readJSONListener.onPositivePostRead(strings);
            else
                readJSONListener.onNegativePostRead();
        }
    }
}
