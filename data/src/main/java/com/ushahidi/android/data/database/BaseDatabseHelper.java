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

package com.ushahidi.android.data.database;

import com.ushahidi.android.data.BuildConfig;
import com.ushahidi.android.data.database.converter.PostEntityConverter;
import com.ushahidi.android.data.database.converter.UserEntityFieldConverter;
import com.ushahidi.android.data.entity.DeploymentEntity;
import com.ushahidi.android.data.entity.MediaEntity;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.PostTagEntity;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.UserEntity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.EntityConverterFactory;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public abstract class BaseDatabseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ushahidi.db";

    private static final int DATABASE_VERSION = 1;

    private static final int LAST_DATABASE_NUKE_VERSION = 0;

    private static final Class[] ENTITIES = new Class[]{DeploymentEntity.class, UserEntity.class,
            TagEntity.class, MediaEntity.class, PostTagEntity.class, PostEntity.class};


    static {
        EntityConverterFactory factory = new EntityConverterFactory() {

            @Override
            public <T> EntityConverter<T> create(Cupboard cupboard, Class<T> type) {
                if(type == PostEntity.class) {
                    return (EntityConverter<T>) new PostEntityConverter(cupboard);
                }
                return null;
            }
        };
        CupboardFactory.setCupboard(new CupboardBuilder()
                .registerFieldConverter(UserEntity.Role.class,
                        new UserEntityFieldConverter<>(UserEntity.Role.class))
                .registerEntityConverterFactory(factory).useAnnotations().build());

        // Register our entities
        for (Class<?> clazz : ENTITIES) {
            cupboard().register(clazz);
        }
    }

    private static String TAG = BaseDatabseHelper.class.getSimpleName();

    private boolean mIsClosed;

    public BaseDatabseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
        // This will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < LAST_DATABASE_NUKE_VERSION) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Nuking Database. Old Version: " + oldVersion);
            }
            cupboard().withDatabase(db).dropAllTables();
            onCreate(db);
        } else {
            // This will upgrade tables, adding columns and new tables.
            // Note that existing columns will not be converted
            cupboard().withDatabase(db).upgradeTables();
        }
    }

    /**
     * Close database connection
     */
    @Override
    public synchronized void close() {
        super.close();
        mIsClosed = true;
    }

    public boolean isClosed() {
        return mIsClosed;
    }

}
