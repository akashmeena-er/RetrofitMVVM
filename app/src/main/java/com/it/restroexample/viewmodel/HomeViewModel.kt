package com.it.restroexample.viewmodel
import android.app.Application
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.it.restroexample.base.repository.resorce.Resource
import com.it.restroexample.base.repository.AuthRepository
import com.it.restroexample.dto.ArticleResponse
import kotlinx.coroutines.launch
/**
 * A class for handing the data for UI created by Akash on 28/12/2020
 */
class HomeViewModel(application: Application): AndroidViewModel(application) {
    /**
     * Object for observing changes
     */
    val authenticationState = LiveEvent<Resource<List<Int>>>()
    val authenticationStateDetails = LiveEvent<Resource<ArticleResponse>>()

    /**
     * calling the News ID API so the UI can observe it.
     */
    fun getAllNewsID() {
        /*lanching the coroutine*/
        viewModelScope.launch {
            /*calling the Repo method*/
            val resource = AuthRepository.getInstance().getAllNewIDs()
            authenticationState.postValue(resource)
        }
    }

    /**
     * calling the news detail API so the UI can observe it.
     */
    fun getNewsDetailFromID(count:Int) {
        /*lanching the coroutine*/
        viewModelScope.launch {
            /*calling the Repo method*/
            val resource = AuthRepository.getInstance().getNewsDetailFromID(count)
            authenticationStateDetails.postValue(resource)
        }
    }


    /**
     * Object creation for ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }

}