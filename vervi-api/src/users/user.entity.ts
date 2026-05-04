import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, OneToMany, ManyToMany, JoinTable } from 'typeorm';
import { Request } from '../requests/request.entity';
import { Service } from '../services/service.entity';
import { Review } from '../reviews/review.entity';
import { Conversation } from '../chat/conversation.entity';
import { Notification } from '../notifications/notification.entity';
import { Message } from '../chat/message.entity';
import { Category } from '../categories/category.entity';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  email: string;

  @Column()
  password: string;

  @Column()
  name: string;

  @Column({ nullable: true })
  bio: string;

  @Column({ nullable: true })
  location: string;

  @Column({ nullable: true })
  photoUrl: string;

  @Column({ default: 0 })
  rating: number;

  @Column({ default: 0 })
  reviewCount: number;

  @Column({ nullable: true })
  suggestedPriceCop: number;

  @Column({ default: false })
  isProvider: boolean;

  @Column({ default: false })
  isOnline: boolean;

  @Column({ default: 0 })
  projectCount: number;

  @Column({ default: 0 })
  requestCount: number;

  @ManyToMany(() => Category, category => category.users)
  @JoinTable({
    name: 'user_categories',
    joinColumn: { name: 'userId', referencedColumnName: 'id' },
    inverseJoinColumn: { name: 'categoryId', referencedColumnName: 'id' }
  })
  categories: Category[];

  @OneToMany(() => Request, request => request.client)
  requests: Request[];

  @OneToMany(() => Service, service => service.client)
  clientServices: Service[];

  @OneToMany(() => Service, service => service.provider)
  providerServices: Service[];

  @OneToMany(() => Review, review => review.reviewer)
  writtenReviews: Review[];

  @OneToMany(() => Review, review => review.reviewed)
  receivedReviews: Review[];

  @OneToMany(() => Conversation, conv => conv.participantA)
  conversationsA: Conversation[];

  @OneToMany(() => Conversation, conv => conv.participantB)
  conversationsB: Conversation[];

  @OneToMany(() => Message, message => message.sender)
  sentMessages: Message[];

  @OneToMany(() => Notification, notification => notification.user)
  notifications: Notification[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
