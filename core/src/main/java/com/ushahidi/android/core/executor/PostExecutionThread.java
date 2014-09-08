/**
 * Ushahidi PostExecutionThread
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.domain.executor
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */
package com.ushahidi.android.core.executor;

/**
 * Thread abstraction created to change the execution context from any thread to any other thread.
 * implementation of this interface will change context and update the UI.
 */
public interface PostExecutionThread {

    /**
     * Causes the {@link Runnable} to be added to the message queue of the Main UI Thread
     * of the application.
     *
     * @param runnable {@link Runnable} to be executed.
     */
    void post(Runnable runnable);
}
