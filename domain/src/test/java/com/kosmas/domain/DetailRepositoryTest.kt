package com.kosmas.domain

import com.kosmas.data.network.service.PokemonService
import com.kosmas.domain.repository.DetailRepository
import com.kosmas.domain.repository.DetailRepositoryImpl
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
class DetailRepositoryTest {

    @Mock
    lateinit var pokemonService: PokemonService

    private lateinit var detailRepository: DetailRepository

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        detailRepository = DetailRepositoryImpl(pokemonService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun mainViewModel_Success_fetchPokemonDetail() = runBlocking {
        val pokemonName = "blaze"
        val job = launch {
            detailRepository.fetchPokemonDetail(pokemonName, {}, {}).collect {
                Assert.assertNotNull(it)
            }
        }
        job.cancel()
    }

    @Test
    fun mainViewModel_Success_Return_Equal_PokemonName_fetchPokemonDetail() = runBlocking {
        val pokemonName = "blaze"
        val job = launch {
            detailRepository.fetchPokemonDetail(pokemonName, {}, {}).collect {
                Assert.assertEquals(pokemonName, it.name)
            }
        }
        job.cancel()
    }

    @Test
    fun mainViewModel_Success_fetchPokemonEvolution() = runBlocking {
        val pokemonId = 1
        val job = launch {
            detailRepository.fetchPokemonEvolution(pokemonId, {}, {}).collect {
                Assert.assertNotNull(it)
            }
        }
        job.cancel()
    }

    @Test
    fun mainViewModel_Success_Return_Equal_PokemonID_fetchPokemonEvolution() = runBlocking {
        val pokemonId = 1
        val job = launch {
            detailRepository.fetchPokemonEvolution(pokemonId, {}, {}).collect {
                Assert.assertEquals(pokemonId, it.id)
            }
        }
        job.cancel()
    }
}