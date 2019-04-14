package pl.Guzooo.PilkarskiPrzybornik;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterAddSharePlayers extends RecyclerView.Adapter<AdapterAddSharePlayers.ViewHolder> {

    ArrayList<Player> players;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Spinner spinner;

        public ViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.name);
            spinner = v.findViewById(R.id.spinner);
            //TODO: ustaw adapter; spinner.setAdapter();
        }
    }

    @Override
    public AdapterAddSharePlayers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_share_player, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(AdapterAddSharePlayers.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(players.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public AdapterAddSharePlayers(ArrayList<Player> players){
        this.players = players;
    }

}
