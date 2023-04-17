package com.sun.imageeditor.screen.search

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.remote.PhotoCollectionRemoteSource
import com.sun.imageeditor.databinding.ActivitySearchBinding
import com.sun.imageeditor.screen.detail.PhotoDetailFragment
import com.sun.imageeditor.screen.search.adapter.RecentSearchAdapter
import com.sun.imageeditor.screen.search.adapter.SearchViewPagerAdapter
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.SearchType
import com.sun.imageeditor.utils.base.BaseActivity
import com.sun.imageeditor.utils.ext.addFragment

class SearchActivity :
    BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate),
    SearchContract.ActivityView
{
    private val mPresenter by lazy {
        SearchPresenter(
            PhotoCollectionRepository.getInstance(
                PhotoCollectionRemoteSource.getInstance()
            ))
    }

    private val mTabList by lazy {
        mutableListOf<Fragment>(
            SearchResultFragment.newInstance(SearchType.PHOTO),
            SearchResultFragment.newInstance(SearchType.COLLECTION)
        )
    }

    private val mRecentSearchAdapter by lazy { RecentSearchAdapter() }

    override fun search() {
        val index = binding.viewPager.currentItem
        (mTabList[index] as? SearchResultFragment)?.let {
            mPresenter.search(
                it.searchType,
                index,
                binding.searchView.query.toString()
            )
            it.query = binding.searchView.query.toString()
        }
    }

    override fun initView() {
        binding.buttonBack.setOnClickListener { onBackPressed() }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()

                search()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.recyclerRecentSearch.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollHorizontally(1) && !mRecentSearchAdapter.isLoading) {
                        mRecentSearchAdapter.isLoading = true
                        mPresenter.getRecentSearch()
                    }
                }
            })

            adapter = mRecentSearchAdapter
        }

        mRecentSearchAdapter.setOnItemClick(object : OnItemRecyclerViewClickListener<Int> {
            override fun onItemClick(parameter: Int?) {
                parameter?.let {
                    val query = mRecentSearchAdapter.getRecentSearch(parameter)
                    binding.searchView.setQuery(query, true)
                    mPresenter.onRecentSearchClick()
                }
            }
        })

        createTabLayout()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (binding.searchView.query.isNotBlank())
            search()
    }

    override fun initData() {
        mPresenter.setView(this, mTabList.size)
        mPresenter.getRecentSearch()
    }

    private fun createTabLayout() {
        mTabList.forEachIndexed { index, _ ->
            val myFragment = supportFragmentManager.findFragmentByTag("f$index")
            if (myFragment is SearchResultFragment) {
                mTabList[index] = myFragment
            }

            (mTabList[index] as? SearchResultFragment)?.run {
                setOnLoadMore {
                    search()
                }

                setOnItemClick(object : OnItemRecyclerViewClickListener<String> {
                    override fun onItemClick(parameter: String?) {
                        when (searchType) {
                            SearchType.COLLECTION -> parameter?.let { mPresenter.onCollectionClick(it) }
                            SearchType.PHOTO -> parameter?.let { mPresenter.onPhotoClick(it) }
                            else -> { }
                        }
                    }
                })
            }
        }

        val adapter = SearchViewPagerAdapter(supportFragmentManager, lifecycle).apply {
            setTabList(mTabList)
        }

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            (mTabList[position] as? SearchResultFragment)?.let {
                tab.text = it.displayName
            }
        }.attach()

        binding.tabLayout.let {
            it.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    hideKeyboard()

                    val tabIndex = it.selectedTabPosition
                    if (binding.searchView.query.isNotBlank()) {
                        val fragment = mTabList[tabIndex]
                        if (fragment is SearchResultFragment) {
                            if (fragment.query != binding.searchView.query.toString())
                                search()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // TODO Not yet implemented
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // TODO Not yet implemented
                }

            })
        }
    }

    override fun showPhoto(url: String?) {
        addFragment(binding.framePhoto.id, PhotoDetailFragment.newInstance())
    }

    override fun startNewActivity(activityClass: Class<*>, bundle: Bundle?) {
        startActivity(
            Intent(this,activityClass).apply {
                bundle?.let { putExtras(it) }
            }
        )
    }

    override fun onGetRecentSearchSuccess(queries: MutableList<String>) {
        mRecentSearchAdapter.addData(queries)
        mRecentSearchAdapter.isLoading = false
    }

    override fun <T : Any> onGetItemsSuccess(
        items: MutableList<T>,
        tabIndex: Int,
        isFirstPage: Boolean
    ) {
        (mTabList[tabIndex] as? SearchResultFragment)?.run {
            onGetItemSuccess(items, isFirstPage)
        }
    }

    override fun onError(msg: String?, tabIndex: Int) {
        android.os.Handler(Looper.getMainLooper()).post {
            (mTabList[tabIndex] as? SearchResultFragment)?.onError(msg)
        }
    }
}
