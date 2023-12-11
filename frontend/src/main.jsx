import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { GoogleOAuthProvider } from '@react-oauth/google';

import App from './App.jsx';
import './index.css';

const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;

const router = createBrowserRouter([
	{
		errorElement: <h1>Error</h1>,
		children: [{ index: true, element: <App /> }],
	},
]);

ReactDOM.createRoot(document.getElementById('root')).render(
	<GoogleOAuthProvider clientId={googleClientId}>
		<RouterProvider router={router} />
	</GoogleOAuthProvider>
);
