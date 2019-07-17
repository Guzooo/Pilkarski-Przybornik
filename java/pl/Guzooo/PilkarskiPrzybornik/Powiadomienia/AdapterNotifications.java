package pl.Guzooo.PilkarskiPrzybornik.Powiadomienia;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.Guzooo.PilkarskiPrzybornik.R;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolder> {

    private ArrayList<Notification> notifications = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView message;
        private LinearLayout buttonsBox;
        private TextView data;

        public ViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.title);
            message = v.findViewById(R.id.message);
            buttonsBox = v.findViewById(R.id.buttons_box);
            data = v.findViewById(R.id.data);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification, viewGroup, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getInfo());
        holder.data.setText(notification.getData());
        holder.buttonsBox.removeAllViews();
        if(notification.getButtons().size() != 0){
            holder.buttonsBox.setVisibility(View.VISIBLE);
            for (int i = 0; i < notification.getButtons().size(); i++) {
                holder.buttonsBox.addView(notification.getButtons().get(i));
            }
        } else {
            holder.buttonsBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void delAllNotifications(){
        int to = notifications.size();
        notifications.clear();
        notifyItemRangeRemoved(0, to);

    }

    public void addNotification(Notification notification){
        notifications.add(notification);
        notifyItemInserted(notifications.size() -1);
    }
}
