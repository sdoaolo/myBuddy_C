package com.mybuddy.member.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.mybuddy.bulletin_post.entity.BulletinPost;
import com.mybuddy.comment.entity.Comment;
import com.mybuddy.follow.entity.Follow;
import com.mybuddy.global.audit.Auditable;
import com.mybuddy.like.entity.Like;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long memberId;

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Setter
    @Column(nullable = false, length = 255)
    private String password;

    @Setter
    @Column(nullable = false, unique = true)
    private String nickname;

    @Setter
    @Column(length = 200)
    private String aboutMe;

    @Setter
    @Column(nullable = false, length = 20)
    private String dogName;

    @Column(nullable = false, length = 200)
    @Enumerated(value = EnumType.STRING)
    private DogGender dogGender;

    @Setter
    @Column(length = 2000)
    private String profileUrl;

    @Setter
    @Column
    private String address;

    @Setter
    @Column
    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus = MemberStatus.ACTIVE; // Excluded from Builder

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BulletinPost> bulletinPosts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

    // 본인이 follower로서 존재하는 리스트
    @OneToMany(mappedBy = "follower")
    private List<Follow> meAsFollowerList = new ArrayList<>();

    // 본인이 followee로서 존재하는 리스트
    @OneToMany(mappedBy = "followee")
    private List<Follow> meAsFolloweeList = new ArrayList<>();

    public void addBulletinPost(BulletinPost bulletinPost) {
        bulletinPosts.add(bulletinPost);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void addFollower(Follow follower) {
        meAsFollowerList.add(follower);
    }

    public void addFollowee(Follow followee) {
        meAsFolloweeList.add(followee);
    }

    @Builder
    public Member(Long memberId, String email, String password, String nickname, String aboutMe,
                  String dogName, DogGender dogGender, String profileUrl, String address) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.dogName = dogName;
        this.dogGender = dogGender;
        this.profileUrl = profileUrl;
        this.address = address;
    }

    public enum DogGender {
        FEMALE("암컷"),
        MALE("수컷");

        @Getter
        private String dogGender;

        DogGender(String dogGender) {
            this.dogGender = dogGender;
        }
    }

    public enum MemberStatus {
        ACTIVE("활성"),
        DORMANT("휴면"),
        DELETED("탈퇴");

        @Getter
        private String memberStatus;

        MemberStatus(String memberStatus) {
            this.memberStatus = memberStatus;
        }
    }
}
