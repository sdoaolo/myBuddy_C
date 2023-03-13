import { Outlet } from 'react-router-dom';
import styled from 'styled-components';
import Footer from './Footer';
import Header from './Header';

const Wrapper = styled.div`
  width: 100%;
  height: 100vh;
  background-color: var(--color-dark-0);
  padding: 20px;
`;

const Container = styled.div`
  background-color: var(--color-light-2);
  width: 100%;
  min-height: calc(100vh - 63px);
  margin: 0 auto;
  border-radius: 10px;
`;

const Frame = styled.div`
  width: 100%;
  min-height: calc(100vh - 133px);
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  padding: 30px;
`;

const InnerFrame = styled.div`
  width: 100%;
  min-height: calc(100vh - 193px);
  background-color: var(--color-light-0);
  border: var(--border);
  border-radius: 10px;
`;

function Layout({ displayHeader = false }) {
  return (
    <Wrapper>
      <Container>
        {displayHeader && <Header />}
        <Frame>
          <InnerFrame>
            <Outlet />
          </InnerFrame>
        </Frame>
      </Container>
      <Footer />
    </Wrapper>
  );
}

export default Layout;
