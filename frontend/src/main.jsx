/* eslint-disable react-refresh/only-export-components */
/* eslint-disable react/prop-types */
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { useState } from 'react';

import App from './App.jsx';
import './index.css';
import { Context } from './components/Context.jsx';

const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;

const router = createBrowserRouter([
	{
		errorElement: <h1>Error</h1>,
		children: [{ index: true, element: <App /> }],
	},
]);

const ContextProvider = ({ children }) => {
	const [email, setEmail] = useState('');
	const [url, setUrl] = useState('');

	return (
		<Context.Provider value={{ email, setEmail, url, setUrl }}>
			{children}
		</Context.Provider>
	);
};

ReactDOM.createRoot(document.getElementById('root')).render(
	<GoogleOAuthProvider clientId={googleClientId}>
		<ContextProvider>
			<RouterProvider router={router} />
		</ContextProvider>
	</GoogleOAuthProvider>
);
