import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToOne, JoinColumn } from 'typeorm';
import { Service } from '../services/service.entity';
import { User } from '../users/user.entity';

@Entity('reviews')
export class Review {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  serviceId: number;

  @ManyToOne(() => Service, service => service.reviews, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'serviceId' })
  service: Service;

  @Column()
  reviewerUserId: number;

  @ManyToOne(() => User, user => user.writtenReviews, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'reviewerUserId' })
  reviewer: User;

  @Column()
  reviewedUserId: number;

  @ManyToOne(() => User, user => user.receivedReviews, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'reviewedUserId' })
  reviewed: User;

  @Column()
  rating: number;

  @Column({ type: 'text', nullable: true })
  comment: string;

  @Column({ nullable: true })
  evidenceImageUrl: string;

  @CreateDateColumn()
  createdAt: Date;
}
