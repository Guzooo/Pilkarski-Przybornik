package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterGames extends Adapter{

    @Override
    public int getOneView() {
        return R.layout.game_info;
    }

    @Override
    public int getEmptyView() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView icon;
        private TextView description;
        private TextView localInfo;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            icon = v.findViewById(R.id.icon);
            description = v.findViewById(R.id.description);
            localInfo = v.findViewById(R.id.local_info);
        }
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        ViewHolder newHolder = new ViewHolder(holder.itemView);

        if(getCursor().moveToPosition(position)){
            final Game game = new Game();
            game.getOfCursor(getCursor(), getContext());

            newHolder.title.setText(game.getName());
            newHolder.icon.setImageResource(game.getImage());
            newHolder.description.setText(game.getDescription());

            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), GameInfoActivity.class);
                    intent.putExtra(GameInfoActivity.EXTRA_ID, game.getId());
                    getContext().startActivity(intent);
                }
            });
        }

    }

    public AdapterGames(Cursor cursor, Context context){
        super(cursor, context);
    }
}
