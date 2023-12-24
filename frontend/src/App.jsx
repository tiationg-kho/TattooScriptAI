/* eslint-disable react-hooks/exhaustive-deps */
import { useState, useEffect, useContext } from 'react';

import './App.css';
import { fetchLoginCheck } from './apis/fetchLoginCheck';
import { Context } from './components/Context';
import PublicPage from './components/PublicPage';
import ProtectedPage from './components/ProtectedPage';
import { closeSocket } from './apis/socketInput';

function App() {
	const [login, setLogin] = useState(false);
	const { setEmail } = useContext(Context);

	const handleLogin = (token, email) => {
		localStorage.setItem('tsai_token', token);
		setLogin(true);
		setEmail(email);
	};

	const handleLogout = () => {
		closeSocket();
		setLogin(false);
		setEmail('');
		localStorage.removeItem('tsai_token');
	};

	useEffect(() => {
		const token = localStorage.getItem('tsai_token');

		if (token) {
			const loginCheck = async () => {
				try {
					const { data } = await fetchLoginCheck(token);
					if (data.email) {
						handleLogin(token, data.email);
					} else {
						throw new Error();
					}
				} catch (e) {
					handleLogout();
				}
			};
			loginCheck();
		}
	}, []);

	return (
		<>
			{!login && <PublicPage handleLogin={handleLogin} />}
			{login && <ProtectedPage handleLogout={handleLogout} />}
		</>
	);
}

export default App;
