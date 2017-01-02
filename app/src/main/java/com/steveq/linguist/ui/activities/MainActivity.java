package com.steveq.linguist.ui.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.steveq.linguist.R;
import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.model.TranslateOutput;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditText mInputWordEditText;
    private Spinner mInputLanguageSpinner;
    private FloatingActionButton mAddFloatingActionButton;
    private FloatingActionButton mExecuteFloatingActionButton;
    private TranslatesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(createActivityView());
        createRecyclerView();
        creatFAB();
    }

    private void creatFAB() {
        mAddFloatingActionButton = (FloatingActionButton) findViewById(R.id.addTranslateFab);
        mExecuteFloatingActionButton = (FloatingActionButton) findViewById(R.id.executeTranslateFab);

        animateFab();

        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "FAB clicked", Toast.LENGTH_LONG).show();
                mAdapter.addOutputOption(new TranslateOutput());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void animateFab() {

        mExecuteFloatingActionButton.setScaleX(0);
        mExecuteFloatingActionButton.setScaleY(0);

        AnimatorSet execFabScaling = scalingAnimation(mAddFloatingActionButton);
        ObjectAnimator execFabSwipe = swipeAnimation(mAddFloatingActionButton);

        AnimatorSet addFabScaling = scalingAnimation(mExecuteFloatingActionButton);
        ObjectAnimator addFabSwipe = swipeAnimation(mExecuteFloatingActionButton);

        AnimatorSet execAnim = new AnimatorSet();
        execAnim.playTogether(execFabScaling, execFabSwipe);

        AnimatorSet addAnim = new AnimatorSet();
        addAnim.playTogether(addFabScaling, addFabSwipe);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(addAnim, execAnim);
        set.start();
    }

    private ObjectAnimator swipeAnimation(View v){
        float startValue = v.getX();

        v.setX(v.getX() + 200);

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "x", startValue + 200, startValue);
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
