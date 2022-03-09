package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.GamesResponse
import com.example.core.data.source.remote.response.ListGamesResponse
import com.example.core.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("games")
    suspend fun getListGames(
        @Query("page_size") size: Int = Constant.SIZE,
        @Query("key") key: String = Constant.KEY
    ): ListGamesResponse

    @GET("games")
    suspend fun searchGames(
        @Query("search") query: String,
        @Query("page_size") size: Int = Constant.SIZE,
        @Query("key") key: String = Constant.KEY
    ): ListGamesResponse

    @GET("games/{id}")
    suspend fun getDetailGames(
        @Path("id") id: Int,
        @Query("key") key: String = Constant.KEY
    ): GamesResponse

}