import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Service } from './service.entity';
import { ServiceEvidence } from './service-evidence.entity';
import { ServicesService } from './services.service';
import { ServicesController } from './services.controller';

@Module({
  imports: [TypeOrmModule.forFeature([Service, ServiceEvidence])],
  providers: [ServicesService],
  controllers: [ServicesController],
  exports: [TypeOrmModule]
})
export class ServicesModule {}
