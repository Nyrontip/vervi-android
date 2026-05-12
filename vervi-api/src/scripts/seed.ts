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
    (1, 10, 'Pendiente', 'Construcción de deck en patio trasero', 'Quiero construir un deck de madera de 4m x 3m en el patio trasero. Con barandal.', 'Bogotá, Chapinero', 800000, false, true, 0, 'https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=600&h=400&fit=crop'),
    (2, 3, 'Pendiente', 'Construir estantería para sala', 'Necesito una estantería de madera para la sala, medidas 2m x 1m. Estilo moderno con estantes abiertos.', 'Bogotá, Suba', 250000, false, true, 1, 'https://images.unsplash.com/photo-1597006335775-98b7e2bc28f8?w=600&h=400&fit=crop'),
    (2, 5, 'En curso', 'Mantenimiento de jardín', 'Podar árboles, cortar pasto y limpiar el jardín frontal. Herramientas las tengo.', 'Bogotá, Suba', 100000, false, true, 2, 'https://images.unsplash.com/photo-1558618666-fcd25c85f82e?w=600&h=400&fit=crop'),
    (2, 8, 'Pendiente', 'Instalación de aire acondicionado', 'Instalar aire acondicionado split en habitación principal. El equipo ya está comprado.', 'Bogotá, Suba', 400000, true, true, 0, 'https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=600&h=400&fit=crop'),
    (1, 6, 'Pendiente', 'Limpieza profunda de apartamento', 'Necesito limpieza general de apartamento de 70m2. Incluye baños, cocina y vidrios.', 'Bogotá, Chapinero', 120000, false, true, 0, 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=600&h=400&fit=crop'),
    (2, 9, 'Borrador', 'Reparación de TV LED', 'Mi televisor LED de 55" no enciende. Hace un clic y se apaga.', 'Bogotá, Suba', 150000, false, true, 0, 'https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=600&h=400&fit=crop'),
    (1, 7, 'Pendiente', 'Mudanza de apartamento', 'Mudanza de apartamento pequeño (1 persona) de Chapinero a Usaquén. Incluye muebles básicos.', 'Bogotá, Chapinero a Usaquén', 200000, false, true, 0, 'https://images.unsplash.com/photo-1600585152220-90363fe7e115?w=600&h=400&fit=crop')
  `);
  console.log('✅ Requests seeded');

  // ── Service Applications ────────────────────────────────────
  await dataSource.query(`
    INSERT INTO service_applications ("requestId", "providerUserId", "presentationMessage", "proposedPriceCop", "immediateAvailability", status) VALUES 
    (1, 3, 'Soy electricista certificado con más de 10 años de experiencia. Puedo hacer el trabajo mañana.', 130000, true, 'ACCEPTED'),
    (1, 4, 'Tengo disponibilidad inmediata y herramientas profesionales para instalar los interruptores.', 145000, true, 'REJECTED'),
    (2, 4, 'Especializada en reparaciones de tuberías y fugas. Puedo llegar en 2 horas.', 75000, true, 'ACCEPTED'),
    (4, 5, 'Carpintero con 15 años de experiencia. Puedo hacer el diseño que necesites con maderas de alta calidad.', 230000, false, 'ACCEPTED'),
    (5, 3, 'Servicio de jardinería completo. Incluye disposición de residuos vegetales.', 90000, true, 'ACCEPTED'),
    (5, 5, 'Equipo completo para mantenimiento de jardines grandes. Trabajo garantizado.', 110000, true, 'REJECTED'),
    (6, 4, 'Instalación profesional de aire acondicionado. Certificada.', 180000, true, 'ACCEPTED'),
    (6, 3, 'Técnico en refrigeración con 8 años de experiencia.', 170000, true, 'REJECTED')
  `);
  console.log('✅ Applications seeded');

  // ── Services ────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO services ("requestId", "clientUserId", "providerUserId", title, summary, location, "totalPriceCop", status, "scheduledAt") VALUES 
    (2, 1, 4, 'Reparación de fuga en cocina', 'Se reparó la fuga y se cambió válvula de paso completa', 'Bogotá, Chapinero', 75000, 'COMPLETED', '2026-05-01 09:00:00'),
    (5, 2, 3, 'Mantenimiento de jardín completo', 'Se podaron árboles, cortó pasto y se limpió el jardín frontal', 'Bogotá, Suba', 90000, 'IN_PROGRESS', '2026-05-12 08:00:00'),
    (4, 2, 5, 'Diseño y construcción de estantería', 'Estantería a medida 2m x 1m en roble macizo con 5 estantes ajustables', 'Bogotá, Suba', 230000, 'SCHEDULED', '2026-05-15 10:00:00'),
    (1, 1, 3, 'Instalación de interruptores eléctricos', 'Instalación completa de 5 interruptores con materiales incluidos', 'Bogotá, Chapinero', 130000, 'SCHEDULED', '2026-05-14 14:00:00'),
    (3, 1, 5, 'Pintura de sala y dos habitaciones', 'Pintura completa de sala + 2 habitaciones con pintura vinilo tipo 1', 'Bogotá, Usaquén', 350000, 'COMPLETED', '2026-04-20 08:00:00'),
    (6, 2, 4, 'Instalación de aire acondicionado', 'Instalación de split 12000 BTU en habitación principal', 'Bogotá, Suba', 180000, 'CANCELLED', '2026-05-10 09:00:00')
  `);
  console.log('✅ Services seeded');

  // ── Service Evidence (for completed services) ──────────────
  await dataSource.query(`
    INSERT INTO service_evidence ("serviceId", "imageUrl", caption, "sortOrder") VALUES 
    (1, 'https://images.unsplash.com/photo-1585704032915-c3400ca199e7?w=600&h=800&fit=crop', 'Fuga detectada bajo el lavabo', 1),
    (1, 'https://images.unsplash.com/photo-1607472586893-edb57bdc0e39?w=600&h=800&fit=crop', 'Válvula de paso reemplazada', 2),
    (1, 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=600&h=800&fit=crop', 'Prueba de presión exitosa', 3),
    (5, 'https://images.unsplash.com/photo-1562259929-b4e1fd3aef09?w=600&h=800&fit=crop', 'Sala antes de pintar', 1),
    (5, 'https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=600&h=800&fit=crop', 'Aplicación de primera mano', 2),
    (5, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=600&h=800&fit=crop', 'Resultado final sala', 3),
    (5, 'https://images.unsplash.com/photo-1560185009-5e8e9f1a44f5?w=600&h=800&fit=crop', 'Habitación principal terminada', 4)
  `);
  console.log('✅ Service evidence seeded');

  // ── Provider-as-Client Requests ────────────────────────────
  await dataSource.query(`
    INSERT INTO requests ("clientUserId", "categoryId", status, title, description, location, "budgetCop", "isUrgent", "isActive", "applicationCount", "imageUrl") VALUES 
    (3, 2, 'Pendiente', 'Reparación de tubería en baño', 'Se tapó la tubería del lavamanos y el agua no drena. Necesito ayuda urgente antes de que empeore.', 'Bogotá, Usaquén', 60000, true, true, 0, 'https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=600&h=400&fit=crop'),
    (4, 1, 'Pendiente', 'Instalación de ventilador de techo', 'Quiero instalar un ventilador de techo en la sala. Ya tengo el ventilador, solo necesito instalación eléctrica.', 'Bogotá, Suba', 90000, false, true, 0, 'https://images.unsplash.com/photo-1595078475328-1ab05d0a6a0e?w=600&h=400&fit=crop'),
    (5, 6, 'Borrador', 'Limpieza de taller de carpintería', 'Necesito una limpieza profunda de mi taller de carpintería después de un proyecto grande. Aprox 40m2.', 'Bogotá, Teusaquillo', 80000, false, true, 0, 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=600&h=400&fit=crop')
  `);
  console.log('✅ Provider requests seeded');

  // ── Provider-to-Provider Applications ───────────────────────
  await dataSource.query(`
    INSERT INTO service_applications ("requestId", "providerUserId", "presentationMessage", "proposedPriceCop", "immediateAvailability", status) VALUES 
    (11, 4, 'Plomera certificada, puedo ir hoy mismo a revisar la tubería. Tengo equipo especializado.', 55000, true, 'PENDING'),
    (11, 5, 'Tengo experiencia en reparaciones de baños. Puedo hacer el trabajo mañana.', 50000, true, 'PENDING'),
    (12, 3, 'Electricista profesional, instalación de ventiladores es mi especialidad.', 80000, true, 'PENDING'),
    (12, 5, 'Puedo hacer la instalación, también ofrezco servicio de mantenimiento.', 85000, false, 'PENDING')
  `);
  console.log('✅ Provider-to-provider applications seeded');

  // ── Additional conversations (provider-to-provider) ────────
  await dataSource.query(`
    INSERT INTO conversations ("participantAUserId", "participantBUserId", "requestId", "lastMessagePreview", "lastMessageAt") VALUES 
    (3, 4, 11, 'Gracias por venir tan rápido, el baño quedó impecable.', '2026-05-12 15:00:00'),
    (4, 3, 12, 'El ventilador quedó perfecto, muchas gracias!', '2026-05-12 16:30:00')
  `);
  console.log('✅ Additional conversations seeded');

  // ── Additional messages (using subqueries for conversation IDs) ─
  await dataSource.query(`
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 3, 'Hola María, tienes disponibilidad para revisar mi baño?', true, '2026-05-12 13:00:00'
    FROM conversations c WHERE c."participantAUserId" = 3 AND c."participantBUserId" = 4 AND c."requestId" = 11;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 4, 'Sí claro, puedo ir hoy a las 4pm. Te parece?', true, '2026-05-12 13:15:00'
    FROM conversations c WHERE c."participantAUserId" = 3 AND c."participantBUserId" = 4 AND c."requestId" = 11;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 3, 'Perfecto, te espero. Gracias!', true, '2026-05-12 13:30:00'
    FROM conversations c WHERE c."participantAUserId" = 3 AND c."participantBUserId" = 4 AND c."requestId" = 11;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 4, 'Ya estoy aquí. Voy a revisar la tubería.', true, '2026-05-12 16:00:00'
    FROM conversations c WHERE c."participantAUserId" = 3 AND c."participantBUserId" = 4 AND c."requestId" = 11;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 3, 'Gracias por venir tan rápido, el baño quedó impecable.', false, '2026-05-12 15:00:00'
    FROM conversations c WHERE c."participantAUserId" = 3 AND c."participantBUserId" = 4 AND c."requestId" = 11;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 4, 'Buenas Juan, puedes instalarme un ventilador de techo?', true, '2026-05-12 15:00:00'
    FROM conversations c WHERE c."participantAUserId" = 4 AND c."participantBUserId" = 3 AND c."requestId" = 12;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 3, 'Claro! Cuándo te viene bien?', true, '2026-05-12 15:15:00'
    FROM conversations c WHERE c."participantAUserId" = 4 AND c."participantBUserId" = 3 AND c."requestId" = 12;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 4, 'Mañana a las 10am?', true, '2026-05-12 15:30:00'
    FROM conversations c WHERE c."participantAUserId" = 4 AND c."participantBUserId" = 3 AND c."requestId" = 12;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 3, 'Perfecto, allá estaré.', true, '2026-05-12 15:45:00'
    FROM conversations c WHERE c."participantAUserId" = 4 AND c."participantBUserId" = 3 AND c."requestId" = 12;
    INSERT INTO messages ("conversationId", "senderUserId", body, "isRead", "sentAt")
    SELECT c.id, 4, 'El ventilador quedó perfecto, muchas gracias!', false, '2026-05-12 16:30:00'
    FROM conversations c WHERE c."participantAUserId" = 4 AND c."participantBUserId" = 3 AND c."requestId" = 12;
  `);
  console.log('✅ Additional messages seeded');

  // ── Additional notifications ───────────────────────────────
  await dataSource.query(`
    INSERT INTO notifications ("userId", title, description, type, "isUnread", "requestId", "applicationId") VALUES 
    (3, 'Nueva postulación', 'María García ha postulado a tu solicitud de reparación de tubería', 'APPLICATION', true, 11, 9),
    (3, 'Nueva postulación', 'Pedro Gómez ha postulado a tu solicitud de reparación de tubería', 'APPLICATION', true, 11, 10),
    (4, 'Nueva postulación', 'Juan Pérez ha postulado a tu solicitud de instalación de ventilador', 'APPLICATION', true, 12, 11),
    (4, 'Nueva postulación', 'Pedro Gómez ha postulado a tu solicitud de instalación de ventilador', 'APPLICATION', true, 12, 12),
    (4, 'Nuevo mensaje', 'Juan Pérez te ha respondido sobre el ventilador', 'MESSAGE', true, 12, NULL),
    (3, 'Nuevo mensaje', 'María García te ha confirmado visita para el baño', 'MESSAGE', true, 11, NULL)
  `);
  console.log('✅ Additional notifications seeded');

  // ── Reviews ─────────────────────────────────────────────────
  await dataSource.query(`
    INSERT INTO reviews ("serviceId", "reviewerUserId", "reviewedUserId", rating, comment) VALUES 
    (1, 1, 4, 5, 'Excelente trabajo, muy profesional y puntual. Quedé muy satisfecho!'),
    (5, 1, 5, 4, 'Buena calidad en la pintura, terminaron en el tiempo acordado.')
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
    (1, 'Postulación aceptada', 'Juan Pérez ha sido asignado a tu solicitud de instalación de interruptores', 'CONFIRMED', true, 1, 1),
    (1, 'Postulación rechazada', 'María García no continuará con la instalación de interruptores', 'APPLICATION', false, 1, 2),
    (1, 'Servicio completado', 'La reparación de fuga en tu cocina ha sido completada con éxito', 'CONFIRMED', false, 2, 3),
    (1, 'Nuevo mensaje', 'Juan Pérez te ha escrito sobre los interruptores', 'MESSAGE', true, 1, NULL),
    (1, 'Recordatorio', 'Tienes una instalación de interruptores programada para mañana a las 2pm', 'REMINDER', true, 1, NULL),
    (2, 'Postulación aceptada', 'Pedro Gómez ha sido asignado a tu solicitud de estantería', 'CONFIRMED', true, 4, 4),
    (2, 'Postulación aceptada', 'Juan Pérez ha sido asignado al mantenimiento de tu jardín', 'CONFIRMED', true, 5, 5),
    (2, 'Postulación rechazada', 'Pedro Gómez no realizará el mantenimiento del jardín', 'APPLICATION', false, 5, 6),
    (2, 'Nuevo mensaje', 'Juan Pérez confirmó asistencia para el jardín mañana', 'MESSAGE', true, 5, NULL),
    (4, 'Servicio asignado', 'Has sido asignada a la instalación de aire acondicionado en Suba', 'CONFIRMED', true, 6, 7),
    (4, 'Servicio completado', 'Reparación de fuga completada. Cliente: Carlos López. Pago: $75,000', 'CONFIRMED', false, 2, NULL),
    (4, 'Recordatorio', 'Tienes una instalación de aire acondicionado programada', 'REMINDER', true, 6, NULL),
    (3, 'Nuevo mensaje', 'Carlos López te ha enviado un mensaje sobre los interruptores', 'MESSAGE', true, 1, NULL),
    (3, 'Servicio asignado', 'Has sido asignado al mantenimiento de jardín de Sofia Hernández', 'CONFIRMED', false, 5, NULL),
    (3, 'Recordatorio', 'Llegarás mañana a las 8am para el jardín de Sofia Hernández', 'REMINDER', true, 5, NULL),
    (5, 'Nuevo mensaje', 'Sofia Hernández te ha escrito sobre la estantería', 'MESSAGE', true, 4, NULL),
    (5, 'Servicio asignado', 'Estantería para Sofia Hernández programada para el 15 de mayo', 'CONFIRMED', true, 4, NULL),
    (1, 'Pago realizado', 'Tu pago por la reparación de fuga ha sido procesado: $75,000 COP', 'PAYMENT', false, 2, NULL),
    (2, 'Pago pendiente', 'Pago por mantenimiento de jardín: $90,000 COP. Se procesará al completar.', 'PAYMENT', true, 5, NULL)
  `);
  console.log('✅ Notifications seeded');

  console.log('🎉 Seed completed successfully!');
  await dataSource.destroy();
}

seed().catch((error) => {
  console.error('❌ Seed failed:', error);
  process.exit(1);
});
