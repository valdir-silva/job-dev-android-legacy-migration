package com.goomer.ps.legacy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goomer.ps.R;

public class MenuDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvPrice = findViewById(R.id.tvPrice);

        int id = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);

        if (name != null) tvName.setText(name);
        if (description != null) tvDescription.setText(description);
        tvPrice.setText(getString(R.string.price, price));

        setTitle(name != null ? name : getString(R.string.app_name));
    }
}
