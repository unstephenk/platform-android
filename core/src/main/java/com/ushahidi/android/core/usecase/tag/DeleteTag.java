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
 * Deletes tag
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteTag implements IDeleteTag {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private Callback mCallback;

    private final ITagRepository mTagRepository;

    private Tag mTag;

    public DeleteTag(ITagRepository tagRepository,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (tagRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mTagRepository = tagRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    private final ITagRepository.TagDeletedCallback mTagDeletedCallback =
            new ITagRepository.TagDeletedCallback() {

                @Override
                public void onTagDeleted() {
                    notifySuccess();
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }
            };

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
        mTagRepository.deleteTag(mTag, mTagDeletedCallback);
    }

    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTagDeleted();
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
