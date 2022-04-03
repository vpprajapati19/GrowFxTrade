package com.growfxtrade.utils;

import android.app.Activity;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;

//    public static String service_url = "http://acutetech.in";
    public static String service_url = "https://orionfxrobo.com";
    public static String service_urll = "https://growfxtrade.com/";
    public static String service_url1 = "https://api.1forge.com";


    public static Retrofit getClient(Context activity) {

        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor( provideHttpLoggingInterceptor() )
                    .addInterceptor( provideOfflineCacheInterceptor() )
                    .addNetworkInterceptor( provideCacheInterceptor() )
                    .cache( provideCache() )
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

           // OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttpClient);
           /* Picasso picasso = new Picasso.Builder(AppController.getInstance())
                    .downloader(okHttp3Downloader)
                    .build();
            Picasso.setSingletonInstance(picasso);*/

            retrofit = new Retrofit.Builder()
                    .baseUrl(service_urll)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getClientone() {
        if (retrofit==null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor( provideHttpLoggingInterceptor() )
                    .addInterceptor( provideOfflineCacheInterceptor() )
                    .addNetworkInterceptor( provideCacheInterceptor() )
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(okHttpClient);


            retrofit = new Retrofit.Builder()
                    .baseUrl(service_urll)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClient1(Activity activity) {

        if (retrofit1 == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(36000, TimeUnit.SECONDS)
                    .connectTimeout(36000, TimeUnit.SECONDS)
                    .build();
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(service_url1)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit1;
    }
    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                        // Timber.d( message );
                    }
                } );
        //httpLoggingInterceptor.setLevel( BuildConfig.DEBUG ? HEADERS : NONE );
        return httpLoggingInterceptor;
    }
    public static Interceptor provideOfflineCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();

                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale( 30, TimeUnit.DAYS )
                            .build();
                    request = request.newBuilder()
                            .cacheControl( cacheControl )
                            .build();

                return chain.proceed( request );
            }
        };
    }
    public static Interceptor provideCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Response response = chain.proceed( chain.request() );
                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge( 2, TimeUnit.SECONDS )
                        .build();
                return response.newBuilder()
                        .build();
            }
        };
    }
    private static Cache provideCache ()
    {
        Cache cache = null;
        try
        {
          /*  cache = new Cache( new File( AppController.getInstance().getCacheDir(), "wallpaper-cache" ),
                    10 * 1024 * 1024 ); // 10 MB*/
        }
        catch (Exception e)
        {
            // Timber.e( e, "Could not create Cache!" );
        }
        return cache;
    }
}
