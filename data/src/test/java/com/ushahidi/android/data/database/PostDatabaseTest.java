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

package com.ushahidi.android.data.database;

import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.api.model.Posts;
import com.ushahidi.android.data.api.model.Tags;
import com.ushahidi.android.data.database.converter.PostEntityConverter;
import com.ushahidi.android.data.database.converter.EnumEntityFieldConverter;
import com.ushahidi.android.data.entity.PostEntity;
import com.ushahidi.android.data.entity.PostTagEntity;
import com.ushahidi.android.data.entity.TagEntity;
import com.ushahidi.android.data.entity.UserEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.EntityConverterFactory;

import static com.ushahidi.android.data.TestHelper.getResource;
import static junit.framework.TestCase.assertNotNull;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Test database implementation without mocks
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Config(manifest=Config.NONE)
//@RunWith(RobolectricTestRunner.class)
public class PostDatabaseTest extends BaseTestCase {

    static {
        EntityConverterFactory factory = new EntityConverterFactory() {

            @Override
            public <T> EntityConverter<T> create(Cupboard cupboard, Class<T> type) {
                if (type == PostEntity.class) {
                    return (EntityConverter<T>) new PostEntityConverter(cupboard);
                }
                return null;
            }
        };
        CupboardFactory.setCupboard(new CupboardBuilder()
                .registerFieldConverter(UserEntity.Role.class,
                        new EnumEntityFieldConverter<>(UserEntity.Role.class))
                .registerEntityConverterFactory(factory).useAnnotations().build());

        // Register our entities
        for (Class<?> clazz : ENTITIES) {
            cupboard().register(clazz);
        }

    }

    //@Test
    public void seedPostEntityTable() throws IOException {
        final String postsJson = getResource("posts.json");
        final String tagsJson = getResource("tags.json");
        assertNotNull(mGson);
        Posts posts = mGson.fromJson(postsJson, Posts.class);
        assertNotNull(posts);
        assertNotNull(posts.getPosts());

        Tags tags = mGson.fromJson(tagsJson, Tags.class);
        assertNotNull(tags);
        assertNotNull(tags.getTags());

        // Seed some tags
        try {
            db.beginTransaction();
            for (TagEntity tagEntity : tags.getTags()) {
                Long row = cupboard().withDatabase(db).put(tagEntity);

                assertNotNull(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }


        try {
            db.beginTransaction();
            for (PostEntity postEntity : posts.getPosts()) {
                Long rows = cupboard().withDatabase(db).put(postEntity);
                if ((rows > 0) && (postEntity.getPostTagEntityList() != null) && (
                        postEntity.getPostTagEntityList().size() > 0)) {

                    for (PostTagEntity postTagEntity : postEntity.getPostTagEntityList()) {
                        postTagEntity.setPostId(postEntity.getId());
                        cupboard().withDatabase(db).put(postTagEntity);
                    }
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }

        final List<PostEntity> postEntities = cupboard().withDatabase(db).query(PostEntity.class)
                .list();

        final List<PostEntity> postEntityList = new ArrayList<>();

        if (postEntities != null) {
            for (PostEntity postEntity : postEntities) {
                List<TagEntity> t = getTagEntity(postEntity);
                postEntity.setTags(t);
                postEntityList.add(postEntity);
            }

        }

        assertNotNull(postEntities);

    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        setupTestDb(cupboard());
    }

    private List<TagEntity> getTagEntity(PostEntity postEntity) {
        List<TagEntity> tagEntityList = new ArrayList<>();

        // fetch post tag entity
        List<PostTagEntity> postTagEntityList = cupboard().withDatabase(db)
                .query(PostTagEntity.class)
                .withSelection("mPostId = ?", String.valueOf(postEntity.getId())).list();

        for (PostTagEntity postTagEntity : postTagEntityList) {
            TagEntity tagEntity = cupboard().withDatabase(db)
                    .get(TagEntity.class, postTagEntity.getTagId());
            tagEntityList.add(tagEntity);
        }

        return tagEntityList;
    }

    @After
    public void onDestroy() throws Exception {
        destroyTestDb();
    }
}
