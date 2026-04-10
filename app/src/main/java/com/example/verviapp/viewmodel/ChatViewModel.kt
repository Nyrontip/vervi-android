package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.ChatDao
import com.example.verviapp.data.entity.MessageEntity
import com.example.verviapp.data.repository.SampleData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.verviapp.model.ChatMessageState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ChatUiState(
	val messages: List<ChatMessageState> = emptyList(),
	val inputText: String = "",
	val showAttachments: Boolean = false,
	val isLoading: Boolean = false,
	val isSending: Boolean = false,
	val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
	private val chatDao: ChatDao
) : ViewModel() {

	private val _uiState = MutableStateFlow(ChatUiState())
	val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
	private var historyJob: Job? = null

	init {
		observeHistory()
	}

	private fun observeHistory() {
		historyJob?.cancel()
		historyJob = viewModelScope.launch {
			try {
				_uiState.value = _uiState.value.copy(isLoading = true, error = null)

				chatDao.observeMessages(SampleData.CHAT_CONVERSATION_ID)
					.map { entities ->
						entities.map { entity -> entity.toUiMessage(SampleData.CHAT_REMOTE_USER_ID) }
					}
				.catch { throwable ->
					_uiState.value = _uiState.value.copy(
						isLoading = false,
						error = throwable.message ?: "Error al cargar el chat"
					)
				}
				.collect { messages ->
					_uiState.value = _uiState.value.copy(
						messages = messages,
						isLoading = false,
						error = null
					)
				}
			} catch (throwable: Throwable) {
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					error = throwable.message ?: "Error al inicializar el chat"
				)
			}
		}
	}

	fun onInputChange(text: String) {
		_uiState.value = _uiState.value.copy(inputText = text)
	}

	fun toggleAttachments() {
		_uiState.value = _uiState.value.copy(showAttachments = !_uiState.value.showAttachments)
	}

	fun sendMessage() {
		val text = _uiState.value.inputText.trim()
		if (text.isEmpty()) return

		viewModelScope.launch {
			try {
				_uiState.value = _uiState.value.copy(isSending = true, error = null)
				chatDao.insertMessage(
					MessageEntity(
						conversationId = SampleData.CHAT_CONVERSATION_ID,
						senderUserId = SampleData.CHAT_LOCAL_USER_ID,
						body = text,
						sentAt = System.currentTimeMillis()
					)
				)
				_uiState.value = _uiState.value.copy(
					inputText = "",
					isSending = false,
					showAttachments = false
				)
			} catch (throwable: Throwable) {
				_uiState.value = _uiState.value.copy(
					isSending = false,
					error = throwable.message ?: "No fue posible enviar el mensaje"
				)
			}
		}
	}

	fun refresh() {
		observeHistory()
	}

	private fun MessageEntity.toUiMessage(remoteUserId: Int): ChatMessageState = ChatMessageState(
		text = body,
		time = TIME_FORMATTER.format(Date(sentAt)),
		isUser = senderUserId != remoteUserId,
		avatar = if (senderUserId == remoteUserId) REMOTE_AVATAR_URL else null
	)


	companion object {
		private const val REMOTE_AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDRoJbuz12pcHLb_QstqeS_pEkfsnOEmftV2Ed727AU3t7bOSHjkSfmCY2JfVePqvopofXX7vTDuvAtMCWbilnFLg_UteSicoAczML_D9PBjI0u3D_lCZ-2105dT2Mwv4OwMnkAO0CpuprG8o6wkdVqagwkTNqWhUPtwj3dZv7Vrx1mEuxbBl4UEjgLoTuGsL33f_JCbYv5gu00GBxBjHtqO18o4EutH2iWhrnjS8BqynnfujcsdMGAbM7C8sUbaRuSwpja0lZrAMUJ"
		private val TIME_FORMATTER = SimpleDateFormat("hh:mm a", Locale.getDefault())
	}
}
