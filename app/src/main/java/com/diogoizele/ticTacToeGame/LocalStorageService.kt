package com.diogoizele.ticTacToeGame

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

data class LocalStorage(
    val playerX: User,
    val playerO: User
)

data class User(
    val name: String,
    var score: Int
)

val Context.dataStore by preferencesDataStore("local-storage")

val PLAYER_X_NAME_KEY = stringPreferencesKey("player_x_name")
val PLAYER_O_NAME_KEY = stringPreferencesKey("player_o_name")
val PLAYER_X_SCORE_KEY = stringPreferencesKey("player_x_score")
val PLAYER_O_SCORE_KEY = stringPreferencesKey("player_x_score")


class LocalStorageService(private val context: Context) {

    suspend fun savePlayersName(playerX: String, playerO: String) {
        context.dataStore.edit { preferences ->
            preferences[PLAYER_X_NAME_KEY] = playerX
            preferences[PLAYER_O_NAME_KEY] = playerO
        }
    }

    suspend fun savePlayerScore(player: Player, score: Int) {
        context.dataStore.edit { preferences ->
            if (player == Player.X) {
                preferences[PLAYER_X_SCORE_KEY] = score.toString()
            } else {
                preferences[PLAYER_O_SCORE_KEY] = score.toString()
            }
        }
    }

    val localStorageFlow = context.dataStore.data.map { preferences ->
        val playerXName = preferences[PLAYER_X_NAME_KEY] ?: ""
        val playerOName = preferences[PLAYER_O_NAME_KEY] ?: ""
        val playerXScore = preferences[PLAYER_X_SCORE_KEY]?.toInt() ?: 0
        val playerOScore = preferences[PLAYER_O_SCORE_KEY]?.toInt() ?: 0

        LocalStorage(
            playerX = User(playerXName, playerXScore),
            playerO = User(playerOName, playerOScore)
        )
    }

    suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}