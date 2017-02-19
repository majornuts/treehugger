package dk.hug.treehugger.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import dk.hug.treehugger.model.Root;

/**
 * Created by  Mads Fisker @ Dis-Play on 2016 - 09/03/16  21:44.
 */
public class DBhandler {

    private final static String trees = "trees";
    private static final String PREF_NAME = "PREF_NAME";

    public static void storeTrees(Context context, Root root) {
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
