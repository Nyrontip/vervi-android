import { Injectable, Inject } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { User } from './user.entity';
import { Category } from '../categories/category.entity';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,
    @InjectRepository(Category)
    private categoriesRepository: Repository<Category>,
  ) {}

  async create(userData: Partial<User>): Promise<User> {
    const { categoryIds, ...userDataWithoutCategories } = userData as any;
    let user = this.usersRepository.create(userDataWithoutCategories as any);
    
    if (categoryIds && Array.isArray(categoryIds) && categoryIds.length > 0) {
      const categories = await this.categoriesRepository.findByIds(categoryIds);
      (user as any).categories = categories;
    }
    
    const saved = await this.usersRepository.save(user);
    return Array.isArray(saved) ? saved[0] : saved;
  }

  async findAll(): Promise<User[]> {
    return this.usersRepository.find({ relations: ['categories'] });
  }

  async findById(id: number): Promise<User | null> {
    return this.usersRepository.findOne({ where: { id }, relations: ['categories'] });
  }

  async findByEmail(email: string): Promise<User | null> {
    return this.usersRepository.findOne({ where: { email }, relations: ['categories'] });
  }

  async update(id: number, userData: Partial<User>): Promise<User> {
    const { categoryIds, ...userDataWithoutCategories } = userData as any;
    
    if (categoryIds && Array.isArray(categoryIds) && categoryIds.length > 0) {
      const user = await this.usersRepository.findOne({ where: { id }, relations: ['categories'] });
      if (user) {
        const categories = await this.categoriesRepository.findByIds(categoryIds);
        user.categories = categories as any;
        await this.usersRepository.save(user);
      }
    }
    
    await this.usersRepository.update(id, userDataWithoutCategories);
    const updatedUser = await this.usersRepository.findOne({ where: { id }, relations: ['categories'] });
    return updatedUser as User;
  }

  async remove(id: number): Promise<void> {
    await this.usersRepository.delete(id);
  }
}


