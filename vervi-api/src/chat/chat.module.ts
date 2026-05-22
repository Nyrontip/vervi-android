import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Conversation } from './conversation.entity';
import { Message } from './message.entity';
import { Request } from '../requests/request.entity';
import { Service } from '../services/service.entity';
import { NotificationsModule } from '../notifications/notifications.module';
import { ChatService } from './chat.service';
import { ChatController } from './chat.controller';

@Module({
  imports: [
    TypeOrmModule.forFeature([Conversation, Message, Request, Service]),
    NotificationsModule,
  ],
  providers: [ChatService],
  controllers: [ChatController],
  exports: [TypeOrmModule]
})
export class ChatModule {}
