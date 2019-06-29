package pl.Guzooo.PilkarskiPrzybornik.Gry.WyborBramkarza;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import pl.Guzooo.PilkarskiPrzybornik.Gry.Settings;
import pl.Guzooo.PilkarskiPrzybornik.PlayersActivity;
import pl.Guzooo.PilkarskiPrzybornik.R;

public class SelectGoalkeeperSettings extends Settings {

    private final String PREFERENCE_NAME = "settingsselectgoalkeeper";
    private final String PREFERENCE_PROBABILITY_FROM_PLAYERS = "probabilityfromplayers";
    private final String PREFERENCE_PROBABILITY_PRODUCT = "probabilityproduct";
    private final String PREFERENCE_PROBABILITY = "probability";

    private final boolean DEFAULT_PROBABILITY_FROM_PLAYERS = true;
    private final int DEFAULT_PROBABILITY_PRODUCT = 3;
    private final int DEFAULT_PROBABILITY = 0;

    private Switch probabilityFromPlayer;
    private TextView probability;
    private EditText probabilityEdit;
    private View buttons;
    private Button minus;
    private Button plus;

    private int product;

    @Override
    public void SetView(View view, final Context context) {
        initializationViews(view);
        probabilityFromPlayer.setChecked(getProbabilityFromPlayer(context));
        probability.setText(getProbabilityProduct(context) * PlayersActivity.getNumberActivePlayers(context) + "");
        probabilityEdit.setText(getProbability(context) + "");
        probabilityEdit.setHint(DEFAULT_PROBABILITY + "");

        ShowHideButtons();

        probabilityFromPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializationViews(null);
                ShowHideButtons();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializationViews(null);
                ClickMinus(view);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializationViews(null);
                ClickPlus(view);
            }
        });
       
        product = getProbabilityProduct(context);
    }

    @Override
    public void Save(View view, Context context) {
        initializationViews(view);
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(PREFERENCE_PROBABILITY_FROM_PLAYERS, probabilityFromPlayer.isChecked());
        editor.putInt(PREFERENCE_PROBABILITY_PRODUCT, product);
        editor.putInt(PREFERENCE_PROBABILITY, getValueSave(probabilityEdit, DEFAULT_PROBABILITY));
        editor.apply();
    }

    @Override
    public void RestoreDefault(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(PREFERENCE_PROBABILITY_FROM_PLAYERS, DEFAULT_PROBABILITY_FROM_PLAYERS);
        editor.putInt(PREFERENCE_PROBABILITY_PRODUCT, DEFAULT_PROBABILITY_PRODUCT);
        editor.putInt(PREFERENCE_PROBABILITY, DEFAULT_PROBABILITY);
        editor.apply();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_goalkeeper_settings;
    }

    @Override
    public SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private void initializationViews(View view){
        if(view != null){
            probabilityFromPlayer = view.findViewById(R.id.probability_from_players);
            probability = view.findViewById(R.id.probability);
            probabilityEdit = view.findViewById(R.id.probability_edit);
            buttons = view.findViewById(R.id.buttons);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
        } else {
            probabilityFromPlayer = getAlertDialog().findViewById(R.id.probability_from_players);
            probability = getAlertDialog().findViewById(R.id.probability);
            probabilityEdit = getAlertDialog().findViewById(R.id.probability_edit);
            buttons = getAlertDialog().findViewById(R.id.buttons);
            minus = getAlertDialog().findViewById(R.id.minus);
            plus = getAlertDialog().findViewById(R.id.plus);
        }

    }

    public void ClickPlus(View v){
        product++;
        probability.setText(product * PlayersActivity.getNumberActivePlayers(v.getContext()) + "");
    }

    public void ClickMinus(View v){
        if (product > 1){
            product--;
        }
        probability.setText(product * PlayersActivity.getNumberActivePlayers(v.getContext()) + "");
    }

    private boolean getProbabilityFromPlayer(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        return  sharedPreferences.getBoolean(PREFERENCE_PROBABILITY_FROM_PLAYERS, DEFAULT_PROBABILITY_FROM_PLAYERS);
    }

    private int getProbabilityProduct(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getInt(PREFERENCE_PROBABILITY_PRODUCT, DEFAULT_PROBABILITY_PRODUCT);
    }

    private int getProbability(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getInt(PREFERENCE_PROBABILITY, DEFAULT_PROBABILITY);
    }

    public int getSavedProbability(Context context){
        int activePlayers = PlayersActivity.getNumberActivePlayers(context);
        int probabilityFromPlayer = activePlayers * getProbabilityProduct(context);
        if(getProbabilityFromPlayer(context)){
            return probabilityFromPlayer;
        }
        int probability = getProbability(context);
        if(probability >= activePlayers)
            return probability;
        else
            return probabilityFromPlayer;
    }

    private void ShowHideButtons(){
        if(probabilityFromPlayer.isChecked()){
            buttons.setVisibility(View.VISIBLE);
            probability.setVisibility(View.VISIBLE);
            probabilityEdit.setVisibility(View.GONE);
        } else {
            buttons.setVisibility(View.GONE);
            probability.setVisibility(View.GONE);
            probabilityEdit.setVisibility(View.VISIBLE);
        }
    }

    private int getValueSave(EditText editText, int DEFAULT){
        if(!editText.getText().toString().trim().equals("")){
            return Integer.valueOf(editText.getText().toString().trim() + "");
        }
        return DEFAULT;
    }
}
