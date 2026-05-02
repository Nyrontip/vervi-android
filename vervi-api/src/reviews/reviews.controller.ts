import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ReviewsService } from './reviews.service';
import { Review } from './review.entity';

@Controller('reviews')
export class ReviewsController {
  constructor(private reviewsService: ReviewsService) {}

  @Get()
  findAll() {
    return this.reviewsService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.reviewsService.findById(+id);
  }

  @Get('service/:serviceId')
  findByService(@Param('serviceId') serviceId: string) {
    return this.reviewsService.findByServiceId(+serviceId);
  }

  @Post()
  create(@Body() reviewData: Partial<Review>) {
    return this.reviewsService.create(reviewData);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() reviewData: Partial<Review>) {
    return this.reviewsService.update(+id, reviewData);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.reviewsService.remove(+id);
  }
}
