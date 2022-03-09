package com.example.core.di

import androidx.room.Room
import com.example.core.BuildConfig
import com.example.core.data.GamesRepository
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.room.GamesDatabase
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiService
import com.example.core.domain.repository.IGamesRepository
import com.example.core.utils.AppExecutors
import com.example.core.utils.Constant
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<GamesDatabase>().gamesDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("games_aqil".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            GamesDatabase::class.java, "Games.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }
}

val networkModule = module {
    single {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val hostname = Constant.HOSTNAME
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/+WSYXXW0rd5TnILDGuJvshU5aExcOMlLxvQBPOT4PS0=")
            .add(hostname, "sha256/RI9CUmPUOpUk2vdVMSZDWj+wtoQO5k9MSCSM9w4grmU=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IGamesRepository> { GamesRepository(get(), get(), get()) }
}