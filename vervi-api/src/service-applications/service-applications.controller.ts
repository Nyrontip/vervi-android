import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ServiceApplicationsService } from './service-applications.service';
import { ServiceApplication } from './service-application.entity';

@Controller('applications')
export class ServiceApplicationsController {
  constructor(private applicationsService: ServiceApplicationsService) {}

  @Get()
  findAll() {
    return this.applicationsService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.applicationsService.findById(+id);
  }

  @Get('request/:requestId')
  findByRequest(@Param('requestId') requestId: string) {
    return this.applicationsService.findByRequestId(+requestId);
  }

  @Get('provider/:providerId')
  findByProvider(@Param('providerId') providerId: string) {
    return this.applicationsService.findByProviderId(+providerId);
  }

  @Post()
  create(@Body() applicationData: Partial<ServiceApplication>) {
    return this.applicationsService.create(applicationData);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() applicationData: Partial<ServiceApplication>) {
    return this.applicationsService.update(+id, applicationData);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.applicationsService.remove(+id);
  }
}
