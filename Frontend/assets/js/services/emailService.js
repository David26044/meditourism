// Email Service
class EmailService {
    
    static async sendWelcomeEmail(emailData) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.SEND_WELCOME_EMAIL}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(emailData)
            });

            return response.ok;
        } catch (error) {
            console.error('Error enviando correo:', error);
            return false;
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

            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.EMAIL}`, {
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
}
