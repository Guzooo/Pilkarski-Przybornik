package pl.Guzooo.PilkarskiPrzybornik.Gry;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.Guzooo.PilkarskiPrzybornik.R;

public abstract class Settings extends DialogFragment {
    public abstract void SetView(View view);
    public abstract void Save(View view);
    public abstract void RestoreDefault();
    public abstract int getLayout();
    public abstract SharedPreferences getPreferences();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View layout = getActivity().getLayoutInflater().inflate(getLayout(), null);
        SetView(layout);
        return new AlertDialog.Builder(getContext(), R.style.AppTheme_AlertDialog)
                .setTitle(R.string.setting)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Save(layout);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.restore_default, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RestoreDefault();
                    }
                })
                .setView(layout)
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        SetView(layout);
        return layout;
    }
}
