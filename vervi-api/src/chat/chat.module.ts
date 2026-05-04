import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Conversation } from './conversation.entity';
import { Message } from './message.entity';
import { ChatService } from './chat.service';
import { ChatController } from './chat.controller';

@Module({
  imports: [TypeOrmModule.forFeature([Conversation, Message])],
  providers: [ChatService],
  controllers: [ChatController],
  exports: [TypeOrmModule]
})
export class ChatModule {}
