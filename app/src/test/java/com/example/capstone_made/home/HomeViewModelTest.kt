package com.example.capstone_made.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.data.Resource
import com.example.core.domain.model.Games
import com.example.core.domain.usecase.GamesUseCase
import com.example.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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
@RunWith(MockitoJUnitRunner.Silent::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<List<Games>>>

    private lateinit var flow: Flow<Resource<List<Games>>>

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(gamesUseCase)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get list games`(): Unit = runBlocking {
        flow = flow {
            emit(Resource.Loading(listOf()))
            delay(10)
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesUseCase.getAllGames()).thenReturn(flow)

        homeViewModel.getListGames().observeForever(observer)
        verify(gamesUseCase).getAllGames()
    }

}