package com.mybuddy.bulletin_post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BulletinPostForMyPageResponseDto {

    private final Long bulletinPostId;

    private final String photoUrl;

    @Builder
    public BulletinPostForMyPageResponseDto(Long bulletinPostId, String photoUrl) {
        this.bulletinPostId = bulletinPostId;
        this.photoUrl = photoUrl;
    }
}
