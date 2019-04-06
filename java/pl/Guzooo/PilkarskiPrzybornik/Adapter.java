package pl.Guzooo.PilkarskiPrzybornik;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
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
    public int getItemViewType(int position) {
        if(emptyCursor){
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0) {
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

    public Adapter(Cursor cursor, Context context){
        this.context= context;
        this.cursor = cursor;
    }

    public void ChangeCursor(Cursor cursor){
        this.cursor.close();
        this.cursor = cursor;
    }

    public Context getContext() {
        return context;
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
