package com.example.core.data.source.local.room

import androidx.room.*
import com.example.core.data.source.local.entity.GamesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    @Query("SELECT * FROM games")
    fun getAllGames(): Flow<List<GamesEntity>>

    @Query("SELECT * FROM games WHERE isFavorite = 1")
    fun getFavoriteGames(): Flow<List<GamesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GamesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: GamesEntity)

    @Update
    fun updateFavoriteGames(gamesEntity: GamesEntity)

    @Query("SELECT * from games WHERE id = :id")
    fun getDetail(id: Int): Flow<GamesEntity>

}