import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { ServiceApplication } from './service-application.entity';
import { Service } from '../services/service.entity';

@Injectable()
export class ServiceApplicationsService {
  constructor(
    @InjectRepository(ServiceApplication)
    private applicationsRepository: Repository<ServiceApplication>,
    @InjectRepository(Service)
    private servicesRepository: Repository<Service>,
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

  async acceptApplication(applicationId: number): Promise<Service> {
    const app = await this.applicationsRepository.findOne({
      where: { id: applicationId },
      relations: ['request'],
    });
    if (!app) throw new Error('Application not found');

    // Aceptar esta
    await this.applicationsRepository.update(applicationId, { status: 'ACCEPTED' });

    // Rechazar las demás del mismo request que sigan PENDING
    await this.applicationsRepository.update(
      { requestId: app.requestId, status: 'PENDING' as any },
      { status: 'REJECTED' as any },
    );

    // Crear servicio
    const service = this.servicesRepository.create({
      requestId: app.requestId,
      clientUserId: app.request.clientUserId,
      providerUserId: app.providerUserId,
      title: app.request.title,
      summary: app.presentationMessage || 'Servicio confirmado',
      location: app.request.location,
      totalPriceCop: app.proposedPriceCop ?? 0,
      status: 'SCHEDULED' as any,
    });
    return this.servicesRepository.save(service);
  }
}
