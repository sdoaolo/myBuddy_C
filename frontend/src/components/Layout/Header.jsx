import React, { useCallback } from 'react';
import { Link, NavLink, useLocation } from 'react-router-dom';

import styled, { css } from 'styled-components';
import { useSelector, useDispatch } from 'react-redux';
import Button from '../UI/Button';
import { ReactComponent as MainLogo } from '../../assets/logo/logo.svg';
import PostNewPage from '../../Pages/PostNewPage';
import useModal from '../../hooks/useModal';

import { logout } from '../../redux/actions/auth';
import AuthVerify from '../../common/AuthVerify';

const HeaderWrapper = styled.header`
  width: 100%;
  height: 70px;
  border-bottom: var(--border);
  position: sticky;
  z-index: 10;
`;

const Nav = styled.nav`
  height: 70px;
  border-bottom: var(--border);
  display: flex;
  justify-content: space-between;
  align-items: start;
`;

const Logo = styled(MainLogo)`
  width: 170px;
  height: 58px;
  margin: 11px 0 2px 23px;
  cursor: pointer;
`;

const MenuWrapper = styled.div`
  width: 254px;
  height: 29px;
  margin: 21px 0;
  ${({ loggedIn }) =>
    loggedIn &&
    css`
      margin-left: 8%;
    `}
`;

const MenuUl = styled.ul`
  display: flex;
  align-items: center;
  justify-content: space-between;
  line-height: 29px;
`;

const LinkStyle = styled.li`
  .selected {
    color: var(--color-tertiary);
    font-weight: bold;
  }
`;

const Menu = styled.div`
  font-size: var(--font-size-20);
  font-weight: 500;

  &:hover {
    color: var(--color-tertiary);
  }
`;

const Login = styled(Button)`
  border-left: var(--border);
  border-bottom: var(--border);
  color: var(--color-dark-0);

  &:hover {
    border-left: var(--border);
    border-bottom: var(--border);
  }
`;

const Logout = styled(Button)`
  border-left: var(--border);
  border-bottom: var(--border);
  background: var(--color-light-0);
  color: var(--color-dark-0);

  &:hover {
    background: var(--color-tertiary);
    border-left: var(--border);
    border-bottom: var(--border);
  }
`;

const Mypage = styled(Button)`
  border-left: var(--border);
  border-bottom: var(--border);

  &:hover {
    border-left: var(--border);
    border-bottom: var(--border);
  }
`;

const Write = styled(Button)`
  border-left: var(--border);
  border-bottom: var(--border);
  background: var(--color-light-0);
  color: var(--color-dark-0);

  &:hover {
    background: var(--color-primary);
    border-left: var(--border);
    border-bottom: var(--border);
  }
`;

function Header() {
  const { openModal, closeModal } = useModal();

  const handleWriteButtonClick = () => {
    openModal(<PostNewPage onClose={closeModal} />);
  };

  const { user: currentUser } = useSelector(state => state.auth);

  const dispatch = useDispatch();

  const location = useLocation();

  const handleLogout = useCallback(() => {
    dispatch(logout());
  }, [dispatch]);

  return (
    <HeaderWrapper>
      <Nav>
        {currentUser ? (
          <Link to="/home">
            <h1>
              <Logo />
            </h1>
          </Link>
        ) : (
          <Link to="/">
            <h1>
              <Logo />
            </h1>
          </Link>
        )}

        <MenuWrapper loggedIn={!!currentUser}>
          <MenuUl>
            <LinkStyle>
              <NavLink
                end
                className={({ isActive }) => (isActive ? 'selected' : '')}
                to="/home"
              >
                <Menu>홈</Menu>
              </NavLink>
            </LinkStyle>
            <LinkStyle>
              <NavLink
                className={() =>
                  location.pathname.startsWith('/friend/search') ||
                  location.pathname.startsWith('/user')
                    ? 'selected'
                    : ''
                }
                to="/friend/search"
              >
                <Menu>친구찾기</Menu>
              </NavLink>
            </LinkStyle>
            <LinkStyle>
              <NavLink
                className={() =>
                  location.pathname.startsWith('/place') ||
                  location.pathname.startsWith('/amenity')
                    ? 'selected'
                    : ''
                }
                to="/place"
              >
                <Menu>추천장소</Menu>
              </NavLink>
            </LinkStyle>
          </MenuUl>
        </MenuWrapper>

        <div>
          {!currentUser ? (
            <Link to="/login">
              <Login variant="headersecondary">로그인</Login>
            </Link>
          ) : (
            <div>
              <Link to="/">
                <Write onClick={handleWriteButtonClick} variant="headerprimary">
                  작성
                </Write>
              </Link>
              <Link to={`/user/${currentUser}`}>
                <Mypage variant="headerprimary">마이페이지</Mypage>
              </Link>
              <Link to="/login">
                <Logout variant="headerprimary" onClick={handleLogout}>
                  로그아웃
                </Logout>
              </Link>
            </div>
          )}

          <AuthVerify handleLogout={handleLogout} />
        </div>
      </Nav>
    </HeaderWrapper>
  );
}

export default Header;
