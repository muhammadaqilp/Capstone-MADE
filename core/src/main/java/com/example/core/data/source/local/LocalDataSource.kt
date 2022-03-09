package com.example.core.data.source.local

import com.example.core.data.source.local.entity.GamesEntity
import com.example.core.data.source.local.room.GamesDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val gamesDao: GamesDao) {

    fun getAllGames(): Flow<List<GamesEntity>> = gamesDao.getAllGames()

    fun getFavoriteGames(): Flow<List<GamesEntity>> = gamesDao.getFavoriteGames()

    suspend fun insertGames(gamesList: List<GamesEntity>) = gamesDao.insertGames(gamesList)

    suspend fun insertGames(games: GamesEntity, newState: Boolean) {
        games.isFavorite = newState
        gamesDao.insertGames(games)
    }

    fun getDetailGames(id: Int): Flow<GamesEntity> = gamesDao.getDetail(id)

    fun updateDataGames(games: GamesEntity) = gamesDao.updateFavoriteGames(games)

    fun setFavoriteGames(games: GamesEntity, newstate: Boolean) {
        games.isFavorite = newstate
        gamesDao.updateFavoriteGames(games)
    }
}