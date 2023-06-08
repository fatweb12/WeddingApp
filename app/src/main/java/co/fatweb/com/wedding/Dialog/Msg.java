package co.fatweb.com.wedding.Dialog;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import co.fatweb.com.wedding.R;


public class Msg {

    private int chosen = 0;
    private Snackbar snackbar;

    public static void Toast(String message, Context c) {
        Toast.makeText(c, message, Toast.LENGTH_LONG).show();
    }

    public static void Print(String message){
        System.out.println(message);
    }

    public static void Dialog(String message, Context context){
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText("OK")
                .positiveColorRes(R.color.colorAccent)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public  void SnackbarMessage(String message, View view){
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                })
                .setActionTextColor(Color.parseColor("#3DA5AD"));
        snackbar.show();
    }

//    public MaterialDialog ConfirmDialog(String title, String message, Context context){
//        MaterialDialog.Builder materialDialog ;
//
//        materialDialog = new MaterialDialog.Builder(context)
//                .title(title)
//                .content(message)
//                .canceledOnTouchOutside(false);
//
//        return materialDialog;
//    }

}
