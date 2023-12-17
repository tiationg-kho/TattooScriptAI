import axios from 'axios';

const backendUrl = import.meta.env.VITE_BACKEND_LOGIN_URL;

export const fetchCreditDeduct = async (token) => {
	try {
		const response = await axios.patch(
			`${backendUrl}/api/v1/credit/deduct`,
			{},
			{
				headers: {
					Authorization: `Bearer ${token}`,
					'Content-Type': 'application/json',
				},
			}
		);
		return response.data;
	} catch (error) {
		console.error('Error:', error);
	}
};
