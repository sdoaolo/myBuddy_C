import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux'; // redux store 사용 위해 추가
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import store from './redux/store';


// import worker from './mocks/browser';

// if (process.env.NODE_ENV === 'development') {
//   worker.start({
//     onUnhandledRequest: 'bypass',
//   });
// }

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </Provider>
  </React.StrictMode>,
);