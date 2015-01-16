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

package com.ushahidi.android.core.usecase;

import com.ushahidi.android.core.Entity;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.ISearchRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

import java.util.List;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class Search<E extends Entity> implements ISearch<E> {

    private ISearchRepository mSearchRepository;

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private ISearch.Callback mCallback;

    private String mQuery;

    private final ISearchRepository.SearchCallback mSearchCallback =
            new ISearchRepository.SearchCallback<E>() {


                @Override
                public void onSearchResult(List<E> entityList) {
                    notifySuccess(entityList);
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }
            };

    /**
     * Default constructor.
     *
     * @param threadExecutor      {@link ThreadExecutor} used to execute this use case in a
     *                            background thread.
     * @param postExecutionThread {@link PostExecutionThread} used to post updates when the use case
     *                            has been executed.
     */
    public Search(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {

        if (threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }

        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public void setRepository(ISearchRepository searchRepository) {

        if(searchRepository == null) {
            throw new IllegalArgumentException("SearchRepository cannot be null");
        }
        mSearchRepository = searchRepository;
    }

    @Override
    public void execute(String query, ISearch.Callback callback) {

        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }

        mQuery = query;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        if(mSearchRepository == null) {
            throw new NullPointerException("You must call setPostRepository(...)");
        }
        mSearchRepository.search(mQuery, mSearchCallback);
    }

    private void notifySuccess(final List<E> entityList) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onEntityFound(entityList);
            }
        });
    }

    private void notifyFailure(final ErrorWrap errorWrap) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(errorWrap);
            }
        });
    }
}
