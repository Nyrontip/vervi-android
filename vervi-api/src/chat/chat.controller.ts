import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ChatService } from './chat.service';
import { Conversation } from './conversation.entity';
import { Message } from './message.entity';

@Controller('chat')
export class ChatController {
  constructor(private chatService: ChatService) {}

  @Get('conversations')
  findAllConversations() {
    return this.chatService.findAllConversations();
  }

  @Get('conversations/:id')
  findConversation(@Param('id') id: string) {
    return this.chatService.findConversationById(+id);
  }

  @Get('conversations/user/:userId')
  findUserConversations(@Param('userId') userId: string) {
    return this.chatService.findConversationsByUser(+userId);
  }

  @Post('conversations')
  createConversation(@Body() conversationData: Partial<Conversation>) {
    return this.chatService.createConversation(conversationData);
  }

  @Get('messages/:conversationId')
  findMessages(@Param('conversationId') conversationId: string) {
    return this.chatService.findMessagesByConversation(+conversationId);
  }

  @Post('messages')
  createMessage(@Body() messageData: Partial<Message>) {
    return this.chatService.createMessage(messageData);
  }

  @Put('messages/:id/read')
  markAsRead(@Param('id') id: string) {
    return this.chatService.markMessageAsRead(+id);
  }
}
