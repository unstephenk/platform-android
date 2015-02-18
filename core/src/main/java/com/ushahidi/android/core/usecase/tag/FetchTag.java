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

package com.ushahidi.android.core.usecase.tag;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.repository.ITagRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

import java.util.List;

/**
 * Use case for fetching tags from the API
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class FetchTag implements IFetchTag {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final ITagRepository.TagListCallback mTagListCallback
            = new ITagRepository.TagListCallback() {
        @Override
        public void onTagListLoaded(List<Tag> tagList) {
            notifySuccess(tagList);
        }

        @Override
        public void onError(ErrorWrap errorWrap) {
            notifyFailure(errorWrap);
        }
    };

    private ITagRepository mTagRepository;

    private Callback mCallback;

    /**
     * Default constructor.
     *
     * @param threadExecutor      {@link ThreadExecutor} used to execute this use case in a
     *                            background thread.
     * @param postExecutionThread {@link PostExecutionThread} used to post updates when the use case
     *                            has been executed.
     */
    public FetchTag(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public void setTagRepository(ITagRepository tagRepository) {
        if (tagRepository == null) {
            throw new NullPointerException("TagRepository cannot be null");
        }

        mTagRepository = tagRepository;
    }


    @Override
    public void execute(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        if (mTagRepository == null) {
            throw new NullPointerException("You must call setTagRepository(...)");
        }
        mTagRepository.getTagListViaApi(mTagListCallback);
    }

    private void notifySuccess(final List<Tag> tagList) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTagFetched(tagList);
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
