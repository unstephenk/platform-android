/**
 * Ushahidi UserRepository
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.domain.respository
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */
package com.ushahidi.android.core.respository;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;

/**
 * Interface that represents a Repository for getting {@link User} related data.
 */
public interface UserRepository {

    interface UserCallback {
        void onUserLoaded(User user);

        void onError(ErrorWrap error);
    }

    /**
     * Get an {@link User} by id.
     *
     * @param id The user id used to retrieve user data.
     * @param userCallback A {@link UserCallback} used for notifying clients.
     */
    void getUser(int id, UserCallback userCallback);
}
