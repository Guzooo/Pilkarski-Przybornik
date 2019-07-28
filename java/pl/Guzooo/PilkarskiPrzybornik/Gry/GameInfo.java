package pl.Guzooo.PilkarskiPrzybornik.Gry;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

public abstract class GameInfo {
    public abstract String getName(Context context);
    public abstract int getIcon(Context context);
    public abstract String getDescription(Context context);
    public abstract String getShortDescription(Context context);
    public abstract ArrayList<String> getButtons(Context context);
    public abstract void Play(int buttonId, Context context);
    public abstract void Reset(Context context);
    public abstract Settings getSettings();
    public abstract boolean getSpecialView();
}
