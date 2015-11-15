package com.bignerdranch.android.reciper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by bubujay on 11/13/15.
 */
public class SnapFragment extends Fragment {

    final static String SNAP_ID = "com.genius.android.reciper";
    private List<Snap> mRecipe;
    private Button mVP;
    private TextView mcoordView;
    private TextView mTitle;
    private ImageView mSnapImage;
    private int snapID;
    public float x;
    public float y;

    public static SnapFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable(SNAP_ID, position);

        SnapFragment fragment = new SnapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snapID = (int)getArguments().getSerializable(SNAP_ID);
        //mCoord.setText("3");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        Recipe.getThisRecipe().testImageSetter();
        mRecipe = Recipe.getThisRecipe().getSnaps();
        mSnapImage = (ImageView) v.findViewById(R.id.snap_image);
        mVP = (Button) v.findViewById(R.id.vpager_button);
        mSnapImage.setImageResource(mRecipe.get(snapID).getPicture());

        mVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                Log.d("fragment", "you touched at x: " + x + " y: " + y);
                String toastString = "x : " + x + " y: " + y;
                Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
            }
        });

        mSnapImage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getRawX();
                    y = event.getRawY();
                    String toastString = "x : " + x + " y: " + y;
                    Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                    Log.d("fragment", "you touched at x: " + x + " y: " + y);
                }
                return true;
            }
        });
        return v;
    }
}
