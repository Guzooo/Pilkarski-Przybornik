package pl.Guzooo.PilkarskiPrzybornik.Gry;

import android.content.Context;

import java.util.ArrayList;

public abstract class GameInfo {
    public abstract String getName(Context context);
    public abstract int getIcon(Context context);
    public abstract String getDescription(Context context);
    public abstract ArrayList<String> getButtons(Context context);
    public abstract void restartGame();
}
