/**
 * Ushahidi GetUser Use Case
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.domain.usecase
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */
package com.ushahidi.android.core.usecase.user;


import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.Interactor;

public interface GetUser extends Interactor {
    /**
     * Callback used to be notified when either a user has been loaded or an error happened.
     */
    interface Callback {
        void onUserLoaded(User user);
        void onError(ErrorWrap error);
    }

    /**
     * Executes this use case.
     *
     * @param id The user id to retrieve.
     * @param callback A {@link GetUser.Callback} used for notify the client.
     */
    public void execute(int id, Callback callback);
}
