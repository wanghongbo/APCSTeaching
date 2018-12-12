package com.wwj.sb.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.wwj.sb.activity.PlayerActivity;
import com.wwj.sb.activity.R;
import com.wwj.sb.custom.LrcProcess;
import com.wwj.sb.domain.AppConstant;
import com.wwj.sb.domain.LrcContent;
import com.wwj.sb.domain.Mp3Info;
import com.wwj.sb.utils.MediaUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class PlayerService extends Service {
	private MediaPlayer mediaPlayer;
	private String path;
	private int msg;
	private boolean isPause;
	private int current = 0;
	private List<Mp3Info> mp3Infos;
	private int status = 3;
	private MyReceiver myReceiver;
	private int currentTime;
	private int duration;
	private LrcProcess mLrcProcess;
	private List<LrcContent> lrcList = new ArrayList<LrcContent>();
	private int index = 0;

	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION";
	public static final String CTL_ACTION = "com.wwj.action.CTL_ACTION";
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT";
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION";
	public static final String SHOW_LRC = "com.wwj.action.SHOW_LRC";
	
	/**
	 * handler
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if(mediaPlayer != null) {
					currentTime = mediaPlayer.getCurrentPosition();
					Intent intent = new Intent();
					intent.setAction(MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					sendBroadcast(intent);
					handler.sendEmptyMessageDelayed(1, 1000);
				}
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "service created");
		mediaPlayer = new MediaPlayer();
		mp3Infos = MediaUtil.getMp3Infos(PlayerService.this);


		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				if (status == 1) {
					mediaPlayer.start();
				} else if (status == 2) {
					current++;
					if(current > mp3Infos.size() - 1) {
						current = 0;
					}
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("current", current);

					sendBroadcast(sendIntent);
					path = mp3Infos.get(current).getUrl();
					play(0);
				} else if (status == 3) {
					current++;
					if (current <= mp3Infos.size() - 1) {
						Intent sendIntent = new Intent(UPDATE_ACTION);
						sendIntent.putExtra("current", current);

						sendBroadcast(sendIntent);
						path = mp3Infos.get(current).getUrl();
						play(0);
					}else {
						mediaPlayer.seekTo(0);
						current = 0;
						Intent sendIntent = new Intent(UPDATE_ACTION);
						sendIntent.putExtra("current", current);

						sendBroadcast(sendIntent);
					}
				} else if(status == 4) {
					current = getRandomIndex(mp3Infos.size() - 1);
					System.out.println("currentIndex ->" + current);
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("current", current);

					sendBroadcast(sendIntent);
					path = mp3Infos.get(current).getUrl();
					play(0);
				}
			}
		});

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CTL_ACTION);
		filter.addAction(SHOW_LRC);
		registerReceiver(myReceiver, filter);
	}


	protected int getRandomIndex(int end) {
		int index = (int) (Math.random() * end);
		return index;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		path = intent.getStringExtra("url");
		current = intent.getIntExtra("listPosition", -1);
		msg = intent.getIntExtra("MSG", 0);
		if (msg == AppConstant.PlayerMsg.PLAY_MSG) {
			play(0);
		} else if (msg == AppConstant.PlayerMsg.PAUSE_MSG) {
			pause();	
		} else if (msg == AppConstant.PlayerMsg.STOP_MSG) {
			stop();
		} else if (msg == AppConstant.PlayerMsg.CONTINUE_MSG) {
			resume();	
		} else if (msg == AppConstant.PlayerMsg.PRIVIOUS_MSG) {
			previous();
		} else if (msg == AppConstant.PlayerMsg.NEXT_MSG) {
			next();
		} else if (msg == AppConstant.PlayerMsg.PROGRESS_CHANGE) {
			currentTime = intent.getIntExtra("progress", -1);
			play(currentTime);
		} else if (msg == AppConstant.PlayerMsg.PLAYING_MSG) {
			handler.sendEmptyMessage(1);
		}
		super.onStart(intent, startId);
	}

	public void initLrc(){
		mLrcProcess = new LrcProcess();

		mLrcProcess.readLRC(mp3Infos.get(current).getUrl());

		lrcList = mLrcProcess.getLrcList();
		PlayerActivity.lrcView.setmLrcList(lrcList);

		PlayerActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(PlayerService.this,R.anim.alpha_z));
		handler.post(mRunnable);
	}
	Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			PlayerActivity.lrcView.setIndex(lrcIndex());
			PlayerActivity.lrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
		}
	};
	

	public int lrcIndex() {
		if(mediaPlayer.isPlaying()) {
			currentTime = mediaPlayer.getCurrentPosition();
			duration = mediaPlayer.getDuration();
		}
		if(currentTime < duration) {
			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
						index = i;
					}
					if (currentTime > lrcList.get(i).getLrcTime()
							&& currentTime < lrcList.get(i + 1).getLrcTime()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1
						&& currentTime > lrcList.get(i).getLrcTime()) {
					index = i;
				}
			}
		}
		return index;
	}

	private void play(int currentTime) {
		try {
			initLrc();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	private void resume() {
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}


	private void previous() {
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);

		sendBroadcast(sendIntent);
		play(0);
	}


	private void next() {
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);

		sendBroadcast(sendIntent);
		play(0);
	}


	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		handler.removeCallbacks(mRunnable);
	}



	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();
			if (currentTime > 0) {
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			intent.setAction(MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			intent.putExtra("duration", duration);
			sendBroadcast(intent);
		}
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int control = intent.getIntExtra("control", -1);
			switch (control) {
			case 1:
				status = 1;
				break;
			case 2:
				status = 2;
				break;
			case 3:
				status = 3;
				break;
			case 4:
				status = 4;
				break;
			}
			
			String action = intent.getAction();
			if(action.equals(SHOW_LRC)){
				current = intent.getIntExtra("listPosition", -1);
				initLrc();
			}
		}
	}

}
