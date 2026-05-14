import { Controller, Post, UseInterceptors, BadRequestException, Req } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { FileInterceptor } from '@nestjs/platform-express';
import { UploadService } from './upload.service';
import type { Request } from 'express';

@ApiTags('upload')
@Controller('upload')
export class UploadController {
  constructor(private uploadService: UploadService) {}

  @Post('image')
  @ApiOperation({ summary: 'Upload image to Cloudinary', description: 'Uploads an image file and returns the Cloudinary URL' })
  @ApiResponse({ status: 201, description: 'Image uploaded successfully', schema: { example: { secure_url: 'https://res.cloudinary.com/demo/image/upload/v1234567890/vervi/sample.jpg' } } })
  @ApiResponse({ status: 400, description: 'No file provided or invalid file' })
  @UseInterceptors(FileInterceptor('file'))
  async uploadImage(@Req() req: Request) {
    if (!req.file) {
      throw new BadRequestException('No file provided');
    }

    const result = await this.uploadService.uploadImage(req.file);
    return result;
  }
}