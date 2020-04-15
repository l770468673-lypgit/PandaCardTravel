package com.pandacard.teavel.https;


import com.pandacard.teavel.utils.LUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Use open source Retrofit for http access http://square.github.io/retrofit/
 *
 * @author
 */
public class HttpManager {

    private final static String TAG = "HttpManager";

    private final static String CONNECT_LOGIN = "http://www.estonecn.com:9999/";

    private final static String CONNECT_LOGIN2 = "http://39.104.83.195:8080/";

    private final static String CONNECT_LOGIN3 = "https://panda.stone3a.com";

    private static HttpManager sHttpManager;


    public static HttpManager getInstance() {
        if (sHttpManager == null) {
            synchronized (HttpManager.class) {
                if (sHttpManager == null) {
                    sHttpManager = new HttpManager();
                    sHttpManager.init();
                    sHttpManager.init2();
                    sHttpManager.init3();
                }
            }
        }
        return sHttpManager;
    }


    private ClientRestAPI mHttpClient;
    private ClientRestAPI mHttpClient2;
    private ClientRestAPI mHttpClient3;

    private Retrofit mRetrofit;
    private Retrofit mRetrofit2;
    private Retrofit mRetrofit3;


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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.certificatePinner(new CertificatePinner.Builder()
//                .add("sbbic.com", "sha1/C8xoaOSEzPC6BgGmxAt/EAcsajw=")
//                .add("closedevice.com", "sha1/T5x9IXmcrQ7YuQxXnxoCmeeQ84c=")
//                .build());
        builder.readTimeout(20, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                /**添加header这几步一定要分开写 不然header
                 *
                 *
                 * 会无效 别问我为什么
                 *我看了build源码 看返回了一个新的对象 猜想是要一个新的对象来接收
                 * 我就只定义了一个新的对象来接受新的Request
                 * 后面应该就可以，但是我没确定是否成功 ，然后我就全部都拆开了吧buider对象
                 * request的新的对象都分开之后 就能看到成功了。。。。巨大的bug 真是让人头疼
                 */
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();

                requestBuilder.addHeader("Content-Type", "text/html; charset=UTF-8")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .addHeader("Access-Control-Allow-Origin", "*")
                        .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                        .addHeader("Vary", "Accept-Encoding")
                        .build();

                LUtils.v(TAG, " builder.readTimeout:  requestBuilder " + requestBuilder);

                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);

            }
        }).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS)//设置超时

                .build();

        // okBuilder.sslSocketFactory(createSSLSocketFactory());
        //    okBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        //    return okBuilder.build();
        //————————————————
        //版权声明：本文为CSDN博主「丶白泽」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
        //原文链接：https://blog.csdn.net/weixin_39397471/article/details/103877854
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

    private void init2() {
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.readTimeout(20, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {

                /**添加header这几步一定要分开写 不然header
                 *
                 *
                 * 会无效 别问我为什么
                 *我看了build源码 看返回了一个新的对象 猜想是要一个新的对象来接收
                 * 我就只定义了一个新的对象来接受新的Request
                 * 后面应该就可以，但是我没确定是否成功 ，然后我就全部都拆开了吧buider对象
                 * request的新的对象都分开之后 就能看到成功了。。。。巨大的bug 真是让人头疼
                 */
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.addHeader("Content-Type", "text/html; charset=UTF-8")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .addHeader("Access-Control-Allow-Origin", "*")
                        .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                        .addHeader("Vary", "Accept-Encoding")
                        .build();

                LUtils.v(TAG, " builder.readTimeout:  requestBuilder " + requestBuilder);

                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);

            }
        }).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS)//设置超时
                .build();

        //-------------
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        mRetrofit2 = new Retrofit.Builder()
                .baseUrl(CONNECT_LOGIN2)
                .client(new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mHttpClient2 = mRetrofit2.create(ClientRestAPI.class);


    }


    private void init3() {
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.readTimeout(20, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {

                /**添加header这几步一定要分开写 不然header
                 *
                 *
                 * 会无效 别问我为什么
                 *我看了build源码 看返回了一个新的对象 猜想是要一个新的对象来接收
                 * 我就只定义了一个新的对象来接受新的Request
                 * 后面应该就可以，但是我没确定是否成功 ，然后我就全部都拆开了吧buider对象
                 * request的新的对象都分开之后 就能看到成功了。。。。巨大的bug 真是让人头疼
                 */
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.addHeader("Content-Type", "text/html; charset=UTF-8")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .addHeader("Access-Control-Allow-Origin", "*")
                        .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                        .addHeader("Vary", "Accept-Encoding")
                        .build();

                LUtils.v(TAG, " builder.readTimeout:  requestBuilder " + requestBuilder);

                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);

            }
        }).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(25, TimeUnit.SECONDS)//设置超时
                .build();

        //-------------
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        mRetrofit3 = new Retrofit.Builder()
                .baseUrl(CONNECT_LOGIN3)
                .client(new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mHttpClient3 = mRetrofit3.create(ClientRestAPI.class);


    }

    public ClientRestAPI getHttpClient() {
        return mHttpClient;
    }

    public ClientRestAPI getHttpClient2() {
        return mHttpClient2;
    }

    public ClientRestAPI getHttpClient3() {
        return mHttpClient3;
    }


}
