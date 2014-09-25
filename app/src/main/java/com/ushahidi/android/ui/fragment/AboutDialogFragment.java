/*
 * Copyright (c) 2014 Ushahidi.
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

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */

import com.ushahidi.android.BuildConfig;
import com.ushahidi.android.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * About Dialog fragment
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AboutDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();

        // Make sure getDialog is not null
        if(getDialog() == null) {
            return;
        }
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_fragment_about, null);

        init(view);

        builder.setView(view);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.about);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void init(View view) {
        if (view != null) {
            TextView version = (TextView) view.findViewById(R.id.app_version);
            StringBuilder sBuilder = new StringBuilder(getString(R.string.version));
            sBuilder.append(" ");
            sBuilder.append(BuildConfig.VERSION_NAME);
            version.setText(sBuilder.toString());

        }
    }

}
