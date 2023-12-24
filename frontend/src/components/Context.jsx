import { createContext } from 'react';

export const Context = createContext({
	email: '',
	setEmail: () => {},
	url: '',
	setUrl: () => {},
});
