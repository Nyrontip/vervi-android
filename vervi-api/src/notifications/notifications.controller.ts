import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiParam, ApiBody } from '@nestjs/swagger';
import { NotificationsService } from './notifications.service';
import { Notification } from './notification.entity';

@ApiTags('notifications')
@Controller('notifications')
export class NotificationsController {
  constructor(private notificationsService: NotificationsService) {}

  @Get()
  @ApiOperation({ summary: 'Get all notifications', description: 'Retrieve a list of all notifications' })
  @ApiResponse({ status: 200, description: 'List of notifications returned successfully' })
  findAll() {
    return this.notificationsService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get notification by ID', description: 'Retrieve a specific notification by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Notification ID' })
  @ApiResponse({ status: 200, description: 'Notification found' })
  @ApiResponse({ status: 404, description: 'Notification not found' })
  findOne(@Param('id') id: string) {
    return this.notificationsService.findById(+id);
  }

  @Get('user/:userId')
  @ApiOperation({ summary: 'Get notifications by user', description: 'Retrieve all notifications for a specific user' })
  @ApiParam({ name: 'userId', type: Number, description: 'User ID' })
  @ApiResponse({ status: 200, description: 'List of user notifications' })
  findByUser(@Param('userId') userId: string) {
    return this.notificationsService.findByUserId(+userId);
  }

  @Get('user/:userId/unread')
  @ApiOperation({ summary: 'Get unread notifications by user', description: 'Retrieve only unread notifications for a user' })
  @ApiParam({ name: 'userId', type: Number, description: 'User ID' })
  @ApiResponse({ status: 200, description: 'List of unread notifications' })
  findUnreadByUser(@Param('userId') userId: string) {
    return this.notificationsService.findUnreadByUserId(+userId);
  }

  @Post()
  @ApiOperation({ summary: 'Create a new notification', description: 'Create a new notification' })
  @ApiResponse({ status: 201, description: 'Notification created successfully' })
  @ApiResponse({ status: 400, description: 'Bad request' })
  @ApiBody({ schema: { example: { userId: 1, title: 'New application', description: 'Someone applied to your request', type: 'APPLICATION' } } })
  create(@Body() notificationData: Partial<Notification>) {
    return this.notificationsService.create(notificationData);
  }

  @Put(':id/read')
  @ApiOperation({ summary: 'Mark notification as read', description: 'Mark a specific notification as read' })
  @ApiParam({ name: 'id', type: Number, description: 'Notification ID' })
  @ApiResponse({ status: 200, description: 'Notification marked as read' })
  @ApiResponse({ status: 404, description: 'Notification not found' })
  markAsRead(@Param('id') id: string) {
    return this.notificationsService.markAsRead(+id);
  }

  @Put('user/:userId/read-all')
  @ApiOperation({ summary: 'Mark all notifications as read', description: 'Mark all notifications for a user as read' })
  @ApiParam({ name: 'userId', type: Number, description: 'User ID' })
  @ApiResponse({ status: 200, description: 'All notifications marked as read' })
  markAllAsRead(@Param('userId') userId: string) {
    return this.notificationsService.markAllAsRead(+userId);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete notification', description: 'Delete a notification by ID' })
  @ApiParam({ name: 'id', type: Number, description: 'Notification ID' })
  @ApiResponse({ status: 200, description: 'Notification deleted successfully' })
  @ApiResponse({ status: 404, description: 'Notification not found' })
  remove(@Param('id') id: string) {
    return this.notificationsService.remove(+id);
  }
}
