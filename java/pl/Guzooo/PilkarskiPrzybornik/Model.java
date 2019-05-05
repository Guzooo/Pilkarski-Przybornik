package pl.Guzooo.PilkarskiPrzybornik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public abstract class Model {

    private int id;

    public abstract String[] onCursor();
    public abstract String databaseName();

    public Model(){
        Empty();
    }

    public abstract void Empty();

    public void getOfId(int id, Context context){
        SQLiteDatabase db = Database.getRead(context);
        Cursor cursor = db.query(databaseName(),
                onCursor(),
                "_id = ?",
                new String[]{Integer.toString(id)},
                null, null, null);

        if(cursor.moveToFirst()) {
            getOfCursor(cursor);
        } else {
            Empty();
        }

        cursor.close();
        db.close();
    }

    public abstract void getOfCursor(Cursor cursor);

    public void insert (Context context){
        try{
            SQLiteDatabase db = Database.getWrite(context);
            db.insert(databaseName(), null, getContentValues());
            db.close();
        } catch (SQLiteException e){
            Database.ShowError(context);
        }
    }

    public void update (Context context){
        try {
            SQLiteDatabase db = Database.getWrite(context);
            db.update(databaseName(),
                    getContentValues(),
                    "_id = ?",
                    new String[] {Integer.toString(getId())});
            db.close();
        } catch (SQLiteException e){
            Database.ShowError(context);
        }
    }

    public void delete (Context context){
        try {
            SQLiteDatabase db = Database.getWrite(context);
            db.delete(databaseName(),
                    "_id = ?",
                    new String[] {Integer.toString(getId())});
            db.close();
        } catch (SQLiteException e){
            Database.ShowError(context);
        }
    }

    public abstract ContentValues getContentValues();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
