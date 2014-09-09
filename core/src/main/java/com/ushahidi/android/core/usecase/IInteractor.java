/**
 * Interactor, an interface for executing the different use cases
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.domain.usecase
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */
package com.ushahidi.android.core.usecase;

/**
 * This interface represents a execution unit for different use cases. Use cases have to implement this
 * interface.
 */
public interface IInteractor extends Runnable {

    /**
     * Execute asynchronously
     */
    void run();
}
