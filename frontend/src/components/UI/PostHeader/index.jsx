import React from 'react';
import styled from 'styled-components';

const PostHeaderWrapper = styled.section`
  width: 100%;
  height: 180px;
  display: flex;
  align-self: flex-start;
  position: -webkit-sticky;
  position: sticky;
  top: 0;
  background-color: var(--color-light-0);
  background-color: unset;
  z-index: 99;
  padding-bottom: 16px;

  @media (max-width: 1363px) {
    height: 162px;
  }

  @media (max-width: 1024px) {
    height: 144px;
  }
`;

const PostHeaderConatiner = styled.div`
  width: 100%;
  position: relative;
  display: flex;
`;

const TitleContainer = styled.div`
  position: absolute;
  display: flex;
  flex-direction: column;
  z-index: 1;
`;

const Title = styled.h2`
  position: relative;
  height: 85px;
  padding: 24px 55px;

  @media (max-width: 1363px) {
    height: 76.5px;
  }

  @media (max-width: 1024px) {
    height: 68px;
  }
`;

const LogoImage = styled.img`
  position: absolute;
  width: 170px;
  height: 130px;
  left: 40px;

  @media (max-width: 1363px) {
    width: 153px;
    height: 117px;
  }

  @media (max-width: 1024px) {
    width: 136px;
    height: 108px;
  }
`;

const PageDescription = styled.div`
  font-size: var(--font-size-20);
  line-height: var(--line-height-20);
  font-weight: 500;
  z-index: 99;
  padding-left: 87px;
  padding-top: 25px;

  @media (max-width: 1363px) {
    padding-top: 22.5px;
  }

  @media (max-width: 1024px) {
    padding-top: 22.5px;
    font-size: 18px;
  }
`;

function PostHeader({ title, img, description, children }) {
  return (
    <PostHeaderWrapper>
      <PostHeaderConatiner>
        <TitleContainer>
          <Title>
            <LogoImage src={img} alt={title} />
          </Title>
          <PageDescription>{description}</PageDescription>
        </TitleContainer>
        {children}
      </PostHeaderConatiner>
    </PostHeaderWrapper>
  );
}

export default PostHeader;
