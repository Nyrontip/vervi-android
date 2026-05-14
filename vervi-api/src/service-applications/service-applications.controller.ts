import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiParam, ApiBody } from '@nestjs/swagger';
import { ServiceApplicationsService } from './service-applications.service';
import { ServiceApplication } from './service-application.entity';

@ApiTags('applications')
@Controller('applications')
export class ServiceApplicationsController {
  constructor(private applicationsService: ServiceApplicationsService) {}

  @Get()
  @ApiOperation({ summary: 'Get all applications', description: 'Retrieve a list of all service applications' })
  @ApiResponse({ status: 200, description: 'List of applications returned successfully' })
  findAll() {
    return this.applicationsService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get application by ID', description: 'Retrieve a specific application by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Application ID' })
  @ApiResponse({ status: 200, description: 'Application found' })
  @ApiResponse({ status: 404, description: 'Application not found' })
  findOne(@Param('id') id: string) {
    return this.applicationsService.findById(+id);
  }

  @Get('request/:requestId')
  @ApiOperation({ summary: 'Get applications by request', description: 'Retrieve all applications for a specific request' })
  @ApiParam({ name: 'requestId', type: Number, description: 'Request ID' })
  @ApiResponse({ status: 200, description: 'List of applications for the request' })
  findByRequest(@Param('requestId') requestId: string) {
    return this.applicationsService.findByRequestId(+requestId);
  }

  @Get('provider/:providerId')
  @ApiOperation({ summary: 'Get applications by provider', description: 'Retrieve all applications from a specific provider' })
  @ApiParam({ name: 'providerId', type: Number, description: 'Provider user ID' })
  @ApiResponse({ status: 200, description: 'List of applications by the provider' })
  findByProvider(@Param('providerId') providerId: string) {
    return this.applicationsService.findByProviderId(+providerId);
  }

  @Post()
  @ApiOperation({ summary: 'Create a new application', description: 'Submit a new application to a service request' })
  @ApiResponse({ status: 201, description: 'Application created successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { requestId: 1, providerUserId: 2, presentationMessage: 'I have 10 years of experience', proposedPriceCop: 130000 } } })
  create(@Body() applicationData: Partial<ServiceApplication>) {
    return this.applicationsService.create(applicationData);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update application', description: 'Update an existing application' })
  @ApiParam({ name: 'id', type: Number, description: 'Application ID' })
  @ApiResponse({ status: 200, description: 'Application updated successfully' })
  @ApiResponse({ status: 404, description: 'Application not found' })
  update(@Param('id') id: string, @Body() applicationData: Partial<ServiceApplication>) {
    return this.applicationsService.update(+id, applicationData);
  }

  @Post(':id/accept')
  @ApiOperation({ summary: 'Accept application', description: 'Accept an application, reject others, and create a Service' })
  @ApiParam({ name: 'id', type: Number, description: 'Application ID' })
  @ApiResponse({ status: 201, description: 'Service created successfully' })
  accept(@Param('id') id: string) {
    return this.applicationsService.acceptApplication(+id);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete application', description: 'Delete an application by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Application ID' })
  @ApiResponse({ status: 200, description: 'Application deleted successfully' })
  @ApiResponse({ status: 404, description: 'Application not found' })
  remove(@Param('id') id: string) {
    return this.applicationsService.remove(+id);
  }
}
