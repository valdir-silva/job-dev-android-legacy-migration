package com.goomer.ps.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.goomer.ps.databinding.ActivityMenuListBinding
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.legacy.MenuAdapter
import com.goomer.ps.legacy.MenuDetailActivity
import com.goomer.ps.presentation.viewmodel.MenuListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuListActivity : AppCompatActivity(), MenuAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMenuListBinding

    private val viewModel: MenuListViewModel by viewModel()

    private lateinit var adapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeUiState()

        viewModel.loadMenu()
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(this)
        binding.rvMenu.apply {
            layoutManager = LinearLayoutManager(this@MenuListActivity)
            this.adapter = this@MenuListActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { result ->
                when (result) {
                    is CardapioResult.Loading -> showLoading()
                    is CardapioResult.Success -> showSuccess(result.value)
                    is CardapioResult.Failure -> showError(result.throwable?.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
        binding.rvMenu.visibility = View.GONE
    }

    private fun showSuccess(items: List<MenuItem>) {
        binding.progressBar.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.rvMenu.visibility = View.VISIBLE
        
        adapter.submitList(items)
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.rvMenu.visibility = View.GONE

         binding.tvError.text = message.orEmpty()
    }

    override fun onItemClick(item: MenuItem) {
        val intent = Intent(this, MenuDetailActivity::class.java).apply {
            putExtra("id", item.id)
            putExtra("name", item.name)
            putExtra("description", item.description)
            putExtra("price", item.price)
            putExtra("imageUrl", item.imageUrl)
        }
        startActivity(intent)
    }
}
