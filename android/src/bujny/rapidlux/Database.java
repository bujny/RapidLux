package bujny.rapidlux;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import bujny.rapidlux.AndroidLauncher;
import bujny.rapidlux.DatabaseInterface;

/**
 * Created by Mateusz Bujnowicz on 3/14/2018.
 */



public class Database implements DatabaseInterface {
    public static final int TWOBOXES = 0;
    public static final int DEFENCE = 1;
    public static final int MEMORY = 2;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public Database(){
        Context applicationContext = AndroidLauncher.getContextOfApplication();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        editor = sharedPreferences.edit();
    }

    @Override
    public int getHighScore(int gameIndex) {
        if(gameIndex==TWOBOXES) return sharedPreferences.getInt("TwoBoxes",0);
        if(gameIndex==DEFENCE) return sharedPreferences.getInt("Defence",0);
        if(gameIndex==MEMORY) return sharedPreferences.getInt("Memory",0);
        return 0;
    }

    @Override
    public void updateHighScore(int gameIndex, int highScore) {
        if(gameIndex==TWOBOXES) editor.putInt("TwoBoxes",highScore);
        if(gameIndex==DEFENCE) editor.putInt("Defence",highScore);
        if(gameIndex==MEMORY) editor.putInt("Memory",highScore);
        editor.apply();
    }
}
