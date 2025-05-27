class UserService {
    static async getUserProfile() {
        try {
            const response = await apiRequest('/user/profile');
            const data = await response.json();
            
            if (response.ok) {
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Error al obtener perfil' };
            }
        } catch (error) {
            console.error('User profile error:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async updateProfile(userData) {
        try {
            const response = await apiRequest('/user/profile', {
                method: 'PUT',
                body: JSON.stringify(userData)
            });

            const data = await response.json();
            
            if (response.ok) {
                // Update local storage
                localStorage.setItem('user', JSON.stringify(data.user));
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Error al actualizar perfil' };
            }
        } catch (error) {
            console.error('Update profile error:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async getUserAppointments() {
        try {
            const response = await apiRequest('/user/appointments');
            const data = await response.json();
            
            if (response.ok) {
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Error al obtener citas' };
            }
        } catch (error) {
            console.error('User appointments error:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async getNotifications() {
        try {
            const response = await apiRequest('/user/notifications');
            const data = await response.json();
            
            if (response.ok) {
                return { success: true, data };
            } else {
                return { success: false, message: data.message || 'Error al obtener notificaciones' };
            }
        } catch (error) {
            console.error('Notifications error:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static getCurrentUserFromStorage() {
        try {
            const userStr = localStorage.getItem('user');
            return userStr ? JSON.parse(userStr) : null;
        } catch (error) {
            console.error('Error parsing user from storage:', error);
            return null;
        }
    }

    static updateUserInStorage(userData) {
        try {
            localStorage.setItem('user', JSON.stringify(userData));
            return true;
        } catch (error) {
            console.error('Error updating user in storage:', error);
            return false;
        }
    }
}
