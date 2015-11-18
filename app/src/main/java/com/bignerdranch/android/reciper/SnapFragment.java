package com.bignerdranch.android.reciper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    public Button mNewRecipe;
    public Button mAllRecipes;
    //private Bitmap background =
      //      ((BitmapDrawable)getResources().getDrawable(R.drawable.kitchen2)).getBitmap();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.start_page, container, false);
        //Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.nice);
        //v.setBackground(new BitmapDrawable(getActivity().getResources(), background));

       /* Recipe.getThisRecipe().testImageSetter();
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
        });*/
        return v;
    }
}
