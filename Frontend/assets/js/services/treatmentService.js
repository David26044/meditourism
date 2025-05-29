// Treatment Service
class TreatmentService {
    
    static async getAllTreatments() {
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.TREATMENTS, {
                method: 'GET'
            });

            if (response.ok) {
                const treatments = await response.json();
                return { success: true, data: treatments };
            } else {
                return { success: false, message: 'Error al obtener tratamientos' };
            }
        } catch (error) {
            console.error('Error en TreatmentService.getAllTreatments:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async getTreatmentById(id) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.TREATMENTS}/${id}`, {
                method: 'GET'
            });

            if (response.ok) {
                const treatment = await response.json();
                return { success: true, data: treatment };
            } else {
                return { success: false, message: 'Tratamiento no encontrado' };
            }
        } catch (error) {
            console.error('Error en TreatmentService.getTreatmentById:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async createTreatment(treatmentData) {
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.TREATMENTS, {
                method: 'POST',
                body: JSON.stringify(treatmentData)
            });

            if (response.ok) {
                const treatment = await response.json();
                return { success: true, data: treatment };
            } else {
                const errorData = await response.json();
                return { success: false, message: errorData.message || 'Error al crear tratamiento' };
            }
        } catch (error) {
            console.error('Error en TreatmentService.createTreatment:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async updateTreatment(id, treatmentData) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.TREATMENTS}/${id}`, {
                method: 'PATCH',
                body: JSON.stringify(treatmentData)
            });

            if (response.ok) {
                const treatment = await response.json();
                return { success: true, data: treatment };
            } else {
                const errorData = await response.json();
                return { success: false, message: errorData.message || 'Error al actualizar tratamiento' };
            }
        } catch (error) {
            console.error('Error en TreatmentService.updateTreatment:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async deleteTreatment(id) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.TREATMENTS}/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                const treatment = await response.json();
                return { success: true, data: treatment };
            } else {
                const errorData = await response.json();
                return { success: false, message: errorData.message || 'Error al eliminar tratamiento' };
            }
        } catch (error) {
            console.error('Error en TreatmentService.deleteTreatment:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }
}

// Make TreatmentService globally available
if (typeof window !== 'undefined') {
    window.TreatmentService = TreatmentService;
}
