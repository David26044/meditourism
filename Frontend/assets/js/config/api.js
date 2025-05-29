// API Configuration
const API_CONFIG = {
    // Use environment variable or detect if running on Vercel/production
    BASE_URL: (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
        ? 'http://localhost:8080/system/api' // URL para desarrollo local
        : 'https://meditourism-production.up.railway.app/system/api', // URL para producción (Railway)
    ENDPOINTS: {
        // Auth endpoints - corregidos para coincidir con AuthController
        LOGIN: '/auth/login',
        REGISTER: '/auth/register',
        FORGOT_PASSWORD: '/auth/send-change-password', // Endpoint correcto del backend
        RESET_PASSWORD: '/auth/change-password',
        REFRESH_TOKEN: '/auth/refresh',
        VERIFY_EMAIL: '/auth/verify-email',
        SEND_VERIFY_EMAIL: '/auth/send-verify-email',
        SEND_PASSWORD_RESET: '/auth/send-change-password', // Agregar endpoint faltante
        // User endpoints
        USER_ME: '/users/me',
        USERS: '/users',
        ROLES: '/roles',
        BLOCKED_USERS: '/blocked-users',
        // Review endpoints
        REVIEWS: '/reviews',
        REVIEWS_LATEST: '/reviews/latest',
        // Clinic endpoints
        CLINICS: '/clinics',
        // Treatment endpoints
        TREATMENTS: '/treatments',
        // Contact form endpoints
        CONTACT_FORMS: '/contact-forms',
        // Email endpoints - Fixed structure
        EMAIL: '/email',
        SEND_WELCOME_EMAIL: '/email/send-welcome-email'
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

    try {
        const response = await fetch(url, config);
        return response;
    } catch (error) {
        console.error('API Request failed:', error);
        throw error;
    }
};
