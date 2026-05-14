package com.example.verviapp.data.repository

import android.content.Context
import com.example.verviapp.data.remote.VerviApi
import com.example.verviapp.data.remote.dto.ImageUploadResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUploadRepository @Inject constructor(
    private val api: VerviApi,
    @ApplicationContext private val context: Context
) {
    suspend fun uploadImage(uri: android.net.Uri): ApiResult<ImageUploadResponse> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return ApiResult.Error("No se pudo abrir la imagen")

            val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()

            val requestBody = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("file", tempFile.name, requestBody)

            val response = api.uploadImage(multipartBody)
            
            tempFile.delete()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error("Respuesta vacía del servidor")
                }
            } else {
                ApiResult.Error("Error al subir imagen: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Error de conexión al subir imagen")
        }
    }
}