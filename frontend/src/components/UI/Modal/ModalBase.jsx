import React from 'react';
import styled, { css } from 'styled-components';
import ModalNonContent from './ModalNonContent';
import { ReactComponent as IconCancleSVG } from '../../../assets/icons/icon-cancle.svg';
import useModal from '../../../hooks/useModal';

const Warapper = styled.section`
  display: flex;
  flex-direction: column;
  background-color: var(--color-light-0);
  border: var(--border);
  border-radius: 5px;
  width: 360px;
`;

const Padding = css`
  padding: 1rem;
`;

const Header = styled.div`
  ${Padding}
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  padding: 1rem;
`;

const CancleButton = styled.button``;

const CancleIcon = styled(IconCancleSVG)`
  color: var(--color-dark-0);
  width: 16px;
  height: 16px;
`;

const Title = styled.h3`
  font-size: 18px;
`;

const Content = styled.p`
  ${Padding}
  min-height: 60px;
`;

const Footer = styled.div`
  ${Padding}
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  gap: 8px;
`;

function ModalBase({ title, content, buttons }) {
  const { closeModal } = useModal();

  return (
    <ModalNonContent>
      <Warapper>
        <Header>
          <Title>{title}</Title>
          <CancleButton onClick={closeModal}>
            <CancleIcon />
          </CancleButton>
        </Header>
        <Content>{content}</Content>
        {buttons && <Footer>{buttons}</Footer>}
      </Warapper>
    </ModalNonContent>
  );
}

export default ModalBase;
