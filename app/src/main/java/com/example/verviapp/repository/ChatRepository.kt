package com.example.verviapp.repository

import com.example.verviapp.model.ChatMessage

interface ChatRepository {
	fun getHistory(): List<ChatMessage>
	fun send(message: ChatMessage)
	fun update(message: ChatMessage)
}


