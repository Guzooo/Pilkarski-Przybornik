package pl.Guzooo.PilkarskiPrzybornik;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlayersActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private AdapterPlayers adapter;

    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButtonMenuDel;
    private FloatingActionButton floatingActionButtonMenuEdit;
    private FloatingActionButton floatingActionButtonAdd;
    private FloatingActionButton floatingActionButtonEdit;
    private FloatingActionButton floatingActionButtonShare;
    private FloatingActionButton floatingActionButtonDel;

    private boolean openMenu;
    private boolean deleteMenu;
    private boolean editMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButtonMenuDel = findViewById(R.id.floating_action_button_menu_del);
        floatingActionButtonMenuEdit = findViewById(R.id.floating_action_button_menu_edit);
        floatingActionButtonAdd = findViewById(R.id.floating_action_button_add);
        floatingActionButtonEdit = findViewById(R.id.floating_action_button_edit);
        floatingActionButtonShare = findViewById(R.id.floating_action_button_share);
        floatingActionButtonDel = findViewById(R.id.floating_action_button_del);

        hideFloatMenu();

        db = Database.getWrite(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshCursor();
        setAdapter();
    }

    private void refreshCursor(){
        cursor = db.query(Player.databaseName,
                Player.onCursor,
                null, null, null, null,
                "NAME");
        if(adapter != null)
            adapter.ChangeCursor(cursor);
    }

    private int getIdOfLastPlayer(){
        int id = 0;
        Cursor cursor = db.query(Player.databaseName,
                new String[]{"_id"},
                null, null, null, null, null);
        if(cursor.moveToLast()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    private int getPositionOfPlayerInCursor(int id){
        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(0) == id){
                    return cursor.getPosition();
                }
            }while (cursor.moveToNext());
        }
        return 0;
    }

    private void setAdapter(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterPlayers(cursor, this);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new Adapter.Listener() {

            @Override
            public void onClick(Model model, Adapter.ViewHolder holder, int id) {
                if(deleteMenu) {
                    DelPlayer(holder, id);
                } else if (editMenu){
                    EditPlayer(id);
                } else {
                    hideAllMenu();
                    AdapterPlayers.ViewHolder newHolder = new AdapterPlayers.ViewHolder(holder.itemView);
                    newHolder.OpenClose();
                    adapter.notifyItemChanged(holder.getAdapterPosition(), holder);
                }
            }

            @Override
            public void onClickEmpty() {
                AnimFloatingButtonSeeMe();
            }

        }, new AdapterPlayers.Listener() {

            @Override
            public void onClickActive(int id, boolean active, AdapterPlayers.ViewHolder holder) {
                hideAllMenu();
                holder.setActive(active);
                Player player = new Player();
                player.getOfId(id, getApplicationContext());
                player.setActive(active);
                player.update(getApplicationContext());
                refreshCursor();
            }
        });
    }

    public void ClickMenu(View v){
        if(cursor.getCount() == 0){
            AddPlayerWindow();
        } else {
            AnimFloatingButtonRotate();
        }
    }

    public void ClickMenuDel(View v){
        hideDelMenu();
    }

    public void ClickMenuEdit(View v){
        hideEditMenu();
    }

    public void ClickAdd(View v){
        AnimFloatingButtonRotate();
        AddPlayerWindow();
    }

    public void ClickEdit(View v){
        AnimFloatingButtonRotate();
        showEditMenu();
    }

    public void ClickShare(View v){
        AnimFloatingButtonRotate();
        LinksManagement.sharePlayers(cursor, this);
    }

    public void ClickDel(View v){
        AnimFloatingButtonRotate();
        showDelMenu();
    }

    private void AddPlayerWindow(){
        final EditText editText = CreateEditText(); //TODO jakieś marginesy, opcjonalnie zrobić layout i tu wstawić 🤷‍♂️
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
                        AddPlayerWindow();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private EditText CreateEditText(){
        EditText editText = new EditText(this);
        editText.setTextColor(getResources().getColor(R.color.primaryText));
        return editText;
    }

    private String FromEditText(EditText editText){
        return editText.getText().toString().trim();
    }

    private void AddPlayer(String name){
        if(name.equals("")){
            Toast.makeText(this, R.string.error_empty_name, Toast.LENGTH_SHORT).show();
        } else {
            Player player = new Player();
            player.setName(name);
            player.insert(this);
            notifyAdapterOnAddPlayer(); //TODO: na przyszłość jak nie bedzie imienia to weź nie chowaj się
        }
    }

    private void notifyAdapterOnAddPlayer(){
        if(adapter.isEmptyCursor())
            adapter.notifyItemRemoved(0);
        refreshCursor();
        adapter.notifyItemInserted(getPositionOfPlayerInCursor(getIdOfLastPlayer()));
    }

    private void DelPlayer(Adapter.ViewHolder holder, int id){
        final Player player = new Player();
        player.getOfId(id, getApplicationContext());
        player.delete(getApplicationContext());
        refreshCursor();
        adapter.notifyItemRemoved(holder.getAdapterPosition());
        if(cursor.getCount() == 0)
            hideDelMenu();
        Snackbar.make(holder.itemView, getString(R.string.del_this_player, player.getName()), Snackbar.LENGTH_LONG)
                .setAction(R.string.back, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        player.insert(getApplicationContext());
                        notifyAdapterOnAddPlayer();
                        Toast.makeText(getApplicationContext(), R.string.player_return, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void EditPlayer (int id){
        final Player player = new Player();
        player.getOfId(id, this);
        final EditText editText = CreateEditText(); //TODO jakieś marginesy, opcjonalnie zrobić layout i tu wstawić 🤷‍♂️
        editText.setText(player.getName());
        editText.setHint(player.getName());
        new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)
                .setTitle(R.string.enter_player_name)
                .setView(editText)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updatePlayer(player, editText);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void updatePlayer(Player player, EditText editText){
        player.setName(editText.getText().toString().trim());
        player.update(this);
        refreshCursor();
        adapter.notifyDataSetChanged(); //TODO: moze juz w tym refreshu w adapterze;
    }

    private void AnimFloatingButtonSeeMe(){
        ObjectAnimator animY = ObjectAnimator.ofFloat(floatingActionButton, "scaleY", 1, 1.5f, 0.75f, 1.25f, 1);
        ObjectAnimator animX = ObjectAnimator.ofFloat(floatingActionButton, "scaleX", 1, 1.5f, 0.75f, 1.25f, 1);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animY, animX);
        animSetXY.setDuration(1000);
        animSetXY.start();
    }

    private void AnimFloatingButtonRotate() {
        int rotateTo;
        if (floatingActionButton.getRotation() == 0) {
            rotateTo = 135;
            showFloatMenu();
        } else {
            rotateTo = 0;
            hideFloatMenu();
        }

        ObjectAnimator
                .ofFloat(floatingActionButton, "rotation", rotateTo)
                .setDuration(250)
                .start();
    }

    private boolean hideAllMenu(){
        if(openMenu){
            AnimFloatingButtonRotate();
            return true;
        }

        if(deleteMenu){
            hideDelMenu();
            return true;
        }

        if(editMenu){
            hideEditMenu();
            return true;
        }

        return false;
    }

    private void hideDelMenu(){
        floatingActionButtonMenuDel.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                floatingActionButton.show();
            }
        });
        deleteMenu = false;
    }

    private void showDelMenu(){
        floatingActionButton.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                floatingActionButtonMenuDel.show();
            }
        });
        deleteMenu = true;
    }

    private void hideEditMenu(){
        floatingActionButtonMenuEdit.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                floatingActionButton.show();
            }
        });
        editMenu = false;
    }

    private void showEditMenu(){
        floatingActionButton.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                floatingActionButtonMenuEdit.show();
            }
        });
        editMenu = true;
    }

    private void hideFloatMenu() {
        floatingActionButtonAdd.hide();
        floatingActionButtonEdit.hide();
        floatingActionButtonShare.hide();
        floatingActionButtonDel.hide();
        openMenu = false;
    }

    private void showFloatMenu() {
        floatingActionButtonAdd.show();
        floatingActionButtonEdit.show();
        floatingActionButtonShare.show();
        floatingActionButtonDel.show();
        openMenu = true;
    }

    public static int getNumberActivePlayers(Context context){
        try {
            SQLiteDatabase db = Database.getRead(context);
            Cursor cursor = db.query(Player.databaseName,
                    null,
                    "ACTIVE = ?",
                    new String[]{Integer.toString(1)},
                    null, null, null);
            int activePlayers = cursor.getCount();
            cursor.close();
            db.close();
            return activePlayers;
        } catch (SQLException e){
            Database.ShowError(context);
            return 0;
        }
    }
}