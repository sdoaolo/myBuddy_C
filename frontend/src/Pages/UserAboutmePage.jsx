import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { useMutation } from 'react-query';
import { useSelector } from 'react-redux';
import Button from '../components/UI/Button';
import {
  deleteUserFollow,
  getUserFollowing,
  postUserFollow,
} from '../api/userApi';
import ModalBase from '../components/UI/Modal/ModalBase';
import useModal from '../hooks/useModal';

const AboutMe = styled.section`
  text-align: center;
  width: 100%;
  aspect-ratio: 2 / 1.25;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-top: 20px;
`;

const AboutMeContent = styled.div`
  width: 100%;
  white-space: pre-line;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  font-weight: 500;
  line-height: 40px;
  margin-bottom: 20px;
  word-wrap: break-word;

  @media screen and (max-height: 750px) {
    margin-bottom: 10px;
    font-size: var(--font-size-16);
  }
`;

const FollowButton = styled(Button)`
  background-color: var(--color-secondary);
  &:hover {
    background-color: var(--color-secondary);
  }
`;

function UserAboutmePage({ userdata, memberId, isMyPage }) {
  let AboutmeButton;
  const userData = userdata;
  const { openModal } = useModal();
  const [isFollowing, setIsFollowing] = useState(false);
  const [isLoadingFollowingStatus, setIsLoadingFollowingStatus] =
    useState(true);
  const { user: currentUser } = useSelector(state => state.auth);

  // Compare following status by currentuser annd memberid
  const storageKey = `following_${currentUser}_${memberId}`;

  // Button change
  const userFollowMutation = useMutation(postUserFollow, {
    onSettled: () => {
      setIsFollowing(true);
      localStorage.setItem(storageKey, true);
    },
  });

  const userFollowDeleteMutation = useMutation(deleteUserFollow, {
    onSettled: () => {
      setIsFollowing(false);
      localStorage.setItem(storageKey, false);
    },
  });

  // Check following status(memberId, currentUser) from local storage
  useEffect(() => {
    async function checkFollowingStatus() {
      try {
        setIsLoadingFollowingStatus(true);
        const storedFollowingStatus = localStorage.getItem(storageKey);
        if (storedFollowingStatus !== null) {
          setIsFollowing(JSON.parse(storedFollowingStatus));
        } else {
          const data = await getUserFollowing({ memberId });
          setIsFollowing(data.isFollowing);
        }
      } catch (error) {
        console.error('Failed to check following status:', error);
      } finally {
        setIsLoadingFollowingStatus(false);
      }
    }

    checkFollowingStatus();
  }, [memberId, storageKey]);

  // Click api call (delete, follow)
  const handleFollowClick = async () => {
    try {
      if (isFollowing) {
        await userFollowDeleteMutation.mutateAsync(
          {
            memberId,
          },
          {
            onSuccess: () => {
              openModal(
                <ModalBase
                  title="UNFOLLOW"
                  content="팔로우를 끊었어요 :)"
                  buttons={
                    <Button onClick={() => window.location.reload()}>
                      확인
                    </Button>
                  }
                />,
              );
            },
            onError: () => {
              openModal(
                <ModalBase
                  title="UNFOLLOW"
                  content="팔로우 끊기에 실패했어요 :/"
                  buttons={
                    <Button onClick={() => window.location.reload()}>
                      확인
                    </Button>
                  }
                />,
              );
            },
          },
        );
      } else {
        await userFollowMutation.mutateAsync(
          {
            memberId,
          },
          {
            onSuccess: () => {
              openModal(
                <ModalBase
                  title="FOLLOW"
                  content="팔로우 했어요! :)"
                  buttons={
                    <Button onClick={() => window.location.reload()}>
                      확인
                    </Button>
                  }
                />,
              );
            },
            onError: () => {
              openModal(
                <ModalBase
                  title="FOLLOW"
                  content="팔로우에 실패했어요 :/"
                  buttons={
                    <Button onClick={() => window.location.reload()}>
                      확인
                    </Button>
                  }
                />,
              );
            },
          },
        );
      }
    } catch (error) {
      console.error('Failed to update following status:', error);
    }
  };

  if (!userData) {
    return null;
  }

  const getFollowButtonText = () => {
    if (isLoadingFollowingStatus) {
      return '로딩중...';
    }
    if (isFollowing) {
      return '팔로우끊기';
    }
    return '팔로우';
  };

  // Button change by location
  if (isMyPage) {
    AboutmeButton = (
      <Link to={`/user/${memberId}/edit`}>
        <Button variant="medium">수정</Button>
      </Link>
    );
  } else {
    AboutmeButton = currentUser ? (
      <FollowButton
        variant="medium"
        onClick={handleFollowClick}
        disabled={isLoadingFollowingStatus}
      >
        {getFollowButtonText()}
      </FollowButton>
    ) : null;
  }
  return (
    <div>
      <AboutMe>
        <AboutMeContent>
          {!userData.aboutMe ? (
            <p>아직 소개글이 없네요!</p>
          ) : (
            <p>{userData.aboutMe}</p>
          )}
        </AboutMeContent>
        {AboutmeButton}
      </AboutMe>
    </div>
  );
}

export default UserAboutmePage;
