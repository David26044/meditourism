// Authentication Service
class AuthService {
    // Variable para cachear información del usuario
    static cachedUser = null;

    static async login(email, password) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.LOGIN}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();
            
            if (response.ok) {
                // Store token
                localStorage.setItem('token', data.token);
                
                // Get complete user info including role
                const userResponse = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.USER_ME}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${data.token}`,
                        'Content-Type': 'application/json'
                    }
                });
                
                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    localStorage.setItem('user', JSON.stringify(userData));
                    this.cachedUser = userData;
                    
                    console.log('Usuario logueado:', userData); // Debug
                    
                    // Update user display with admin badge if needed
                    setTimeout(() => {
                        if (typeof UserService !== 'undefined') {
                            UserService.updateUserDisplay();
                            UserService.updateUserAvatar();
                        }
                    }, 100);
                }
                
                return { 
                    success: true, 
                    data,
                    message: 'Inicio de sesión exitoso'
                };
            } else {
                return { 
                    success: false, 
                    message: data.message || 'Credenciales incorrectas'
                };
            }
        } catch (error) {
            console.error('Login error:', error);
            return { 
                success: false, 
                message: 'Error de conexión. Verifica que el servidor esté ejecutándose.'
            };
        }
    }

    static async register(username, email, password) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.REGISTER}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ 
                    name: username, 
                    email, 
                    password 
                })
            });

            const data = await response.json();
            
            if (response.ok) {
                return { 
                    success: true, 
                    data,
                    message: 'Registro exitoso. Ya puedes iniciar sesión.'
                };
            } else {
                return { 
                    success: false, 
                    message: data.message || 'Error al registrarse'
                };
            }
        } catch (error) {
            console.error('Register error:', error);
            return { 
                success: false, 
                message: 'Error de conexión. Verifica que el servidor esté ejecutándose.'
            };
        }
    }

    static async forgotPassword(email) {
        try {
            // Usar query parameter como espera el backend
            const url = `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.FORGOT_PASSWORD}?email=${encodeURIComponent(email)}`;
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const message = await response.text(); // El backend devuelve String
                return { 
                    success: true, 
                    message: message || 'Enlace de recuperación enviado a tu email'
                };
            } else {
                const errorData = await response.json().catch(() => ({ 
                    message: 'Error al enviar email de recuperación' 
                }));
                return { 
                    success: false, 
                    message: errorData.message || 'Error al enviar email de recuperación'
                };
            }
        } catch (error) {
            console.error('Forgot password error:', error);
            return { 
                success: false, 
                message: 'Error de conexión. Verifica que el servidor esté ejecutándose.'
            };
        }
    }

    static async resetPassword(token, newPassword) {
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.RESET_PASSWORD}?token=${token}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ newPassword: newPassword })
            });

            const data = await response.text(); // El backend devuelve String, no JSON
            
            if (response.ok) {
                return { success: true, message: data };
            } else {
                return { success: false, message: data || 'Error al cambiar la contraseña' };
            }
        } catch (error) {
            console.error('Reset password error:', error);
            return { success: false, message: 'Error de conexión' };
        }
    }

    static async checkUserSession() {
        if (this.cachedUser) {
            return this.cachedUser;
        }

        const token = localStorage.getItem('token'); // Changed from 'authToken' to 'token'
        if (token) {
            try {
                const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.USER_ME}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`, // Changed from 'authToken' to 'token'
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const user = await response.json();
                    this.cachedUser = user;
                    return user;
                } else {
                    console.error('No se pudo obtener la información del usuario');
                    return null;
                }
            } catch (error) {
                console.error('Error al verificar la sesión del usuario:', error);
                return null;
            }
        }
        return null;
    }

    static logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('rememberedEmail');
        localStorage.removeItem('rememberUser');
        this.cachedUser = null;
        
        // Remove admin badge before redirect
        const adminBadge = document.getElementById('adminBadge');
        if (adminBadge) {
            adminBadge.remove();
        }
        
        window.location.href = 'login.html';
    }

    static getCurrentUser() {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    }

    static getToken() {
        return localStorage.getItem('token');
    }

    static isAuthenticated() {
        return !!this.getToken();
    }

    static isAdmin() {
        if (typeof UserService !== 'undefined') {
            return UserService.isAdmin();
        }
        
        const user = this.getCurrentUser();
        return user && user.roleEntity && user.roleEntity.name === 'ROLE_ADMIN';
    }
}
