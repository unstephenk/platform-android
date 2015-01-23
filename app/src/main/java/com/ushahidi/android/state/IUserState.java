package com.ushahidi.android.state;

import com.ushahidi.android.model.UserAccountModel;
import com.ushahidi.android.model.UserModel;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public interface IUserState extends Istate {

    public void setUserProfile(UserModel userProfile);

    public void setActiveUserAccount(UserAccountModel account);

    public void unauthorized();

    public static class AccountChangedEvent {

    }

    public static class UserProfileChangedEvent {

    }

    public static class UnauthorizedAccessEvent {

    }
}
