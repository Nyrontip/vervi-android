import { Test, TestingModule } from '@nestjs/testing';
import { ServiceApplicationsService } from './service-applications.service';
import { getRepositoryToken } from '@nestjs/typeorm';
import { ServiceApplication } from './service-application.entity';

describe('ServiceApplicationsService', () => {
  let service: ServiceApplicationsService;

  const mockApplicationsRepository = {
    find: jest.fn(),
    findOne: jest.fn(),
    create: jest.fn(),
    save: jest.fn(),
    update: jest.fn(),
    delete: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        ServiceApplicationsService,
        { provide: getRepositoryToken(ServiceApplication), useValue: mockApplicationsRepository },
      ],
    }).compile();

    service = module.get<ServiceApplicationsService>(ServiceApplicationsService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});