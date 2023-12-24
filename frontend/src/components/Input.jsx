/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react/prop-types */
import { useState, useEffect, useContext } from 'react';

import { fetchCreditCheck } from '../apis/fetchCreditCheck';
import { fetchCreditDeduct } from '../apis/fetchCreditDeduct';
import { buildSocket, closeSocket, getBuiltSocket } from '../apis/socketInput';
import { Context } from './Context';

const Input = ({ handleLogout }) => {
	const [text, setText] = useState('');
	const [credit, setCredit] = useState(0);
	const [error, setError] = useState('');
	const [isAllowed, setIsAllowed] = useState(true);
	const { setUrl } = useContext(Context);

	useEffect(() => {
		const token = localStorage.getItem('tsai_token');
		const urlCheck = () => {
			const url = localStorage.getItem('url');
			if (url) {
				setUrl(url);
				setError('');
			} else {
				setUrl('');
			}
		};
		const intervalId = setInterval(urlCheck, 1000);
		if (token) {
			const creditCheck = async () => {
				const { data } = await fetchCreditCheck(token);
				if (data !== undefined && data !== null) setCredit(data);
				else {
					handleLogout();
				}
			};
			creditCheck();
		}
		return () => {
			localStorage.removeItem('url');
			setUrl('');
			clearInterval(intervalId);
		};
	}, []);

	const handleChange = (e) => {
		setText(e.target.value);
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		if (!isAllowed) {
			return;
		}
		if (getBuiltSocket()) {
			return setError('Your tattoo is still processing..');
		}
		if (!text || text.trim().length === 0) {
			return setError('Input is required.');
		}
		if (/[^a-zA-Z0-9 .,!?]/.test(text)) {
			return setError('Cannot contain special characters.');
		}
		if (credit <= 0) {
			return setError('Credit is not enough.');
		}
		setIsAllowed(false);
		setTimeout(() => setIsAllowed(true), 1000);

		try {
			const token = localStorage.getItem('tsai_token');
			setError('');
			const { data } = await fetchCreditDeduct(token);
			if (data !== undefined && data !== null) {
				setCredit(data);
			}

			buildSocket(text.trim());
			setText('');
			setError('Your tattoo is still processing..');
		} catch (error) {
			setError('Something went wrong.');
			handleLogout();
		}
	};

	return (
		<>
			<form onSubmit={(e) => handleSubmit(e)}>
				<div>
					<label>Current credit: {credit}</label>
				</div>
				<div>
					<label>Describe your tattoo design requirements:</label>
				</div>
				<div>
					<input value={text} onChange={(e) => handleChange(e)}></input>
				</div>
				<div>
					<button>Submit</button>
				</div>
				<div>
					<span>{error}</span>
				</div>
			</form>
			<button onClick={closeSocket}>Cancel</button>
		</>
	);
};

export default Input;
