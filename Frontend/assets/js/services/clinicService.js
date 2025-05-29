class ClinicService {
    static async getAllClinics() {
        try {
            // Default clinics for fallback
            const defaultClinics = [
                { id: 1, name: 'Sede Marsella', address: 'Marsella, Pereira' },
                { id: 2, name: 'Sede Chapinero', address: 'Chapinero, Pereira' },
                { id: 3, name: 'Sede Lago de los Héroes', address: 'Lago de los Héroes, Pereira' }
            ];

            // Try to fetch from API first
            try {
                if (typeof apiRequest !== 'undefined') {
                    const response = await apiRequest(API_CONFIG.ENDPOINTS.CLINICS || '/api/clinics');
                    if (response.ok) {
                        const clinics = await response.json();
                        return { success: true, data: clinics };
                    }
                }
            } catch (apiError) {
                console.log('Clinics API not available, using default clinics');
            }

            return { success: true, data: defaultClinics };
        } catch (error) {
            console.error('Error in getAllClinics:', error);
            return { success: false, message: 'Error al cargar clínicas' };
        }
    }

    static async getClinicById(id) {
        try {
            const result = await this.getAllClinics();
            if (result.success) {
                const clinic = result.data.find(c => c.id === parseInt(id));
                if (clinic) {
                    return { success: true, data: clinic };
                } else {
                    return { success: false, message: 'Clínica no encontrada' };
                }
            }
            return result;
        } catch (error) {
            console.error('Error in getClinicById:', error);
            return { success: false, message: 'Error al cargar clínica' };
        }
    }
}

// Make ClinicService globally available
if (typeof window !== 'undefined') {
    window.ClinicService = ClinicService;
}
