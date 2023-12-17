import axios from 'axios';

const backendUrl = import.meta.env.VITE_BACKEND_LOGIN_URL;

export const fetchCreditCheck = async (token) => {
	try {
		const response = await axios.get(`${backendUrl}/api/v1/credit`, {
			headers: {
				Authorization: `Bearer ${token}`,
				'Content-Type': 'application/json',
			},
		});
		return response.data;
	} catch (error) {
		console.error('Error:', error);
	}
};
