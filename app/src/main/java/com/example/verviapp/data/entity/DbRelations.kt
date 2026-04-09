package com.example.verviapp.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithCategories(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = UserCategoryCrossRef::class,
            parentColumn = "userId",
            entityColumn = "categoryId"
        )
    )
    val categories: List<CategoryEntity>
)

data class RequestWithAttachments(
    @Embedded val request: RequestEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "requestId"
    )
    val attachments: List<RequestAttachmentEntity>
)

data class RequestWithApplications(
    @Embedded val request: RequestEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "requestId"
    )
    val applications: List<ServiceApplicationEntity>
)

data class ServiceWithEvidence(
    @Embedded val service: ServiceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "serviceId"
    )
    val evidence: List<ServiceEvidenceEntity>
)

data class ServiceWithReviews(
    @Embedded val service: ServiceEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "serviceId"
    )
    val reviews: List<ReviewEntity>
)

data class ConversationWithMessages(
    @Embedded val conversation: ConversationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversationId"
    )
    val messages: List<MessageEntity>
)

