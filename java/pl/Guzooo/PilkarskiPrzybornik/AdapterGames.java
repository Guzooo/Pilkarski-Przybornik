package pl.Guzooo.PilkarskiPrzybornik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
        private ImageView settings;
        private Button button;
        private TextView localInfo;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            icon = v.findViewById(R.id.icon);
            description = v.findViewById(R.id.description);
            settings = v.findViewById(R.id.setting);
            button = v.findViewById(R.id.button);
            localInfo = v.findViewById(R.id.local_info);
        }
    }

    @Override
    public void onBindViewHolder(final Adapter.ViewHolder holder, int position) {
        final ViewHolder newHolder = new ViewHolder(holder.itemView);

        if(getCursor().moveToPosition(position+1)){//TODO:po dodaniu maszyny losującej kasujemy "+1"
            final Game game = new Game();
            game.getOfCursor(getCursor(), getContext());

            newHolder.title.setText(game.getName(getContext()));
            newHolder.icon.setImageResource(game.getImage(getContext()));
            newHolder.description.setText(game.getShortDescription(getContext()));
            newHolder.button.setText(game.getButtonsName(getContext()).get(game.getButtonsOrder().get(0)));
            newHolder.localInfo.setVisibility(View.GONE);

            if(game.getSettings() == null){
                newHolder.settings.setVisibility(View.INVISIBLE);
            } else {
                newHolder.settings.setVisibility(View.VISIBLE);
            }

            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { Games.setCurrentGame(game);
                    Intent intent = new Intent(getContext(), GameInfoActivity.class);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), newHolder.icon, "gameimage");
                    getContext().startActivity(intent, optionsCompat.toBundle());
                }
            });

            newHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Games.setCurrentGame(game);
                    game.getGameInfo().Reset(getContext());
                    game.getGameInfo().Play(game.getButtonsOrder().get(0), getContext());
                    game.update(getContext());
                }
            });

            newHolder.settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    game.getSettings().show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "settings");
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
