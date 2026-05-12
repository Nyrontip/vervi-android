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

  // ── Categories ──────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO categories (name) VALUES 
    ('Electricidad'), ('Plomería'), ('Carpintería'), ('Pintura'), ('Jardinería'),
    ('Limpieza'), ('Mudanzas'), ('Aire Acondicionado'), ('Electrónica'), ('Construcción')
    ON CONFLICT (name) DO NOTHING
  `);
  console.log('✅ Categories seeded');

  // ── Users ──────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO users (email, password, name, bio, location, rating, "reviewCount", "suggestedPriceCop", "isProvider", "isOnline", "projectCount", "requestCount", "photoUrl") VALUES 
    ('client1@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Carlos López', 'Cliente satisfecho, siempre busco profesionales de confianza', 'Bogotá, Chapinero', 0, 0, NULL, false, true, 0, 3, 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop'),
    ('client2@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Sofia Hernández', 'Cliente frecuente, amo mantener mi hogar en perfecto estado', 'Bogotá, Suba', 0, 0, NULL, false, true, 0, 3, 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop'),
    ('provider1@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Juan Pérez', 'Electricista profesional con 10 años de experiencia. Trabajos garantizados.', 'Bogotá, Usaquén', 4.8, 25, 80000, true, true, 15, 0, 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop'),
    ('provider2@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'María García', 'Plomera certificada especializada en hogares. Respuesta rápida.', 'Bogotá, Suba', 4.5, 18, 60000, true, false, 12, 0, 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop'),
    ('provider3@example.com', '$2b$10$K/B3Ns6Mi9AJy6ydhM9U.uM9V6WdskQWQPV5S4hNMe0.I5IhLIuFq', 'Pedro Gómez', 'Carpintero con experiencia en muebles a medida y restauración.', 'Bogotá, Teusaquillo', 4.9, 30, 100000, true, true, 20, 0, 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop')
    ON CONFLICT (email) DO NOTHING
  `);
  console.log('✅ Users seeded');

  // ── Requests ────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO requests ("clientUserId", "categoryId", status, title, description, location, "budgetCop", "isUrgent", "isActive", "applicationCount", "imageUrl") VALUES 
    (1, 1, 'Pendiente', 'Instalación de 5 interruptores', 'Necesito instalar 5 interruptores modernos en mi apartamento nuevo. Los materiales los tengo, solo necesito la mano de obra calificada.', 'Bogotá, Chapinero', 150000, false, true, 2, 'https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=600&h=400&fit=crop'),
    (1, 2, 'Pendiente', 'Reparación de fuga en cocina', 'Hay una fuga de agua bajo el lavabo de la cocina. Ya intenté apretar las conexiones pero sigue goteando. Urgente.', 'Bogotá, Chapinero', 80000, true, true, 1, 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=600&h=400&fit=crop'),
    (1, 4, 'Borrador', 'Pintura de sala y habitación', 'Quiero pintar la sala y dos habitaciones. Colores neutros. Aprox 60m2 en total.', 'Bogotá, Usaquén', 350000, false, true, 0, 'https://images.unsplash.com/photo-1562259929-b4e1fd3aef09?w=600&h=400&fit=crop'),
    (2, 3, 'Pendiente', 'Construir estantería para sala', 'Necesito una estantería de madera para la sala, medidas 2m x 1m. Estilo moderno con estantes abiertos.', 'Bogotá, Suba', 250000, false, true, 1, 'https://images.unsplash.com/photo-1597006335775-98b7e2bc28f8?w=600&h=400&fit=crop'),
    (2, 5, 'En curso', 'Mantenimiento de jardín', 'Podar árboles, cortar pasto y limpiar el jardín frontal. Herramientas las tengo.', 'Bogotá, Suba', 100000, false, true, 2, 'https://images.unsplash.com/photo-1558618666-fcd25c85f82e?w=600&h=400&fit=crop'),
    (2, 8, 'Pendiente', 'Instalación de aire acondicionado', 'Instalar aire acondicionado split en habitación principal. El equipo ya está comprado.', 'Bogotá, Suba', 400000, true, true, 0, 'https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=600&h=400&fit=crop')
  `);
  console.log('✅ Requests seeded');

  // ── Service Applications ────────────────────────────────────
  await dataSource.query(`
    INSERT INTO service_applications ("requestId", "providerUserId", "presentationMessage", "proposedPriceCop", "immediateAvailability", status) VALUES 
    (1, 3, 'Soy electricista certificado con más de 10 años de experiencia. Puedo hacer el trabajo mañana.', 130000, true, 'PENDING'),
    (1, 4, 'Tengo disponibilidad inmediata y herramientas profesionales para instalar los interruptores.', 145000, true, 'PENDING'),
    (2, 4, 'Especializada en reparaciones de tuberías y fugas. Puedo llegar en 2 horas.', 75000, true, 'ACCEPTED'),
    (4, 5, 'Carpintero con 15 años de experiencia. Puedo hacer el diseño que necesites con maderas de alta calidad.', 230000, false, 'PENDING'),
    (5, 3, 'Servicio de jardinería completo. Incluye disposición de residuos vegetales.', 90000, true, 'PENDING'),
    (5, 5, 'Equipo completo para mantenimiento de jardines grandes. Trabajo garantizado.', 110000, true, 'PENDING')
  `);
  console.log('✅ Applications seeded');

  // ── Services ────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO services ("requestId", "clientUserId", "providerUserId", title, summary, location, "totalPriceCop", status, "scheduledAt") VALUES 
    (2, 1, 4, 'Reparación de fuga en cocina', 'Se reparó la fuga y se cambió válvula de paso completa', 'Bogotá, Chapinero', 75000, 'COMPLETED', '2026-05-01 09:00:00'),
    (5, 2, 3, 'Mantenimiento de jardín completo', 'Se podaron árboles, cortó pasto y se limpió el jardín', 'Bogotá, Suba', 90000, 'IN_PROGRESS', '2026-05-12 08:00:00'),
    (4, 2, 5, 'Diseño y construcción de estantería', 'Estantería a medida 2m x 1m en roble, 5 estantes', 'Bogotá, Suba', 230000, 'SCHEDULED', '2026-05-15 10:00:00')
  `);
  console.log('✅ Services seeded');

  // ── Reviews ─────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO reviews ("serviceId", "reviewerUserId", "reviewedUserId", rating, comment) VALUES 
    (1, 1, 4, 5, 'Excelente trabajo, muy profesional y puntual. Quedé muy satisfecho!'),
    (1, 1, 3, 4, 'Buen trabajo, llegó a tiempo aunque cobra un poco caro.')
  `);
  console.log('✅ Reviews seeded');

  // ── Conversations ───────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO conversations ("participantAUserId", "participantBUserId", "requestId", "lastMessagePreview", "lastMessageAt") VALUES 
    (1, 3, 1, 'Cuándo puedes venir a hacer el presupuesto?', '2026-05-06 11:30:00'),
    (1, 4, 2, 'Perfecto, nos vemos mañana a las 9am.', '2026-05-05 18:00:00'),
    (2, 5, 4, 'El diseño me encanta, comencemos la próxima semana.', '2026-05-08 15:00:00'),
    (2, 3, 5, 'Sí, llegaré a las 8am con todo el equipo.', '2026-05-11 17:30:00')
  `);
  console.log('✅ Conversations seeded');

  // ── Messages ────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt") VALUES 
    (1, 1, 'Hola Juan, tienes disponibilidad para instalarme los interruptores?', true, '2026-05-06 10:00:00'),
    (1, 3, 'Sí claro, a qué hora te viene bien?', true, '2026-05-06 10:15:00'),
    (1, 1, 'Podrías venir mañana a las 10am?', true, '2026-05-06 10:30:00'),
    (1, 3, 'Perfecto, ahí estaré. Te confirmo por aquí.', true, '2026-05-06 10:45:00'),
    (1, 1, 'Cuándo puedes venir a hacer el presupuesto?', false, '2026-05-06 11:30:00'),
    (2, 1, 'Hola María, necesito la reparación urgente. Puedes venir hoy?', true, '2026-05-05 17:00:00'),
    (2, 4, 'Sí, puedo ir ahora mismo. Estaré en 1 hora.', true, '2026-05-05 17:15:00'),
    (2, 1, 'Perfecto, te espero. Gracias!', true, '2026-05-05 17:30:00'),
    (2, 4, 'Perfecto, nos vemos mañana a las 9am.', false, '2026-05-05 18:00:00'),
    (3, 2, 'Hola Pedro, me interesa la estantería que me propusiste', true, '2026-05-07 10:00:00'),
    (3, 5, 'Claro! Te puedo mostrar algunos diseños si quieres.', true, '2026-05-07 11:00:00'),
    (3, 2, 'Sí, envíame fotos de referencia porfa', true, '2026-05-07 11:30:00'),
    (3, 5, 'Te envié algunas opciones. El diseño que propones me gusta, cuándo podrías empezar?', true, '2026-05-07 14:00:00'),
    (3, 2, 'El diseño me encanta, comencemos la próxima semana.', false, '2026-05-08 15:00:00'),
    (4, 2, 'Juan, confirmado para el jardín mañana a las 8am?', true, '2026-05-11 16:00:00'),
    (4, 3, 'Confirmado! Llevaré todo el equipo necesario.', true, '2026-05-11 16:30:00'),
    (4, 2, 'Genial, te espero.', true, '2026-05-11 17:00:00'),
    (4, 3, 'Sí, llegaré a las 8am con todo el equipo.', false, '2026-05-11 17:30:00')
  `);
  console.log('✅ Messages seeded');

  // ── Notifications ───────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO notifications ("userId", title, description, type, "isUnread", "requestId", "applicationId") VALUES 
    (1, 'Nueva postulación', 'Juan Pérez ha postulado a tu solicitud de instalación de interruptores', 'APPLICATION', true, 1, 1),
    (1, 'Nueva postulación', 'María García ha postulado a tu solicitud de instalación de interruptores', 'APPLICATION', true, 1, 2),
    (1, 'Postulación aceptada', 'María García aceptó tu solicitud de reparación de fuga', 'CONFIRMED', false, 2, 3),
    (2, 'Nueva postulación', 'Pedro Gómez ha postulado a tu solicitud de construcción de estantería', 'APPLICATION', true, 4, 4),
    (2, 'Nueva postulación', 'Juan Pérez ha postulado a tu solicitud de mantenimiento de jardín', 'APPLICATION', true, 5, 5),
    (2, 'Nueva postulación', 'Pedro Gómez ha postulado a tu solicitud de mantenimiento de jardín', 'APPLICATION', true, 5, 6),
    (4, 'Servicio confirmado', 'Tu servicio de reparación de fuga ha sido programado. Cliente: Carlos López. Fecha: 1 de mayo.', 'CONFIRMED', false, 2, NULL),
    (3, 'Nuevo mensaje', 'Carlos López te ha enviado un mensaje sobre la instalación de interruptores', 'MESSAGE', true, 1, NULL),
    (3, 'Servicio asignado', 'Has sido asignado al mantenimiento de jardín de Sofia Hernández', 'CONFIRMED', false, 5, NULL),
    (4, 'Recordatorio', 'Recuerda que tienes una reparación programada para mañana a las 9am', 'REMINDER', true, 2, NULL),
    (5, 'Nuevo mensaje', 'Sofia Hernández te ha escrito sobre la estantería', 'MESSAGE', true, 4, NULL),
    (1, 'Pago realizado', 'Tu pago por la reparación de fuga ha sido procesado: $75,000 COP', 'PAYMENT', false, 2, NULL)
  `);
  console.log('✅ Notifications seeded');

  console.log('🎉 Seed completed successfully!');
  await dataSource.destroy();
}

seed().catch((error) => {
  console.error('❌ Seed failed:', error);
  process.exit(1);
});
