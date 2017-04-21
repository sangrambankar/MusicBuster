package com.blakky.musicbuster.helpers;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.blakky.musicbuster.R;
import com.google.common.base.Preconditions;



/**
 * Created by sangrambankar on 4/6/17.
 */
public class DialogHelper {

    /**
     * Shows a Dialog with a message
     * @param context The view
     * @param titleResID The view
     * @param messageResID Description
     */
    public static void showMessageDialog(final Context context,
                                         @StringRes final int titleResID,
                                         @StringRes final int messageResID){
        new MaterialDialog.Builder(context)
                .title(titleResID)
                .content(messageResID)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .show();
    }

    /**
     * Shows a confirm Dialog.
     * @param context The view
     * @param titleResID title
     * @param messageResID Description
     * @param onPositiveCallback An implementation of {@link com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback}
     * @param onNegativeCallback An implementation of {@link com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback}
     */
    public static void confirmDialogAction(final Context context,
                                           @StringRes final int titleResID,
                                           @StringRes final int messageResID,
                                           MaterialDialog.SingleButtonCallback onPositiveCallback,
                                           MaterialDialog.SingleButtonCallback onNegativeCallback){
        new MaterialDialog.Builder(context)
                .title(titleResID)
                .content(messageResID)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("Cancel")
                .negativeColorRes(R.color.colorPrimary)

                .onPositive(onPositiveCallback)
                .onNegative(onNegativeCallback)
                .show();
    }

    /**
     * Shows a Simple Dialog with a list of choices
     * @param mContext Current Context
     * @param titleResId Dialog Title String Resource ID
     * @param stringIDs Arrays of choices Title Resource IDs
     * @param drawableIDs List of choices Drawable Resource IDs
     * @param listCallback An implementation of  {@link com.afollestad.materialdialogs.MaterialDialog.ListCallback} if needed or null otherwise
     */
    public static void showListItemsDialog(final Context mContext, @StringRes final int titleResId,
                                           @ArrayRes final int[] stringIDs, @ArrayRes final int[] drawableIDs,
                                           @Nullable final MaterialDialog.ListCallback listCallback){
        Preconditions.checkArgument(stringIDs.length == drawableIDs.length,
                "String IDs length must be the same length as the Drawable IDs length");
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(mContext);
        for (int i = 0; i < stringIDs.length; i++) {
            adapter.add(new MaterialSimpleListItem.Builder(mContext)
                    .content(stringIDs[i])
                    .icon(drawableIDs[i])
                    .iconPaddingDp(3)
                    .backgroundColor(Color.WHITE)
                    .build());
        }
        new MaterialDialog.Builder(mContext)
                .title(titleResId)
                .adapter(adapter, listCallback)
                .listSelector(R.drawable.tracks_list_item_active)
                .show();
    }
}
