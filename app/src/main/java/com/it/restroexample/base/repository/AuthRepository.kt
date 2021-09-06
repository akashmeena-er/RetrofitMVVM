package com.it.restroexample.base.repository
import com.it.restroexample.dto.ArticleResponse
import com.it.restroexample.base.repository.resorce.Resource

/**
 * Repo class created by Akash 22.49 28/12/2020
 * This is for the perpose of getting responce from Server
 */

class AuthRepository {

    private val apicall=RetrofitClient.createApiService()
    companion object {
        private var projectRepository: AuthRepository? = null
        @Synchronized
        @JvmStatic
        fun getInstance(): AuthRepository {
            if (projectRepository == null) {
                projectRepository = AuthRepository()
            }
            return projectRepository!!
        }
    }

    /**
     *calling the api in Suspend function
     */

    suspend fun getAllNewIDs(): Resource<List<Int>> {
        return when (val apiResponse = apicall.topNews()
            )
            {
            is ApiSuccessResponse -> {
                Resource.success(apiResponse.body)
            }
            is ApiTimeoutError -> Resource.error(null, apiResponse.errorMessage)
            is ApiNetworkError -> Resource.error(null, apiResponse.errorMessage)
            is ApiEmptyResponse -> Resource.error(null, "")
            is ApiErrorResponse -> {
                Resource.error(null, apiResponse.errorMessage)
            }
        }
    }
    /**
     *calling the api in Suspend function
     */
    suspend fun getNewsDetailFromID(newsID:Int): Resource<ArticleResponse> {
        return when (val apiResponse = apicall.detail(newsID)
        )
        {
            is ApiSuccessResponse -> {
                Resource.success(apiResponse.body)
            }
            is ApiTimeoutError -> Resource.error(null, apiResponse.errorMessage)
            is ApiNetworkError -> Resource.error(null, apiResponse.errorMessage)
            is ApiEmptyResponse -> Resource.error(null, "")
            is ApiErrorResponse -> {
                Resource.error(null, apiResponse.errorMessage)
            }
            null -> TODO()
        }
    }
}

