// API Configuration
const API_CONFIG = {
    BASE_URL: 'http://localhost:8080/system/api', // Actualizado para coincidir con application.properties
    ENDPOINTS: {
        // Auth endpoints - corregidos para coincidir con AuthController
        LOGIN: '/auth/login',
        REGISTER: '/auth/register',
        FORGOT_PASSWORD: '/auth/send-change-password', // Endpoint correcto del backend
        RESET_PASSWORD: '/auth/change-password',
        REFRESH_TOKEN: '/auth/refresh',
        VERIFY_EMAIL: '/auth/verify-email',
        SEND_VERIFY_EMAIL: '/auth/send-verify-email',
        // User endpoints
        USER_ME: '/users/me',
        USERS: '/users',
        // Review endpoints
        REVIEWS: '/reviews',
        REVIEWS_LATEST: '/reviews/latest',
        // Clinic endpoints
        CLINICS: '/clinics',
        // Treatment endpoints
        TREATMENTS: '/treatments',
        // Contact form endpoints
        CONTACT_FORMS: '/contact-forms',
        // Email endpoints
        EMAIL: '/email',
        SEND_WELCOME_EMAIL: '/send-welcome-email'
    },
    TIMEOUT: 10000, // 10 segundos
    HEADERS: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
};

// Función helper para hacer requests con configuración base
const apiRequest = async (endpoint, options = {}) => {
    const url = `${API_CONFIG.BASE_URL}${endpoint}`;
    const config = {
        timeout: API_CONFIG.TIMEOUT,
        headers: {
            ...API_CONFIG.HEADERS,
            ...options.headers
        },
        ...options
    };

    // Agregar token si existe
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return fetch(url, config);
};
