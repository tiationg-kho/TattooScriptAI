/* eslint-disable react/prop-types */
import { GoogleLogin } from '@react-oauth/google';

import { fetchLogin } from '../apis/fetchLogin';

const Login = ({ handleLogin }) => {
	return (
		<GoogleLogin
			onSuccess={async (res) => {
				const credential = res.credential;
				const { token, email } = await fetchLogin(credential);
				if (token && email) {
					handleLogin(token, email);
				}
			}}
			onError={() => {
				console.log('Login Failed');
			}}
		/>
	);
};

export default Login;
