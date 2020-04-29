package com.xlu.utils.servers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;

import com.xlu.po.Jieshuo;
import com.xlu.po.MyEvent;
import com.xlu.sys.SystemManger;
import com.xlu.utils.AppUtil;
import com.xlu.utils.Constance;
import com.xlu.utils.FileUtil;

import java.io.IOException;

import de.greenrobot.event.EventBus;

public class PlayService extends Service implements OnPreparedListener,
        OnCompletionListener, OnErrorListener {

	private MediaPlayer player;
	private MediaPlayer player1;
	private Context context;
	public int continueState;
	private AudioManager mAudioManager;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		context = this;
		return new PlayBinder();
	}

	public class PlayBinder extends Binder {

		public void play(Jieshuo jieshuo1) {

			try {
				String path = FileUtil.getLocalFile(
						FileUtil.getApkStorageFile(PlayService.this),
						jieshuo1.getYuyin()).getAbsolutePath();
				player.reset();
				player.setDataSource(path);
				player.prepare();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void playTishi(String name, int i) {
			continueState = i;
			AssetManager am = getAssets();// 初始化AssetManager
			try {
				AssetFileDescriptor fd = am.openFd(name);
				player1.reset();
				player1.setDataSource(fd.getFileDescriptor(),
						fd.getStartOffset(), fd.getLength());
				player1.prepare(); //播放准备
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void resume() {
			// TODO Auto-generated method stub
			player.start();
		}

		public void pause(int i) {
			// TODO Auto-generated method stub
			if (i == 1 && player.isPlaying())
			{
				player.pause();
				SystemManger.yuying="";
			}
			else if (player1.isPlaying()) {
				player1.pause();
			}
		}

		public int getProgress() {
			int progress = (int) ((player.getCurrentPosition() * 100 * 1.0f)
					/ player.getDuration() * 1.0f);
			return progress;
		}

		public boolean isPlaying() {
			// TODO Auto-generated method stub
			if(player!=null)
			return player.isPlaying();
			else
				return false;
		}

		public void play(String gongluesrc) {
			// TODO Auto-generated method stub
			try {
				player.reset();
				player.setDataSource(Constance.HTTP_URL + gongluesrc);
				player.prepareAsync();
				AppUtil.showToastMsg(context, "缓冲中...");
			} catch (IllegalStateException e) {
				e.printStackTrace();
				player.stop();
				player.release();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				player.stop();
				player.release();
			}
		}
		public void play1(String jieshuoSrc){
			try {
				player.reset();
				player.setDataSource(jieshuoSrc);
				player.prepareAsync();
				AppUtil.showToastMsg(context, "缓冲中...");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void seekTo(int progress) {
			// TODO Auto-generated method stub
			player.seekTo(player.getDuration() * progress / 100);
		}

		public void stop(int i) {
			// TODO Auto-generated method stub
			if (i == 1) {
				player.stop();
				SystemManger.yuying="";
			}
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		player = new MediaPlayer();
		player1 = new MediaPlayer();
		player.setOnPreparedListener(this);
		player.setOnErrorListener(this);
		player1.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player1.setOnCompletionListener(this);
	
		
		
		
		
	}
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (mp.equals(player)) {
			Intent intent = new Intent(Constance.JIESHUO_PLAY_END);
			context.sendBroadcast(intent);
			SystemManger.yuying="";
			player.seekTo(0);
			player.pause();
		} else if (continueState == 1) {
			Intent intent = new Intent(Constance.TISHI_END);
			context.sendBroadcast(intent);
		}else if(continueState==0){
//			EventBus.getDefault().post(new MyEvent(Integer.MAX_VALUE - 1003));
			Intent intent = new Intent(Constance.LINE_TISHI_END);
			context.sendBroadcast(intent);
		}

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
		mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_SYSTEM, AudioManager.AUDIOFOCUS_GAIN);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}
	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {       
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ){//暂时失去语音焦点
				EventBus.getDefault().post(
						new MyEvent(Constance.MYEVETN_LOSS_FOCUSS));
				
			// Pause playback        
			} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {//长时间失去语音焦点
				EventBus.getDefault().post(
						new MyEvent(Constance.MYEVETN_LOSS_FOCUSS));
//				mAudioManager.unregisterMediaButtonEventReceiver(RemoteControlReceiver);      
				mAudioManager.abandonAudioFocus(afChangeListener);              // Stop playback   
				} 
			else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
					// Lower the volume       
					}
				else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
					EventBus.getDefault().post(
							new MyEvent(Constance.MYEVETN_GET_FOCUSS));//重新获得播放焦点
        
			}     
			} 
	};


	@Override
	public void onDestroy() {
		super.onDestroy();
		if(player!=null){
			if(player.isPlaying())
			player.stop();
			player.release();

		}
		if(player1!=null){
			if(player1.isPlaying())
				player1.stop();
			player1.release();
		}
	}
}
