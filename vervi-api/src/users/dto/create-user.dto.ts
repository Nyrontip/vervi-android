import { IsEmail, IsNotEmpty, IsString, IsBoolean, IsOptional, IsNumber, IsArray } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateUserDto {
  @ApiProperty({ example: 'user@example.com', description: 'User email address' })
  @IsEmail()
  email: string;

  @ApiProperty({ example: 'password123', description: 'User password' })
  @IsString()
  @IsNotEmpty()
  password: string;

  @ApiProperty({ example: 'John Doe', description: 'User full name' })
  @IsString()
  @IsNotEmpty()
  name: string;

  @ApiPropertyOptional({ description: 'User biography' })
  @IsOptional()
  @IsString()
  bio?: string;

  @ApiPropertyOptional({ example: 'Bogotá, Colombia', description: 'User location' })
  @IsOptional()
  @IsString()
  location?: string;

  @ApiPropertyOptional({ example: 'https://example.com/photo.jpg', description: 'Profile photo URL' })
  @IsOptional()
  @IsString()
  photoUrl?: string;

  @ApiPropertyOptional({ description: 'User rating' })
  @IsOptional()
  @IsNumber()
  rating?: number;

  @ApiPropertyOptional({ description: 'Number of reviews' })
  @IsOptional()
  @IsNumber()
  reviewCount?: number;

  @ApiPropertyOptional({ example: 50000, description: 'Suggested price in COP' })
  @IsOptional()
  @IsNumber()
  suggestedPriceCop?: number;

  @ApiPropertyOptional({ description: 'Whether user is a service provider' })
  @IsOptional()
  @IsBoolean()
  isProvider?: boolean;

  @ApiPropertyOptional({ description: 'Whether user is currently online' })
  @IsOptional()
  @IsBoolean()
  isOnline?: boolean;

  @ApiPropertyOptional({ description: 'Array of category IDs', type: [Number] })
  @IsOptional()
  @IsArray()
  @IsNumber({}, { each: true })
  categoryIds?: number[];
}
