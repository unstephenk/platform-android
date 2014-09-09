package com.ushahidi.android.core;

/**
 * Entity, base class for all entities to be derive from
 *
 * @author     Ushahidi Team <team@ushahidi.com>
 * @package    Ushahidi\Android\Dowmain
 * @copyright  2014 Ushahidi
 * @license    https://www.gnu.org/licenses/agpl-3.0.html GNU Affero General Public License Version 3 (AGPL3)
 */

public abstract class Entity {

    protected abstract int getId();

    protected abstract void setId(int id);
}
