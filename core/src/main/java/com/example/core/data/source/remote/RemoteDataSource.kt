package com.example.core.data.source.remote

import android.util.Log
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.ApiService
import com.example.core.data.source.remote.response.GamesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getAllGames(): Flow<ApiResponse<List<GamesResponse>>> {
        return flow {
            try {
                val response = apiService.getListGames()
                val dataArray = response.games
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.games))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getResultSearchGames(query: String): Flow<ApiResponse<List<GamesResponse>>> {
        return flow {
            try {
                val response = apiService.searchGames(query)
                val dataArray = response.games
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.games))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailGames(id: Int): Flow<ApiResponse<GamesResponse>> {
        return flow {
            try {
                val response = apiService.getDetailGames(id)
                val data = GamesResponse(
                    response.id,
                    response.name,
                    response.image,
                    response.rating,
                    response.description,
                    response.genres
                )
                if (data.genres.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}