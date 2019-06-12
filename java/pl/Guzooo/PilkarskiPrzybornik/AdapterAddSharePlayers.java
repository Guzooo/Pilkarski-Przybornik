package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterAddSharePlayers extends RecyclerView.Adapter<AdapterAddSharePlayers.ViewHolder> {

    private ArrayList<Player> players;
    private ArrayAdapter<String> adapterSpinner;
    private static ArrayList<Integer> positions = new ArrayList<>();

    private Cursor cursor;
    private SQLiteDatabase db;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Spinner spinner;
        int position;

        public ViewHolder(View v, ArrayAdapter<String> adapter){
            super(v);
            name = v.findViewById(R.id.name);
            spinner = v.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    positions.set(position, i-2); //TODO: jak wybierzemy gracza który już jest wybrany, to zmieni się w tamtym miejscu na dodaj nowego gracza
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    @Override
    public AdapterAddSharePlayers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_share_player, parent, false);
        return new ViewHolder(layout, adapterSpinner);
    }

    @Override
    public void onBindViewHolder(AdapterAddSharePlayers.ViewHolder viewHolder, int position) {
        viewHolder.name.setText(players.get(position).getName());
        viewHolder.position = viewHolder.getAdapterPosition();

        int i = adapterSpinner.getPosition(players.get(position).getName());
        int j = positions.get(position);
        if(j == -3 && i >= 0){
            viewHolder.spinner.setSelection(i);
        } else if (j > -3){
            viewHolder.spinner.setSelection(j + 2);
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public AdapterAddSharePlayers(ArrayList<Player> players, Context context){
        this.players = players;
        adapterSpinner = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapterSpinner.add(context.getString(R.string.new_player));
        adapterSpinner.add(context.getString(R.string.skip));
        db = Database.getRead(context);
        cursor = db.query(Player.databaseName,
                Player.onCursor,
                null, null, null, null, "NAME");
        if(cursor.moveToFirst()){
            do{
                Player player = new Player();
                player.getOfCursor(cursor, context);
                adapterSpinner.add(player.getName());
            } while (cursor.moveToNext());
        }

        positions.clear();
        for (Player player : players){
            positions.add(-3);
        }
    }

    public void SaveChanges(Context context){
        for(int i = 0; i < players.size(); i++){
            int position = positions.get(i);
            if(position < -1){
                players.get(i).insert(context);
            } else if (position > -1){
                if(cursor.moveToPosition(position)){
                    Player player = new Player();
                    player.getOfCursor(cursor, context);
                    player.getOfPlayer(players.get(i));
                    player.update(context);
                }
            }
        }
        cursor.close();
        db.close();
    }
}
