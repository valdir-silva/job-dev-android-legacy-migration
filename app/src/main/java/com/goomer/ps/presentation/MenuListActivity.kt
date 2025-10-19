package com.goomer.ps.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goomer.ps.legacy.LegacyAsyncTask
import com.goomer.ps.legacy.LegacyDataSource
import com.goomer.ps.legacy.MenuAdapter
import com.goomer.ps.legacy.MenuDetailActivity
import com.goomer.ps.R
import com.goomer.ps.domain.model.MenuItem

class MenuListActivity : AppCompatActivity(), MenuAdapter.OnItemClickListener {
    private var rvMenu: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorContainer: LinearLayout? = null
    private var btnRetry: Button? = null

    private var adapter: MenuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)

        rvMenu = findViewById(R.id.rvMenu)
        progressBar = findViewById(R.id.progressBar)
        errorContainer = findViewById(R.id.errorContainer)
        btnRetry = findViewById(R.id.btnRetry)

        adapter = MenuAdapter(this)
        rvMenu?.setLayoutManager(LinearLayoutManager(this))
        rvMenu?.setAdapter(adapter)

        btnRetry?.setOnClickListener { v: View? -> loadData() }

        loadData()
    }

    private fun loadData() {
        showLoading()
        val ds = LegacyDataSource(this)
        LegacyAsyncTask(ds, object : LegacyAsyncTask.Listener {
            override fun onLoaded(items: MutableList<MenuItem?>?) {
                runOnUiThread {
                    hideLoading()
                    adapter?.submitList(items)
                }
            }

            override fun onFailed(e: Exception?) {
                runOnUiThread {
                    showError()
                }
            }
        }).execute()
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
        errorContainer?.visibility = View.GONE
        rvMenu?.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
        errorContainer?.visibility = View.GONE
        rvMenu?.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar?.visibility = View.GONE
        errorContainer?.visibility = View.VISIBLE
        rvMenu?.visibility = View.GONE
    }

    override fun onItemClick(item: MenuItem) {
        val it = Intent(this, MenuDetailActivity::class.java)
        it.putExtra("id", item.id)
        it.putExtra("name", item.name)
        it.putExtra("description", item.description)
        it.putExtra("price", item.price)
        it.putExtra("imageUrl", item.imageUrl)
        startActivity(it)
    }
}