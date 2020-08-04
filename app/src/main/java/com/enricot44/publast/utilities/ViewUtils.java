package com.enricot44.publast.utilities;

import android.content.Context;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.enricot44.publast.R;
import com.google.android.material.snackbar.Snackbar;

public class ViewUtils {
    public static void showSnackbar(Context context, CoordinatorLayout coordinatorLayout, int messageId, final OnUndo onUndo) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, messageId, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));

        if (onUndo != null) {
            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUndo.undo();
                }
            });
        }

        snackbar.show();
    }


    public interface OnUndo {
        void undo();
    }
}
