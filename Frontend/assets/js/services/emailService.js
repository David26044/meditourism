// Email Service
class EmailService {
    
    static async sendWelcomeEmail(emailData) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.EMAIL.BASE}`, {
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
                subject: `Nuevo contacto de ${contactData.name}`,
                body: `
                    Nuevo mensaje de contacto:
                    
                    Nombre: ${contactData.name}
                    Email: ${contactData.email}
                    Teléfono: ${contactData.phone || 'No proporcionado'}
                    Servicio: ${contactData.service || 'No especificado'}
                    
                    Mensaje:
                    ${contactData.message}
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
