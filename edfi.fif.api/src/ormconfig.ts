import * as dotenv from 'dotenv';

dotenv.config();

module.exports = {
  type: 'postgres',
  host: process.env.FIF_API_DB_HOST,
  port: parseInt(process.env.FIF_API_DB_PORT, 10),
  username: process.env.FIF_API_DB_USERNAME,
  password: process.env.FIF_API_DB_PASSWORD,
  database: process.env.FIF_API_DB_DATABASE,
  entities: ['./**/*.entity.js'],
  synchronize: true,
};
