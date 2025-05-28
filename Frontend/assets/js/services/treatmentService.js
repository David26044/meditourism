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
}
