package com.goomer.ps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuListActivity extends AppCompatActivity implements MenuAdapter.OnItemClickListener {

    private RecyclerView rvMenu;
    private ProgressBar progressBar;
    private LinearLayout errorContainer;
    private Button btnRetry;

    private MenuAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        rvMenu = findViewById(R.id.rvMenu);
        progressBar = findViewById(R.id.progressBar);
        errorContainer = findViewById(R.id.errorContainer);
        btnRetry = findViewById(R.id.btnRetry);

        adapter = new MenuAdapter(this);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(adapter);

        btnRetry.setOnClickListener(v -> loadData());

        loadData();
    }

    private void loadData() {
        showLoading();
        LegacyDataSource ds = new LegacyDataSource(this);
        new LegacyAsyncTask(ds, new LegacyAsyncTask.Listener() {
            @Override
            public void onLoaded(List<MenuItem> items) {
                runOnUiThread(() -> {
                    hideLoading();
                    adapter.submitList(items);
                });
            }

            @Override
            public void onFailed(Exception e) {
                runOnUiThread(() -> {
                    showError();
                });
            }
        }).execute();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        errorContainer.setVisibility(View.GONE);
        rvMenu.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        rvMenu.setVisibility(View.VISIBLE);
    }

    private void showError() {
        progressBar.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
        rvMenu.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(MenuItem item) {
        Intent it = new Intent(this, MenuDetailActivity.class);
        it.putExtra("id", item.id);
        it.putExtra("name", item.name);
        it.putExtra("description", item.description);
        it.putExtra("price", item.price);
        it.putExtra("imageUrl", item.imageUrl);
        startActivity(it);
    }
}
