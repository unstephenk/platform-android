/*
 * Copyright (c) 2014 Ushahidi.
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

import com.ushahidi.android.model.Model;
import com.ushahidi.android.ui.activity.BaseActivity;
import com.ushahidi.android.ui.adapter.BaseListAdapter;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Base {@link android.app.ListFragment} that every fragment list will extend from.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseListFragment<M extends Model, L extends BaseListAdapter>
        extends ListFragment {

    private static String TAG = BaseListFragment.class.getSimpleName();

    /**
     * Layout resource mId
     */
    protected final int mLayout;

    /**
     * Menu resource mId
     */
    protected final int mMenu;

    /**
     * ListView resource mId
     */
    private final int mListViewId;

    /**
     * ListAdpater class
     */
    private final Class<L> mAdapterClass;

    /**
     * ListAdapter
     */
    protected L mAdapter;

    /**
     * ListView
     */
    protected ListView mListView;

    protected BaseListFragment(Class<L> adapterClass, int layout, int menu, int listViewId) {
        mAdapterClass = adapterClass;
        mListViewId = listViewId;
        mMenu = menu;
        mLayout = layout;
    }

    /**
     * Uses reflection to create a new instance of a class
     *
     * @param type        The class to create an instance
     * @param constructor The constructor of the class
     * @param params      The parameters to pass to the constructor
     * @return The created instance
     */
    private static <T> T createInstance(Class type, Class constructor,
            Object... params) {
        try {
            return (T) type.getConstructor(constructor).newInstance(params);
        } catch (IllegalAccessException e) {
            Timber.e(TAG, "IllegalAccessException", e);
        } catch (IllegalStateException e) {
            Timber.e(TAG, "IllegalStateException", e);
        } catch (InvocationTargetException e) {
            Timber.e(TAG, "InvocationTargetException", e);
            for (StackTraceElement exception : e.getStackTrace()) {
                Timber.e(TAG,
                        String.format("%s", exception.toString()));
            }
        } catch (NoSuchMethodException e) {
            Timber.e(TAG, "NoSuchMethodException", e);
        } catch (InflateException e) {
            Timber.e(TAG, "InflateException", e);
        } catch (java.lang.InstantiationException e) {
            Timber.e(TAG, "InstantiationException", e);
        }
        return null;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        if (mListViewId != 0) {
            mListView = getListView();
            mAdapter = BaseListFragment.createInstance(mAdapterClass, Context.class, getActivity());
            mListView.setFocusable(true);
            mListView.setFocusableInTouchMode(true);
            mListView.setAdapter(mAdapter);
        }

    }

    /**
     * Initializes the {@link com.ushahidi.android.presenter.IPresenter} for this fragment in a MVP
     * pattern used to architect the application presentation layer.
     */
    abstract void initPresenter();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (mMenu != 0) {
            inflater.inflate(mMenu, menu);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        injectDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = null;
        if (mLayout != 0) {
            root = inflater.inflate(mLayout, container, false);
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    /**
     * Replace every field annotated using @Inject annotation with the provided dependency specified
     * inside a Dagger module value.
     */
    private void injectDependencies() {
        ((BaseActivity) getActivity()).inject(this);
    }

    protected M getSelectedItem() {
        return (M) mListView.getSelectedItem();
    }

    public void onItemSelected(AdapterView<?> adapterView,
            android.view.View view, int position, long id) {
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * value.
     *
     * @param view to extract each widget injected in the fragment.
     */
    private void injectViews(final View view) {
        ButterKnife.inject(this, view);
    }

    /**
     * Shows a {@link android.widget.Toast} message.
     *
     * @param message A message resource
     */
    protected void showToast(int message) {
        Toast.makeText(getActivity(), getText(message), Toast.LENGTH_LONG)
                .show();
    }
}
