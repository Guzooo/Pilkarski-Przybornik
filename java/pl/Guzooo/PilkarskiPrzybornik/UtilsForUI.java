package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

public class UtilsForUI {

    public static Button getButton(Context context) {
        Button button = new Button(context);
        button.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorSecondary), PorterDuff.Mode.MULTIPLY);
        return button;
    }
}
