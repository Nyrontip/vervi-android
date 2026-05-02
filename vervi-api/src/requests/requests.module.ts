import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Request } from './request.entity';
import { RequestAttachment } from './request-attachment.entity';
import { RequestsService } from './requests.service';
import { RequestsController } from './requests.controller';

@Module({
  imports: [TypeOrmModule.forFeature([Request, RequestAttachment])],
  providers: [RequestsService],
  controllers: [RequestsController],
  exports: [TypeOrmModule]
})
export class RequestsModule {}
