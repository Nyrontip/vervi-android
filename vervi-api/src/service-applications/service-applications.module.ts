import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ServiceApplication } from './service-application.entity';
import { ServiceApplicationsService } from './service-applications.service';
import { ServiceApplicationsController } from './service-applications.controller';

@Module({
  imports: [TypeOrmModule.forFeature([ServiceApplication])],
  providers: [ServiceApplicationsService],
  controllers: [ServiceApplicationsController],
  exports: [TypeOrmModule]
})
export class ServiceApplicationsModule {}
