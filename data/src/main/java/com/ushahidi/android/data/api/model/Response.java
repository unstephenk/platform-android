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

package com.ushahidi.android.data.api.model;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class Response {

    private int limit;

    private int offset;

    private String order;

    private String created;

    private String curr;

    private String next;

    private String prev;

    private int count;

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public String getCreated() {
        return created;
    }

    public String getOrder() {
        return order;
    }

    public String getCurr() {
        return curr;
    }

    public String getNext() {
        return next;
    }

    public String getPrev() {
        return prev;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Response{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", order='" + order + '\'' +
                ", created='" + created + '\'' +
                ", curr='" + curr + '\'' +
                ", next='" + next + '\'' +
                ", prev='" + prev + '\'' +
                ", count=" + count +
                '}';
    }
}
