import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Service } from './service.entity';
import { ServiceEvidence } from './service-evidence.entity';

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
    
    // Save evidence if provided
    if (evidence && Array.isArray(evidence) && evidence.length > 0) {
      const newEvidences = evidence.map(ev => {
        const item = new ServiceEvidence();
        item.serviceId = id;
        item.imageUrl = ev.imageUrl;
        item.caption = ev.caption || '';
        return item;
      });
      await this.servicesRepository.manager.save(newEvidences);
    }

    if (Object.keys(columns).length > 0) {
      await this.servicesRepository.update(id, columns);
    }
    
    return this.findById(id);
  }


  async remove(id: number): Promise<void> {
    await this.servicesRepository.delete(id);
  }
}
