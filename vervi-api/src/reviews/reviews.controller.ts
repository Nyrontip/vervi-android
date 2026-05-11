import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiParam, ApiBody } from '@nestjs/swagger';
import { ReviewsService } from './reviews.service';
import { Review } from './review.entity';

@ApiTags('reviews')
@Controller('reviews')
export class ReviewsController {
  constructor(private reviewsService: ReviewsService) {}

  @Get()
  @ApiOperation({ summary: 'Get all reviews', description: 'Retrieve a list of all service reviews' })
  @ApiResponse({ status: 200, description: 'List of reviews returned successfully' })
  findAll() {
    return this.reviewsService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get review by ID', description: 'Retrieve a specific review by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Review ID' })
  @ApiResponse({ status: 200, description: 'Review found' })
  @ApiResponse({ status: 404, description: 'Review not found' })
  findOne(@Param('id') id: string) {
    return this.reviewsService.findById(+id);
  }

  @Get('service/:serviceId')
  @ApiOperation({ summary: 'Get reviews by service', description: 'Retrieve all reviews for a specific service' })
  @ApiParam({ name: 'serviceId', type: Number, description: 'Service ID' })
  @ApiResponse({ status: 200, description: 'List of reviews for the service' })
  findByService(@Param('serviceId') serviceId: string) {
    return this.reviewsService.findByServiceId(+serviceId);
  }

  @Post()
  @ApiOperation({ summary: 'Create a new review', description: 'Create a new review for a completed service' })
  @ApiResponse({ status: 201, description: 'Review created successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { serviceId: 1, reviewerUserId: 1, reviewedUserId: 2, rating: 5, comment: 'Excellent work!' } } })
  create(@Body() reviewData: Partial<Review>) {
    return this.reviewsService.create(reviewData);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update review', description: 'Update an existing review' })
  @ApiParam({ name: 'id', type: Number, description: 'Review ID' })
  @ApiResponse({ status: 200, description: 'Review updated successfully' })
  @ApiResponse({ status: 404, description: 'Review not found' })
  update(@Param('id') id: string, @Body() reviewData: Partial<Review>) {
    return this.reviewsService.update(+id, reviewData);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete review', description: 'Delete a review by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Review ID' })
  @ApiResponse({ status: 200, description: 'Review deleted successfully' })
  @ApiResponse({ status: 404, description: 'Review not found' })
  remove(@Param('id') id: string) {
    return this.reviewsService.remove(+id);
  }
}
