package com.it.restroexample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.it.restroexample.adapter.NewsAdapter
import com.it.restroexample.base.repository.resorce.Status
import com.it.restroexample.dto.ArticleResponse
import com.it.restroexample.local.SqliteHelper
import com.it.restroexample.util.BaseFragment
import com.it.restroexample.util.BaseInterface.Companion.PN_FORM
import com.it.restroexample.util.MyWorker
import com.it.restroexample.util.OnClickListenerApp
import com.it.restroexample.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.message_layout.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * created by Akash on 24/12/2020
 */
class Home : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private var adapter: NewsAdapter? = null
    private var sqliteHelper: SqliteHelper? = null
    private var list = ArrayList<ArticleResponse>()
    private lateinit var listNewsID: List<Int>
    private lateinit var homeViewModel: HomeViewModel
    private var isLatestNews = false
    private var maxLimit = 49
    private var limit = 7
    private var offset = 7
    private var count = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home, container, false)
    }
    override fun onRefresh() {
        swipe_container.isRefreshing = false
        getData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        swipe_container.setOnRefreshListener(this)
        tvRetry.setOnClickListener(this)
        /**
         * cancel previous work manager
         */
        WorkManager.getInstance().cancelAllWork()
        /**
         * set constaints for work manager
         */
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        /**
         * set periodic for work manager which execute every 15 minutes
         */
        val request: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints).addTag("MYWORKER")
                .build()
        WorkManager.getInstance().enqueue(request)
        /**
         *
         * obesrve when work manger excecute work manager
         */
        WorkManager.getInstance().getWorkInfoByIdLiveData(request.id)
            .observe(viewLifecycleOwner, {
                /**
                 * update latest news flag to false so we can get fresh data for API when d
                 */
                isLatestNews = false

            })
        /**
         * create viewmodel object
         */
        val factory = HomeViewModel.Factory(activity!!.application)
        val viewModel = ViewModelProviders.of(this, factory)
            .get(HomeViewModel::class.java)
        val homeViewModelFactory = HomeViewModel.Factory(activity!!.application)
        homeViewModel = ViewModelProviders.of(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)
        /**
         *call Api for news ids
         */
        getData()
        /**
         *To observe the newsID api data from then Viewmodel
         */
        viewModel.authenticationState.observe(viewLifecycleOwner) {
            if (it.status == Status.SUCCESS) {
                isLatestNews = true
                listNewsID = it.data!!
                getDetails()
            }
            else if (it.status == Status.ERROR)
            {
                /**
                 * Dialog to show error message from API
                 */
                hideProgressDialog()
                dialogOK(
                    requireActivity(),
                    resources.getString(R.string.whoops),
                    it.message!!
                )
            }
        }
        /**
         *To observe the  news details api data from then Viewmodel
         */
        viewModel.authenticationStateDetails.observe(viewLifecycleOwner) { it ->
            if (it.status == Status.SUCCESS) {
                val articleResponse: ArticleResponse = it.data!!
                /**
                 *
                Add news article to local database and list
                 */
                Log.d("respomce", "  "+articleResponse+ "  "+articleResponse.url+"  "+articleResponse.score)
                if(articleResponse.url!="null" &&  articleResponse.score!=null) {
                    sqliteHelper!!.addTask(articleResponse)
                    list.add(articleResponse)
                }
                count++
//                list.sortBy { it.score }
                if (count == limit) {
                    hideProgressDialog()
                    adapter!!.notifyDataSetChanged()
                } else if (count < limit) {
                    callNewsDetailApi(listNewsID[count])
                }

            } else if (it.status == Status.ERROR) {
                /**
                 * Dialog to show error message from API
                 */
                hideProgressDialog()
                dialogOK(
                    requireActivity(),
                    resources.getString(R.string.whoops),
                    it.message!!
                )

            }
        }
    }
    /**
     * This is call to method in Viewmodel for newsid API
     */
    private fun callNewsIdApi() {
        showProgressDialog()
        homeViewModel.getAllNewsID()
    }
    private fun callNewsDetailApi(count: Int) {
        if (!isNetworkAvailable) {
            hideProgressDialog()
            dialogOK(
                requireActivity(),
                resources.getString(R.string.whoops),
                resources.getString(R.string.network_error)
            )
            return
        }
        homeViewModel.getNewsDetailFromID(count)
    }
    /**
     * set recycler view and set layout manager
     */
    private fun setRecyclerView() {
        LinearLayoutManager(requireContext()).also { rvList.layoutManager = it }
        adapter = NewsAdapter(list, maxLimit)
        adapter!!.onClickCallback = onItemClickCallback
        rvList.adapter = adapter
    }
    /**
     *
     * get call back from News adapter for news Item
     */
    private var onItemClickCallback: OnClickListenerApp.OnClickCallback =
        object : OnClickListenerApp.OnClickCallback {
            override fun onClicked(view: View, position: Int, url: String) {
                if (url=="load") {
                     limit=limit+offset
                    showProgressDialog()
                    callNewsDetailApi(count)

                } else {
                    val fragment = NewsDetail()
                    val bundle = Bundle()
                    /**
                     *passing you URL to NewsDetails Fragment for showing it to webview
                     */
                    bundle.putString(PN_FORM, url)
                    fragment.arguments = bundle
                    addFragmentWithBack(fragment, "NewsDetail")
                }
            }
        }

    override fun onClick(view: View) {
        when (view) {
            tvRetry -> {
                llMessageMain.visibility = View.GONE
                getData()
            }
        }
    }

    private fun getData() {
        if (!isNetworkAvailable || isLatestNews) {
            sqliteHelper = SqliteHelper(requireContext())
            list = sqliteHelper!!.getAllTopNews
            setRecyclerView()

        } else {
            /**
             * getAllAllTopNews latest data from Server
             */
            if (swipe_container != null)
                swipe_container.isRefreshing = false
            callNewsIdApi()
        }
    }
    private fun getDetails() {
        sqliteHelper = SqliteHelper(requireContext())
//        delete previous records from local
        sqliteHelper!!.deletePreviousData()
        /**
         *
        if the list size is less then the 20 so preventing the ArrayIndexOutOf exceptions setting limit to listsize
         */
        if (listNewsID.size < maxLimit) {
            limit = listNewsID.size - 1
        }
//        showProgressDialog()
        callNewsDetailApi(listNewsID[count])

    }
}



