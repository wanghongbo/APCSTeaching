package com.wwj.sb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.wwj.sb.activity.R;

public class Settings {

	public static final String DOWNLOAD_MUSIC_DIRECTORY = "/Music/download_music/";

	public static final String DOWNLOAD_LYRIC_DIRECTORY = "/Music/download_lyric/";

	public static final String DOWNLOAD_ALBUM_DIRECTORY="/Music/download_album/";

	public static final String DOWNLOAD_ARTIST_DIRECTORY="/Music/download_artist/";

	public static final String PREFERENCE_NAME = "com.wwj.music.settings";
	public static final String KEY_SKINID = "skin_id";
	private SharedPreferences settingPreferences;
	public static final String KEY_AUTO_SLEEP = "sleep";
	public static final String KEY_BRIGHTNESS = "brightness";
	public static final float KEY_DARKNESS = 0.1f;

	public static final int[] SKIN_RESOURCES = {
		R.drawable.main_bg01, R.drawable.main_bg02,
		R.drawable.main_bg03, R.drawable.main_bg04,
		R.drawable.main_bg05, R.drawable.main_bg06
	};
	
	public Settings(Context context, boolean isWrite) {
		settingPreferences = context.getSharedPreferences(PREFERENCE_NAME,
				isWrite ? Context.MODE_WORLD_READABLE
						: Context.MODE_WORLD_WRITEABLE);
	}
	

	public String getValue(String key) {
		return settingPreferences.getString(key, null);
	}

	public int getCurrentSkinResId() {
		int skinIndex = settingPreferences.getInt(KEY_SKINID, 0);
		if(skinIndex >= SKIN_RESOURCES.length) {
			skinIndex = 0;
		}
		return SKIN_RESOURCES[skinIndex];
	}
	

	public int getCurrentSkinId() {
		int skinIndex = settingPreferences.getInt(KEY_SKINID, 0);
		if(skinIndex >= SKIN_RESOURCES.hashCode()) {
			skinIndex = 0;
		}
		return skinIndex;
	}

	public void setCurrentSkinResId(int skinIndex) {
		Editor it = settingPreferences.edit();
		it.putInt(KEY_SKINID, skinIndex);
		it.commit();
	}

	public void setValue(String key, String value) {
		Editor it = settingPreferences.edit();
		it.putString(key, value);
		it.commit();
	}
}
