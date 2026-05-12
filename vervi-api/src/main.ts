import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  
  // Enable CORS for mobile app
  app.enableCors({
    origin: '*',
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE,OPTIONS',
    credentials: true,
  });
  
  // Global validation pipe
  app.useGlobalPipes(new ValidationPipe({ whitelist: true }));
  
  // Global API prefix
  app.setGlobalPrefix('api');
  
  // Swagger configuration
  const config = new DocumentBuilder()
    .setTitle('VerviApp API')
    .setDescription('API documentation for VerrviApp - Service marketplace application')
    .setVersion('1.0')
    .addBearerAuth(
      {
        type: 'http',
        scheme: 'bearer',
        bearerFormat: 'JWT',
        name: 'JWT',
        description: 'Enter JWT token',
        in: 'header',
      },
      'JWT-auth',
    )
    .addTag('auth', 'Authentication endpoints')
    .addTag('users', 'User management')
    .addTag('categories', 'Service categories')
    .addTag('requests', 'Service requests')
    .addTag('services', 'Confirmed services')
    .addTag('applications', 'Service applications')
    .addTag('reviews', 'Service reviews')
    .addTag('chat', 'Chat and messaging')
    .addTag('notifications', 'User notifications')
    .build();
  
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api/docs', app, document);
  
  await app.listen(process.env.PORT ?? 3000);
}
bootstrap();
