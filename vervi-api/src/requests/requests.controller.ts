import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiParam, ApiBody } from '@nestjs/swagger';
import { RequestsService } from './requests.service';
import { Request } from './request.entity';

@ApiTags('requests')
@Controller('requests')
export class RequestsController {
  constructor(private requestsService: RequestsService) {}

  @Get()
  @ApiOperation({ summary: 'Get all requests', description: 'Retrieve a list of all service requests' })
  @ApiResponse({ status: 200, description: 'List of requests returned successfully' })
  findAll() {
    return this.requestsService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get request by ID', description: 'Retrieve a specific request by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Request ID' })
  @ApiResponse({ status: 200, description: 'Request found' })
  @ApiResponse({ status: 404, description: 'Request not found' })
  findOne(@Param('id') id: string) {
    return this.requestsService.findById(+id);
  }

  @Get('client/:clientId')
  @ApiOperation({ summary: 'Get requests by client', description: 'Retrieve all requests for a specific client' })
  @ApiParam({ name: 'clientId', type: Number, description: 'Client user ID' })
  @ApiResponse({ status: 200, description: 'List of client requests' })
  findByClient(@Param('clientId') clientId: string) {
    return this.requestsService.findByClientId(+clientId);
  }

  @Post()
  @ApiOperation({ summary: 'Create a new request', description: 'Create a new service request' })
  @ApiResponse({ status: 201, description: 'Request created successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { clientUserId: 1, title: 'Install electrical outlets', description: 'Need 5 outlets installed', location: 'Bogotá', budgetCop: 150000 } } })
  create(@Body() requestData: Partial<Request>) {
    return this.requestsService.create(requestData);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update request', description: 'Update an existing request' })
  @ApiParam({ name: 'id', type: Number, description: 'Request ID' })
  @ApiResponse({ status: 200, description: 'Request updated successfully' })
  @ApiResponse({ status: 404, description: 'Request not found' })
  update(@Param('id') id: string, @Body() requestData: Partial<Request>) {
    return this.requestsService.update(+id, requestData);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete request', description: 'Delete a request by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Request ID' })
  @ApiResponse({ status: 200, description: 'Request deleted successfully' })
  @ApiResponse({ status: 404, description: 'Request not found' })
  remove(@Param('id') id: string) {
    return this.requestsService.remove(+id);
  }
}
