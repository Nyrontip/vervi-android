import 'reflect-metadata';
import { DataSource } from 'typeorm';
import { config } from 'dotenv';

config();

const dataSource = new DataSource({
  type: 'postgres',
  host: process.env.DB_HOST || 'localhost',
  port: parseInt(process.env.DB_PORT || '5432'),
  username: process.env.DB_USERNAME,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE || 'vervi_db',
  ssl:
    process.env.NODE_ENV === 'production'
      ? { rejectUnauthorized: false }
      : false,
  synchronize: true,
  logging: false,
});

const TABLES = [
  'notifications',
  'messages',
  'conversations',
  'reviews',
  'service_evidence',
  'services',
  'service_applications',
  'request_attachments',
  'requests',
  'user_category_cross_ref',
  'users',
  'categories',
];

async function truncate() {
  console.log('🔄 Connecting to database...');
  await dataSource.initialize();

  console.log('🧹 Truncating all tables...');
  for (const table of TABLES) {
    try {
      await dataSource.query(`TRUNCATE TABLE "${table}" RESTART IDENTITY CASCADE`);
      console.log(`  ✓ ${table}`);
    } catch (err: any) {
      if (err.message?.includes('does not exist')) {
        console.log(`  - ${table} (not found, skipping)`);
      } else {
        console.error(`  ✗ ${table}: ${err.message}`);
      }
    }
  }

  console.log('✅ All tables truncated. Run npm run seed to repopulate.');
  await dataSource.destroy();
}

truncate().catch((error) => {
  console.error('❌ Truncate failed:', error);
  process.exit(1);
});
