/*
 * Copyright (c) 2015 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.ui.fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import com.ushahidi.android.R;
import com.ushahidi.android.model.PostModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class MapPostFragment extends BaseFragment implements
        OnMapReadyCallback, ClusterManager.OnClusterClickListener<PostModel>,
        ClusterManager.OnClusterInfoWindowClickListener<PostModel>,
        ClusterManager.OnClusterItemInfoWindowClickListener<PostModel> {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static MapPostFragment mMapPostFragment;

    private ClusterManager<PostModel> mClusterManager;

    private MapFragment mMapFragment;

    private GoogleMap mMap;

    private Random mRandom = new Random(1984);

    private HashMap<Marker, PostModel> markers = new HashMap<>();

    /**
     * BaseFragment
     */
    public MapPostFragment() {
        super(R.layout.map_post, 0);
    }

    public static MapPostFragment newInstance() {

        if (mMapPostFragment == null) {
            mMapPostFragment = new MapPostFragment();
        }

        return mMapPostFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    public void onResume() {
        super.onResume();
        // Set up Google map
        setUpMapIfNeeded();
    }

    @Override
    void initPresenter() {
        // Make sure there is Google play service installed on the user's device
        checkPlayServices();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                mClusterManager = new ClusterManager<>(getActivity(), mMap);
                mClusterManager.setRenderer(new PostModelRenderer());
                mMap.setOnCameraChangeListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
                mMap.setOnInfoWindowClickListener(mClusterManager);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mClusterManager.setOnClusterInfoWindowClickListener(this);
                mClusterManager.setOnClusterItemInfoWindowClickListener(this);

                addDummyPostItems();
                mClusterManager.cluster();
            }
        }
    }

    //TODO: Remove this dummy generated post model
    private void addDummyPostItems() {
        for(int i =0; i < 50; i++) {
            PostModel postModel = new PostModel();
            postModel.setTitle("New "+i);
            postModel.setCreated(new Date());
            postModel.setContent("Junction");
            postModel.setPosition(position());
            if(mClusterManager !=null) {
                mClusterManager.addItem(postModel);
            }
        }

    }

    //TODO: Remove this dummy generated latng
    private LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }

    /**
     * Check if Google play services is installed on the user's device. If it's not
     * prompt user to about it.
     *
     * @return boolean
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public boolean onClusterClick(Cluster<PostModel> postModelCluster) {

        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<PostModel> postModelCluster) {
       //Do nothing
    }

    @Override
    public void onClusterItemInfoWindowClick(PostModel postModel) {
        //TODO launch post detail view
        //For now show a toast with the title
        showToast(postModel.getTitle());
    }

    /**
     * Draws custom colored circle for clustered pins.
     */
    private class PostModelRenderer extends DefaultClusterRenderer<PostModel> {
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getActivity());

        private final float mDensity;

        private ShapeDrawable mColoredCircleBackground;

        /**
         * Icons for each bucket.
         */
        private SparseArray<BitmapDescriptor> mIcons = new SparseArray<BitmapDescriptor>();

        public PostModelRenderer() {
            super(getActivity().getApplicationContext(), mMap, mClusterManager);
            mDensity = getActivity().getResources().getDisplayMetrics().density;
            mClusterIconGenerator.setContentView(makeSquareTextView(getActivity()));
            mClusterIconGenerator.setTextAppearance(R.style.CustomClusterIcon_TextAppearance);
            mClusterIconGenerator.setBackground(makeClusterBackground());
        }

        private SquareTextView makeSquareTextView(Context context) {
            SquareTextView squareTextView = new SquareTextView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            squareTextView.setLayoutParams(layoutParams);
            squareTextView.setId(R.id.text);
            squareTextView.setTextColor(getActivity().getResources().getColor(R.color.body_text_1));
            int twelveDpi = (int) (12 * mDensity);
            squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
            return squareTextView;
        }

        private LayerDrawable makeClusterBackground() {
            mColoredCircleBackground = new ShapeDrawable(new OvalShape());
            ShapeDrawable outline = new ShapeDrawable(new OvalShape());

            outline.getPaint().setColor(getActivity().getResources()
                    .getColor(R.color.cluster_solid_color)); // Solid red
            LayerDrawable background = new LayerDrawable(new Drawable[]{outline, mColoredCircleBackground});
            int strokeWidth = (int) (mDensity * 5);
            background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
            return background;
        }

        private int getColor(int clusterSize) {
            final float hueRange = 100;
            final float sizeRange = 300;
            final float size = Math.min(clusterSize, sizeRange);
            final float hue = (sizeRange - size) * (sizeRange - size) / (sizeRange * sizeRange) * hueRange;
            return Color.HSVToColor(new float[]{
                    hue, 0f, 1f
            });
        }

        @Override
        protected void onBeforeClusterItemRendered(PostModel post, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(post, markerOptions);
            markerOptions.snippet(post.getContent());
            markerOptions.title(post.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<PostModel> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
            int bucket = getBucket(cluster);
            BitmapDescriptor descriptor = mIcons.get(bucket);

            if (descriptor == null) {
                mColoredCircleBackground.getPaint().setColor(getColor(bucket));
                descriptor = BitmapDescriptorFactory
                        .fromBitmap(mClusterIconGenerator.makeIcon(getClusterText(bucket)));
                mIcons.put(bucket, descriptor);
            }

            markerOptions.anchor(.5f,.5f);
            markerOptions.icon(descriptor);

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

        protected void onClusterRendered(Cluster<PostModel> cluster, Marker marker) {
            super.onClusterRendered(cluster, marker);
            for (PostModel postModel : cluster.getItems()) {
                markers.put(marker, postModel);
            }
        }
    }
}
