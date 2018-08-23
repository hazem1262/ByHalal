package com.example.alexander.halalappv1.gallery;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class GalleryPagerAdapter extends FragmentPagerAdapter {

    private List<List<String>> mImagesList;
    private int mCount ;
    private String mRestaurantName;

    public GalleryPagerAdapter(FragmentManager fragmentManager, List<List<String>> imagesList , int count, String restaurantName) {
        super(fragmentManager);
        this.mImagesList = imagesList;
        this.mCount = count ;
        this.mRestaurantName = restaurantName;
    }

    @Override
    public Fragment getItem(int position) {
        GalleryFragment galleryFragment = new GalleryFragment(position, mImagesList, mRestaurantName);
        return galleryFragment;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
