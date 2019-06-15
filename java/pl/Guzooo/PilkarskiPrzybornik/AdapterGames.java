package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
        private Button button;
        private TextView localInfo;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            icon = v.findViewById(R.id.icon);
            description = v.findViewById(R.id.description);
            button = v.findViewById(R.id.button);
            localInfo = v.findViewById(R.id.local_info);
        }
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        ViewHolder newHolder = new ViewHolder(holder.itemView);

        if(getCursor().moveToPosition(position)){
            final Game game = new Game();
            game.getOfCursor(getCursor(), getContext());

            newHolder.title.setText(game.getName(getContext()));
            newHolder.icon.setImageResource(game.getImage(getContext()));
            newHolder.description.setText(game.getShortDescription(getContext()));
            newHolder.button.setText(game.getButtonsName(getContext()).get(game.getButtonsOrder().get(0)));
            newHolder.localInfo.setVisibility(View.GONE);

            newHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Games.setCurrentGame(game);
                    game.getGameInfo().Play(game.getButtonsOrder().get(0), getContext());
                }
            });

            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Games.setCurrentGame(game);
                    Intent intent = new Intent(getContext(), GameInfoActivity.class);
                    getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        //TODO:Usuń
        return 2;
    }

    public AdapterGames(Cursor cursor, Context context){
        super(cursor, context);
    }
}
