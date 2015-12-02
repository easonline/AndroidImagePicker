package com.pizidea.imagepickerDemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.Locale;


public class A3App extends Application {
	private static final String TAG = A3App.class.getSimpleName();

	public static final String SMS_SDK_APPKEY = "93d78f165a8c";
	public static final String SMS_SDK_APPSECRET = "7daa9e277793ad3f6ca25f05be5a57bb";
	
	private float oldFontScale;
	private Locale oldLocale;
	
	private String deviceToken = "";

	public ApplicationStatus currentStatus;
	private Context mCurrentContext;
	private int mActCount = 0;
	
	private static A3App instance;
	public static enum ApplicationStatus {
		READY,
		RUNNING,
		BACKGROUND,
		FOREGROUND;
	}
	

	private Handler workHandler;
	private Handler mainHandler;



	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		oldFontScale = getResources().getConfiguration().fontScale;
		oldLocale = getResources().getConfiguration().locale;
		

		HandlerThread workerThread = new HandlerThread("global_worker_thread");
		workerThread.start();
		workHandler = new Handler(workerThread.getLooper());
		mainHandler = new Handler(Looper.getMainLooper());


		initImageLoader(this);


	}



	public static void initImageLoader(Context context){
		if(!ImageLoader.getInstance().isInited()){
			ImageLoaderConfiguration config = null;
			if(BuildConfig.DEBUG){
				config = new ImageLoaderConfiguration.Builder(context)
						/*.threadPriority(Thread.NORM_PRIORITY - 2)
						.memoryCacheSize((int) (Runtime.getRuntime().maxMemory() / 4))
						.diskCacheSize(500 * 1024 * 1024)
						.writeDebugLogs()
						.diskCacheFileNameGenerator(new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO).build();*/

						//.memoryCacheExtraOptions(200, 200)
						//.diskCacheExtraOptions(200, 200, null).threadPoolSize(3)
						.threadPriority(Thread.NORM_PRIORITY - 1)
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						//.denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2 * 1024 * 1024))
						/*.memoryCacheSize(20 * 1024 * 1024)*/
						.memoryCacheSizePercentage(13)
						.diskCacheSize(500 * 1024 * 1024)
						//.imageDownloader(new BaseImageDownloader(A3App.getInstance().getApplicationContext()))
						//.imageDecoder(new BaseImageDecoder(true))
						//.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
						.writeDebugLogs()
						.build();
			}else{
				config = new ImageLoaderConfiguration.Builder(context)
						.threadPriority(Thread.NORM_PRIORITY - 2)
						.diskCacheSize(500 * 1024 * 1024)
						.diskCacheFileNameGenerator(new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO).build();
			}
			ImageLoader.getInstance().init(config);
		}
	}


	public static A3App getInstance(){
		return instance;
	}

	/**
	 * 修改系统字号和地区时，会导致crash
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (oldFontScale != newConfig.fontScale || !oldLocale.equals(newConfig.locale)) {
			System.exit(0);
		}
	}
	
	public void onBaseActivityStarted(Activity current) {
		mActCount ++;
		mCurrentContext = current;
	}

	public void clear() {
		mCurrentContext = null;
	}
	
	public void onBaseActivityStopped() {
		mActCount --;
	}
	
	private void checkCurrentStatus(){
		if (mActCount <= 0) {
			currentStatus = ApplicationStatus.BACKGROUND;
		}else {
			currentStatus = ApplicationStatus.FOREGROUND;
		}
	}
	
	/*public BaseActivity getCurrentContext() {
		return (BaseActivity) mCurrentContext;
	}*/
	
	public Handler getWorkHandler() {
		return workHandler;
	}
	
	public Handler getMainHandler() {
		return mainHandler;
	}
	
	
	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}



}
