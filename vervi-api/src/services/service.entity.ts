import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, OneToMany, JoinColumn } from 'typeorm';
import { Request } from '../requests/request.entity';
import { User } from '../users/user.entity';
import { ServiceEvidence } from './service-evidence.entity';
import { Review } from '../reviews/review.entity';
import { Conversation } from '../chat/conversation.entity';
import { Notification } from '../notifications/notification.entity';

@Entity('services')
export class Service {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ nullable: true })
  requestId: number;

  @ManyToOne(() => Request, request => request.services, { nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'requestId' })
  request: Request;

  @Column()
  clientUserId: number;

  @ManyToOne(() => User, user => user.clientServices, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'clientUserId' })
  client: User;

  @Column()
  providerUserId: number;

  @ManyToOne(() => User, user => user.providerServices, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'providerUserId' })
  provider: User;

  @Column()
  title: string;

  @Column({ nullable: true })
  summary: string;

  @Column({ nullable: true })
  location: string;

  @Column()
  totalPriceCop: number;

  @Column({ type: 'timestamp', nullable: true })
  scheduledAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  startedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  completedAt: Date;

  @Column({ nullable: true })
  imageUrl: string;

  @Column({
    type: 'enum',
    enum: ['SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'],
    default: 'SCHEDULED'
  })
  status: string;

  @OneToMany(() => ServiceEvidence, evidence => evidence.service, { cascade: true })
  evidence: ServiceEvidence[];

  @OneToMany(() => Review, review => review.service, { cascade: true })
  reviews: Review[];

  @OneToMany(() => Conversation, conversation => conversation.service)
  conversations: Conversation[];

  @OneToMany(() => Notification, notification => notification.service)
  notifications: Notification[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
