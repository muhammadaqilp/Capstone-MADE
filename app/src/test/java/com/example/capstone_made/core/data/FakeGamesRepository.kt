package com.example.capstone_made.core.data

import com.example.core.data.NetworkBoundResource
import com.example.core.data.NetworkOnlyResource
import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.response.GamesResponse
import com.example.core.domain.model.Games
import com.example.core.domain.repository.IGamesRepository
import com.example.core.utils.AppExecutors
import com.example.core.utils.Constant
import com.example.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeGamesRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IGamesRepository {
    override fun getAllGames(): Flow<Resource<List<Games>>> =
        object : NetworkBoundResource<List<Games>, List<GamesResponse>>() {
            override fun loadFromDB(): Flow<List<Games>> {
                return localDataSource.getAllGames().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Games>?): Boolean =
                data.isNullOrEmpty() || data.size < Constant.MINIMUM_SIZE

            override suspend fun createCall(): Flow<ApiResponse<List<GamesResponse>>> =
                remoteDataSource.getAllGames()

            override suspend fun saveCallResult(data: List<GamesResponse>) {
                val gamesList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertGames(gamesList)
            }

        }.asFlow()

    override fun getResultSearchGames(query: String): Flow<Resource<List<Games>>> =
        object : NetworkOnlyResource<List<Games>, List<GamesResponse>>() {
            override fun loadFromNetwork(data: List<GamesResponse>): Flow<List<Games>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<GamesResponse>>> =
                remoteDataSource.getResultSearchGames(query)

        }.asFlow()

    override fun getDetailGames(id: Int): Flow<Resource<Games>> =
        object : NetworkBoundResource<Games, GamesResponse>() {
            override fun loadFromDB(): Flow<Games> {
                return localDataSource.getDetailGames(id).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: Games?): Boolean {
                return data?.description == null
            }

            override suspend fun createCall(): Flow<ApiResponse<GamesResponse>> =
                remoteDataSource.getDetailGames(id)

            override suspend fun saveCallResult(data: GamesResponse) {
                val games = DataMapper.mapResponsesToEntities(data)
                appExecutors.diskIO().execute { localDataSource.updateDataGames(games) }
            }

        }.asFlow()

    override fun getFavoriteGames(): Flow<List<Games>> {
        return localDataSource.getFavoriteGames().map { DataMapper.mapEntitiesToDomain(it) }
    }

    override fun setFavoriteGames(games: Games, state: Boolean) {
        val gamesEntity = DataMapper.mapDomainToEntity(games)
        appExecutors.diskIO().execute { localDataSource.setFavoriteGames(gamesEntity, state) }
    }

    override fun getDetailGamesFromSearch(id: Int): Flow<Resource<Games>> =
        object : NetworkOnlyResource<Games, GamesResponse>() {
            override fun loadFromNetwork(data: GamesResponse): Flow<Games> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<GamesResponse>> =
                remoteDataSource.getDetailGames(id)

        }.asFlow()

    override suspend fun insertDataGames(games: Games, state: Boolean) {
        val a = DataMapper.mapDomainToEntity(games)
        localDataSource.insertGames(a, state)
    }
}