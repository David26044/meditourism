// Contact Form Service
class ContactService {
    
    static async submitContactForm(formData) {
        console.log('🌐 ContactService.submitContactForm iniciado');
        console.log('📤 Datos a enviar:', formData);
        
        try {
            // Validar que todos los campos requeridos estén presentes
            const requiredFields = ['userId', 'treatmentId', 'fullName', 'email', 'inquiryType', 'message', 'acceptTerms'];
            const missingFields = [];
            
            requiredFields.forEach(field => {
                if (formData[field] === null || formData[field] === undefined || formData[field] === '') {
                    if (field === 'acceptTerms' && formData[field] === false) {
                        missingFields.push(field);
                    } else if (field !== 'acceptTerms') {
                        missingFields.push(field);
                    }
                }
            });
            
            if (missingFields.length > 0) {
                console.error('❌ Campos requeridos faltantes:', missingFields);
                return {
                    success: false,
                    message: `Campos requeridos faltantes: ${missingFields.join(', ')}`
                };
            }
            
            console.log('✅ Validación previa exitosa, enviando al backend...');
            
            const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS, {
                method: 'POST',
                body: JSON.stringify(formData)
            });

            console.log('📨 Respuesta HTTP status:', response.status);
            console.log('📨 Respuesta HTTP headers:', response.headers);

            if (response.ok) {
                const data = await response.json();
                console.log('✅ Respuesta exitosa del backend:', data);
                return {
                    success: true,
                    data,
                    message: 'Formulario de contacto enviado exitosamente'
                };
            } else {
                const errorData = await response.json().catch(() => null);
                console.error('❌ Error del backend:', {
                    status: response.status,
                    statusText: response.statusText,
                    errorData: errorData
                });
                
                return {
                    success: false,
                    message: errorData?.message || `Error del servidor: ${response.status}`,
                    errors: errorData?.errors || null
                };
            }
        } catch (error) {
            console.error('💥 Error en ContactService.submitContactForm:', error);
            console.error('Stack trace:', error.stack);
            return {
                success: false,
                message: 'Error de conexión. Verifica que el servidor esté ejecutándose.'
            };
        }
    }

    static async getUserContactForms(userId) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/user/${userId}`, {
                method: 'GET'
            });

            if (response.ok) {
                const forms = await response.json();
                return { success: true, data: forms };
            } else {
                return { success: false, message: 'Error al obtener formularios' };
            }
        } catch (error) {
            console.error('Error en ContactService.getUserContactForms:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async getAllContactForms() {
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS, {
                method: 'GET'
            });

            if (response.ok) {
                const forms = await response.json();
                return { success: true, data: forms };
            } else {
                return { success: false, message: 'Error al obtener formularios' };
            }
        } catch (error) {
            console.error('Error en ContactService.getAllContactForms:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async getTreatmentContactCounts() {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/treatment-contact-count`, {
                method: 'GET'
            });

            if (response.ok) {
                const counts = await response.json();
                return { success: true, data: counts };
            } else {
                return { success: false, message: 'Error al obtener estadísticas' };
            }
        } catch (error) {
            console.error('Error en ContactService.getTreatmentContactCounts:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }
}
