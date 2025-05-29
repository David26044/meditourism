// User Service
class UserService {
    static cachedUser = null;

    static async getCurrentUserInfo() {
        console.log('🔍 UserService.getCurrentUserInfo() - Iniciando...');
        try {
            const token = localStorage.getItem('token');
            console.log('🔑 Token encontrado:', token ? 'SÍ' : 'NO');
            if (!token) {
                console.log('❌ No hay token, retornando null');
                return null;
            }

            console.log('🌐 Haciendo request a:', API_CONFIG.ENDPOINTS.USER_ME);
            const response = await apiRequest(API_CONFIG.ENDPOINTS.USER_ME, {
                method: 'GET'
            });

            console.log('📡 Response status:', response.status);
            if (response.ok) {
                const user = await response.json();
                console.log('👤 Usuario obtenido del servidor:', user);
                // Store updated user info
                localStorage.setItem('user', JSON.stringify(user));
                AuthService.cachedUser = user;
                console.log('✅ Usuario guardado en localStorage y cache');
                return user;
            } else {
                console.error('❌ Error al obtener información del usuario - Status:', response.status);
                return null;
            }
        } catch (error) {
            console.error('💥 Error en UserService.getCurrentUserInfo:', error);
            return null;
        }
    }

    static getStoredUser() {
        console.log('📦 UserService.getStoredUser() - Obteniendo usuario almacenado...');
        const userStr = localStorage.getItem('user');
        const user = userStr ? JSON.parse(userStr) : null;
        console.log('🗃️ Usuario en localStorage (raw):', userStr);
        console.log('👤 Usuario parseado:', user);
        return user;
    }

    static isAdmin() {
        console.log('🛡️ UserService.isAdmin() - Verificando permisos de admin...');
        const user = this.getStoredUser();
        console.log('👤 Usuario para verificar admin:', user);
        const isAdmin = user && user.role && user.role.name === 'ADMIN';
        console.log('🔐 Es admin:', isAdmin);
        return isAdmin;
    }

    static isAuthenticated() {
        const token = localStorage.getItem('token');
        return !!token;
    }

    static getCurrentUser() {
        return this.getStoredUser();
    }

    static async refreshUserInfo() {
        console.log('🔄 UserService.refreshUserInfo() - Refrescando información del usuario...');
        const token = localStorage.getItem('token');
        if (!token) return null;

        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.USER_ME, {
                method: 'GET'
            });

            if (response.ok) {
                const user = await response.json();
                localStorage.setItem('user', JSON.stringify(user));
                this.cachedUser = user;
                return user;
            }
        } catch (error) {
            console.error('Error refreshing user info:', error);
        }
        return null;
    }

    static updateUserDisplay() {
        console.log('🎨 UserService.updateUserDisplay() - Actualizando display del usuario...');
        const userNameElement = document.getElementById('userName');
        console.log('🎯 Elemento userName encontrado:', userNameElement);
        
        if (!userNameElement) {
            console.error('❌ No se encontró el elemento #userName en el DOM');
            return;
        }

        const user = this.getStoredUser();
        console.log('👤 Usuario para mostrar:', user);
        
        if (user && user.name) {
            let displayName = user.name;
            console.log('📝 Nombre base:', displayName);
            
            // Add admin badge if user is admin
            if (this.isAdmin()) {
                displayName += ' (admin)';
                console.log('🛡️ Agregando badge de admin, nombre final:', displayName);
            }
            
            userNameElement.textContent = displayName;
            userNameElement.style.display = 'inline';
            console.log('✅ Nombre actualizado en DOM:', displayName);
            console.log('👁️ Elemento visible:', userNameElement.style.display);
            
            // Update admin badge in DOM if needed
            this.updateAdminBadge();
        } else {
            console.log('❌ No hay usuario o no tiene nombre, ocultando elemento');
            userNameElement.style.display = 'none';
        }
    }

    static updateAdminBadge() {
        console.log('🏷️ UserService.updateAdminBadge() - Actualizando badge de admin...');
        // Remove existing admin badge
        const existingBadge = document.getElementById('adminBadge');
        if (existingBadge) {
            console.log('🗑️ Removiendo badge existente');
            existingBadge.remove();
        }

        if (this.isAdmin()) {
            console.log('🛡️ Usuario es admin, creando badge...');
            const userInfo = document.querySelector('.user-info');
            console.log('🎯 Elemento user-info encontrado:', userInfo);
            if (userInfo) {
                const adminBadge = document.createElement('span');
                adminBadge.id = 'adminBadge';
                adminBadge.className = 'admin-badge';
                adminBadge.textContent = 'ADMIN';
                adminBadge.style.cssText = `
                    background: #ff4757;
                    color: white;
                    font-size: 10px;
                    padding: 2px 6px;
                    border-radius: 10px;
                    margin-left: 8px;
                    font-weight: bold;
                `;
                userInfo.appendChild(adminBadge);
                console.log('✅ Badge de admin creado y agregado');
            } else {
                console.error('❌ No se encontró elemento .user-info para agregar badge');
            }
        } else {
            console.log('👤 Usuario no es admin, no se agrega badge');
        }
    }

    static updateUserAvatar() {
        console.log('🖼️ UserService.updateUserAvatar() - Actualizando avatar...');
        const userAvatar = document.getElementById('userAvatar');
        console.log('🎯 Elemento userAvatar encontrado:', userAvatar);
        if (!userAvatar) {
            console.error('❌ No se encontró el elemento #userAvatar');
            return;
        }

        const user = this.getStoredUser();
        
        if (user) {
            console.log('👤 Usuario encontrado, mostrando avatar');
            userAvatar.style.display = 'flex';
            
            if (user.avatar) {
                userAvatar.innerHTML = `<img src="${user.avatar}" alt="Avatar" class="avatar-img">`;
            } else {
                userAvatar.innerHTML = '<i class="fas fa-user"></i>';
            }
            console.log('✅ Avatar visible');
        } else {
            console.log('❌ No hay usuario, ocultando avatar');
            userAvatar.style.display = 'none';
        }
    }

    static async isUserBlocked(userId = null) {
        console.log('🔍 UserService.isUserBlocked() - Verificando si usuario está bloqueado...');
        try {
            const targetUserId = userId || this.getCurrentUser()?.id;
            if (!targetUserId) return false;

            const response = await apiRequest(API_CONFIG.ENDPOINTS.BLOCKED_USERS);
            if (response.ok) {
                const blockedUsers = await response.json();
                const isBlocked = blockedUsers.some(blocked => blocked.user.id === targetUserId);
                console.log('✅ Estado de bloqueo verificado:', isBlocked);
                return isBlocked;
            }
            return false;
        } catch (error) {
            console.error('💥 Error en UserService.isUserBlocked:', error);
            return false;
        }
    }

    static async validateUserCanPerformAction(actionName) {
        console.log(`🔍 UserService.validateUserCanPerformAction() - Validando acción: ${actionName}`);
        
        if (!this.isAuthenticated()) {
            throw new Error('Debes iniciar sesión para realizar esta acción');
        }

        const isBlocked = await this.isUserBlocked();
        if (isBlocked) {
            throw new Error('Tu cuenta ha sido suspendida. No puedes realizar esta acción.');
        }

        return true;
    }

    static showAdminBadge() {
        const userInfo = document.querySelector('.user-info');
        if (userInfo && !document.getElementById('adminBadge')) {
            const badge = document.createElement('span');
            badge.id = 'adminBadge';
            badge.className = 'admin-badge-small';
            badge.innerHTML = '<i class="fas fa-crown"></i> Admin';
            userInfo.appendChild(badge);
        }
    }
}
