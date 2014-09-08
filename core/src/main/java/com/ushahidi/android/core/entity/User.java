/**
 * Ushahidi User Entity
 *
 * @author Ushahidi Team <team@ushahidi.com>
 * @package com.ushahidi.android.domain.entity
 * @copyright 2014 Ushahidi
 * @license https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3
 * (AGPL3)
 */
package com.ushahidi.android.core.entity;

import com.ushahidi.android.core.Entity;

public class User extends Entity{

    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
