import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout/Layout';
import LoginPage from './Pages/LoginPage';
import SignupPage from './Pages/SignupPage';
import ErrorPage from './Pages/ErrorPage';
import HomePage from './Pages/HomePage';
import MyPage from './Pages/MyPage';

function Router() {
  return (
    <Routes>
      <Route element={<Layout displayBgimg />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="*" element={<ErrorPage />} />
      </Route>

      <Route element={<Layout displayHeader />}>
        <Route path="/" element={<HomePage />} />
      </Route>

      <Route element={<Layout displayHeader displayProfile displayFlex />}>
        <Route path="/mypage" element={<MyPage />} />
      </Route>
    </Routes>
  );
}

export default Router;
