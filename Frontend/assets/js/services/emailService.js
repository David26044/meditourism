// Email Service
class EmailService {
    
    static async sendWelcomeEmail(emailData) {
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.SEND_WELCOME_EMAIL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(emailData)
            });

            const result = await response.json().catch(() => ({}));
            return {
                success: response.ok,
                message: result.message || (response.ok ? 'Email enviado' : 'Error al enviar email')
            };
        } catch (error) {
            console.error('Error enviando correo:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async sendContactEmail(contactData) {
        try {
            const emailContent = {
                recipient: 'info@meditourism.com',
                subject: `Nuevo contacto: ${contactData.treatmentName || 'Consulta General'}`,
                body: `
                    Nuevo formulario de contacto recibido:
                    
                    Usuario: ${contactData.userName || 'Usuario'}
                    Email: ${contactData.email}
                    Tratamiento: ${contactData.treatmentName || 'No especificado'}
                    
                    Mensaje:
                    ${contactData.message}
                    
                    Fecha: ${new Date().toLocaleString('es-CO')}
                `
            };

            const response = await apiRequest(API_CONFIG.ENDPOINTS.EMAIL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(emailContent)
            });

            return response.ok;
        } catch (error) {
            console.error('Error enviando correo de contacto:', error);
            return false;
        }
    }

    static async sendVerificationEmail(email) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.SEND_VERIFY_EMAIL}?email=${encodeURIComponent(email)}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });

            const result = await response.json().catch(() => ({}));
            return {
                success: response.ok,
                message: result.message || (response.ok ? 'Email de verificación enviado' : 'Error al enviar verificación')
            };
        } catch (error) {
            console.error('Error enviando correo de verificación:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async sendPasswordResetEmail(email) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.SEND_PASSWORD_RESET}?email=${encodeURIComponent(email)}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });

            const result = await response.json().catch(() => ({}));
            return {
                success: response.ok,
                message: result.message || (response.ok ? 'Email de restablecimiento enviado' : 'Error al enviar email')
            };
        } catch (error) {
            console.error('Error enviando correo de restablecimiento:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }
}

// Make EmailService globally available
if (typeof window !== 'undefined') {
    window.EmailService = EmailService;
}
