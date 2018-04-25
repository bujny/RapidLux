package bujny.rapidlux;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import bujny.rapidlux.Start;

public class AndroidLauncher extends AndroidApplication {
	public static Context contextOfApplication;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		contextOfApplication = getApplicationContext();
		initialize(new Start(new Database()), config);
	}

	public static Context getContextOfApplication(){
		return contextOfApplication;
	}
}
