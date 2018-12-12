package com.wwj.sb.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wwj.sb.custom.LrcView;
import com.wwj.sb.domain.AppConstant;
import com.wwj.sb.domain.Mp3Info;
import com.wwj.sb.service.PlayerService;
import com.wwj.sb.utils.ImageUtil;
import com.wwj.sb.utils.MediaUtil;

import java.util.HashMap;
import java.util.List;

public class PlayerActivity extends Activity {
	private TextView musicTitle = null;
	private TextView musicArtist = null;
	private Button previousBtn;
	private Button repeatBtn;
	private Button playBtn;
	private Button shuffleBtn;
	private Button nextBtn;
	private Button queueBtn;
	private SeekBar music_progressBar;
	private TextView currentProgress;
	private TextView finalProgress;

	private String title;
	private String artist;
	private String url;
	private int listPosition;
	private int currentTime;
	private int duration;
	private int flag;

	private int repeatState;
	private final int isCurrentRepeat = 1;
	private final int isAllRepeat = 2;
	private final int isNoneRepeat = 3;
	private boolean isPlaying;
	private boolean isPause;
	private boolean isNoneShuffle;
	private boolean isShuffle;

	private List<Mp3Info> mp3Infos;
	public static LrcView lrcView;

	private PlayerReceiver playerReceiver;
	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION";
	public static final String CTL_ACTION = "com.wwj.action.CTL_ACTION";
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT";
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION";
	public static final String MUSIC_PLAYING = "com.wwj.action.MUSIC_PLAYING";
	public static final String REPEAT_ACTION = "com.wwj.action.REPEAT_ACTION";
	public static final String SHUFFLE_ACTION = "com.wwj.action.SHUFFLE_ACTION";
	public static final String SHOW_LRC = "com.wwj.action.SHOW_LRC";
	
	
	
	private AudioManager am;
	RelativeLayout ll_player_voice;
	int currentVolume;
	int maxVolume;
	ImageButton ibtn_player_voice;
	SeekBar sb_player_voice;

	private Animation showVoicePanelAnimation;
	private Animation hiddenVoicePanelAnimation;
	
	
	private ImageView musicAlbum;
	private ImageView musicAblumReflection;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("PlayerActivity onCreated");
		setContentView(R.layout.play_activity_layout);
		
		findViewById();
		setViewOnclickListener();
		getDataFromBundle();
		
