import { Controller, Get, Post, Body, Param, Put } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiParam, ApiBody } from '@nestjs/swagger';
import { ChatService } from './chat.service';
import { Conversation } from './conversation.entity';
import { Message } from './message.entity';

@ApiTags('chat')
@Controller('chat')
export class ChatController {
  constructor(private chatService: ChatService) {}

  @Get('conversations')
  @ApiOperation({ summary: 'Get all conversations', description: 'Retrieve a list of all chat conversations' })
  @ApiResponse({ status: 200, description: 'List of conversations returned successfully' })
  findAllConversations() {
    return this.chatService.findAllConversations();
  }

  @Get('conversations/:id')
  @ApiOperation({ summary: 'Get conversation by ID', description: 'Retrieve a specific conversation by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Conversation ID' })
  @ApiResponse({ status: 200, description: 'Conversation found' })
  @ApiResponse({ status: 404, description: 'Conversation not found' })
  findConversation(@Param('id') id: string) {
    return this.chatService.findConversationById(+id);
  }

  @Get('conversations/user/:userId')
  @ApiOperation({ summary: 'Get conversations by user', description: 'Retrieve all conversations for a specific user' })
  @ApiParam({ name: 'userId', type: Number, description: 'User ID' })
  @ApiResponse({ status: 200, description: 'List of user conversations' })
  findUserConversations(@Param('userId') userId: string) {
    return this.chatService.findConversationsByUser(+userId);
  }

  @Post('conversations')
  @ApiOperation({ summary: 'Create a new conversation', description: 'Start a new chat conversation' })
  @ApiResponse({ status: 201, description: 'Conversation created successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { participantAUserId: 1, participantBUserId: 2, requestId: 1 } } })
  createConversation(@Body() conversationData: Partial<Conversation>) {
    return this.chatService.createConversation(conversationData);
  }

  @Post('conversations/find-or-create')
  @ApiOperation({ summary: 'Find or create a conversation', description: 'Find an existing conversation between two users for a request, or create a new one' })
  @ApiResponse({ status: 201, description: 'Conversation found or created' })
  @ApiBody({ schema: { example: { participantAUserId: 1, participantBUserId: 2, requestId: 1 } } })
  findOrCreateConversation(@Body() body: { participantAUserId: number; participantBUserId: number; requestId?: number }) {
    return this.chatService.findOrCreateConversation(body.participantAUserId, body.participantBUserId, body.requestId);
  }

  @Get('messages/:conversationId')
  @ApiOperation({ summary: 'Get messages by conversation', description: 'Retrieve all messages for a specific conversation' })
  @ApiParam({ name: 'conversationId', type: Number, description: 'Conversation ID' })
  @ApiResponse({ status: 200, description: 'List of messages returned successfully' })
  findMessages(@Param('conversationId') conversationId: string) {
    return this.chatService.findMessagesByConversation(+conversationId);
  }

  @Post('messages')
  @ApiOperation({ summary: 'Send a new message', description: 'Send a new message in a conversation' })
  @ApiResponse({ status: 201, description: 'Message sent successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { conversationId: 1, senderUserId: 1, body: 'Hello, when can you come?' } } })
  createMessage(@Body() messageData: Partial<Message>) {
    return this.chatService.createMessage(messageData);
  }

  @Put('messages/:id/read')
  @ApiOperation({ summary: 'Mark message as read', description: 'Mark a message as read' })
  @ApiParam({ name: 'id', type: Number, description: 'Message ID' })
  @ApiResponse({ status: 200, description: 'Message marked as read' })
  @ApiResponse({ status: 404, description: 'Message not found' })
  markAsRead(@Param('id') id: string) {
    return this.chatService.markMessageAsRead(+id);
  }
}
