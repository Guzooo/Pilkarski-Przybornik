package pl.Guzooo.PilkarskiPrzybornik;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

public class TransparentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri link = getIntent().getData();
        if(link == null) {
            finish();
            return;
        }

        LinksManagement.getPlayers(link, this);

        RecyclerView recyclerView = new RecyclerView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
      //  AdapterAddSharePlayers adapter = new AdapterAddSharePlayers();



        /*a.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //finish();
            }
        });*/ //TODO: metoda zwraca AlertDialog, ustawiamy słuchacza że jak się wyłączy to i aktywność też
    }
}
