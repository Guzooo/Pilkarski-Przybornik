package pl.Guzooo.PilkarskiPrzybornik.Gry.WyborBramkarza;

import android.content.Context;

public class SelectGoalkeeperSettings {

    private static final String PREFERENCE_NAME = "settingsselectgoalkeeper";

    //TODO: dla aktualizujących z 5
    public static void DelPreferences(Context context) {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
