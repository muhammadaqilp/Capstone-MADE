package com.example.capstone_made.core.data

import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.entity.GamesEntity
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.response.GamesResponse
import com.example.core.domain.repository.IGamesRepository
import com.example.core.utils.AppExecutors
import com.example.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class GamesRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var appExecutors: AppExecutors

    private lateinit var gamesRepository: IGamesRepository

    private lateinit var gamesEntities: Flow<List<GamesEntity>>
    private lateinit var gamesEntity: Flow<GamesEntity>
    private lateinit var gamesResponse: Flow<ApiResponse<List<GamesResponse>>>

    companion object {
        const val SEARCH = "FIFA"
        const val ID = 8
    }

    @Before
    fun setUp() {
        gamesRepository = FakeGamesRepository(remoteDataSource, localDataSource, appExecutors)

        gamesEntities = flow {
            emit(DataDummy.generateGameEntities())
        }
        `when`(localDataSource.getAllGames()).thenReturn(gamesEntities)
        `when`(localDataSource.getFavoriteGames()).thenReturn(gamesEntities)

        gamesEntity = flow {
            emit(DataDummy.generateGameEntities().first())
        }
        `when`(localDataSource.getDetailGames(ID)).thenReturn(gamesEntity)

        gamesResponse = flow {
            emit(ApiResponse.Success(DataDummy.generateGameResponse()))
        }
    }

    @Test
    fun `get list games`() = runTest {
        gamesRepository.getAllGames().collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
                assertEquals(10, value.data?.size)
            }
        }
        verify(localDataSource, atLeast(2)).getAllGames()
    }

    @Test
    fun `search game`() = runTest {
        `when`(remoteDataSource.getResultSearchGames(SEARCH)).thenReturn(gamesResponse)
        gamesRepository.getResultSearchGames(SEARCH).collect { value ->
            assertNotNull(value)
        }
        val result = remoteDataSource.getResultSearchGames(SEARCH)
        assertNotNull(result)
    }

    @Test
    fun `get favorite games`() = runTest {
        val result = gamesRepository.getFavoriteGames().first()
        verify(localDataSource).getFavoriteGames()
        assertNotNull(result)
        assertEquals(10, result.size)
    }

    @Test
    fun `get detail games`() = runTest {
        gamesRepository.getDetailGames(ID).collectIndexed { index, value ->
            if (index == 0){
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
            }
        }

        verify(localDataSource, atLeast(2)).getDetailGames(ID)
    }

}