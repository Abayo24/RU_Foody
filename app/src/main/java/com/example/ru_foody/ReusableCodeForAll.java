package com.example.ru_foody;

import android.app.AlertDialog;
import android.content.Context;

public class ReusableCodeForAll {

    public static void ShowAlert(Context context, String title , String message){

        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // cannot be dismissed by clicking outside
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            // Dismiss the dialog when the button is clicked
            dialog.dismiss();
        });
        // Set the title and message of the dialog
        builder.setTitle(title).setMessage(message).show();
    }
}
