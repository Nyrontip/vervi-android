import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToOne, JoinColumn } from 'typeorm';
import { Request } from './request.entity';

@Entity('request_attachments')
export class RequestAttachment {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  requestId: number;

  @ManyToOne(() => Request, request => request.attachments, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'requestId' })
  request: Request;

  @Column()
  uri: string;

  @Column({ nullable: true })
  mimeType: string;

  @Column({ default: 0 })
  sortOrder: number;

  @CreateDateColumn()
  createdAt: Date;
}
