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

package com.ushahidi.android.data.entity.mapper;

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.data.entity.PostEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class used to map {@link com.ushahidi.android.data.entity.PostEntity} to {@link
 * com.ushahidi.android.core.entity.Post} in core
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostEntityMapper {

    private TagEntityMapper mTagEntityMapper;

    private UserEntityMapper mUserEntityMapper;

    public PostEntityMapper() {
        mTagEntityMapper = new TagEntityMapper();
        mUserEntityMapper = new UserEntityMapper();
    }

    /**
     * Maps {@link com.ushahidi.android.data.entity.PostEntity} to {@link
     * com.ushahidi.android.core.entity.Post}
     *
     * @param postEntity The {@link com.ushahidi.android.data.entity.PostEntity} to be
     *                         mapped
     * @return The {@link com.ushahidi.android.core.entity.Post} entity
     */
    public Post map(PostEntity postEntity) {
        Post post = null;

        if (postEntity != null) {
            post = new Post();
            post.setId(postEntity.getId());
            post.setStatus(postEntity.getStatus());
            post.setTitle(postEntity.getTitle());
            post.setCreated(postEntity.getCreated());
            post.setUpdated(postEntity.getUpdated());
            post.setType(postEntity.getType());
            post.setSlug(postEntity.getSlug());
            post.setTags(mTagEntityMapper.map(postEntity.getTags()));
            post.setAuthorEmail(postEntity.getAuthorEmail());
            post.setAuthorRealname(postEntity.getAuthorRealname());
            post.setContent(postEntity.getContent());
            post.setStatus(postEntity.getStatus());
            post.setUser(mUserEntityMapper.map(postEntity.getUser()));

        }

        return post;
    }

    public PostEntity unmap(Post post) {
        PostEntity postEntity = null;

        if (post != null) {
            postEntity = new PostEntity();
            postEntity.setId(post.getId());
            postEntity.setStatus(post.getStatus());
            postEntity.setTitle(post.getTitle());
            postEntity.setCreated(post.getCreated());
            postEntity.setUpdated(post.getUpdated());
            postEntity.setType(post.getType());
            postEntity.setSlug(post.getSlug());
            postEntity.setTags(mTagEntityMapper.unmap(post.getTags()));
            postEntity.setAuthorEmail(post.getAuthorEmail());
            postEntity.setAuthorRealname(post.getAuthorRealname());
            postEntity.setContent(post.getContent());
            postEntity.setStatus(post.getStatus());
            postEntity.setUser(mUserEntityMapper.unmap(post.getUser()));
        }
        return postEntity;
    }

    /**
     * Maps a list {@link com.ushahidi.android.data.entity.PostEntity} into a list of {@link com.ushahidi.android.core.entity.Post}.
     *
     * @param postEntityList List to be mapped.
     * @return {@link com.ushahidi.android.core.entity.Post}
     */
    public List<Post> map(List<PostEntity> postEntityList) {
        List<Post> postList = new ArrayList<>();
        Post post;
        for (PostEntity postEntity : postEntityList) {
            post = map(postEntity);
            if (post != null) {
                postList.add(post);
            }
        }

        return postList;
    }
}
