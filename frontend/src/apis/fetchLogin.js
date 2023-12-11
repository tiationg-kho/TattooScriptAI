import axios from 'axios';

const backendUrl = import.meta.env.VITE_BACKEND_URL;

export const fetchLogin = async (credential) => {
	try {
		const response = await axios.post(
			`${backendUrl}/api/v1/login`,
			{ credential },
			{
				headers: {
					'Content-Type': 'application/json',
				},
			}
		);
		return response.data;
	} catch (error) {
		console.error('Error:', error);
	}
};
