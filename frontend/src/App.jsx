import { useState, useEffect } from 'react';
import './App.css';
import Login from './components/Login';
import { fetchLoginCheck } from './apis/fetchLoginCheck';

function App() {
	const [login, setLogin] = useState(false);
	const [email, setEmail] = useState('');

	useEffect(() => {
		const token = localStorage.getItem('tsai_token');

		if (token) {
			const loginCheck = async () => {
				try {
					const { data } = await fetchLoginCheck(token);
					if (data.email) {
						setLogin(true);
						setEmail(data.email);
					} else {
						throw new Error();
					}
				} catch (e) {
					setLogin(false);
					setEmail('');
					localStorage.removeItem('tsai_token');
				}
			};
			loginCheck();
		}
	}, []);

	const handleLogin = (token, email) => {
		localStorage.setItem('tsai_token', token);
		setLogin(true);
		setEmail(email);
	};

	return (
		<>
			{!login && <Login handleLogin={handleLogin} />}
			{!login && <div>Please login</div>}
			{login && <div>Welcome: {email}</div>}
		</>
	);
}

export default App;
