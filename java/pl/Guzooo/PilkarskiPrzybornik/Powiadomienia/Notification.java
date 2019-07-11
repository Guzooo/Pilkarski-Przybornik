package pl.Guzooo.PilkarskiPrzybornik.Powiadomienia;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.Guzooo.PilkarskiPrzybornik.R;

public class Notification {

    private String title;
    private String info;
    private String data;
    private String version;
    private String language;
    private ArrayList<Button> buttons = new ArrayList<>();

    private final static String CODE_TITLE = "title";
    private final static String CODE_INFO = "info";
    private final static String CODE_DATA = "data";
    private final static String CODE_VERSION = "version";
    private final static String CODE_LANGUAGE = "language";
    private final static String CODE_BUTTONS = "buttons";
    private final static String CODE_BUTTON_TITLE = "label";
    private final static String CODE_BUTTON_LINK = "www";

    public Notification(JSONObject object, Context context){
        try {
            setTitle(object.getString(CODE_TITLE));
            setInfo(object.getString(CODE_INFO));
            setData(object.getString(CODE_DATA));
            setVersion(object.getString(CODE_VERSION));
            setLanguage(object.getString(CODE_LANGUAGE));
            setButtons(object.getJSONArray(CODE_BUTTONS), context);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    private void setInfo(String info) {
        this.info = info;
    }

    public String getData() {
        return data;
    }

    private void setData(String data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    private void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    private void setButtons(final JSONArray array, final Context context) {
        for(int i = 0; i < array.length(); i++){
            try {
                Button button = new Button(context);
                final JSONObject object = array.getJSONObject(i);
                button.setText(object.getString(CODE_BUTTON_TITLE));
                button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorSecondary), PorterDuff.Mode.MULTIPLY);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri = Uri.parse(object.getString(CODE_BUTTON_LINK));
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
                buttons.add(button);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
