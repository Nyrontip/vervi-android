import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ServiceApplication } from './service-application.entity';
import { Service } from '../services/service.entity';
import { ServiceApplicationsService } from './service-applications.service';
import { ServiceApplicationsController } from './service-applications.controller';

@Module({
  imports: [TypeOrmModule.forFeature([ServiceApplication, Service])],
  providers: [ServiceApplicationsService],
  controllers: [ServiceApplicationsController],
  exports: [TypeOrmModule]
})
export class ServiceApplicationsModule {}
