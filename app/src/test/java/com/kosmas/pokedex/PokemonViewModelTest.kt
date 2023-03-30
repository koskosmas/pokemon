package com.kosmas.pokedex

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kosmas.data.model.data.PokemonItemData
import com.kosmas.domain.repository.DetailRepository
import com.kosmas.domain.repository.HomeRepository
import com.kosmas.pokedex.ui.home.PokemonViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PokemonViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: HomeRepository

    @Mock
    lateinit var detailRepository: DetailRepository

    private lateinit var viewModel: PokemonViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        viewModel = PokemonViewModel(repository, detailRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun mainViewModel_Success_fetchPokemonDetail() = runBlocking {
        val pokemons: List<PokemonItemData> =
            listOf(
                PokemonItemData(
                    name = "blaze",
                    url = "https://pokeapi.co/api/v2/ability/66/"
                )
            )

        Mockito.`when`(
            repository.fetchPokemonList(
                page = 0,
                onStart = { },
                onComplete = { },
                onError = { }
            )
        ).thenReturn(
            flow { emit(pokemons) }
        )

        viewModel.fetchPokemonsForTesting()

        val job = launch {
            viewModel.pokemonItemDataList.observeForever {
                it?.let { data -> Assert.assertNotNull(data) }
            }
        }
        job.cancel()
    }
}