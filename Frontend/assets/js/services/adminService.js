class AdminService {
    static async getAllUsers() {
        console.log('🔍 AdminService.getAllUsers() - Obteniendo todos los usuarios...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.USERS);
            if (response.ok) {
                const users = await response.json();
                console.log('✅ Usuarios obtenidos:', users);
                return users;
            } else {
                throw new Error('Error al obtener usuarios');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllUsers:', error);
            throw error;
        }
    }

    static async getAllRoles() {
        console.log('🔍 AdminService.getAllRoles() - Obteniendo roles...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.ROLES);
            if (response.ok) {
                const roles = await response.json();
                console.log('✅ Roles obtenidos:', roles);
                return roles;
            } else {
                throw new Error('Error al obtener roles');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllRoles:', error);
            throw error;
        }
    }

    static async updateUserRole(userId, roleId) {
        console.log(`🔄 AdminService.updateUserRole() - Actualizando rol del usuario ${userId} a rol ${roleId}`);
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${userId}/role?roleId=${roleId}`, {
                method: 'PATCH'
            });
            if (response.ok) {
                const updatedUser = await response.json();
                console.log('✅ Rol actualizado:', updatedUser);
                return updatedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al actualizar rol');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.updateUserRole:', error);
            throw error;
        }
    }

    static async getAllBlockedUsers() {
        console.log('🔍 AdminService.getAllBlockedUsers() - Obteniendo usuarios bloqueados...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.BLOCKED_USERS);
            if (response.ok) {
                const blockedUsers = await response.json();
                console.log('✅ Usuarios bloqueados obtenidos:', blockedUsers);
                return blockedUsers;
            } else {
                throw new Error('Error al obtener usuarios bloqueados');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllBlockedUsers:', error);
            throw error;
        }
    }

    static async blockUser(userId, reason) {
        console.log(`🚫 AdminService.blockUser() - Bloqueando usuario ${userId}`);
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.BLOCKED_USERS, {
                method: 'POST',
                body: JSON.stringify({
                    userId: userId,
                    reason: reason
                })
            });
            if (response.ok) {
                const blockedUser = await response.json();
                console.log('✅ Usuario bloqueado:', blockedUser);
                return blockedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al bloquear usuario');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.blockUser:', error);
            throw error;
        }
    }

    static async unblockUser(blockedUserId) {
        console.log(`✅ AdminService.unblockUser() - Desbloqueando usuario ${blockedUserId}`);
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.BLOCKED_USERS}/${blockedUserId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                const unblockedUser = await response.json();
                console.log('✅ Usuario desbloqueado:', unblockedUser);
                return unblockedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al desbloquear usuario');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.unblockUser:', error);
            throw error;
        }
    }

    static async deleteNormalUser(userId) {
        console.log(`🗑️ AdminService.deleteNormalUser() - Eliminando usuario normal ${userId}`);
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${userId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                const deletedUser = await response.json();
                console.log('✅ Usuario normal eliminado:', deletedUser);
                return deletedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al eliminar usuario normal');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.deleteNormalUser:', error);
            throw error;
        }
    }
}

// Make AdminService globally available
if (typeof window !== 'undefined') {
    window.AdminService = AdminService;
}
