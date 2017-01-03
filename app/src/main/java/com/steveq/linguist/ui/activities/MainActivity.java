package com.steveq.linguist.ui.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.steveq.linguist.R;
import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.model.TranslateOutput;

public class MainActivity extends AppCompatActivity {

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

    private void creatFAB() {
        mActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        mAddFloatingActionButton = (FloatingActionButton) findViewById(R.id.addTranslateFab);
        mExecuteFloatingActionButton = (FloatingActionButton) findViewById(R.id.executeTranslateFab);
        mRefreshFloatingActionButton = (FloatingActionButton) findViewById(R.id.refreshFab);

        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "add FAB clicked", Toast.LENGTH_LONG).show();
                mAdapter.addOutputOption(new TranslateOutput());
                mAdapter.notifyDataSetChanged();
            }
        });

        mExecuteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "exe FAB clicked", Toast.LENGTH_LONG).show();
            }
        });

        mRefreshFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "refresh FAB clicked", Toast.LENGTH_LONG).show();
            }
        });
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

    private AnimatorSet scalingAnimation(View v){
        ObjectAnimator xScaling = ObjectAnimator.ofFloat(v, "scaleX", 0, 1);
        xScaling.setInterpolator(new AccelerateDecelerateInterpolator());
        xScaling.setDuration(500);

        ObjectAnimator yScaling = ObjectAnimator.ofFloat(v, "scaleY", 0, 1);
        yScaling.setInterpolator(new AccelerateDecelerateInterpolator());
        yScaling.setDuration(500);

        AnimatorSet set = new AnimatorSet();

        set.playTogether(xScaling, yScaling);
        return set;
    }

    private View createActivityView(){
        View view = this.getLayoutInflater().inflate(R.layout.activity_main, null, false);
        mInputWordEditText = (EditText) view.findViewById(R.id.inputWordEditText);
        mInputLanguageSpinner = (Spinner) view.findViewById(R.id.inputLanguageSpinner);
        mTranslateCardView = (CardView) view.findViewById(R.id.translateCardView);

        mTranslateCardView.setVisibility(View.INVISIBLE);
        animateTranslateCardView();

        return view;
    }

    private void animateTranslateCardView() {
        ObjectAnimator cardSwipe = swipeAnimation(mTranslateCardView, 1000, -1);
        cardSwipe.setDuration(1000);
        cardSwipe.start();
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
