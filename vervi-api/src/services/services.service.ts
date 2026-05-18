import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Service } from './service.entity';

@Injectable()
export class ServicesService {
  constructor(
    @InjectRepository(Service)
    private servicesRepository: Repository<Service>,
  ) { }

  async findAll(): Promise<Service[]> {
    return this.servicesRepository.find({ relations: ['client', 'provider', 'request', 'evidence', 'reviews'] });
  }

  async findById(id: number): Promise<Service | null> {
    return this.servicesRepository.findOne({ where: { id }, relations: ['client', 'provider', 'request', 'evidence', 'reviews', 'conversations'] });
  }

  async findByClientId(clientId: number): Promise<Service[]> {
    return this.servicesRepository.find({ where: { clientUserId: clientId }, relations: ['provider', 'request'] });
  }

  async findByProviderId(providerId: number): Promise<Service[]> {
    return this.servicesRepository.find({ where: { providerUserId: providerId }, relations: ['client', 'request'] });
  }

  async create(serviceData: Partial<Service>): Promise<Service> {
    const service = this.servicesRepository.create(serviceData);
    return this.servicesRepository.save(service);
  }

  async update(id: number, serviceData: Partial<Service>): Promise<Service | null> {
    // Filter out relational properties so TypeORM update does not try to query across one-to-many/many-to-one relations
    const { client, provider, request, evidence, reviews, conversations, ...columns } = serviceData as any;
    await this.servicesRepository.update(id, columns);
    return this.findById(id);
  }


  async remove(id: number): Promise<void> {
    await this.servicesRepository.delete(id);
  }
}
