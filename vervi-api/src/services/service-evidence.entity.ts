import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToOne, JoinColumn } from 'typeorm';
import { Service } from './service.entity';

@Entity('service_evidence')
export class ServiceEvidence {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  serviceId: number;

  @ManyToOne(() => Service, service => service.evidence, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'serviceId' })
  service: Service;

  @Column()
  imageUrl: string;

  @Column({ nullable: true })
  caption: string;

  @Column({ default: 0 })
  sortOrder: number;

  @CreateDateColumn()
  createdAt: Date;
}
