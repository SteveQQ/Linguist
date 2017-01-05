package com.steveq.linguist.ui.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.steveq.linguist.Api.GlosbeAPI;
import com.steveq.linguist.Api.GlosbeCallback;
import com.steveq.linguist.Api.GlosbeClient;
import com.steveq.linguist.R;
import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.database.TranslationsDataSource;
import com.steveq.linguist.model.response.Phrase;
import com.steveq.linguist.model.response.TranslationResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


public class MainActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private EditText mInputWordEditText;
    private Spinner mInputLanguageSpinner;
    private CardView mTranslateCardView;
    private TranslatesAdapter mAdapter;
    public ProgressBar mProgressBar;
    private TranslationsDataSource translationsDataSource;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(createActivityView());
        if(savedInstanceState != null) {
            createRecyclerView(savedInstanceState.getParcelableArrayList("PHRASES"));
        } else {
            createRecyclerView(null);
        }
        creatFAB();
        Thread dbCreateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                createDB();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "db created", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dbCreateThread.setName("dbCreateThread");
        dbCreateThread.start();
    }

    private void createDB() {
        translationsDataSource = new TranslationsDataSource(this);
        translationsDataSource.initDB();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("PHRASES", mAdapter.getOutputs());
    }

    private View createActivityView(){
        View view = this.getLayoutInflater().inflate(R.layout.activity_main, null, false);
        mInputWordEditText = (EditText) view.findViewById(R.id.inputWordEditText);
        mInputLanguageSpinner = (Spinner) view.findViewById(R.id.inputLanguageSpinner);
        mTranslateCardView = (CardView) view.findViewById(R.id.translateCardView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mHandler = new Handler();

        mTranslateCardView.setVisibility(View.INVISIBLE);
        animateTranslateCardView();

        mInputLanguageSpinner.setSelection(1);

        mProgressBar.setVisibility(View.GONE);

        return view;
    }

    private void createRecyclerView(ArrayList phrases) {
        if(phrases == null){
            mAdapter = new TranslatesAdapter(this);
        } else {
            mAdapter = new TranslatesAdapter(phrases, this);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.translatesRecycler);
        mRecyclerView.hasFixedSize();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void creatFAB() {
        FloatingActionMenu mActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        FloatingActionButton mAddFloatingActionButton = (FloatingActionButton) findViewById(R.id.addTranslateFab);
        FloatingActionButton mExecuteFloatingActionButton = (FloatingActionButton) findViewById(R.id.executeTranslateFab);
        FloatingActionButton mRefreshFloatingActionButton = (FloatingActionButton) findViewById(R.id.refreshFab);

        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] langs = getResources().getStringArray(R.array.langs);
                mAdapter.addOutputOption(new Phrase(langs[mAdapter.getOutputs().size()%4]));
                mAdapter.notifyDataSetChanged();
            }
        });

        mExecuteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = mInputLanguageSpinner.getSelectedItem().toString();
                String phrase = mInputWordEditText.getText().toString();
                for(int i=0; i < mAdapter.getOutputs().size(); i++) {
                    String dest = mAdapter.getOutputs().get(i).getLanguage();
                    GlosbeAPI glosbeAPI = GlosbeClient.getClient().create(GlosbeAPI.class);

                    Call<TranslationResponse> call = glosbeAPI.loadTranslation(generateParamsMap(from, dest, phrase));
                    call.enqueue(new GlosbeCallback(mAdapter, MainActivity.this));
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mRefreshFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter = new TranslatesAdapter(MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mInputWordEditText.setText("");
                mInputLanguageSpinner.setSelection(1);
            }
        });
    }

    private Map generateParamsMap(String from, String dest, String phrase) {
        Map paramsMap = new HashMap();
        paramsMap.put("from", from);
        paramsMap.put("dest", dest);
        paramsMap.put("format", "json");
        paramsMap.put("phrase", phrase);
        return paramsMap;
    }

    private ObjectAnimator swipeAnimation(View v, int offset, int direction){
        int off;

        if(direction == -1){
            off = -offset;
        } else {
            off = offset;
        }

        float startValue = v.getX();

        v.setVisibility(View.VISIBLE);

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "x", startValue + off, startValue);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        return anim;
    }

    private void animateTranslateCardView() {
        ObjectAnimator cardSwipe = swipeAnimation(mTranslateCardView, 1000, -1);
        cardSwipe.setDuration(1000);
        cardSwipe.start();
    }
}
