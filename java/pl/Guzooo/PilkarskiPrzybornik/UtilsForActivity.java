package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class UtilsForActivity {

    private static final int MAXIMAL_TIME_DOUBLE_TAB = 500;

    private static long tabTime;

    public static boolean DoubleTab(){
        if(tabTime + MAXIMAL_TIME_DOUBLE_TAB > System.currentTimeMillis()){
            return true;
        } else {
            tabTime = System.currentTimeMillis();
            Log.d("czas", tabTime + "");
            return false;
        }
    }

    public static void ToastDoubleTabExit(Context context){
        Toast.makeText(context, R.string.double_tab_exit, Toast.LENGTH_SHORT).show();
    }
}
