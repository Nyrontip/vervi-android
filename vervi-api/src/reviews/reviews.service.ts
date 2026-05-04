import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Review } from './review.entity';

@Injectable()
export class ReviewsService {
  constructor(
    @InjectRepository(Review)
    private reviewsRepository: Repository<Review>,
  ) {}

  async findAll(): Promise<Review[]> {
    return this.reviewsRepository.find({ relations: ['service', 'reviewer', 'reviewed'] });
  }

  async findById(id: number): Promise<Review | null> {
    return this.reviewsRepository.findOne({ where: { id }, relations: ['service', 'reviewer', 'reviewed'] });
  }

  async findByServiceId(serviceId: number): Promise<Review[]> {
    return this.reviewsRepository.find({ where: { serviceId }, relations: ['reviewer', 'reviewed'] });
  }

  async create(reviewData: Partial<Review>): Promise<Review> {
    const review = this.reviewsRepository.create(reviewData);
    return this.reviewsRepository.save(review);
  }

  async update(id: number, reviewData: Partial<Review>): Promise<Review | null> {
    await this.reviewsRepository.update(id, reviewData);
    return this.findById(id);
  }

  async remove(id: number): Promise<void> {
    await this.reviewsRepository.delete(id);
  }
}
