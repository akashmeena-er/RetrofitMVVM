package com.it.restroexample.viewmodel
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import com.it.restroexample.base.repository.resorce.Resource
import com.it.restroexample.base.repository.AuthRepository
import com.it.restroexample.local.ArticleResponse
import com.it.restroexample.local.UserRepository
import kotlinx.coroutines.launch
import java.util.ArrayList


/**
 * A class for handing the data for UI created by Akash on 28/12/2020
 */
class HomeViewModel(application: Application): AndroidViewModel(application) {


    val authenticationState = LiveEvent<Resource<List<Int>>>()
    val authenticationStateDetails = LiveEvent<Resource<ArticleResponse>>()

    /**
     * calling the News ID API so the UI can observe it.
     */
    fun getAllNewsID() {

        viewModelScope.launch {
            /**
             * calling the Repo method
             */
            val resource = AuthRepository.getInstance().getAllNewIDs()
            authenticationState.postValue(resource)
        }
    }

    fun addTask(context: Context?, articleResponse: ArticleResponse) {
        UserRepository(context!!).insertUser(articleResponse);
    }

    fun getAllTopNews(context: Context?) : ArrayList<ArticleResponse> {
     return UserRepository(context!!).getAllUsers() as ArrayList<ArticleResponse>;
    }

    /**
     * calling the news detail API so the UI can observe it.
     */
    fun getNewsDetailFromID(count:Int) {
        viewModelScope.launch {
            val resource = AuthRepository.getInstance().getNewsDetailFromID(count)
            authenticationStateDetails.postValue(resource)
        }
    }
}