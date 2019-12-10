package com.pandacard.teavel.https;



import com.pandacard.teavel.utils.LUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Use open source Retrofit for http access http://square.github.io/retrofit/
 *
 * @author
 */
public class HttpManager {

    //oAuO41dgPvVoQCHvfRM5KOVU9bY0
    //88313113
    private final static String TAG = "HttpManager";

    private final static String CONNECT_LOGIN = "http://39.104.83.195:8080/";


    private static HttpManager sHttpManager;


    public static HttpManager getInstance() {
        if (sHttpManager == null) {
            synchronized (HttpManager.class) {
                if (sHttpManager == null) {
                    sHttpManager = new HttpManager();
                    sHttpManager.init();
                }
            }
        }
        return sHttpManager;
    }


    private ClientRestAPI mHttpClient;

    private Retrofit mRetrofit;


    private void init() {

        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LUtils.v(TAG, "OkHttpMessage:" + message);
            }
        });
        loggingInterceptor.setLevel(level);

        //
        //        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //
        //        builder.readTimeout(20, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //
        //                /**添加header这几步一定要分开写 不然header
        //                 *
        //                 *
        //                 * 会无效 别问我为什么
        //                 *我看了build源码 看返回了一个新的对象 猜想是要一个新的对象来接收
        //                 * 我就只定义了一个新的对象来接受新的Request
        //                 * 后面应该就可以，但是我没确定是否成功 ，然后我就全部都拆开了吧buider对象
        //                 * request的新的对象都分开之后 就能看到成功了。。。。巨大的bug 真是让人头疼
        //                 */
        //                Request request = chain.request();
        //                Request.Builder requestBuilder = request.newBuilder();
        //                requestBuilder.addHeader("Content-Type", "text/html; charset=UTF-8")
        //                        .addHeader("Connection", "keep-alive")
        //                        .addHeader("Accept", "*/*")
        //                        .addHeader("Access-Control-Allow-Origin", "*")
        //                        .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
        //                        .addHeader("Vary", "Accept-Encoding")
        //                        .build();
        //
        //                LUtils.v(TAG, " builder.readTimeout:  requestBuilder " + requestBuilder);
        //
        //                Request newRequest = requestBuilder.build();
        //                return chain.proceed(newRequest);
        //
        //            }
        //        }).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS)//设置超时
        //                .build();

        //-------------
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        mRetrofit = new Retrofit.Builder()
                .baseUrl(CONNECT_LOGIN)
                .client(new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mHttpClient = mRetrofit.create(ClientRestAPI.class);


    }

    public ClientRestAPI getHttpClient() {
        return mHttpClient;
    }


}
