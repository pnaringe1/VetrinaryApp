package com.i.vetrinarykotlinapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.i.vetrinarykotlinapp.model.Config
import com.i.vetrinarykotlinapp.model.Pet
import com.i.vetrinarykotlinapp.util.TestCoroutineRule
import com.i.vetrinarykotlinapp.viewModel.ConfigViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * When the server gives 200, it should return success to the UI layer.
When the server gives an error, it should return an error to the UI layer.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ConfigViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var webRepository: WebRepository


    @Mock
    private lateinit var petObserver: Observer<Resource<List<Pet>>>

    @Mock
    private lateinit var configObserver: Observer<Resource<List<Config>>>

    @Before
    fun setUp() {

    }

    /**
     *  This method is to mocked the configObserver to return the success with an empty list.
     *  And fetch and verify
     */
    @Test
    fun givenConfigServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<WebRepository>())
                .`when`(webRepository)
                .getConfigUrl()
            val viewModel = ConfigViewModel(webRepository)
            viewModel.configs?.observeForever(configObserver)
            verify(webRepository).getConfigUrl()
            verify(configObserver).onChanged(Resource.success(emptyList()))
            viewModel.configs?.removeObserver(configObserver)
        }
    }

    /**
     * This method is to mocked the configObserver to return the error. And fetch and verify.
     */
    @Test
    fun givenConfigServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message"
            doThrow(RuntimeException(errorMessage))
                .`when`(webRepository)
                .getConfigUrl()
            val viewModel = ConfigViewModel(webRepository)
            viewModel.configs?.observeForever(configObserver)
            verify(webRepository).getConfigUrl()
            verify(configObserver).onChanged(
                Resource.error(
                    RuntimeException(errorMessage).toString()
                )
            )
            viewModel.configs?.removeObserver(configObserver)
        }
    }

    /**
     *  This method is to mocked the petObserver to return the success with an empty list.
     *  And fetch and verify
     */
    @Test
    fun givenPetsServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(emptyList<WebRepository>())
                .`when`(webRepository)
                .getPetUrl()
            val viewModel = ConfigViewModel(webRepository)
            viewModel.pets?.observeForever(petObserver)
            verify(webRepository).getPetUrl()
            verify(petObserver).onChanged(Resource.success(emptyList()))
            viewModel.pets?.removeObserver(petObserver)
        }
    }

    /**
     * This method is to mocked the petObserver to return the error.
     * And fetch and verify.
     */
    @Test
    fun givenPetsServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message"
            doThrow(RuntimeException(errorMessage))
                .`when`(webRepository)
                .getPetUrl()
            val viewModel = ConfigViewModel(webRepository)
            viewModel.pets?.observeForever(petObserver)
            verify(webRepository).getConfigUrl()
            verify(petObserver).onChanged(
                Resource.error(
                    RuntimeException(errorMessage).toString()
                )
            )
            viewModel.pets?.removeObserver(petObserver)
        }
    }


    @After
    fun tearDown() {

    }

}