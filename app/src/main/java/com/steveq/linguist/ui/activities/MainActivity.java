package com.steveq.linguist.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.steveq.linguist.R;
import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.utilities.DismissKeyboard;

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private TranslatesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_main, null, false);
        view.setOnClickListener(new DismissKeyboard(this));
        setContentView(view);

        createRecyclerView();
    }

    private void createRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.translatesRecycler);
        mAdapter = new TranslatesAdapter();
        mRecyclerView.hasFixedSize();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
    }
}
