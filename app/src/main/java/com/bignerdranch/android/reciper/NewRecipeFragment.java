package com.bignerdranch.android.reciper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by bubujay on 11/19/15.
 */
public class NewRecipeFragment extends Fragment {
    private final static int REQUEST_PHOTO = 0;

    private Button mNewPhoto;
    private RecipeBook mTheBook;
    //private int mPicker = 0;
    //private ArrayList<Snap> mSnapSnaps = new ArrayList<>();
    //private ArrayList<File> mSnapPhotos = new ArrayList<>();

    public static Fragment newInstance(){
        NewRecipeFragment fragment = new NewRecipeFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_recipe_page, container, false);
        mNewPhoto = (Button)v.findViewById(R.id.new_recipe_camera_button);

        mTheBook = RecipeBook.getTheRecipeBook(getActivity());
        mNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTheBook.getLatest().newSnap();
            }
        });


        return v;
    }
}

/*code that should go inside onCreateView()*/

        /*RecipeInfoDialog dialog = RecipeInfoDialog.newInstance();
        dialog.show(getFragmentManager(), "Input Recipe Name and category");*/


        /*mTheBook = RecipeBook.getTheRecipeBook();
        mTheBook.newRecipe("test");
        Snap testSnap = mTheBook.getLatest().newSnap();

        PackageManager packageManager = getActivity().getPackageManager();

        final Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = testSnap.getPhotoFile()!= null &&
                photoIntent.resolveActivity(packageManager) != null;

        //mNewPhoto.setEnabled(canTakePhoto);

        if(canTakePhoto) {
            Uri uri = Uri.fromFile(testSnap.getPhotoFile());
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(photoIntent, REQUEST_PHOTO);

                // mPicker++;
                //v = inflater.inflate(R.layout.activity_fragment_c, container, false);

            }
        });*/