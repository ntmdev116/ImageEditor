package com.sun.imageeditor.screen.search

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.databinding.FragmentSearchResultBinding
import com.sun.imageeditor.screen.search.adapter.SearchResultAdapter
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.SearchType
import com.sun.imageeditor.utils.base.BaseFragment


class SearchResultFragment :
    BaseFragment<FragmentSearchResultBinding>(FragmentSearchResultBinding::inflate),
    SearchContract.View
{
    private var mAdapter: SearchResultAdapter? = null
    private var mOnLoadMore: () -> Unit = { }
    private var mOnItemClick: OnItemRecyclerViewClickListener<String>? = null

    var query: String = ""
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, query)
    }

    var searchType: SearchType? = null
    val displayName: String?
        get() = searchType?.displayName

    fun setOnLoadMore(onLoadMore: () -> Unit) {
        mOnLoadMore = onLoadMore
    }

    fun setOnItemClick(listener: OnItemRecyclerViewClickListener<String>) {
        mOnItemClick = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchType = it.getSerializable(SEARCH_TYPE) as? SearchType
            mAdapter = searchType?.let { st ->
                SearchResultAdapter(st).apply {
                    setOnItemClick(mOnItemClick)
                }
            }
        }
    }

    fun <T : Any> onGetItemSuccess(items: MutableList<T>, isFirstPage: Boolean) {
        mAdapter?.run {
            if (isFirstPage)
                setData(items)
            else
                addData(items)

            isPhotoLoading = false
        }
    }

    override fun clearResult() {
        mAdapter?.setData(mutableListOf())
    }

    override fun onError(msg: String?) {
        mAdapter?.isPhotoLoading = false
    }

    override fun initData() {
        // TODO Not yet implemented
    }

    override fun initView() {
        binding.recyclerViewThumb.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    mAdapter?.run {
                        if (!recyclerView.canScrollVertically(1) && !isPhotoLoading) {
                            isPhotoLoading = true
                            mOnLoadMore()
                        }
                    }
                }
            })

            (layoutManager as? GridLayoutManager)?.spanCount = when (searchType) {
                SearchType.PHOTO -> 2
                SearchType.COLLECTION -> 1
                else -> throw IllegalStateException(ILLEGAL_SEARCH_TYPE_ERROR)
            }

            adapter = mAdapter
        }
    }

    companion object {
        private const val SEARCH_TYPE = "SEARCH_TYPE"
        private const val SEARCH_QUERY = "SEARCH_QUERY"

        const val ILLEGAL_SEARCH_TYPE_ERROR = "ILLEGAL_SEARCH_TYPE_ERROR"

        fun newInstance(searchType: SearchType) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SEARCH_TYPE, searchType)
                }
                this.searchType = searchType
            }
    }
}
