import React from 'react';
import styled from 'styled-components';

const UserHeaderWrapper = styled.section`
  margin-bottom: 10px;
  display: flex;
  justify-content: center;
  font-weight: 500;
  z-index: 10;
  width: 95%;
  padding-right: 10px;

  @media screen and (max-height: 800px) {
    margin-bottom: 5px;
  }
`;

const UserHeaderContent = styled.div`
  width: 100%;
`;

const UserDataWrapper = styled.div`
  width: 100%;
  display: flex;
`;

const Name = styled.div`
  font-size: 40px;
  margin-top: 12px;
  margin-right: 60px;

  @media screen and (max-height: 800px) {
    margin-top: 23px;
    font-size: 30px;
  }
`;

function UserEditHeader({ userdata }) {
  return (
    <UserHeaderWrapper UserHeaderWrapper>
      <UserHeaderContent>
        <UserDataWrapper>
          <Name>{userdata.dogName}</Name>
        </UserDataWrapper>
      </UserHeaderContent>
    </UserHeaderWrapper>
  );
}

export default UserEditHeader;
