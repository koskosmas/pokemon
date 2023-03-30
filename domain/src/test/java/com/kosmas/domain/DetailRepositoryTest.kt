package com.kosmas.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kosmas.data.model.data.PokemonItemData
import com.kosmas.data.model.response.PokemonResponse
import com.kosmas.data.network.service.PokemonService
import com.kosmas.domain.repository.DetailRepository
import com.kosmas.domain.repository.DetailRepositoryImpl
import com.kosmas.domain.repository.HomeRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
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
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailRepositoryTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

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
    fun mainViewModel_Success_fetchPokemonTypes() = runBlocking {
        val pokemons = PokemonResponse(
            10, null, null, mutableListOf(
                PokemonItemData(
                    name = "blaze", url = "https://pokeapi.co/api/v2/ability/66/"
                )
            )
        )

        Mockito.`when`(
            pokemonService.fetchPokemonList(10, 0)
        ).thenReturn(Response.success(pokemons))


        val job = launch {
            detailRepository.fetchPokemonDetail("blaze", {}, {}).collect {
                Assert.assertNotNull(it)
            }
        }
        job.cancel()
    }
}