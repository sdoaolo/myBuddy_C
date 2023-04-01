import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

import { ReactComponent as Commentlogo } from '../../../assets/icons/icon-comment.svg';
import { elapsedTime } from '../../../utils/time';

import useObserverFetch from '../../../hooks/useObserverFetch';

const ItemBox = styled.li`
  border: var(--border);
  border-radius: 5px;
  display: flex;
  flex-direction: column;
  width: 300px;
  background-color: var(--color-light-0);
  z-index: 10;

  &:hover {
    box-shadow: 10px 10px 0 0 var(--color-dark-0);
  }

  @media (max-width: 1363px) {
    width: 85%;
    height: 88%;
  }
`;

const PostBody = styled.section`
  height: 100%;
  cursor: pointer;
`;

const PostImage = styled.img`
  width: 100%;
  height: 299px;
  min-height: 299px;
  aspect-ratio: 1/1;
  object-fit: cover;
`;

const PostBox = styled.div`
  padding: 10px 10px 13px 10px;
  color: var(--color-dark-0);
  border-top: var(--border);
  border-bottom: var(--border);
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;

  @media (max-width: 1363px) {
    height: 35%;
    font-size: 14px;
  }
`;

const ContentBox = styled.p`
  line-height: 1.4rem;
  padding-bottom: 12px;
  height: 72px;

  width: 100%;
  display: inline-block;

  overflow: hidden;
  text-overflow: ellipsis;

  white-space: normal;
  text-align: left;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
`;

const CommentBox = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  padding-left: 3px;

  @media (max-width: 1363px) {
    margin-top: -10px;
  }
`;

const Comment = styled.span`
  margin-left: 4px;
  font-size: var(--font-size-13);
  line-height: 19px;
`;

const PostFooter = styled.section`
  display: flex;
  justify-content: space-between;
  padding: 11px 10px 10px 12px;
`;

const PostUsername = styled.div`
  font-weight: 500;
`;

const PostCreatedAt = styled.span`
  font-size: 13px;
  line-height: 22px;
  opacity: 0.5;
`;

function PostItem({
  bulletinPostId,
  photoUrl,
  postContent,
  commentCount,
  nickname,
  dogName,
  createdAt,
  memberId,
  isLastItem,
  onFetch,
  onClick,
}) {
  const displayTimeText = elapsedTime(createdAt);
  const { ref } = useObserverFetch({ isLastItem, onFetch });

  const handleBodyClick = () => {
    onClick(bulletinPostId);
  };

  return (
    <ItemBox ref={ref} data-post-id={bulletinPostId}>
      <PostBody onClick={handleBodyClick}>
        <PostImage src={photoUrl} alt={`${nickname} 게시글 사진`} />
        <PostBox>
          <ContentBox>{postContent}</ContentBox>
          <CommentBox>
            <Commentlogo />
            <Comment>{commentCount}개</Comment>
          </CommentBox>
        </PostBox>
      </PostBody>
      <PostFooter>
        <Link to={`/user/${memberId}`}>
          <PostUsername>
            {nickname}🏠{dogName}
          </PostUsername>
        </Link>
        <PostCreatedAt>{displayTimeText}</PostCreatedAt>
      </PostFooter>
    </ItemBox>
  );
}

export default PostItem;
