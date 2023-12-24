/* eslint-disable react/prop-types */
import Login from './Login';
import tiger from '../assets/tiger.png';

const PublicPage = ({ handleLogin }) => {
	return (
		<>
			<div>Welcome to Tattoo Script AI</div>
			<div>
				<Login handleLogin={handleLogin} />
			</div>
			<div>Please login</div>
			<div>
				<img
					src={tiger}
					alt='tiger image'
					style={{ width: '200px', height: '200px' }}
				/>
			</div>
		</>
	);
};

export default PublicPage;
