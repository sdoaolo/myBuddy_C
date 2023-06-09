import React from 'react';
import styled from 'styled-components';
import { useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import Modal from '../UI/Modal/Modal';
import Card from '../UI/Card/Card';
import followersLogo from '../../assets/logo/followers_logo.svg';
import { getUserFollower } from '../../api/userApi';
import useModal from '../../hooks/useModal';
import Loading from '../UI/Loading';

const NoFollowers = styled.div`
  width: 100%;
  height: 100%;
  font-size: var(--font-size-20);
  font-weight: 500;
  margin-top: 150px;
  text-align: center;
`;

const FollowerListCard = styled.div`
  width: 371px;
  height: auto;
  max-height: 410px;
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  overflow-y: auto;
  -ms-overflow-style: none;
  scrollbar-width: none;
  ::-webkit-scrollbar {
    display: none;
  }
`;

const FollowerWrapper = styled.div`
  width: 100%;
  height: 100%;
  padding: 0 5px;
  display: flex;
  flex-wrap: wrap;
  cursor: pointer;
`;

const FollowerCard = styled(Card)`
  width: 368px;
  height: 80px;
  box-shadow: 5px 5px var(--color-dark-0);
  border-radius: 5px;
  background-color: var(--color-light-0);
  display: flex;
  justify-content: space-between;
  padding: 10px;
  margin-bottom: 10px;
`;

const Profile = styled.div`
  width: 16%;
  aspect-ratio: 1/1;
  border: var(--border);
  border-radius: 5px;
  flex-grow: 1;
`;

const ProfileImg = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 5px;
  object-fit: cover;
  vertical-align: top;
`;

const ProfileInfo = styled.div`
  flex-grow: 100;
  margin-left: 20px;
  display: flex;
  justify-content: center;
  flex-direction: column;
`;

const DogName = styled.h3`
  font-size: var(--font-size-20);
  font-weight: 500;
  margin-bottom: 5px;
`;

const NickName = styled.p`
  font-size: var(--font-size-13);
  font-weight: 500;
  color: var(--color-primary);
`;

function FollowerModal() {
  const { closeModal } = useModal();
  const navigate = useNavigate();
  const {
    isLoading,
    error,
    data: follower,
  } = useQuery('follower', () =>
    getUserFollower({
      page: 1,
      size: 10,
    }),
  );

  if (isLoading) {
    return <Loading />;
  }

  if (error) {
    return <p>Error: {error.message}</p>;
  }

  const handleCardClick = memberId => {
    navigate(`/user/${memberId}`);
    closeModal();
  };

  return (
    <Modal titleImage={followersLogo}>
      <FollowerListCard>
        {follower.data.length === 0 ? (
          <NoFollowers>아직 팔로워가 없어요!</NoFollowers>
        ) : (
          follower.data.map(({ memberId, nickname, dogName, profileUrl }) => (
            <FollowerWrapper key={memberId}>
              <FollowerCard onClick={() => handleCardClick(memberId)}>
                <Profile>
                  <ProfileImg
                    src={
                      profileUrl ??
                      'https://cdn-icons-png.flaticon.com/512/1130/1130933.png?w=2000&t=st=1680005925~exp=1680006525~hmac=8e8077d62e937c5ca56e24827f856a436440d7fb244eff53af34fffddc88d213'
                    }
                    alt={`${dogName}의 프로필 이미지`}
                  />
                </Profile>
                <ProfileInfo>
                  <DogName>{dogName}</DogName>
                  <NickName>{nickname}</NickName>
                </ProfileInfo>
              </FollowerCard>
            </FollowerWrapper>
          ))
        )}
      </FollowerListCard>
    </Modal>
  );
}

export default FollowerModal;
