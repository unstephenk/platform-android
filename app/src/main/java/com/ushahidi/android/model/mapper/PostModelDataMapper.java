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

package com.ushahidi.android.model.mapper;

import com.google.common.base.Preconditions;

import com.ushahidi.android.core.entity.Post;
import com.ushahidi.android.data.entity.mapper.TagEntityMapper;
import com.ushahidi.android.model.PostModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps {@link com.ushahidi.android.core.entity.Post} to {@link com.ushahidi.android.model.PostModel}
 * and vice versa
 *
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class PostModelDataMapper {

    private TagModelDataMapper mTagModelDataMapper;

    public PostModelDataMapper() {
        mTagModelDataMapper = new TagModelDataMapper();
    }

    /**
     * Maps {@link com.ushahidi.android.data.entity.PostEntity} to {@link
     * com.ushahidi.android.core.entity.Post}
     *
     * @param post The {@link com.ushahidi.android.data.entity.PostEntity} to be mapped
     * @return The {@link com.ushahidi.android.core.entity.Post} entity
     */
    public PostModel map(Post post) {
        Preconditions.checkNotNull(post, "Post cannot be null");
        PostModel postModel = new PostModel();
        postModel.setId(post.getId());
        postModel.setStatus(post.getStatus());
        postModel.setTitle(post.getTitle());
        postModel.setCreated(post.getCreated());
        postModel.setUpdated(post.getUpdated());
        postModel.setType(post.getType());
        postModel.setSlug(post.getSlug());
        postModel.setTags(mTagModelDataMapper.map(post.getTags()));
        postModel.setAuthorEmail(post.getAuthorEmail());
        postModel.setAuthorRealname(post.getAuthorRealname());
        postModel.setContent(post.getContent());
        postModel.setStatus(post.getStatus());

        return postModel;
    }

    public Post unmap(PostModel postModel) {
        Preconditions.checkNotNull(postModel, "Post cannot be null");

        Post post = new Post();
        post.setId(postModel.getId());
        post.setStatus(postModel.getStatus());
        post.setTitle(postModel.getTitle());
        post.setCreated(postModel.getCreated());
        post.setUpdated(postModel.getUpdated());
        post.setType(postModel.getType());
        post.setSlug(postModel.getSlug());
        post.setTags(mTagModelDataMapper.unmap(postModel.getTags()));
        post.setAuthorEmail(postModel.getAuthorEmail());
        post.setAuthorRealname(postModel.getAuthorRealname());
        post.setContent(postModel.getContent());
        post.setStatus(postModel.getStatus());

        return post;
    }

    /**
     * Maps a list {@link com.ushahidi.android.data.entity.PostEntity} into a list of {@link
     * com.ushahidi.android.core.entity.Post}.
     *
     * @param postList List to be mapped.
     * @return {@link com.ushahidi.android.model.PostModel}
     */
    public List<PostModel> map(List<Post> postList) {
        List<PostModel> postModelList = new ArrayList<>();
        if (postList != null && !postList.isEmpty()) {
            for (Post post : postList) {
                postModelList.add(map(post));
            }
        }
        return postModelList;
    }
}
