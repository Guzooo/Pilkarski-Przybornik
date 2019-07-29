package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "mainsettings";
    private static final String PREFERENCES_INTERNET_DOWNLOAD = "internetdownload";
    private static final String PREFERENCES_RANDOM_NUMBERS_FROM_PLAYERS = "randomnumbersfromplayers";
    private static final String PREFERENCES_RANDOM_NUMBERS_PRODUCT = "randomnumbersproduct";
    private static final String PREFERENCES_RANDOM_NUMBERS = "randomnumbers";

    public static final int INTERNET_DOWNLOAD_ONLY_WIFI = 0;
    public static final int INTERNET_DOWNLOAD_ALWAYS = 1;
    public static final int INTERNET_DOWNLOAD_NEVER = 2;

    private static final int DEFAULT_INTERNET_DOWNLOAD = INTERNET_DOWNLOAD_ONLY_WIFI;
    private static final boolean DEFAULT_RANDOM_NUMBERS_FROM_PLAYERS = true;
    private static final int DEFAULT_RANDOM_NUMBERS_PRODUCT = 3;
    private static final int DEFAULT_RANDOM_NUMBERS = 0;

    private Switch randomNumbersFromPlayers;
    private TextView randomNumbers;
    private EditText randomNumbersEdit;
    private View randomNumbersButtons;
    private TextView randomNumbersProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SetSpinnerInternetDownload();
        SetRandomNumberViews();
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

    public void ClickRandomNumbersPlus(View v){
        int product = getRandomNumberProduct();
        product++;
        setRandomNumbersProduct(product);
        setRandomNumbers(product);
        SavePreferencesRandomNumbersProduct(product);
    }

    public void ClickRandomNumbersMinus(View v){
        int product = getRandomNumberProduct();
        if(product > 1) {
            product--;
            setRandomNumbersProduct(product);
            setRandomNumbers(product);
            SavePreferencesRandomNumbersProduct(product);
        }
    }

    private int getRandomNumberProduct(){
        return Integer.valueOf(randomNumbersProduct.getText().toString().replace("x", ""));
    }

    private void SetSpinnerInternetDownload(){
        Spinner spinnerInternetDownload = findViewById(R.id.spinner_internet_download);
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

    private void SetRandomNumberViews(){
        SetRandomNumbersFromPlayers();
        SetRandomNumbersEdit();
        randomNumbersProduct = findViewById(R.id.random_numbers_product);
        randomNumbers = findViewById(R.id.random_numbers);
        randomNumbersButtons = findViewById(R.id.random_numbers_buttons);
        setRandomNumbersProduct(getPreferencesRandomNumberProduct(this));
        setRandomNumbers(getRandomNumberProduct());
        setVisibilityRandomNumbersButtons(randomNumbersFromPlayers.isChecked());
    }

    private void SetRandomNumbersFromPlayers(){
        randomNumbersFromPlayers = findViewById(R.id.random_numbers_from_players);
        randomNumbersFromPlayers.setChecked(getPreferencesRandomNumberFromPlayers(this));
        randomNumbersFromPlayers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setVisibilityRandomNumbersButtons(b);
                SavePreferencesRandomNumberFromPlayers(b);
            }
        });
    }

    private void SetRandomNumbersEdit(){
        randomNumbersEdit = findViewById(R.id.random_numbers_edit);
        randomNumbersEdit.setText(getPreferencesRandomNumber(this) + "");
        randomNumbersEdit.setHint(DEFAULT_RANDOM_NUMBERS + "");
        randomNumbersEdit.setFocusable(false);
        randomNumbersEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int randomNumbers = DEFAULT_RANDOM_NUMBERS;
                if(!editable.toString().equals(""))
                    randomNumbers = Integer.valueOf(editable.toString());
                SavePreferencesRandomNumber(randomNumbers);
            }
        });
    }

    private void setRandomNumbersProduct(int product){
        randomNumbersProduct.setText(product + "x");
    }

    private void setRandomNumbers(int product){
        randomNumbers.setText((PlayersActivity.getNumberActivePlayers(this) * product) + "");
    }

    private void setVisibilityRandomNumbersButtons(boolean b){
        if(b){
            randomNumbersButtons.setVisibility(View.VISIBLE);
            randomNumbers.setVisibility(View.VISIBLE);
            randomNumbersEdit.setVisibility(View.GONE);
        } else {
            randomNumbersButtons.setVisibility(View.GONE);
            randomNumbers.setVisibility(View.GONE);
            randomNumbersEdit.setVisibility(View.VISIBLE);
        }
    }

    private void SavePreferencesInternetDownload(int option){
        getSharedPreferencesEditor()
                .putInt(PREFERENCES_INTERNET_DOWNLOAD, option)
                .apply();
    }

    public static int getPreferencesInternetDownload(Context context){
        return getSharedPreferences(context).getInt(PREFERENCES_INTERNET_DOWNLOAD, DEFAULT_INTERNET_DOWNLOAD);
    }

    private void SavePreferencesRandomNumberFromPlayers(boolean b){
        getSharedPreferencesEditor()
                .putBoolean(PREFERENCES_RANDOM_NUMBERS_FROM_PLAYERS, b)
                .apply();
    }

    private static boolean getPreferencesRandomNumberFromPlayers(Context context){
        return getSharedPreferences(context).getBoolean(PREFERENCES_RANDOM_NUMBERS_FROM_PLAYERS, DEFAULT_RANDOM_NUMBERS_FROM_PLAYERS);
    }

    private void SavePreferencesRandomNumbersProduct(int product){
        getSharedPreferencesEditor()
                .putInt(PREFERENCES_RANDOM_NUMBERS_PRODUCT, product)
                .apply();
    }

    private static int getPreferencesRandomNumberProduct(Context context){
        return getSharedPreferences(context).getInt(PREFERENCES_RANDOM_NUMBERS_PRODUCT, DEFAULT_RANDOM_NUMBERS_PRODUCT);
    }

    private void SavePreferencesRandomNumber(int randomNumber){
        getSharedPreferencesEditor()
                .putInt(PREFERENCES_RANDOM_NUMBERS, randomNumber)
                .apply();
    }

    private static int getPreferencesRandomNumber(Context context){
        return getSharedPreferences(context).getInt(PREFERENCES_RANDOM_NUMBERS, DEFAULT_RANDOM_NUMBERS);
    }

    public static int getPreferencesFinalRandomNumber(Context context){
        int activePlayers = PlayersActivity.getNumberActivePlayers(context);
        boolean fromPlayer = getPreferencesRandomNumberFromPlayers(context);
        int product = getPreferencesRandomNumberProduct(context);
        int randomNumbers = getPreferencesRandomNumber(context);
        int randomNumbersFromPlayers = activePlayers * product;
        if(!fromPlayer && randomNumbers >= activePlayers)
            return randomNumbers;
        return randomNumbersFromPlayers;
    }

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }

    private SharedPreferences.Editor getSharedPreferencesEditor(){
        return getSharedPreferences(this).edit();
    }
}
