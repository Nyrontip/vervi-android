import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { ServiceApplication } from './service-application.entity';

@Injectable()
export class ServiceApplicationsService {
  constructor(
    @InjectRepository(ServiceApplication)
    private applicationsRepository: Repository<ServiceApplication>,
  ) {}

  async findAll(): Promise<ServiceApplication[]> {
    return this.applicationsRepository.find({ relations: ['request', 'provider'] });
  }

  async findById(id: number): Promise<ServiceApplication | null> {
    return this.applicationsRepository.findOne({ where: { id }, relations: ['request', 'provider', 'notifications'] });
  }

  async findByRequestId(requestId: number): Promise<ServiceApplication[]> {
    return this.applicationsRepository.find({ where: { requestId }, relations: ['provider'] });
  }

  async findByProviderId(providerId: number): Promise<ServiceApplication[]> {
    return this.applicationsRepository.find({ where: { providerUserId: providerId }, relations: ['request'] });
  }

  async create(applicationData: Partial<ServiceApplication>): Promise<ServiceApplication> {
    const application = this.applicationsRepository.create(applicationData);
    return this.applicationsRepository.save(application);
  }

  async update(id: number, applicationData: Partial<ServiceApplication>): Promise<ServiceApplication | null> {
    await this.applicationsRepository.update(id, applicationData);
    return this.findById(id);
  }

  async remove(id: number): Promise<void> {
    await this.applicationsRepository.delete(id);
  }
}
