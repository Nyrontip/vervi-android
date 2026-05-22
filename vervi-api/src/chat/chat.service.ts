import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Conversation } from './conversation.entity';
import { Message } from './message.entity';
import { Request } from '../requests/request.entity';
import { Service } from '../services/service.entity';
import { NotificationsService } from '../notifications/notifications.service';

@Injectable()
export class ChatService {
  constructor(
    @InjectRepository(Conversation)
    private conversationsRepository: Repository<Conversation>,
    @InjectRepository(Message)
    private messagesRepository: Repository<Message>,
    @InjectRepository(Request)
    private requestsRepository: Repository<Request>,
    @InjectRepository(Service)
    private servicesRepository: Repository<Service>,
    private notificationsService: NotificationsService,
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
    const saved = await this.messagesRepository.save(message);

    // Gatillar inicio de servicio si es primer mensaje del provider
    await this.tryStartService(messageData.conversationId!, messageData.senderUserId!);

    // Notificar al otro participante de la conversación
    const conversation = await this.conversationsRepository.findOne({
      where: { id: saved.conversationId },
    });
    if (conversation) {
      const otherUserId = conversation.participantAUserId === saved.senderUserId
        ? conversation.participantBUserId
        : conversation.participantAUserId;

      await this.notificationsService.create({
        userId: otherUserId,
        title: 'Nuevo mensaje',
        description: `Tienes un nuevo mensaje en la conversación`,
        type: 'MESSAGE',
        conversationId: saved.conversationId,
      });
    }

    return saved;
  }

  async markMessageAsRead(messageId: number): Promise<Message | null> {
    await this.messagesRepository.update(messageId, { isRead: true });
    return this.messagesRepository.findOne({ where: { id: messageId } });
  }

  private async tryStartService(conversationId: number, senderUserId: number) {
    const conversation = await this.conversationsRepository.findOne({
      where: { id: conversationId },
      relations: ['request'],
    });
    if (!conversation?.requestId) return;

    // Buscar service con providerUserId = sender y status SCHEDULED
    const service = await this.servicesRepository.findOne({
      where: {
        requestId: conversation.requestId,
        providerUserId: senderUserId,
        status: 'SCHEDULED' as any,
      },
    });
    if (!service) return;

    // Solo si es el primer mensaje del provider en esta conversación
    const previousCount = await this.messagesRepository.count({
      where: { conversationId, senderUserId },
    });
    if (previousCount > 1) return;

    // Transicionar
    await this.requestsRepository.update(conversation.requestId, { status: 'En curso' as any });
    await this.servicesRepository.update(service.id, { status: 'IN_PROGRESS' as any });
  }
}
