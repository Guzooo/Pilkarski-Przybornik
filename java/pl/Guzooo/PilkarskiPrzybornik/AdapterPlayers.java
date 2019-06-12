package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class AdapterPlayers extends Adapter {

    private Listener listener;

    @Override
    public int getOneView() {
        return R.layout.one_player_statistics;
    }

    @Override
    public int getEmptyView() {
        return R.layout.one_player_statistics;
    }

    public interface Listener{
        public void onClickActive(int id, boolean active, ViewHolder holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private Switch active;

        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            description = v.findViewById(R.id.description);
            active = v.findViewById(R.id.active);
        }

        public void setActive(boolean active){
            if(active){
                itemView.setBackgroundResource(R.color.colorPrimary);
            } else {
                itemView.setBackgroundResource(R.color.colorSecondary);
            }
        }

        public void OpenClose(){
            if(description.getVisibility() == View.VISIBLE){
                description.setVisibility(View.GONE);
            } else {
                description.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBindViewHolder(final Adapter.ViewHolder holder, int position) {

        final ViewHolder newHolder = new ViewHolder(holder.itemView);

        if (isEmptyCursor()) {
            newHolder.active.setVisibility(View.GONE);
            newHolder.name.setVisibility(View.GONE);
            newHolder.description.setVisibility(View.VISIBLE);
            newHolder.description.setText(R.string.click_plus_to_add_player);

            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getListener() != null) {
                        getListener().onClickEmpty();
                    }
                }
            });
        } else if (getCursor().moveToPosition(position)) {
            final Player player = new Player();
            player.getOfCursor(getCursor(), getContext());

            newHolder.name.setText(player.getName());
            newHolder.description.setText(player.getDescription(getContext()));
            newHolder.active.setChecked(player.isActive());
            newHolder.setActive(player.isActive());

            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getListener() != null && getCursor().moveToPosition(holder.getAdapterPosition())) {
                        getListener().onClick(player, holder, getCursor().getInt(0));
                    }
                }
            });

            newHolder.active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && getCursor().moveToPosition(holder.getAdapterPosition())) {
                        listener.onClickActive(getCursor().getInt(0), newHolder.active.isChecked(), newHolder);
                    }
                }
            });
        }
    }

    public void setListener(Adapter.Listener listener, Listener newListener) {
        setListener(listener);
        this.listener = newListener;
    }

    public AdapterPlayers(Cursor cursor, Context context) {
        super(cursor, context);
    }
}
