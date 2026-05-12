import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Conversation } from './conversation.entity';
import { Message } from './message.entity';

@Injectable()
export class ChatService {
  constructor(
    @InjectRepository(Conversation)
    private conversationsRepository: Repository<Conversation>,
    @InjectRepository(Message)
    private messagesRepository: Repository<Message>,
  ) {}

  async findAllConversations(): Promise<Conversation[]> {
    return this.conversationsRepository.find({ relations: ['participantA', 'participantB', 'messages'] });
  }

  async findConversationById(id: number): Promise<Conversation | null> {
    return this.conversationsRepository.findOne({ where: { id }, relations: ['participantA', 'participantB', 'messages', 'request', 'service'] });
  }

  async findConversationsByUser(userId: number): Promise<Conversation[]> {
    return this.conversationsRepository.find({
      where: [
        { participantAUserId: userId },
        { participantBUserId: userId }
      ],
      relations: ['participantA', 'participantB', 'messages']
    });
  }

  async createConversation(conversationData: Partial<Conversation>): Promise<Conversation> {
    const conversation = this.conversationsRepository.create(conversationData);
    return this.conversationsRepository.save(conversation);
  }

  async findOrCreateConversation(
    participantAUserId: number,
    participantBUserId: number,
    requestId?: number,
  ): Promise<Conversation> {
    const existing = await this.conversationsRepository.findOne({
      where: [
        { participantAUserId, participantBUserId, requestId },
        { participantAUserId: participantBUserId, participantBUserId: participantAUserId, requestId },
      ],
      relations: ['participantA', 'participantB', 'request'],
    });
    if (existing) return existing;

    return this.createConversation({ participantAUserId, participantBUserId, requestId });
  }

  async findMessagesByConversation(conversationId: number): Promise<Message[]> {
    return this.messagesRepository.find({ where: { conversationId }, relations: ['sender'] });
  }

  async createMessage(messageData: Partial<Message>): Promise<Message> {
    const message = this.messagesRepository.create(messageData);
    return this.messagesRepository.save(message);
  }

  async markMessageAsRead(messageId: number): Promise<Message | null> {
    await this.messagesRepository.update(messageId, { isRead: true });
    return this.messagesRepository.findOne({ where: { id: messageId } });
  }
}
