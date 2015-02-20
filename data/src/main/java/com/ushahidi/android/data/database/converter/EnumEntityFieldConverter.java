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

package com.ushahidi.android.data.database.converter;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Locale;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class EnumEntityFieldConverter<E extends Enum> implements FieldConverter<E> {

    private final Class<E> mEnumClass;

    public EnumEntityFieldConverter(Class<E> enumClass) {
        this.mEnumClass = enumClass;
    }

    @Override
    public E fromCursorValue(Cursor cursor, int columnIndex) {
        return (E) Enum.valueOf(mEnumClass, cursor.getString(columnIndex).toUpperCase(
                Locale.getDefault()));
    }

    @Override
    public void toContentValue(E value, String key, ContentValues values) {
        values.put(key, value.toString());
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
