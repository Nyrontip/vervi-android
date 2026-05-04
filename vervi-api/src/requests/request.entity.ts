import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { User } from '../users/user.entity';
import { Category } from '../categories/category.entity';
import { RequestAttachment } from './request-attachment.entity';
import { ServiceApplication } from '../service-applications/service-application.entity';
import { Service } from '../services/service.entity';
import { Notification } from '../notifications/notification.entity';
import { Conversation } from '../chat/conversation.entity';

@Entity('requests')
export class Request {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ nullable: true })
  clientUserId: number;

  @ManyToOne(() => User, user => user.requests, { nullable: true })
  @JoinColumn({ name: 'clientUserId' })
  client: User;

  @Column({ nullable: true })
  categoryId: number;

  @ManyToOne(() => Category, category => category.requests, { nullable: true })
  @JoinColumn({ name: 'categoryId' })
  category: Category;

  @Column({
    type: 'enum',
    enum: ['Borrador', 'Pendiente', 'En curso', 'Cerrado'],
    default: 'Borrador'
  })
  status: string;

  @Column()
  title: string;

  @Column({ type: 'text', nullable: true })
  description: string;

  @Column({ nullable: true })
  location: string;

  @Column({ nullable: true })
  budgetCop: number;

  @Column({ nullable: true })
  requiredDateMillis: number;

  @Column({ nullable: true })
  imageUrl: string;

  @Column({ default: false })
  isUrgent: boolean;

  @Column({ default: true })
  isActive: boolean;

  @Column({ default: 0 })
  applicationCount: number;

  @Column({ type: 'timestamp', nullable: true })
  closedAt: Date;

  @OneToMany(() => RequestAttachment, attachment => attachment.request, { cascade: true })
  attachments: RequestAttachment[];

  @OneToMany(() => ServiceApplication, application => application.request, { cascade: true })
  applications: ServiceApplication[];

  @OneToMany(() => Notification, notification => notification.request)
  notifications: Notification[];

  @OneToMany(() => Conversation, conversation => conversation.request)
  conversations: Conversation[];

  @OneToMany(() => Service, service => service.request)
  services: Service[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
