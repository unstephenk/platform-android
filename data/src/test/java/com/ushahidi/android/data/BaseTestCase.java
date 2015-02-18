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

package com.ushahidi.android.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ushahidi.android.data.api.Date;
import com.ushahidi.android.data.api.DateDeserializer;
import com.ushahidi.android.data.api.ValueDeserializer;
import com.ushahidi.android.data.entity.DeploymentEntity;
import com.ushahidi.android.data.entity.MediaEntity;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.PostTagEntity;
import com.ushahidi.android.data.entity.PostValueEntity;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.UserEntity;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;

import nl.qbusict.cupboard.Cupboard;

/**
 * Base class for Robolectric tests. Inherit from this class to create a test.
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */

public class BaseTestCase {

    protected static final Class[] ENTITIES = new Class[]{DeploymentEntity.class, UserEntity.class,
            TagEntity.class, MediaEntity.class, PostTagEntity.class, PostEntity.class};

    private static final String TEST_DB = "ushahidi_test.db";

    public Gson mGson;

    protected SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        builder.registerTypeAdapter(PostValueEntity.class, new ValueDeserializer());
        mGson = builder.create();

    }

    protected void setupTestDb(final Cupboard cupboard) {

        SQLiteOpenHelper helper = new SQLiteOpenHelper(RuntimeEnvironment.application, TEST_DB,
                null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                cupboard.withDatabase(db).createTables();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        db = helper.getWritableDatabase();
    }

    protected void destroyTestDb() {
        RuntimeEnvironment.application.deleteDatabase(TEST_DB);
    }

    /**
     * Resets a Singleton class. Uses Reflection to find a private field called sInstance then
     * nullifies the field.
     *
     * @param clazz The class to reset.
     */
    protected void clearSingleton(Class clazz) {
        Field instance;
        try {
            instance = clazz.getDeclaredField("sInstance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
