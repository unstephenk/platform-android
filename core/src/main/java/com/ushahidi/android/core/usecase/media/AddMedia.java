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
 * Use case for adding a new media to the database.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddMedia implements IAddMedia {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IMediaRepository mMediaRepository;

    private final IMediaRepository.MediaAddCallback mRepositoryCallback =
            new IMediaRepository.MediaAddCallback() {

                @Override
                public void onMediaAdded() {
                    notifySuccess();
                }

                @Override
                public void onError(ErrorWrap error) {
                    notifyFailure(error);
                }

            };

    private Media mMedia;

    private Callback mCallback;

    public AddMedia(IMediaRepository mediaRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (mediaRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mMediaRepository = mediaRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Media media, Callback callback) {
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
        mMediaRepository.addMedia(mMedia, mRepositoryCallback);
    }


    private void notifySuccess() {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMediaAdded();
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
