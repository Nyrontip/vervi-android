import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { CategoriesService } from './categories.service';
import { Category } from './category.entity';

@Controller('categories')
export class CategoriesController {
  constructor(private categoriesService: CategoriesService) {}

  @Get()
  findAll() {
    return this.categoriesService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.categoriesService.findById(+id);
  }

  @Post()
  create(@Body() categoryData: Partial<Category>) {
    return this.categoriesService.create(categoryData);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() categoryData: Partial<Category>) {
    return this.categoriesService.update(+id, categoryData);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.categoriesService.remove(+id);
  }
}
