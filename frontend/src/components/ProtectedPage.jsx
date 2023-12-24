/* eslint-disable react/prop-types */
import { useContext } from 'react';

import { Context } from './Context';
import Input from './Input';
import GeneratedImage from './GeneratedImage';

const ProtectedPage = ({ handleLogout }) => {
	const { email, url } = useContext(Context);

	return (
		<>
			<div>Welcome: {email}</div>
			<Input handleLogout={handleLogout} />
			{url && <GeneratedImage url={url} />}
			<div></div>
			<button onClick={handleLogout}>Logout</button>
		</>
	);
};

export default ProtectedPage;
