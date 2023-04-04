package com.kosmas.pokedex

import com.kosmas.domain.repository.DetailRepository
import com.kosmas.domain.repository.HomeRepository
import com.kosmas.pokedex.ui.home.PokemonViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PokemonViewModelTest {

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
    fun mainViewModel_Success_fetchPokemonList() = runBlocking {
        viewModel.fetchPokemonsForTesting()

        val job = launch {
            viewModel.pokemonItemDataList.observeForever {
                Assert.assertNotNull(it)
            }
        }
        job.cancel()
    }

    @Test
    fun mainViewModel_Success_fetchPokemonDetail() = runBlocking {
        val pokemonName = "bulbasaur"

        viewModel.fetchPokemonDetailForTesting(pokemonName)

        val job = launch {
            viewModel.pokemonDetail.observeForever {
                Assert.assertNotNull(it)
            }
        }
        job.cancel()
    }

    @Test
    fun mainViewModel_Success_Return_Equal_PokemonName_fetchPokemonDetail() = runBlocking {
        val pokemonName = "bulbasaur"

        viewModel.fetchPokemonDetailForTesting(pokemonName)

        val job = launch {
            viewModel.pokemonDetail.observeForever {
                Assert.assertEquals(it.name, pokemonName)
            }
        }
        job.cancel()
    }
}