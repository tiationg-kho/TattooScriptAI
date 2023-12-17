const backendUrl = import.meta.env.VITE_BACKEND_INPUT_SOCKET_URL;

let socket = null;

export const buildSocket = (message) => {
	socket = new WebSocket(backendUrl);

	socket.onopen = () => {
		console.log('Connected to input_service');
		socket.send(message);
		console.log('send message', message);
	};

	socket.onmessage = (event) => {
		console.log('Message from server:', event.data);
	};
};

export const closeSocket = () => {
	if (socket) {
		socket.close();
		socket = null;
	}
};

export const getBuiltSocket = () => {
	if (socket) {
		return true;
	}
	return false;
};
