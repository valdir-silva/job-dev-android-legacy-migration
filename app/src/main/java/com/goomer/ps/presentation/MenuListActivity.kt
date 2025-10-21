package com.goomer.ps.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.goomer.ps.R
import com.goomer.ps.databinding.ActivityMenuListBinding
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.legacy.MenuDetailActivity
import com.goomer.ps.presentation.adapter.MenuAdapter
import com.goomer.ps.presentation.viewmodel.MenuListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuListActivity :
    AppCompatActivity(),
    MenuAdapter.OnItemClickListener {
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
                    is CardapioResult.Failure -> showError(result.throwable)
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

    private fun showError(throwable: Throwable?) {
        binding.progressBar.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.rvMenu.visibility = View.GONE

        val errorMessage = getErrorMessage(throwable)
        binding.tvError.text = errorMessage
    }

    private fun getErrorMessage(throwable: Throwable?): String =
        when (throwable?.message) {
            com.goomer.ps.domain.exception.ErrorCode.INVALID_ID.name -> getString(R.string.error_invalid_id, 0)
            com.goomer.ps.domain.exception.ErrorCode.ITEM_NOT_FOUND.name -> getString(R.string.error_item_not_found, 0)
            else -> getString(R.string.error_load_menu)
        }

    override fun onItemClick(item: MenuItem) {
        val intent =
            Intent(
                this,
                MenuDetailActivity::class.java,
            ).apply {
                putExtra(getString(R.string.extra_key_id), item.id)
                putExtra(getString(R.string.extra_key_name), item.name)
                putExtra(getString(R.string.extra_key_description), item.description)
                putExtra(getString(R.string.extra_key_price), item.price)
                putExtra(getString(R.string.extra_key_image_url), item.imageUrl)
            }
        startActivity(intent)
    }
}
