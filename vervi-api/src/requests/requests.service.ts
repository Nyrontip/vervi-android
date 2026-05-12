import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Request } from './request.entity';

@Injectable()
export class RequestsService {
  constructor(
    @InjectRepository(Request)
    private requestsRepository: Repository<Request>,
  ) {}

  async findAll(): Promise<Request[]> {
    return this.requestsRepository.find({ relations: ['client', 'category', 'attachments', 'applications'] });
  }

  async findById(id: number): Promise<Request | null> {
    return this.requestsRepository.findOne({ where: { id }, relations: ['client', 'category', 'attachments', 'applications', 'services'] });
  }

  async findByClientId(clientId: number): Promise<Request[]> {
    return this.requestsRepository.find({
      where: { clientUserId: clientId },
      order: { createdAt: 'DESC' },
      relations: ['client', 'category', 'attachments'],
    });
  }

  async create(requestData: Partial<Request>): Promise<Request> {
    const request = this.requestsRepository.create(requestData);
    return this.requestsRepository.save(request);
  }

  async update(id: number, requestData: Partial<Request>): Promise<Request | null> {
    await this.requestsRepository.update(id, requestData);
    return this.findById(id);
  }

  async remove(id: number): Promise<void> {
    await this.requestsRepository.delete(id);
  }
}

