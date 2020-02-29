package com.pandacard.teavel.apps;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.Toast;

import com.litesuits.orm.LiteOrm;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.xlu.widgets.CircleBitmapDisplayer;
import com.xlu.widgets.ScaleRoundedBitmapDisplayer;

import java.io.File;

public class MyApplication extends Application {

    private static String TAG = "MyApplication";
    public static String city;
    public static String deviceId;
    private static Context context;

    // ------------------------ImageLoader-------------------------------------
    public static DisplayImageOptions roundedOption;
    public static DisplayImageOptions circleOption;
    public static DisplayImageOptions normalOption;

    public static double jingdu;
    public static double weidu;
    public static LiteOrm liteOrm;
    public static ImageLoaderConfiguration config;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
       liteOrm = LiteOrm.newSingleInstance(this, "pandacard.db");
        MobSDK.init(this);
        ShareUtil.initShared(this);
        HttpManager.getInstance();
        MyApplication.city="北京市";
        initImageLoader();
        initImageLoaderOptions();
    }


    public static LiteOrm getLiteOrm() {
        return liteOrm;
    }


    private static void initImageLoaderOptions() {
        roundedOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.iv_fail)
                .showImageForEmptyUri(R.drawable.iv_fail)
                .showImageOnFail(R.drawable.iv_fail).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .delayBeforeLoading(100).resetViewBeforeLoading(true)
                .displayer(new ScaleRoundedBitmapDisplayer(10)).build();
        circleOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.iv_fail)
                .showImageForEmptyUri(R.drawable.iv_fail)
                .showImageOnFail(R.drawable.iv_fail).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .delayBeforeLoading(100).resetViewBeforeLoading(true)
                .displayer(new CircleBitmapDisplayer()).build();
        normalOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.iv_fail)
                .showImageForEmptyUri(R.drawable.iv_fail)
                .showImageOnFail(R.drawable.iv_fail).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
                .delayBeforeLoading(100).resetViewBeforeLoading(true).build();
    }
    public static Context getContext() {
        return context;
    }
    private static void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(MyApplication.getContext(),
                "wideroad/Cache");// 获取到缓存的目录地址

        // 线程池内加载的数量
// 线程优先级
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
        config = new ImageLoaderConfiguration.Builder(
                MyApplication.getContext())
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 线程优先级
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(
                        new BaseImageDownloader(MyApplication.getContext(), 5 * 1000, 30 * 1000))
                .build();
        ImageLoader.getInstance().init(config);
    }
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            LUtils.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

}
