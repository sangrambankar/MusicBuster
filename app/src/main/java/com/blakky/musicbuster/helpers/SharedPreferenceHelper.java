package com.blakky.musicbuster.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by sangrambankar on 4/5/17.
 */

public class SharedPreferenceHelper {

    private final static String SHARED_PREF_FILE_NAME = SharedPreferenceHelper.class.getPackage().toString();

    public static SharedPreferences getAppPrefs(final Context mContext) {
        return mContext.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(final SharedPreferences appPreferences) {
        return appPreferences.edit();
    }

    /**
     * Saves a boolean value into a SharedPreferences file.
     * @param mContext Application context
     * @param key The name of the value that will be saved
     * @param value {@link Boolean}
     */
    public static void saveKeyValuePairBoolean(final Context mContext, final String key, final boolean value) {
        checkNotNull(mContext, "Context cannot be null");
        checkArgument(!isNullOrEmpty(key), "key cannot be null");
        getEditor(getAppPrefs(mContext)).putBoolean(key, value).apply();
    }

    /**
     * Gets a boolean value from a SharedPreferences file.
     * @param mContext Application context
     * @param key The name of the value that was saved.
     * @return {@link Boolean}
     */
    public static boolean getBooleanValueOfKey(final Context mContext, final String key) {
        checkNotNull(mContext, "Context cannot be null");
        checkArgument(!isNullOrEmpty(key), "key cannot be null");
        return getAppPrefs(mContext).getBoolean(key, false);
    }

    /**
     * Removes keys value when the app is destroyed.
     * @param mContext Application context
     * @param pKey Value that will be removed
     */
    public static void removeKeyValue(final Context mContext, final String pKey) {
        checkNotNull(mContext, "Context cannot be null");
        checkArgument(!isNullOrEmpty(pKey), "key cannot be null");
        getEditor(getAppPrefs(mContext)).remove(pKey).apply();
    }
}
