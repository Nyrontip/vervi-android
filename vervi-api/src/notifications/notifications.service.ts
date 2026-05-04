import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Notification } from './notification.entity';

@Injectable()
export class NotificationsService {
  constructor(
    @InjectRepository(Notification)
    private notificationsRepository: Repository<Notification>,
  ) {}

  async findAll(): Promise<Notification[]> {
    return this.notificationsRepository.find({ relations: ['user', 'request', 'service', 'conversation', 'application'] });
  }

  async findById(id: number): Promise<Notification | null> {
    return this.notificationsRepository.findOne({ where: { id }, relations: ['user', 'request', 'service', 'conversation', 'application'] });
  }

  async findByUserId(userId: number): Promise<Notification[]> {
    return this.notificationsRepository.find({ where: { userId }, relations: ['request', 'service', 'conversation', 'application'] });
  }

  async findUnreadByUserId(userId: number): Promise<Notification[]> {
    return this.notificationsRepository.find({ where: { userId, isUnread: true }, relations: ['request', 'service', 'conversation', 'application'] });
  }

  async create(notificationData: Partial<Notification>): Promise<Notification> {
    const notification = this.notificationsRepository.create(notificationData);
    return this.notificationsRepository.save(notification);
  }

  async markAsRead(id: number): Promise<Notification | null> {
    await this.notificationsRepository.update(id, { isUnread: false });
    return this.findById(id);
  }

  async markAllAsRead(userId: number): Promise<void> {
    await this.notificationsRepository.update({ userId }, { isUnread: false });
  }

  async remove(id: number): Promise<void> {
    await this.notificationsRepository.delete(id);
  }
}
