package com.example.capstone_made.core.domain

import com.example.core.data.Resource
import com.example.core.domain.model.Games
import com.example.core.domain.repository.IGamesRepository
import com.example.core.domain.usecase.GamesInteractor
import com.example.core.domain.usecase.GamesUseCase
import com.example.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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
class GamesUseCaseTest {

    private lateinit var gamesUseCase: GamesUseCase
    private lateinit var gamesList: Flow<Resource<List<Games>>>
    private lateinit var gamesDetail: Flow<Resource<Games>>
    private lateinit var gamesFavorite: Flow<List<Games>>

    companion object {
        const val SEARCH = "FIFA"
        const val ID = 8
    }

    @Mock
    private lateinit var gamesRepository: IGamesRepository

    @Before
    fun setUp() {
        gamesUseCase = GamesInteractor(gamesRepository)

        gamesList = flow {
            emit(Resource.Loading(listOf()))
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesRepository.getAllGames()).thenReturn(gamesList)
        `when`(gamesRepository.getResultSearchGames(SEARCH)).thenReturn(gamesList)

        gamesDetail = flow {
            emit(Resource.Success(DataDummy.generateGameList().first()))
        }
        `when`(gamesRepository.getDetailGames(ID)).thenReturn(gamesDetail)

        gamesFavorite = flow {
            emit(DataDummy.generateGameList())
        }
        `when`(gamesRepository.getFavoriteGames()).thenReturn(gamesFavorite)
    }

    @Test
    fun `test all list games`() = runTest {
        gamesUseCase.getAllGames().collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
            }
        }

        verify(gamesRepository).getAllGames()
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun `search games`() = runTest {
        gamesUseCase.getResultSearchGames(SEARCH).collectIndexed { index, value ->
            if (index == 0) {
                assertEquals(true, value is Resource.Loading)
            } else {
                assertEquals(true, value is Resource.Success)
                assertNotNull(value)
            }
        }

        verify(gamesRepository).getResultSearchGames(SEARCH)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun `favorite games`() = runTest {
        val result = gamesUseCase.getFavoriteGames().first()

        verify(gamesRepository).getFavoriteGames()
        assertNotNull(result)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun `set favorite games`() = runTest {
        val data = DataDummy.generateGameList().first()
        gamesUseCase.setFavoriteGames(data, true)
        verify(gamesRepository).setFavoriteGames(data, true)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun `insert to db`() = runTest {
        val data = DataDummy.generateGameList().first()
        gamesUseCase.insertDataGames(data, true)
        verify(gamesRepository).insertDataGames(data, true)
        verifyNoMoreInteractions(gamesRepository)
    }

    @Test
    fun `get detail data`() = runTest {
        gamesUseCase.getDetailGames(ID).collect { value ->
            assertNotNull(value)
        }

        verify(gamesRepository).getDetailGames(ID)
        verifyNoMoreInteractions(gamesRepository)
    }


}