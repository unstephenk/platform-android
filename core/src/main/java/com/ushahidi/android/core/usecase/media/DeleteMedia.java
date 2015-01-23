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
import com.ushahidi.android.core.repository.IMediaRepository;
import com.ushahidi.android.core.task.PostExecutionThread;
import com.ushahidi.android.core.task.ThreadExecutor;

/**
 * Delete media use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeleteMedia implements IDeleteMedia {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private IDeleteMedia.Callback mCallback;

    private final IMediaRepository mMediaRepository;

    private Media mMedia;

    public DeleteMedia(IMediaRepository mediaRepository,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (mediaRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mMediaRepository = mediaRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    private final IMediaRepository.MediaDeletedCallback mMediaDeletedCallback =
            new IMediaRepository.MediaDeletedCallback() {

                @Override
                public void onMediaDeleted() {
                    notifySuccess();
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }
            };

    @Override
    public void execute(Media media, IDeleteMedia.Callback callback) {
        if (media == null) {
            throw new IllegalArgumentException("Media cannot be null");
        }

        if (callback == null) {
            throw new IllegalArgumentException("Use case callback cannot be null");
        }

        mMedia = media;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mMediaRepository.deleteMedia(mMedia, mMediaDeletedCallback);
    }

    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMediaDeleted();
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
