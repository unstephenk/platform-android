package com.ushahidi.android.state;

import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.model.UserModel;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IUserState extends Istate {

    void setUserProfile(UserModel userProfile);

    void setActiveUserAccount(UserAccountModel account);

    void unauthorized();

    class AccountChangedEvent {

    }

    class UserProfileChangedEvent {

    }

    class UnauthorizedAccessEvent {

    }
}
