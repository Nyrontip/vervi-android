import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiParam, ApiBody } from '@nestjs/swagger';
import { ServicesService } from './services.service';
import { Service } from './service.entity';

@ApiTags('services')
@Controller('services')
export class ServicesController {
  constructor(private servicesService: ServicesService) {}

  @Get()
  @ApiOperation({ summary: 'Get all services', description: 'Retrieve a list of all confirmed services' })
  @ApiResponse({ status: 200, description: 'List of services returned successfully' })
  findAll() {
    return this.servicesService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get service by ID', description: 'Retrieve a specific service by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Service ID' })
  @ApiResponse({ status: 200, description: 'Service found' })
  @ApiResponse({ status: 404, description: 'Service not found' })
  findOne(@Param('id') id: string) {
    return this.servicesService.findById(+id);
  }

  @Get('client/:clientId')
  @ApiOperation({ summary: 'Get services by client', description: 'Retrieve all services for a specific client' })
  @ApiParam({ name: 'clientId', type: Number, description: 'Client user ID' })
  @ApiResponse({ status: 200, description: 'List of client services' })
  findByClient(@Param('clientId') clientId: string) {
    return this.servicesService.findByClientId(+clientId);
  }

  @Get('provider/:providerId')
  @ApiOperation({ summary: 'Get services by provider', description: 'Retrieve all services for a specific provider' })
  @ApiParam({ name: 'providerId', type: Number, description: 'Provider user ID' })
  @ApiResponse({ status: 200, description: 'List of provider services' })
  findByProvider(@Param('providerId') providerId: string) {
    return this.servicesService.findByProviderId(+providerId);
  }

  @Post()
  @ApiOperation({ summary: 'Create a new service', description: 'Create a new confirmed service' })
  @ApiResponse({ status: 201, description: 'Service created successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { clientUserId: 1, providerUserId: 2, title: 'Electrical installation', totalPriceCop: 120000, status: 'SCHEDULED' } } })
  create(@Body() serviceData: Partial<Service>) {
    return this.servicesService.create(serviceData);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update service', description: 'Update an existing service' })
  @ApiParam({ name: 'id', type: Number, description: 'Service ID' })
  @ApiResponse({ status: 200, description: 'Service updated successfully' })
  @ApiResponse({ status: 404, description: 'Service not found' })
  update(@Param('id') id: string, @Body() serviceData: Partial<Service>) {
    return this.servicesService.update(+id, serviceData);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete service', description: 'Delete a service by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Service ID' })
  @ApiResponse({ status: 200, description: 'Service deleted successfully' })
  @ApiResponse({ status: 404, description: 'Service not found' })
  remove(@Param('id') id: string) {
    return this.servicesService.remove(+id);
  }
}
