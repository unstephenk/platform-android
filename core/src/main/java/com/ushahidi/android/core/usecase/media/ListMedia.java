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

package com.ushahidi.android.core.usecase.media;

import com.ushahidi.android.core.entity.Media;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.respository.IMediaRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

import java.util.List;

/**
 * Use case that fetches a list of media items.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class ListMedia implements IListMedia {

    private final IMediaRepository mIMediaRepository;

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IMediaRepository.MediaListCallback mRepositoryCallback
            = new IMediaRepository.MediaListCallback() {

        @Override
        public void onMediaListLoaded(List<Media> mediaList) {
            notifySuccess(mediaList);
        }

        @Override
        public void onError(ErrorWrap error) {
            notifyFailure(error);
        }
    };

    private IListMedia.Callback mCallback;

    /**
     * Constructor.
     *
     * @param mediaRepository     A {@link com.ushahidi.android.core.respository.IMediaRepository}
     *                            as a source for retrieving data.
     * @param threadExecutor      {@link ThreadExecutor} used to execute this use case in a
     *                            background thread.
     * @param postExecutionThread {@link PostExecutionThread} used to post updates when the use case
     *                            has been executed.
     */
    public ListMedia(IMediaRepository mediaRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (mediaRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        mIMediaRepository = mediaRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(IListMedia.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mIMediaRepository.getMediaList(mRepositoryCallback);
    }

    private void notifySuccess(final List<Media> mediaList) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMediaListLoaded(mediaList);
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
