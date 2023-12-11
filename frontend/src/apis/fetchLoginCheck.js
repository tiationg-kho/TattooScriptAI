import axios from 'axios';

const backendUrl = import.meta.env.VITE_BACKEND_URL;

export const fetchLoginCheck = async (token) => {
	try {
		const response = await axios.get(`${backendUrl}/api/v1/login`, {
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
