package com.example.capstone_made.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.data.Resource
import com.example.core.domain.model.Games
import com.example.core.domain.usecase.GamesUseCase
import com.example.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailGamesViewModelTest {

    private lateinit var detailGamesViewModel: DetailGamesViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<Games>>

    private lateinit var flow: Flow<Resource<Games>>

    companion object {
        const val ID = 8
    }

    @Before
    fun setUp() {
        detailGamesViewModel = DetailGamesViewModel(gamesUseCase)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get detail games`() = runTest {
        flow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(DataDummy.generateGameList().first()))
        }
        `when`(gamesUseCase.getDetailGames(ID)).thenReturn(flow)

        detailGamesViewModel.getDetailGames(ID).observeForever(observer)
        verify(gamesUseCase, atLeastOnce()).getDetailGames(ID)
    }


}