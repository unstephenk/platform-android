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
 * Implements {@link com.ushahidi.android.core.usecase.media.IGetMedia} that fetches list of {@link
 * com.ushahidi.android.core.entity.Media}
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class GetMedia implements IGetMedia {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private final IMediaRepository mMediaRepository;

    private final IMediaRepository.MediaDetailsCallback mMediaDetailsCallback =
            new IMediaRepository.MediaDetailsCallback() {

                @Override
                public void onMediaLoaded(Media media) {
                    notifySuccess(media);
                }

                @Override
                public void onError(ErrorWrap errorWrap) {
                    notifyFailure(errorWrap);
                }
            };

    private IGetMedia.Callback mCallback;

    private long mMediaId;

    public GetMedia(IMediaRepository mediaRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (mediaRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mMediaRepository = mediaRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(long mediaId, IGetMedia.Callback callback) {
        if (mediaId < 0 || callback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }
        mMediaId = mediaId;
        mCallback = callback;
        mThreadExecutor.execute(this);
    }

    private void notifySuccess(final Media media) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMediaLoaded(media);
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
        mMediaRepository.getMediaById(mMediaId, mMediaDetailsCallback);
    }

}
