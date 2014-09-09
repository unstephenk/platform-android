/**
 * GetUser, implements the get user use case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.domain.repository
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */
package com.ushahidi.android.core.usecase.user;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.executor.PostExecutionThread;
import com.ushahidi.android.core.executor.ThreadExecutor;
import com.ushahidi.android.core.respository.UserRepository;

/**
 * This class is an implementation of {@link IGetUser} that represents a use case for
 * retrieving data related to a specific {@link User}.
 */
public class GetUser implements IGetUser {

    private final UserRepository mUserRepository;

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private Callback mCallback;

    private int mId;

    public GetUser(UserRepository userRepository, ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        if (userRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null");
        }

        mUserRepository = userRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(int id, Callback callback) {
        if (id < 0 ) {
            throw new IllegalArgumentException("User id cannot be less than zero");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null");
        }

        mCallback = callback;
        mId = id;
        mThreadExecutor.execute(this);
    }

    @Override
    public void run() {
        mUserRepository.getUser(mId,mUserCallback);
    }

    private final UserRepository.UserCallback mUserCallback = new UserRepository.UserCallback() {
        @Override
        public void onUserLoaded(User user) {

        }

        @Override
        public void onError(ErrorWrap error) {

        }
    };

    private void notifySuccess(final User user) {
        mPostExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onUserLoaded(user);
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
