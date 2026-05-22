import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ServiceApplication } from './service-application.entity';
import { Service } from '../services/service.entity';
import { Request } from '../requests/request.entity';
import { NotificationsModule } from '../notifications/notifications.module';
import { ServiceApplicationsService } from './service-applications.service';
import { ServiceApplicationsController } from './service-applications.controller';

@Module({
  imports: [
    TypeOrmModule.forFeature([ServiceApplication, Service, Request]),
    NotificationsModule,
  ],
  providers: [ServiceApplicationsService],
  controllers: [ServiceApplicationsController],
  exports: [TypeOrmModule]
})
export class ServiceApplicationsModule {}
