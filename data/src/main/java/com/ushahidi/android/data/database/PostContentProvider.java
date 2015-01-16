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

package com.ushahidi.android.data.database;

import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.task.TaskExecutor;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostContentProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = PostContentProvider.class
            .getName();

    public static final int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    private static final String TAG = PostContentProvider.class.getSimpleName();

    private static final String[] COLUMNS = {
            BaseColumns._ID, // must include this column
            SearchManager.SUGGEST_COLUMN_TEXT_1, // First line (title)
            SearchManager.SUGGEST_COLUMN_TEXT_2, // Second line (smaller text)
            SearchManager.SUGGEST_COLUMN_SHORTCUT_ID
    };


    public PostContentProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        String query = selectionArgs[0];
        if (query == null || query.length() == 0) {
            return null;
        }

        MatrixCursor cursor = new MatrixCursor(COLUMNS);

        try {
            List<PostEntity> list = getSearchResults(query);
            for (int i = 0; i < list.size(); i++) {
                cursor.addRow(createRow(i, list.get(i).getTitle(), list.get(i).getContent(),
                        list.get(i).getId()));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to lookup " + query, e);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    private Object[] createRow(Integer id, String text1, String text2, Long rowId) {
        return new Object[]{
                id, // _id
                text1, // text1
                text2, // text2
                String.valueOf(rowId),
                "android.intent.action.SEARCH", // action
                SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT};
    }

    private List<PostEntity> getSearchResults(String query) {
        List<PostEntity> postEntities = new ArrayList<>();
        final PostDatabaseHelper mPostDatabaseHelper = PostDatabaseHelper.getInstance(getContext(),
                TaskExecutor.getInstance());
        if (!TextUtils.isEmpty(query)) {
            query = query.toLowerCase(Locale.ENGLISH);
            postEntities = mPostDatabaseHelper.search(query);
            Collections.sort(postEntities);
        }
        return postEntities;
    }
}
