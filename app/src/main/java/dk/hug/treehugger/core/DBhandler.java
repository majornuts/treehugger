package dk.hug.treehugger.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import dk.hug.treehugger.model.Root;

/**
 * Created by  Mads Fisker on 2016 - 09/03/16  21:44.
 */
public class DBhandler {

    private final static String trees = "trees";
    private final static String treeState = "treeState";
    private static final String PREF_NAME = "PREF_NAME";


    public static void storeTreeState(Context context, int RootState) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(treeState, RootState).apply();
    }

    public static int getTreeState(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int s = settings.getInt(treeState, 0);
        return s;
    }


    public static void storeTrees(Context context, Root root) {
        storeTreeState(context,1);
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(trees, serialize(root)).apply();
    }

    public static Root getTrees(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String s = settings.getString(trees, null);
        ObjectMapper mapper = new ObjectMapper();
        Root root = null;
        try {
            root = mapper.readValue(s, Root.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Nullable
    public static String serialize(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
