/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepickerDemo;

import android.app.Application;
import android.content.Context;
import android.os.HandlerThread;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class A3App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread workerThread = new HandlerThread("global_worker_thread");
		workerThread.start();
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

}
