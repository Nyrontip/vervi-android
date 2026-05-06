import { Test, TestingModule } from '@nestjs/testing';
import { ServiceApplicationsController } from './service-applications.controller';
import { ServiceApplicationsService } from './service-applications.service';

describe('ServiceApplicationsController', () => {
  let controller: ServiceApplicationsController;
  let applicationsService: ServiceApplicationsService;

  const mockApplicationsService = {
    findAll: jest.fn(),
    findById: jest.fn(),
    findByRequestId: jest.fn(),
    findByProviderId: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
    remove: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [ServiceApplicationsController],
      providers: [
        { provide: ServiceApplicationsService, useValue: mockApplicationsService },
      ],
    }).compile();

    controller = module.get<ServiceApplicationsController>(ServiceApplicationsController);
    applicationsService = module.get<ServiceApplicationsService>(ServiceApplicationsService);
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });
});