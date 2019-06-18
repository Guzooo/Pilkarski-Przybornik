package pl.Guzooo.PilkarskiPrzybornik.Gry.Krol;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import pl.Guzooo.PilkarskiPrzybornik.Gry.Settings;
import pl.Guzooo.PilkarskiPrzybornik.R;

public class KingSettingsFragment extends Settings {

    private final String PREFERENCE_NAME = "settingsking";
    private final String PREFERENCE_LIVE = "live";
    private final String PREFERENCE_STAKE = "STAKE";

    private final int DEFAULT_LIVE = 5;
    private final int DEFAULT_STAKE = 3;

    private EditText life;
    private EditText stake;

    @Override
    public void SetView(View view) {
        initializationViews(view);
        life.setText(getLive() + "");
        stake.setText(getStake() + "");
        life.setHint(DEFAULT_LIVE + "");
        stake.setHint(DEFAULT_STAKE + "");
    }

    @Override
    public void Save(View view) {
        initializationViews(view);
        SharedPreferences.Editor editor = getPreferences().edit();
        Save(editor, life, PREFERENCE_LIVE, DEFAULT_LIVE);
        Save(editor, stake, PREFERENCE_STAKE, DEFAULT_STAKE);
        editor.apply();
    }

    @Override
    public void RestoreDefault() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(PREFERENCE_LIVE, DEFAULT_LIVE);
        editor.putInt(PREFERENCE_STAKE, DEFAULT_STAKE);
        editor.apply();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_king_settings;
    }

    @Override
    public SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private void initializationViews(View view) {
        life = view.findViewById(R.id.life);
        stake = view.findViewById(R.id.stake);
    }

    public int getLive() {
        SharedPreferences sharedPreferences = getPreferences();
        return sharedPreferences.getInt(PREFERENCE_LIVE, DEFAULT_LIVE);
    }

    public int getStake() {
        SharedPreferences sharedPreferences = getPreferences();
        return sharedPreferences.getInt(PREFERENCE_STAKE, DEFAULT_STAKE);
    }

    private void Save(SharedPreferences.Editor editor, EditText editText, String PREFERENCE, int DEFAULT){
        String string = editText.getText().toString().trim();
        int save = DEFAULT;
        if(!string.equals(""))
            save = Integer.valueOf(string);
        editor.putInt(PREFERENCE, save);
    }
}
