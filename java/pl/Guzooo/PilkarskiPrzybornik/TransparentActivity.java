package pl.Guzooo.PilkarskiPrzybornik;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TransparentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri link = getIntent().getData();
        LinksManagement.getPlayers(link, this, this);
    }
}
