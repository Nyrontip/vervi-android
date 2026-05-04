import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ServicesService } from './services.service';
import { Service } from './service.entity';

@Controller('services')
export class ServicesController {
  constructor(private servicesService: ServicesService) {}

  @Get()
  findAll() {
    return this.servicesService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.servicesService.findById(+id);
  }

  @Get('client/:clientId')
  findByClient(@Param('clientId') clientId: string) {
    return this.servicesService.findByClientId(+clientId);
  }

  @Get('provider/:providerId')
  findByProvider(@Param('providerId') providerId: string) {
    return this.servicesService.findByProviderId(+providerId);
  }

  @Post()
  create(@Body() serviceData: Partial<Service>) {
    return this.servicesService.create(serviceData);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() serviceData: Partial<Service>) {
    return this.servicesService.update(+id, serviceData);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.servicesService.remove(+id);
  }
}
