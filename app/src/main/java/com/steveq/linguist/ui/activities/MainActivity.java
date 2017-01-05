package com.steveq.linguist.ui.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.steveq.linguist.Api.GlosbeAPI;
import com.steveq.linguist.Api.GlosbeClient;
import com.steveq.linguist.R;
import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.model.response.Phrase;
import com.steveq.linguist.model.response.TranslationResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements Callback<TranslationResponse> {

    private RecyclerView mRecyclerView;
    private EditText mInputWordEditText;
    private Spinner mInputLanguageSpinner;
    private FloatingActionMenu mActionMenu;
    private FloatingActionButton mAddFloatingActionButton;
    private FloatingActionButton mExecuteFloatingActionButton;
    private FloatingActionButton mRefreshFloatingActionButton;
    private CardView mTranslateCardView;
    private TranslatesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(createActivityView());
        createRecyclerView();
        creatFAB();
    }

    private View createActivityView(){
        View view = this.getLayoutInflater().inflate(R.layout.activity_main, null, false);
        mInputWordEditText = (EditText) view.findViewById(R.id.inputWordEditText);
        mInputLanguageSpinner = (Spinner) view.findViewById(R.id.inputLanguageSpinner);
        mTranslateCardView = (CardView) view.findViewById(R.id.translateCardView);

        mTranslateCardView.setVisibility(View.INVISIBLE);
        animateTranslateCardView();

        mInputLanguageSpinner.setSelection(1);

        return view;
    }

    private void createRecyclerView() {
        mAdapter = new TranslatesAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.translatesRecycler);
        mRecyclerView.hasFixedSize();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void creatFAB() {
        mActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        mAddFloatingActionButton = (FloatingActionButton) findViewById(R.id.addTranslateFab);
        mExecuteFloatingActionButton = (FloatingActionButton) findViewById(R.id.executeTranslateFab);
        mRefreshFloatingActionButton = (FloatingActionButton) findViewById(R.id.refreshFab);

        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "add FAB clicked", Toast.LENGTH_LONG).show();
                mAdapter.addOutputOption(new Phrase());
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

                    Map paramsMap = generateParamsMap(from, dest, phrase);

                    Call<TranslationResponse> call = glosbeAPI.loadTranslation(paramsMap);
                    call.enqueue(MainActivity.this);
                }
            }
        });

        mRefreshFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "refresh FAB clicked", Toast.LENGTH_LONG).show();
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

    @Override
    public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {

        String translatedText = response.body().getTuc().get(0).getPhrase().getText();
        String destLan = response.body().getTuc().get(0).getPhrase().getLanguage();
        for(int i=0; i < mAdapter.getOutputs().size(); i++){
            String destLanKeep = mAdapter.getOutputs().get(i).getLanguage().substring(0, 2);
            if(destLan.equals(destLanKeep)){
                mAdapter.getOutputs().get(i).setText(translatedText);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<TranslationResponse> call, Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
