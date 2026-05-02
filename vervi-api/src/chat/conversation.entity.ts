import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { User } from '../users/user.entity';
import { Request } from '../requests/request.entity';
import { Service } from '../services/service.entity';
import { Message } from './message.entity';
import { Notification } from '../notifications/notification.entity';

@Entity('conversations')
export class Conversation {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  participantAUserId: number;

  @ManyToOne(() => User, user => user.conversationsA, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'participantAUserId' })
  participantA: User;

  @Column()
  participantBUserId: number;

  @ManyToOne(() => User, user => user.conversationsB, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'participantBUserId' })
  participantB: User;

  @Column({ nullable: true })
  requestId: number;

  @ManyToOne(() => Request, request => request.conversations, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'requestId' })
  request: Request;

  @Column({ nullable: true })
  serviceId: number;

  @ManyToOne(() => Service, service => service.conversations, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'serviceId' })
  service: Service;

  @Column({ nullable: true })
  lastMessagePreview: string;

  @Column({ type: 'timestamp', nullable: true })
  lastMessageAt: Date;

  @OneToMany(() => Message, message => message.conversation, { cascade: true })
  messages: Message[];

  @OneToMany(() => Notification, notification => notification.conversation)
  notifications: Notification[];

  @CreateDateColumn()
  createdAt: Date;
}
