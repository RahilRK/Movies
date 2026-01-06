package com.rk.movies.di

import com.rk.movies.network.MyApiRequest
import com.rk.movies.util.Constant.BASE_URL
import com.rk.movies.util.Constant.BASE_URL2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    @Named("BaseUrl")
    fun provideBaseUrl(): String = BASE_URL

    @Provides
    @Singleton
    @Named("BaseUrl2")
    fun provideBaseUrl2(): String = BASE_URL2

    @Provides
    @Singleton
    @Named("MovieRetrofit")
    fun provideMovieRetrofit(
        okHttpClient: OkHttpClient,
        @Named("BaseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("SearchRetrofit")
    fun provideSearchRetrofit(
        okHttpClient: OkHttpClient,
        @Named("BaseUrl2") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("MovieApi")
    fun provideMovieApi(@Named("MovieRetrofit") retrofit: Retrofit): MyApiRequest {
        return retrofit.create(MyApiRequest::class.java)
    }

    @Provides
    @Singleton
    @Named("SearchApi")
    fun provideSearchApi(@Named("SearchRetrofit") retrofit: Retrofit): MyApiRequest {
        return retrofit.create(MyApiRequest::class.java)
    }
}
