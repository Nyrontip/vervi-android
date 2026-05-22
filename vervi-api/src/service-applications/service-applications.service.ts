import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { ServiceApplication } from './service-application.entity';
import { Service } from '../services/service.entity';
import { Request } from '../requests/request.entity';
import { NotificationsService } from '../notifications/notifications.service';

@Injectable()
export class ServiceApplicationsService {
  constructor(
    @InjectRepository(ServiceApplication)
    private applicationsRepository: Repository<ServiceApplication>,
    @InjectRepository(Service)
    private servicesRepository: Repository<Service>,
    @InjectRepository(Request)
    private requestsRepository: Repository<Request>,
    private notificationsService: NotificationsService,
  ) { }

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
    const saved = await this.applicationsRepository.save(application);

    // Notificar al cliente y al provider sobre la nueva postulación
    const request = await this.requestsRepository.findOne({
      where: { id: saved.requestId },
    });

    if (request) {
      // Cliente: alguien se postuló
      await this.notificationsService.create({
        userId: request.clientUserId,
        title: 'Nueva postulación',
        description: `Un profesional se postuló a "${request.title}"`,
        type: 'APPLICATION',
        requestId: saved.requestId,
        applicationId: saved.id,
      });

      // Provider: confirmación de postulación enviada
      await this.notificationsService.create({
        userId: saved.providerUserId,
        title: 'Postulación enviada',
        description: `Te postulaste a "${request.title}"`,
        type: 'APPLICATION',
        requestId: saved.requestId,
        applicationId: saved.id,
      });
    }

    return saved;
  }

  async update(id: number, applicationData: Partial<ServiceApplication>): Promise<ServiceApplication | null> {
    // Filter out relational properties so TypeORM update does not try to query across relations
    const { request, provider, notifications, ...columns } = applicationData as any;
    await this.applicationsRepository.update(id, columns);
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
      imageUrl: app.request.imageUrl,
      status: 'SCHEDULED' as any,
    });
    const savedService = await this.servicesRepository.save(service);

    // Notificar al cliente y al provider que el servicio fue confirmado
    await this.notificationsService.create({
      userId: app.request.clientUserId,
      title: 'Servicio confirmado',
      description: `Tu servicio "${app.request.title}" ha sido confirmado. El profesional pronto se pondrá en contacto.`,
      type: 'CONFIRMED',
      requestId: app.requestId,
      serviceId: savedService.id,
    });

    await this.notificationsService.create({
      userId: app.providerUserId,
      title: 'Servicio asignado',
      description: `Has sido seleccionado para "${app.request.title}". El cliente espera tu contacto.`,
      type: 'CONFIRMED',
      requestId: app.requestId,
      serviceId: savedService.id,
    });

    return savedService;
  }
}
