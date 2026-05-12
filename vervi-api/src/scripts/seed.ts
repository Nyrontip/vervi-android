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

async function seed() {
  console.log('🔄 Connecting to database...');
  await dataSource.initialize();

  // Categories
  await dataSource.query(`
    INSERT INTO categories (name) VALUES 
    ('Electricidad'), ('Plomería'), ('Carpintería'), ('Pintura'), ('Jardinería'),
    ('Limpieza'), ('Mudanzas'), ('Aire Acondicionado'), ('Electrónica'), ('Construcción')
    ON CONFLICT (name) DO NOTHING
  `);
  console.log('✅ Categories seeded');

  // Users
  await dataSource.query(`
    INSERT INTO users (email, password, name, bio, location, rating, "reviewCount", "suggestedPriceCop", "isProvider", "isOnline", "projectCount", "requestCount") VALUES 
    ('client1@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Carlos López', 'Cliente satisfecho', 'Bogotá, Chapinero', 0, 0, NULL, false, true, 0, 3),
    ('client2@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Sofia Hernández', 'Cliente frecuente', 'Bogotá, Suba', 0, 0, NULL, false, true, 0, 3),
    ('provider1@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Juan Pérez', 'Electricista profesional con 10 años de experiencia', 'Bogotá, Usaquén', 4.8, 25, 80000, true, true, 15, 0),
    ('provider2@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'María García', 'Plomera certificada especializada en hogares', 'Bogotá, Suba', 4.5, 18, 60000, true, false, 12, 0),
    ('provider3@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Pedro Gómez', 'Carpintero con experiencia en muebles a medida', 'Bogotá, Teusaquillo', 4.9, 30, 100000, true, true, 20, 0)
    ON CONFLICT (email) DO NOTHING
  `);
  console.log('✅ Users seeded');

  // Requests
  await dataSource.query(`
    INSERT INTO requests ("clientUserId", "categoryId", status, title, description, location, "budgetCop", "isUrgent", "isActive", "applicationCount") VALUES 
    (1, 1, 'Pendiente', 'Instalación de 5 interruptores', 'Necesito instalar 5 interruptores en mi apartamento nuevo', 'Bogotá, Chapinero', 150000, false, true, 2),
    (1, 2, 'Pendiente', 'Reparación de fuga en cocina', 'Hay una fuga de agua bajo el lavabo de la cocina', 'Bogotá, Chapinero', 80000, true, true, 1),
    (1, 4, 'Borrador', 'Pintura de sala y habitación', 'Quiero pintar la sala y dos habitaciones', 'Bogotá, Usaquén', 350000, false, true, 0),
    (2, 3, 'Pendiente', 'Construir estantería para sala', 'Necesito una estantería de madera para la sala, medidas 2m x 1m', 'Bogotá, Suba', 250000, false, true, 1),
    (2, 5, 'En curso', 'Mantenimiento de jardín', 'Podar árboles y cortar pasto del jardín frontal', 'Bogotá, Suba', 100000, false, true, 2),
    (2, 8, 'Pendiente', 'Instalación de aire acondicionado', 'Instalar aire acondicionado split en habitación principal', 'Bogotá, Suba', 400000, true, true, 0)
  `);
  console.log('✅ Requests seeded');

  // Service Applications
  await dataSource.query(`
    INSERT INTO service_applications ("requestId", "providerUserId", "presentationMessage", "proposedPriceCop", "immediateAvailability", status) VALUES 
    (1, 3, 'Soy electricista certificado con más de 10 años de experiencia. Puedo hacer el trabajo mañana.', 130000, true, 'PENDING'),
    (1, 4, 'Tengo disponibilidad inmediata y herramientas profesionales', 145000, true, 'PENDING'),
    (2, 4, 'Especializada en reparaciones de tuberías y fugas. Puedo llegar en 2 horas.', 75000, true, 'ACCEPTED'),
    (4, 5, 'Carpintero con 15 años de experiencia. Puedo hacer el diseño que necesites.', 230000, false, 'PENDING'),
    (5, 3, 'Servicio de jardinería completo. Incluye disposición de residuos.', 90000, true, 'PENDING'),
    (5, 5, 'Equipo completo para mantenimiento de jardines grandes.', 110000, true, 'PENDING')
  `);
  console.log('✅ Applications seeded');

  // Services
  await dataSource.query(`
    INSERT INTO services ("requestId", "clientUserId", "providerUserId", title, summary, location, "totalPriceCop", status, "scheduledAt") VALUES 
    (2, 1, 4, 'Reparación de fuga en cocina', 'Se reparó la fuga y se cambió válvula de paso', 'Bogotá, Chapinero', 75000, 'COMPLETED', '2026-05-01 09:00:00')
  `);
  console.log('✅ Services seeded');

  // Reviews
  await dataSource.query(`
    INSERT INTO reviews ("serviceId", "reviewerUserId", "reviewedUserId", rating, comment) VALUES 
    (1, 1, 4, 5, 'Excelente trabajo, muy profesional y puntual. Recomendado!'),
    (1, 2, 3, 4, 'Buen trabajo, llegó a tiempo pero cobran un poco caro.')
  `);
  console.log('✅ Reviews seeded');

  // Conversations
  await dataSource.query(`
    INSERT INTO conversations ("participantAUserId", "participantBUserId", "requestId", "lastMessagePreview", "lastMessageAt") VALUES 
    (1, 3, 1, 'Cuándo puedes venir?', '2026-05-06 11:30:00'),
    (1, 4, 2, 'Perfecto, nos vemos mañana a las 9am', '2026-05-05 18:00:00'),
    (2, 5, 4, 'El diseño que propones me gusta, cuándo podrías?', '2026-05-07 10:00:00')
  `);
  console.log('✅ Conversations seeded');

  // Messages
  await dataSource.query(`
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt") VALUES 
    (1, 1, 'Hola, tienes disponibilidad para mañana?', true, '2026-05-06 10:00:00'),
    (1, 3, 'Sí, a qué hora te viene bien?', false, '2026-05-06 10:15:00'),
    (1, 1, 'Cuándo puedes venir?', false, '2026-05-06 11:30:00'),
    (2, 1, 'Puedes llegar a las 9am?', true, '2026-05-05 17:00:00'),
    (2, 4, 'Perfecto, nos vemos mañana', false, '2026-05-05 18:00:00'),
    (3, 2, 'El diseño que propones me gusta, cuándo podrías?', false, '2026-05-07 10:00:00'),
    (3, 5, 'Podría empezar el próximo lunes, te parece?', false, '2026-05-07 14:00:00')
  `);
  console.log('✅ Messages seeded');

  // Notifications
  await dataSource.query(`
    INSERT INTO notifications ("userId", title, description, type, "isUnread", "requestId", "applicationId") VALUES 
    (1, 'Nueva postulación', 'Juan Pérez ha postulado a tu solicitud de instalación de interruptores', 'APPLICATION', true, 1, 1),
    (1, 'Nueva postulación', 'María García ha postulado a tu solicitud de instalación de interruptores', 'APPLICATION', true, 1, 2),
    (1, 'Postulación aceptada', 'María García aceptó tu solicitud de reparación de fuga', 'APPLICATION', true, 2, 3),
    (2, 'Nueva postulación', 'Pedro Gómez ha postulado a tu solicitud de construcción de estantería', 'APPLICATION', true, 4, 4),
    (2, 'Nueva postulación', 'Juan Pérez ha postulado a tu solicitud de mantenimiento de jardín', 'APPLICATION', true, 5, 5),
    (2, 'Nueva postulación', 'María García ha postulado a tu solicitud de mantenimiento de jardín', 'APPLICATION', true, 5, 6),
    (4, 'Servicio confirmado', 'Tu servicio de reparación de fuga ha sido confirmado para mañana a las 9am', 'CONFIRMED', false, 2, NULL),
    (3, 'Nuevo mensaje', 'Tienes un mensaje de Sofia Hernández', 'MESSAGE', true, NULL, NULL)
  `);
  console.log('✅ Notifications seeded');

  console.log('🎉 Seed completed successfully!');
  await dataSource.destroy();
}

seed().catch((error) => {
  console.error('❌ Seed failed:', error);
  process.exit(1);
});
