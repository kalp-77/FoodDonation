package com.example.foodapp.di

import android.app.Application
import com.example.foodapp.repository.AuthRepository
import com.example.foodapp.repository.AuthRepositoryImpl
import com.example.foodapp.repository.MainRepository
import com.example.foodapp.repository.RepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ) : AuthRepository {
        return AuthRepositoryImpl(db, auth)
    }

    @Provides
    @Singleton
    fun provideRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth

    ) : MainRepository {
        return RepositoryImpl(database, auth)
    }

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(@ApplicationContext app: Application): Application = app

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

}