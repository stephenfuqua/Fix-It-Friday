import * as dotenv from 'dotenv';

dotenv.config();

module.exports = {
  type: 'postgres',
  host: process.env.FIF_API_DB_HOST || 'localhost',
  port: parseInt(process.env.FIF_API_DB_PORT, 10) || 5432,
  username: process.env.FIF_API_DB_USERNAME || 'postgres',
  password: process.env.FIF_API_DB_PASSWORD,
  database: process.env.FIF_API_DB_DATABASE || 'fixitfriday',
  entities: ['./**/*.entity.js'],
  synchronize: true,
};