		mp3Infos = MediaUtil.getMp3Infos(PlayerActivity.this);
		registerReceiver();

		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);

		showVoicePanelAnimation = AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.push_up_in);
		hiddenVoicePanelAnimation = AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.push_up_out);

		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sb_player_voice.setProgress(currentVolume);
		initView();
		am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
		System.out.println("currentVolume--->"+currentVolume);
		System.out.println("maxVolume-->" + maxVolume);
		
	}

	private void registerReceiver() {
		playerReceiver = new PlayerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_ACTION);
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		registerReceiver(playerReceiver, filter);
	}

	/**
	 *
	 */
	private void findViewById() {
		musicTitle = (TextView) findViewById(R.id.musicTitle);
		musicArtist = (TextView) findViewById(R.id.musicArtist);
		previousBtn = (Button) findViewById(R.id.previous_music);
		repeatBtn = (Button) findViewById(R.id.repeat_music);
		playBtn = (Button) findViewById(R.id.play_music);
		shuffleBtn = (Button) findViewById(R.id.shuffle_music);
		nextBtn = (Button) findViewById(R.id.next_music);
		queueBtn = (Button) findViewById(R.id.play_queue);
		music_progressBar = (SeekBar) findViewById(R.id.audioTrack);
		currentProgress = (TextView) findViewById(R.id.current_progress);
		finalProgress = (TextView) findViewById(R.id.final_progress);
		lrcView = (LrcView) findViewById(R.id.lrcShowView);
		ibtn_player_voice = (ImageButton) findViewById(R.id.ibtn_player_voice);
		ll_player_voice = (RelativeLayout) findViewById(R.id.ll_player_voice);
		sb_player_voice = (SeekBar) findViewById(R.id.sb_player_voice);
		musicAlbum = (ImageView) findViewById(R.id.iv_music_ablum);
		musicAblumReflection = (ImageView) findViewById(R.id.iv_music_ablum_reflection);
	}
	

	/**
	 *
	 */
	private void setViewOnclickListener() {
		ViewOnclickListener ViewOnClickListener = new ViewOnclickListener();
		previousBtn.setOnClickListener(ViewOnClickListener);
		repeatBtn.setOnClickListener(ViewOnClickListener);
		playBtn.setOnClickListener(ViewOnClickListener);
		shuffleBtn.setOnClickListener(ViewOnClickListener);
		nextBtn.setOnClickListener(ViewOnClickListener);
		queueBtn.setOnClickListener(ViewOnClickListener);
		music_progressBar
				.setOnSeekBarChangeListener(new SeekBarChangeListener());
		ibtn_player_voice.setOnClickListener(ViewOnClickListener);
		sb_player_voice.setOnSeekBarChangeListener(new SeekBarChangeListener());
	}

	/**
	 * 
	 *
	 */
	private class MobliePhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: //
				Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
				playBtn.setBackgroundResource(R.drawable.play_selector);
				intent.setAction("com.wwj.media.MUSIC_SERVICE");
				intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
				startService(intent);
				isPlaying = false;
				isPause = true;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
			case TelephonyManager.CALL_STATE_RINGING:
				Intent intent2 = new Intent(PlayerActivity.this, PlayerService.class);
				playBtn.setBackgroundResource(R.drawable.pause_selector);
				intent2.setAction("com.wwj.media.MUSIC_SERVICE");
				intent2.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
				startService(intent2);
				isPlaying = true;
				isPause = false;
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("PlayerActivity has started");
	}

	/**
	 *
	 */
	private void getDataFromBundle() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		title = bundle.getString("title");
		artist = bundle.getString("artist");
		url = bundle.getString("url");
		listPosition = bundle.getInt("listPosition");
		repeatState = bundle.getInt("repeatState");
		isShuffle = bundle.getBoolean("shuffleState");
		flag = bundle.getInt("MSG");
		currentTime = bundle.getInt("currentTime");
		duration = bundle.getInt("duration");
	}
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("PlayerActivity has paused");
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
		System.out.println("PlayerActivity has onResume");
	}

	/**
	 *
	 */
	public void initView() {
		isPlaying = true;
		isPause = false;
		musicTitle.setText(title);
		musicArtist.setText(artist);
		music_progressBar.setProgress(currentTime);
		music_progressBar.setMax(duration);
		sb_player_voice.setMax(maxVolume);
		Mp3Info mp3Info = mp3Infos.get(listPosition);
		showArtwork(mp3Info);
		switch (repeatState) {
		case isCurrentRepeat:
			shuffleBtn.setClickable(false);
			repeatBtn.setBackgroundResource(R.drawable.repeat_current_selector);
			break;
		case isAllRepeat:
			shuffleBtn.setClickable(false);
			repeatBtn.setBackgroundResource(R.drawable.repeat_all_selector);
			break;
		case isNoneRepeat:
			shuffleBtn.setClickable(true);
			repeatBtn.setBackgroundResource(R.drawable.repeat_none_selector);
			break;
		}
		if (isShuffle) {
			isNoneShuffle = false;
			shuffleBtn.setBackgroundResource(R.drawable.shuffle_selector);
			repeatBtn.setClickable(false);
		} else {
			isNoneShuffle = true;
			shuffleBtn.setBackgroundResource(R.drawable.shuffle_none_selector);
			repeatBtn.setClickable(true);
		}
		if (flag == AppConstant.PlayerMsg.PLAYING_MSG) {
			Toast.makeText(PlayerActivity.this, "1" + title, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setAction(SHOW_LRC);
			intent.putExtra("listPosition", listPosition);
			sendBroadcast(intent);
		} else if (flag == AppConstant.PlayerMsg.PLAY_MSG) {
			playBtn.setBackgroundResource(R.drawable.play_selector);
			play();
		} else if (flag == AppConstant.PlayerMsg.CONTINUE_MSG) {
			Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
			playBtn.setBackgroundResource(R.drawable.play_selector);
			intent.setAction("com.wwj.media.MUSIC_SERVICE");
			intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
			startService(intent);
		}
		
	}


	private void showArtwork(Mp3Info mp3Info) {
		Bitmap bm = MediaUtil.getArtwork(this, mp3Info.getId(), mp3Info.getAlbumId(), true, false);

		Animation albumanim = AnimationUtils.loadAnimation(PlayerActivity.this, R.anim.album_replace);

		musicAlbum.startAnimation(albumanim);
		if(bm != null) {
			musicAlbum.setImageBitmap(bm);
			musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));
		} else {
			bm = MediaUtil.getDefaultArtwork(this, false);
			musicAlbum.setImageBitmap(bm);
			musicAblumReflection.setImageBitmap(ImageUtil.createReflectionBitmapForSingle(bm));
		}
		
	}

	
	/**

	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(playerReceiver);
		System.out.println("PlayerActivity has stoped");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("PlayerActivity has Destoryed");
	}

	/*
	 * 
	 *
	 * 
	 */
	private class ViewOnclickListener implements OnClickListener {
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.play_music:
				if (isPlaying) {
					playBtn.setBackgroundResource(R.drawable.pause_selector);
					intent.setAction("com.wwj.media.MUSIC_SERVICE");
					intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
					startService(intent);
					isPlaying = false;
					isPause = true;
				} else if (isPause) {
					playBtn.setBackgroundResource(R.drawable.play_selector);
					intent.setAction("com.wwj.media.MUSIC_SERVICE");
					intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
					startService(intent);
					isPause = false;
					isPlaying = true;
				}
				break;
			case R.id.previous_music:
				previous_music();
				break;
			case R.id.next_music:
				next_music();
				break;
			case R.id.repeat_music:
				if (repeatState == isNoneRepeat) {
					repeat_one();
					shuffleBtn.setClickable(false);
					repeatState = isCurrentRepeat;
				} else if (repeatState == isCurrentRepeat) {
					repeat_all();
					shuffleBtn.setClickable(false);
					repeatState = isAllRepeat;
				} else if (repeatState == isAllRepeat) {
					repeat_none();
					shuffleBtn.setClickable(true);
					repeatState = isNoneRepeat;
				}
				Intent intent = new Intent(REPEAT_ACTION);
				switch (repeatState) {
				case isCurrentRepeat:
					repeatBtn
							.setBackgroundResource(R.drawable.repeat_current_selector);
					Toast.makeText(PlayerActivity.this,
							R.string.repeat_current, Toast.LENGTH_SHORT).show();

					intent.putExtra("repeatState", isCurrentRepeat);
					sendBroadcast(intent);
					break;
				case isAllRepeat:
					repeatBtn
							.setBackgroundResource(R.drawable.repeat_all_selector);
					Toast.makeText(PlayerActivity.this, R.string.repeat_all,
							Toast.LENGTH_SHORT).show();
					intent.putExtra("repeatState", isAllRepeat);
					sendBroadcast(intent);
					break;
				case isNoneRepeat:
					repeatBtn
							.setBackgroundResource(R.drawable.repeat_none_selector);
					Toast.makeText(PlayerActivity.this, R.string.repeat_none,
							Toast.LENGTH_SHORT).show();
					intent.putExtra("repeatState", isNoneRepeat);
					break;
				}
				break;
			case R.id.shuffle_music:
				Intent shuffleIntent = new Intent(SHUFFLE_ACTION);
				if (isNoneShuffle) {
					shuffleBtn
							.setBackgroundResource(R.drawable.shuffle_selector);
					Toast.makeText(PlayerActivity.this, R.string.shuffle,
							Toast.LENGTH_SHORT).show();
					isNoneShuffle = false;
					isShuffle = true;
					shuffleMusic();
					repeatBtn.setClickable(false);
					shuffleIntent.putExtra("shuffleState", true);
					sendBroadcast(shuffleIntent);
				} else if (isShuffle) {
					shuffleBtn
							.setBackgroundResource(R.drawable.shuffle_none_selector);
					Toast.makeText(PlayerActivity.this, R.string.shuffle_none,
							Toast.LENGTH_SHORT).show();
					isShuffle = false;
					isNoneShuffle = true;
					repeatBtn.setClickable(true);
					shuffleIntent.putExtra("shuffleState", false);
					sendBroadcast(shuffleIntent);
				}
				break;
				
			case R.id.ibtn_player_voice:
				voicePanelAnimation();
				break;
			case R.id.play_queue:
				showPlayQueue();
				break;
			}
		}
	}

	/**
	 * 
	 */
	private class SeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch(seekBar.getId()) {
			case R.id.audioTrack:
				if (fromUser) {
					audioTrackChange(progress);
				}
				break;
			case R.id.sb_player_voice:
				am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				System.out.println("am--->" + progress);
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

	}

	/**
	 *
	 */
	public void play() {
		//
		repeat_none();
		Intent intent = new Intent();
		intent.setAction("com.wwj.media.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("MSG", flag);
		startService(intent);
	}
	
	
	
	/**
	 *
	 */
	public void showPlayQueue() {
		LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View playQueueLayout = layoutInflater.inflate(R.layout.play_queue_layout, (ViewGroup)findViewById(R.id.play_queue_layout));
		ListView queuelist = (ListView) playQueueLayout.findViewById(R.id.lv_play_queue);
		
		List<HashMap<String, String>> queues = MediaUtil.getMusicMaps(mp3Infos);
		SimpleAdapter adapter = new SimpleAdapter(this, queues, R.layout.play_queue_item_layout, new String[]{"title",
				"Artist", "duration"}, new int[]{R.id.music_title, R.id.music_Artist, R.id.music_duration});
		queuelist.setAdapter(adapter);
		AlertDialog.Builder builder;
		final AlertDialog dialog;
		builder = new AlertDialog.Builder(this);
		dialog = builder.create();
		dialog.setView(playQueueLayout);
		dialog.show();
	}


	public void voicePanelAnimation() {
		if(ll_player_voice.getVisibility() == View.GONE) {
			ll_player_voice.startAnimation(showVoicePanelAnimation);
			ll_player_voice.setVisibility(View.VISIBLE);
		}
		else{
			ll_player_voice.startAnimation(hiddenVoicePanelAnimation);
			ll_player_voice.setVisibility(View.GONE);
		}
	}

	/**
	 *
	 */
	public void shuffleMusic() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 4);
		sendBroadcast(intent);
	}

	/**
	 *
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.wwj.media.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);
	}

	/**
	 *
	 */
	public void repeat_one() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 1);
		sendBroadcast(intent);
	}

	/**
	 *
	 */
	public void repeat_all() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 2);
		sendBroadcast(intent);
	}

	/**
	 *
	 */
	public void repeat_none() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 3);
		sendBroadcast(intent);
	}

	/**
	 *
	 */
	public void previous_music() {
		playBtn.setBackgroundResource(R.drawable.play_selector);
		listPosition = listPosition - 1;
		if (listPosition >= 0) {
			Mp3Info mp3Info = mp3Infos.get(listPosition);
			showArtwork(mp3Info);
			musicTitle.setText(mp3Info.getTitle());
			musicArtist.setText(mp3Info.getArtist());
			url = mp3Info.getUrl();
			Intent intent = new Intent();
			intent.setAction("com.wwj.media.MUSIC_SERVICE");
			intent.putExtra("url", mp3Info.getUrl());
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", AppConstant.PlayerMsg.PRIVIOUS_MSG);
			startService(intent);
			
			
			
		} else {
			listPosition = 0;
			Toast.makeText(PlayerActivity.this, "2", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 *
	 */
	public void next_music() {
		playBtn.setBackgroundResource(R.drawable.play_selector);
		listPosition = listPosition + 1;
		if (listPosition <= mp3Infos.size() - 1) {
			Mp3Info mp3Info = mp3Infos.get(listPosition);
			showArtwork(mp3Info);
			url = mp3Info.getUrl();
			musicTitle.setText(mp3Info.getTitle());
			musicArtist.setText(mp3Info.getArtist());
			Intent intent = new Intent();
			intent.setAction("com.wwj.media.MUSIC_SERVICE");
			intent.putExtra("url", mp3Info.getUrl());
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);
			startService(intent);
			
		} else {
			listPosition = mp3Infos.size() - 1;
			Toast.makeText(PlayerActivity.this, "3", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 
	 *
	 * 
	 */
	public class PlayerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", -1);
				currentProgress.setText(MediaUtil.formatTime(currentTime));
				music_progressBar.setProgress(currentTime);
			} else if (action.equals(MUSIC_DURATION)) {
				int duration = intent.getIntExtra("duration", -1);
				music_progressBar.setMax(duration);
				finalProgress.setText(MediaUtil.formatTime(duration));
			} else if (action.equals(UPDATE_ACTION)) {
				listPosition = intent.getIntExtra("current", -1);
				url = mp3Infos.get(listPosition).getUrl();
				if (listPosition >= 0) {
					musicTitle.setText(mp3Infos.get(listPosition).getTitle());
					musicArtist.setText(mp3Infos.get(listPosition).getArtist());
				}
				if (listPosition == 0) {
					finalProgress.setText(MediaUtil.formatTime(mp3Infos.get(
							listPosition).getDuration()));
					playBtn.setBackgroundResource(R.drawable.pause_selector);
					isPause = true;
				}
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch(keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:				if(action == KeyEvent.ACTION_UP) {
				if(currentVolume < maxVolume) {
					currentVolume = currentVolume + 1;
					sb_player_voice.setProgress(currentVolume);
					am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if(action == KeyEvent.ACTION_UP) {
				if(currentVolume > 0) {
					currentVolume = currentVolume - 1;
					sb_player_voice.setProgress(currentVolume);
					am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
			default:
				return super.dispatchKeyEvent(event);
		}
	}
}
