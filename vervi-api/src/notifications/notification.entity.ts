import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToOne, JoinColumn } from 'typeorm';
import { User } from '../users/user.entity';
import { Request } from '../requests/request.entity';
import { Service } from '../services/service.entity';
import { ServiceApplication } from '../service-applications/service-application.entity';
import { Conversation } from '../chat/conversation.entity';

@Entity('notifications')
export class Notification {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  userId: number;

  @ManyToOne(() => User, user => user.notifications, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'userId' })
  user: User;

  @Column()
  title: string;

  @Column({ type: 'text' })
  description: string;

  @Column({
    type: 'enum',
    enum: ['APPLICATION', 'MESSAGE', 'CONFIRMED', 'PAYMENT', 'REMINDER'],
  })
  type: string;

  @Column({ default: true })
  isUnread: boolean;

  @Column({ nullable: true })
  requestId: number;

  @ManyToOne(() => Request, request => request.notifications, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'requestId' })
  request: Request;

  @Column({ nullable: true })
  serviceId: number;

  @ManyToOne(() => Service, service => service.notifications, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'serviceId' })
  service: Service;

  @Column({ nullable: true })
  conversationId: number;

  @ManyToOne(() => Conversation, conversation => conversation.notifications, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'conversationId' })
  conversation: Conversation;

  @Column({ nullable: true })
  applicationId: number;

  @ManyToOne(() => ServiceApplication, application => application.notifications, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'applicationId' })
  application: ServiceApplication;

  @CreateDateColumn()
  createdAt: Date;
}
