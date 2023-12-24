# Use an official Node.js runtime as the base image
FROM node:18-slim

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

CMD ["npm", "run", "dev"]
