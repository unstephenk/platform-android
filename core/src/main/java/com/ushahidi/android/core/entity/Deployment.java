/**
 * Deployment, entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.core.entity
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */
package com.ushahidi.android.core.entity;

import com.ushahidi.android.core.Entity;

public class Deployment extends Entity{

    private int mId;
    @Override
    protected int getId() {
        return 0;
    }

    @Override
    protected void setId(int id) {
        mId = id;
    }
}
