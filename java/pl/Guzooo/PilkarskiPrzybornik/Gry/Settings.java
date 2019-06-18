package pl.Guzooo.PilkarskiPrzybornik.Gry;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.Guzooo.PilkarskiPrzybornik.R;

public abstract class Settings extends DialogFragment {
    public abstract void SetView(View view, Context context);
    public abstract void Save(View view, Context context);
    public abstract void RestoreDefault(Context context);
    public abstract int getLayout();
    public abstract SharedPreferences getPreferences(Context context);

    private static DialogListener dialogListener;

    public interface DialogListener{
        void Dismiss();
    }

    public void setDialogListener(DialogListener dialogListener){
        Settings.dialogListener = dialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View layout = getActivity().getLayoutInflater().inflate(getLayout(), null);
        SetView(layout, getContext());
        return new AlertDialog.Builder(getContext(), R.style.AppTheme_AlertDialog)
                .setTitle(R.string.setting)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Save(layout, getContext());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.restore_default, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RestoreDefault(getContext());
                    }
                })
                .setView(layout)
                .create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dialogListener != null) {
            dialogListener.Dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        SetView(layout, getContext());
        return layout;
    }
}
