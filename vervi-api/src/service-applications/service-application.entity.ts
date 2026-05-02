import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, JoinColumn, OneToMany } from 'typeorm';
import { Request } from '../requests/request.entity';
import { User } from '../users/user.entity';
import { Notification } from '../notifications/notification.entity';

@Entity('service_applications')
export class ServiceApplication {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  requestId: number;

  @ManyToOne(() => Request, request => request.applications, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'requestId' })
  request: Request;

  @Column()
  providerUserId: number;

  @ManyToOne(() => User, user => user.providerServices, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'providerUserId' })
  provider: User;

  @Column({ type: 'text' })
  presentationMessage: string;

  @Column({ nullable: true })
  proposedPriceCop: number;

  @Column({ nullable: true })
  evidenceUri: string;

  @Column({ default: false })
  immediateAvailability: boolean;

  @Column({
    type: 'enum',
    enum: ['PENDING', 'ACCEPTED', 'REJECTED'],
    default: 'PENDING'
  })
  status: string;

  @OneToMany(() => Notification, notification => notification.application)
  notifications: Notification[];

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
