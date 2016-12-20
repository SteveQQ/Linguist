package com.steveq.linguist.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.steveq.linguist.R;
import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.model.TranslateOutput;
import com.steveq.linguist.utilities.DismissKeyboard;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditText mInputWordEditText;
    private Spinner mInputLanguageSpinner;
    private FloatingActionButton mFloatingActionButton;
    private TranslatesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(createActivityView());
        createRecyclerView();
        creatFAB();

    }

    private void creatFAB() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.addTranslateFab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "FAB clicked", Toast.LENGTH_LONG).show();
                mAdapter.addOutputOption(new TranslateOutput());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private View createActivityView(){
        View view = this.getLayoutInflater().inflate(R.layout.activity_main, null, false);
        mInputWordEditText = (EditText) view.findViewById(R.id.inputWordEditText);
        mInputLanguageSpinner = (Spinner) view.findViewById(R.id.inputLanguageSpinner);
        return view;
    }

    private void createRecyclerView() {
        mAdapter = new TranslatesAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.translatesRecycler);
        mRecyclerView.hasFixedSize();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
    }
}
