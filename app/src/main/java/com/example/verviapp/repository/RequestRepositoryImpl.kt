package com.example.verviapp.repository

import com.example.verviapp.model.ClientSummary
import com.example.verviapp.model.ChatSummary
import com.example.verviapp.model.RequestDetail

class RequestRepositoryImpl : RequestRepository {

    override suspend fun getRequest(requestId: String): RequestDetail {
        // sample data matching what's used in the UI
        return RequestDetail(
            id = requestId,
            title = "Reparación Aire Acondicionado",
            date = "14 de Octubre, 2023 • 10:30 AM",
            price = "$125.000",
            description = "Se realizó la revisión técnica completa del sistema central. Se identificó fuga en el serpentín, se procedió a sellado y recarga de gas refrigerante R-410A. Limpieza profunda de filtros y drenaje incluida.",
            status = "Completado",
            roleLabel = "Como: Prestador",
            images = listOf(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuAqSS5a-xrrQHgeG8aJRjhsQjmDnhoVgSqV7xk7-tl8uWw91Us660y-5igqlR-FCKR9CtUdB_YyNZP6ezIqaznemYPwVGwKxWoBzNq2HUKzD4ibp1kS4_q5uTA9ehFv4Mrg_kEZXA5k7Ifx6PX1A6Q-O3F6d2yIBMOSUg3qScu4KvuHxzw2LwSXZkel36bsovDutGxTznsGPsVPIBGlC60wJ-rf9dWxtWtfvbCVhkl4Vtn1HhSo1onmosKKltJLoTYm31tu0dKBNsWv",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuDhZBU2xxX1oJm7TDWGMbzXqkH12m4QfnmI8vRo5bu5qCy16P3F70yxnwPsZ1UQzfVNc_RcV6UR5nBVUGlCYoI21hnq_q4pMZXuZiiKa63eDka09BRZvq2vQllKMZ2r8g9_XuoSe88f_tCqiMXBiz-9vMK_l27P7zHkTSSA-0wauDkYIWWwZqQWMrd243CtvJnAZBRrO2nvbnF-_noXA1bbuwEOoVn_steHIOf249lWdMMyD4-Ic9-KWSTgCyXWmSpSTb61SOLAa5uZ",
                "https://lh3.googleusercontent.com/aida-public/AB6AXuBjAV7UadivynJLQcW0ZCie0huPCq5-NgOsEUKoIixEJv_cQx9QyFr0ZAeV84se42IPISvVAeVfST19okT8dhlxRd_SNrS0Ja0kAforSZ8ItFM1xxUEZGQ12UaaNekxVTioulr46maivNSO05w7naHieBStO7kQee2Vi5137VfqTxCn1xQMGKPitZAu9GclF6BF5ymob3ewjysuuFkdwCi2bKrb_5V9WJ4CSaCRT6chQXRkGX0vdXF-Kvp5rKXEfeOIqlpA6iq4u7VQ"
            ),
            client = ClientSummary(
                name = "Mariana Restrepo",
                rating = 4.9f,
                location = "Medellín, Antioquia",
                phone = "+57 300 0000000",
                avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA4proU8h62Ta-hxlsKb4sn_tOVh71LEFwXcF5QBLhKrk3B8tPVOoHJSCGLo_DkCMtADx2mppfKT4TZv0R6MWuwCQCV_oBhvsm6q5Hlfq34SpwsGriembr4OPEOAcMQ7LcO_l9JAJKuAYBLQD1xa_EeXC1ZIW8GNbE8849OHo1Zjc_bAQ2AEy5Wg2-UQ52fUWLEs_uE5oBWnEpKRC9JNIVIr9gErPZTq8OXH10b_ShLLTG1Q3xet0_s2RZNtrK5OX3S5GUHjSCZP_QP"
            ),
            chat = ChatSummary(
                lastMessage = "Hola, ¿ya quedó listo el equipo?",
                unreadCount = 0
            )
        )
    }
}

