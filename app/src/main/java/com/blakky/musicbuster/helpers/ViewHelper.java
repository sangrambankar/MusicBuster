package com.blakky.musicbuster.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;


/**
 * Created by sangrambankar on 4/6/17.
 */

public class ViewHelper {

    /**
     * Helper function to show or hide a view.
     * @param view An {@link Optional} of the view.
     * @param show True if the view is to be shown or false otherwise.
     */
    public static void showOrMakeViewGone(@NonNull final Optional<View> view, final boolean show){
        if (view.isPresent()){
            view.get().setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Helper function to show or hide a view.
     * @param view An {@link Optional} of the view.
     * @param show True if the view is to be shown or false otherwise.
     */
    public static void showOrHideView(@NonNull final Optional<View> view, final boolean show){
        if (view.isPresent()){
            view.get().setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * Indicates if a view is visible or not.
     * @param view The view that will be validated.
     * @return True if it is visible or false otherwise.
     */
    public static boolean isViewVisible(@NonNull final Optional<View> view){
        return ( (view.isPresent()) && (view.get().getVisibility() == View.VISIBLE) );
    }

    /**
     * Helper method that hides the keyboard.
     * @param view View that helps to hide the keyboard (i.e EditText).
     * @param activity UI where the keyboard will be hidden.
     */
    public static void hideKeyboard(@NonNull final Optional<View> view, final Activity activity){
        Preconditions.checkNotNull(activity,"Activity cannot be null");
        try {
            if (view.isPresent()) {
                final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.get().getWindowToken(), 0);
            }
        }catch (final Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Helper function that makes sure that the EditText is not null.
     * @param editText {@link EditText}
     * @return True if the EditText is not null and empty, false otherwise.
     */
    public static boolean isEditTextEmpty(@NonNull final EditText editText){
        Preconditions.checkNotNull(editText, "EditText cannot be null");
        return ( (editText != null) && (!Strings.isNullOrEmpty(editText.getText().toString())) );
    }
}
