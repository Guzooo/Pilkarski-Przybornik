package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "mainsettings";
    private static final String PREFERENCES_INTERNET_DOWNLOAD = "internetdownload";

    public static final int INTERNET_DOWNLOAD_ONLY_WIFI = 0;
    public static final int INTERNET_DOWNLOAD_ALWAYS = 1;
    public static final int INTERNET_DOWNLOAD_NEVER = 2;

    private Spinner spinnerInternetDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SetSpinnerInternetDownload();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ClickMsg(View v){
        OpenPage("https://www.messenger.com/t/GuzoooApps");
    }

    public void ClickFb(View v){
        OpenPage("https://www.facebook.com/GuzoooApps");
    }

    private void OpenPage(String page){
        Uri uri = Uri.parse(page);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void SetSpinnerInternetDownload(){
        spinnerInternetDownload = findViewById(R.id.spinner_internet_download);
        spinnerInternetDownload.setSelection(getPreferencesInternetDownload(this));
        spinnerInternetDownload.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SavePreferencesInternetDownload(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void SavePreferencesInternetDownload(int option){
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREFERENCES_INTERNET_DOWNLOAD, option);
        editor.apply();
    }

    public static int getPreferencesInternetDownload(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(PREFERENCES_INTERNET_DOWNLOAD, 0);
    }
}
