package com.lcabrales.simgas.di.module

import com.lcabrales.simgas.BuildConfig
import com.lcabrales.simgas.BuildConfig.BASE_URL
import com.lcabrales.simgas.data.RemoteApiInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Module which provides all required dependencies about network
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object NetworkModule {

    private const val TIMEOUT = 30L

    /**
     * Provides the Post service implementation.
     *
     * @param retrofit the Retrofit object used to instantiate the service.
     *
     * @return the Post service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRemoteApiInterface(retrofit: Retrofit): RemoteApiInterface {
        return retrofit.create(RemoteApiInterface::class.java)
    }

    /**
     * Provides the Retrofit object with a custom OkHttpClient with a logging interceptor enabled
     * and a Moshi Kotlin converter.
     *
     * @return the Retrofit object.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        //Create an OkHttpClient with a timeout
        val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)

        //Add a logging interceptor for debug only
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            okHttpClientBuilder.addInterceptor(logging)
        }

        //Needed in order for Moshi parsing to work in Kotlin
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        //Return a Retrofit instance
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }
}