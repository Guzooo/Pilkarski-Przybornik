package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public abstract class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Cursor cursor;
    private Listener listener;
    private boolean emptyCursor;

    public abstract int getOneView();
    public abstract int getEmptyView();

    public interface Listener{
        public void onClick(Model model, ViewHolder holder, int id);
        public void onClickEmpty();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(!emptyCursor) {
            view = LayoutInflater.from(parent.getContext()).inflate(getOneView(), parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(getEmptyView(), parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(cursor.getCount() == 0){
            emptyCursor = true;
            return 1;
        }
        emptyCursor = false;
        return cursor.getCount();
    }

    public Adapter(Cursor cursor){
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Listener getListener() {
        return listener;
    }

    public boolean isEmptyCursor() {
        return emptyCursor;
    }
}
