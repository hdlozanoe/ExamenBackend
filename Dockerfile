# Use a Node.js base image
FROM node:lts-alpine

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.jso
COPY package*.json ./

# Install dependencies
RUN yarn install --production

# Copy the application code
COPY . .

# Set the command to run the application
CMD ["node", "src/index.js"]