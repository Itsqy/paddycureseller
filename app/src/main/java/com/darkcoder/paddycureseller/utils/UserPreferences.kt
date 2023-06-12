package com.darkcoder.paddycureseller.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.darkcoder.paddycure.data.model.local.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {

        return dataStore.data.map { user ->
            UserModel(
                user[stringPreferencesKey("name")] ?: "",
                user[stringPreferencesKey("id")] ?: "",
                user[stringPreferencesKey("token")] ?: "",
                user[booleanPreferencesKey("login")] ?: false,
            )

        }

    }

    suspend fun logout() {
        dataStore.edit { user ->
            user[stringPreferencesKey("name")] = ""
            user[stringPreferencesKey("id")] = ""
            user[stringPreferencesKey("token")] = ""
            user[booleanPreferencesKey("login")] = false

        }
    }

    suspend fun saveUser(data: UserModel) {
        dataStore.edit { user ->
            user[stringPreferencesKey("name")] = data.userName
            user[stringPreferencesKey("id")] = data.userId
            user[stringPreferencesKey("token")] = data.userToken
            user[booleanPreferencesKey("login")] = data.isLogin

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}