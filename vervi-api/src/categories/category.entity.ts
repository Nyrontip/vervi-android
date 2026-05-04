import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, ManyToMany, OneToMany } from 'typeorm';
import { User } from '../users/user.entity';
import { Request } from '../requests/request.entity';

@Entity('categories')
export class Category {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  name: string;

  @ManyToMany(() => User, user => user.categories)
  users: User[];

  @OneToMany(() => Request, request => request.category)
  requests: Request[];

  @CreateDateColumn()
  createdAt: Date;
}
