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

package com.ushahidi.android.ui.adapter;

import com.ushahidi.android.R;
import com.ushahidi.android.model.DeploymentModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

/**
 * Manages list of deployments
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class DeploymentAdapter extends BaseListAdapter<DeploymentModel> {

    public DeploymentAdapter(Context context, List<DeploymentModel> deploymentModelList) {
        this(context);
        setItems(deploymentModelList);
    }

    public DeploymentAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Widgets widgets;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_deployment_item, null);
            widgets = new Widgets(view);
            view.setTag(widgets);
        } else {
            widgets = (Widgets) view.getTag();
        }

        // initialize view with content
        widgets.title.setText(getItem(position).getTitle());
        widgets.description.setText(getItem(position).getDescription());
        widgets.position = position;

        /*if (getItem(position).getStatus() == 1) {
            widgets.listCheckBox.setChecked(true);
        } else {
            widgets.listCheckBox.setChecked(false);
        }*/

        return view;
    }

    public class Widgets implements View.OnClickListener {

        TextView title;

        TextView description;

        CheckedTextView listCheckBox;

        int position = 0;

        public Widgets(View convertView) {

            title = (TextView) convertView.findViewById(R.id.deployment_title);
            description = (TextView) convertView.findViewById(R.id.deployment_description);

            listCheckBox = (CheckedTextView) convertView
                    .findViewById(R.id.deployment_selected);

            listCheckBox.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listCheckBox.isChecked()) {
                listCheckBox.setChecked(false);
            } else {

                listCheckBox.setChecked(true);
            }
        }
    }
}
