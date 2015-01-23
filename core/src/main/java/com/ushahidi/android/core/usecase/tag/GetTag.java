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
import com.ushahidi.android.core.repository.ITagRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * Implements {@link IGetTag} that fetches list
 * of {@link com.ushahidi.android.core.entity.Tag}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GetTag implements IGetTag {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final ITagRepository mTagRepository;

    private final ITagRepository.TagDetailsCallback mTagDetailsCallback =
            new ITagRepository.TagDetailsCallback() {

                @Override
                public void onTagLoaded(Tag tag) {
                    notifySuccess(tag);
                }

                @Override
                public void onError(ErrorWrap errorWrap) {
                    notifyFailure(errorWrap);
                }
            };

    private Callback mCallback;

    private long mTagId;

    public GetTag(ITagRepository tagRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (tagRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mTagRepository = tagRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(long tagId, Callback callback) {
        if (tagId < 0 || callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mTagId = tagId;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    private void notifySuccess(final Tag tag) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTagLoaded(tag);
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

    @Override
    public void run() {
        mTagRepository.getTagById(mTagId, mTagDetailsCallback);
    }
}
