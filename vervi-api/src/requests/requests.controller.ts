import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { RequestsService } from './requests.service';
import { Request } from './request.entity';

@Controller('requests')
export class RequestsController {
  constructor(private requestsService: RequestsService) {}

  @Get()
  findAll() {
    return this.requestsService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.requestsService.findById(+id);
  }

  @Get('client/:clientId')
  findByClient(@Param('clientId') clientId: string) {
    return this.requestsService.findByClientId(+clientId);
  }

  @Post()
  create(@Body() requestData: Partial<Request>) {
    return this.requestsService.create(requestData);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() requestData: Partial<Request>) {
    return this.requestsService.update(+id, requestData);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.requestsService.remove(+id);
  }
}
