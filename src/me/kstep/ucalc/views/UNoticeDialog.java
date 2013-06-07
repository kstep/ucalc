package me.kstep.ucalc.views;

import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

public class UNoticeDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private CharSequence title;
    private CharSequence message;

	public static void show(Activity context, CharSequence title, CharSequence message) {
		UNoticeDialog dialog = new UNoticeDialog(title, message);
		dialog.show(context.getFragmentManager(), "popup");
	}
	
    public UNoticeDialog(CharSequence title, CharSequence message) {
        super();
        this.title = title;
        this.message = message;
    }

    public void onClick(DialogInterface dialog, int id) {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton("Dismiss", this);

        return builder.create();
    }
}
