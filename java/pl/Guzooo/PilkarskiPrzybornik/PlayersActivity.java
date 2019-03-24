package pl.Guzooo.PilkarskiPrzybornik;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
    }

    public void ClickAddPlayer(View v){
        final EditText editText = CreateEditText();

        new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
                .setTitle(R.string.enter_player_name)
                .setView(editText)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddPlayer(FromEditText(editText));
                    }
                })
                .setNeutralButton(R.string.next_player, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddPlayer(FromEditText(editText));
                        ClickAddPlayer(null);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private EditText CreateEditText(){
        EditText editText = new EditText(this);
        editText.setTextColor(getResources().getColor(R.color.primaryText));
        return editText;
    }

    private boolean AddPlayer(String name){
        if(name.equals("")){
            Toast.makeText(this, R.string.error_empty_name, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Player player = new Player();
            player.setName(name);
            player.insert(this);
            return true;
        }
    }

    private String FromEditText(EditText editText){
        return editText.getText().toString().trim();
    }
}