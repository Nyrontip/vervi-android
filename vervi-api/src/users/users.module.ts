import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from './user.entity';
import { UsersService } from './users.service';
import { UsersController } from './users.controller';
import { CategoriesModule } from '../categories/categories.module';

@Module({
  imports: [
    TypeOrmModule.forFeature([User]),
    CategoriesModule,
  ],
  providers: [UsersService],
  controllers: [UsersController],
  exports: [TypeOrmModule, UsersService]
})
export class UsersModule {}
