package com.isoterik.android.mybaby.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;

import com.isoterik.android.mybaby.R;

public class AlertUtil
{
    public static AlertDialog buildAlert (Context context, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Okay", new AlertDialog.OnClickListener()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        changeDialogButtonsColor(dialog, context.getResources().getColor(R.color.colorPrimaryDark));
        return dialog;
    }

    public static AlertDialog buildConfirmDialog (Context context, String title, String message, final Runnable onConfirm)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Cancel", new AlertDialog.OnClickListener()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new AlertDialog.OnClickListener()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        onConfirm.run();
                    }
                });

        AlertDialog dialog = builder.create();
        changeDialogButtonsColor(dialog, context.getResources().getColor(R.color.colorPrimaryDark));
        return dialog;
    }

    private static void changeDialogButtonsColor (AlertDialog dialog, int color)
    {
        dialog.setOnShowListener( dialogInterface ->
        {
            if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);

            if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
        });
    }

    public static void showAlert (Context context, String title, String message)
    {
        buildAlert(context, title, message).show();
    }

    public static void showConfirmDialog (Context context, String title, String message, final Runnable onConfirm)
    {
        buildConfirmDialog(context, title, message, onConfirm).show();
    }
}
