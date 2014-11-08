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

import java.util.List;

/**
 * Implements {@link IListTag} that fetches list of {@link com.ushahidi.android.core.entity.Tag}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListTag implements IListTag {

    private final ITagRepository mITagRepository;

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final ITagRepository.TagListCallback mRepositoryCallback
            = new ITagRepository.TagListCallback() {

        @Override
        public void onTagListLoaded(List<Tag> tagList) {
            notifySuccess(tagList);
        }

        @Override
        public void onError(ErrorWrap error) {
            notifyFailure(error);
        }
    };

    private Callback mCallback;

    /**
     * Constructor.
     *
     * @param tagRepository       A {@link com.ushahidi.android.core.respository.ITagRepository} as
     *                            a source for retrieving data.
     * @param threadExecutor      {@link com.ushahidi.android.core.task.ThreadExecutor} used to
     *                            execute this use case in a background thread.
     * @param postExecutionThread {@link com.ushahidi.android.core.task.PostExecutionThread} used to
     *                            post updates when the use case has been executed.
     */
    public ListTag(ITagRepository tagRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (tagRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        mITagRepository = tagRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
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
        mITagRepository.getTagList(mRepositoryCallback);
    }

    private void notifySuccess(final List<Tag> tagList) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTagListLoaded(tagList);
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
