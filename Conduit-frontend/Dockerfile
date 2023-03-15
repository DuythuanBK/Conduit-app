# Fetching the latest node image on alpine linux
FROM node:18-alpine AS development

# Declaring env
ENV NODE_ENV development

# Setting up the work directory
WORKDIR /conduit-app

# Installing dependencies
COPY ./package.json /conduit-app
COPY ./package-lock.json /conduit-app
RUN npm install

# Copying all the files in our project
COPY src /conduit-app/src
COPY public /conduit-app/public

# Starting our application
CMD npm start
EXPOSE 3000