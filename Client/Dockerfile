FROM node:latest as builder

RUN mkdir -p /client

WORKDIR /client

COPY . .

RUN npm install
RUN npm run build --prod

CMD ["npm", "start"]
