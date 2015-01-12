/*
 * Copyright (c) 2015 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */

package com.ushahidi.android.ui.fragment;

import com.andreabaccega.widget.FormEditText;
import com.ushahidi.android.R;
import com.ushahidi.android.presenter.SendFeedbackPresenter;

import android.content.Context;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class SendFeedbackFragment extends BaseFragment implements SendFeedbackPresenter.View {

    @Inject
    SendFeedbackPresenter mSendFeedbackPresenter;

    @InjectView(R.id.feedback_message)
    FormEditText mFormEditText;

    @InjectView(R.id.feedback_device_info)
    TextView mDeviceInfo;

    @InjectView(R.id.select_subject)
    Spinner mSubject;

    public SendFeedbackFragment() {
        super(R.layout.fragment_send_feedback, 0);
    }

    @Override
    void initPresenter() {
        mSendFeedbackPresenter.setView(this);
        final String deviceInfo = mSendFeedbackPresenter.getDeviceInfo();
        mDeviceInfo.setText(deviceInfo);
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @OnClick(R.id.feedback_send)
    public void onClickSubmit() {
        final String subject = mSubject.getSelectedItem().toString();
        final String deviceInfo = mDeviceInfo.getText().toString();
        final String message = mFormEditText.getText().toString();
        mSendFeedbackPresenter.send(subject, deviceInfo, message);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
