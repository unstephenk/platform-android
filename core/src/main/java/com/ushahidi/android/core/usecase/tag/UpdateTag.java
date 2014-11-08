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

package com.ushahidi.android.core.usecase.tag;

import com.ushahidi.android.core.entity.Tag;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.ITagRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * Implements update tag use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UpdateTag implements IUpdateTag {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final ITagRepository mTagRepository;

    private final ITagRepository.TagUpdateCallback mTagUpdateCallback =
            new ITagRepository.TagUpdateCallback() {

                @Override
                public void onTagUpdated() {
                    notifySuccess();
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }
            };

    private Tag mTag;

    private Callback mCallback;

    public UpdateTag(ITagRepository tagRepository,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (tagRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mTagRepository = tagRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Tag tag, Callback callback) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }

        if (callback == null) {
            throw new IllegalArgumentException("Use case callback cannot be null");
        }

        mTag = tag;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mTagRepository.updateTag(mTag, mTagUpdateCallback);
    }

    /**
     * Notifies client when a successful update occurs.
     */
    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTagUpdated();
            }
        });
    }

    /**
     * Notifies client of any failure that may occuring during the update process
     *
     * @param errorWrap The {@link com.ushahidi.android.core.exception.ErrorWrap}
     */
    private void notifyFailure(final ErrorWrap errorWrap) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(errorWrap);
            }
        });
    }
}
