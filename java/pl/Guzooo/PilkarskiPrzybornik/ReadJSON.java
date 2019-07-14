package pl.Guzooo.PilkarskiPrzybornik;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ReadJSON extends AsyncTask<String, JSONObject, Boolean> {

    private ReadJSONListener readJSONListener;

    public interface ReadJSONListener{
        void onPreRead();
        void onPostRead();
        void onProgressRead(JSONObject object);
        void onPositivePostRead();
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
    protected Boolean doInBackground(String... strings) {
        try {
            String pageLine;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(strings[0]).openStream()));
            while ((pageLine = reader.readLine()) != null) {
                JSONObject object = new JSONObject(pageLine);
                publishProgress(object);
                wait();
            }
            reader.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(JSONObject... objects) {
        super.onProgressUpdate(objects);
        if(readJSONListener != null){
            for(JSONObject object : objects) {
                readJSONListener.onProgressRead(object);
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);

        if(readJSONListener != null){
            readJSONListener.onPostRead();
            if(bool)
                readJSONListener.onPositivePostRead();
            else
                readJSONListener.onNegativePostRead();
        }
    }
}
