package com.example.alexander.halalappv1.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.DisplayImageActivity;
import com.example.alexander.halalappv1.utils.ScreenUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressLint("ValidFragment")
public class GalleryFragment extends Fragment {

    public static final String RESTAURANT_NAME_KEY = "RestaurantName";
    public static final String IMAGE_URL_KEY = "ImageUrl";

    public int mPosition;
    public List<List<String>> mImagesList;
    public List<String> mGridImagesList;
    private String mRestaurantName;

    @SuppressLint("ValidFragment")
    public GalleryFragment(int position, List<List<String>> imagesList, String restaurantName) {
        this.mPosition = position;
        this.mImagesList = imagesList;
        this.mGridImagesList = mImagesList.get(position);
        this.mRestaurantName = restaurantName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, null);
        GridView gridView = rootView.findViewById(R.id.gallery_fragment_grid_view);
        gridView.setAdapter(new ImageAdapter(getContext()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getContext(), DisplayImageActivity.class);
                intent.putExtra(RESTAURANT_NAME_KEY, mRestaurantName);
                intent.putExtra(IMAGE_URL_KEY, mGridImagesList.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

        ScreenUtility screenUtility = new ScreenUtility(getActivity());
        float dpWidth = screenUtility.getWidth();
        float dpHeight = screenUtility.getHeight();
        int imageWidth = dpToPx(dpWidth) / 3;
        int imageHeight = dpToPx(dpHeight - 166) / 3;

        public ImageAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return mGridImagesList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mainView;
            if (convertView == null) {
                mainView = inflater.inflate(R.layout.gallery_list_item, null);
                ImageView galleryImageView = mainView.findViewById(R.id.iv_gallery_image);
                galleryImageView.getLayoutParams().width = imageWidth;
                galleryImageView.getLayoutParams().height = imageHeight;

//                if (position == 0 || position == 3 || position == 6) {
//                    galleryImageView.setPadding(dpToPx(16), 0, dpToPx(8), dpToPx(16));
//                } else if (position == 1 || position == 4 || position == 7) {
//                    galleryImageView.setPadding(dpToPx(8), 0, dpToPx(8), dpToPx(16));
//                } else if (position == 2 || position == 5 || position == 8) {
//                    galleryImageView.setPadding(dpToPx(8), 0, dpToPx(16), dpToPx(16));
//                }

                Picasso.with(getActivity())
                        .load(mGridImagesList.get(position))
                        .error(R.drawable.ic_launcher_background)
                        .into(galleryImageView);
            } else {
                mainView = convertView;
            }

            return mainView;
        }

        private int dpToPx(float dp) {
            float density = getActivity().getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }
    }
}
