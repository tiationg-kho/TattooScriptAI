/* eslint-disable react/prop-types */
import Login from './Login';

const PublicPage = ({ handleLogin }) => {
	return (
		<>
			<div>Welcome to Tattoo Script AI</div>
			<div>
				<Login handleLogin={handleLogin} />
			</div>
			<div>Please login</div>
		</>
	);
};

export default PublicPage;
