// UI Utility Functions
class UIUtils {
    
    static showMessage(element, message, type = 'info') {
        if (!element) return;
        
        element.textContent = message;
        element.className = `message ${type}`;
        element.style.display = 'block';
        
        // Auto hide after 5 seconds for success messages
        if (type === 'success') {
            setTimeout(() => {
                element.style.display = 'none';
            }, 5000);
        }
    }

    static clearMessage(element) {
        if (!element) return;
        element.textContent = '';
        element.style.display = 'none';
    }

    static setLoadingState(button, isLoading) {
        if (!button) return;

        const btnText = button.querySelector('.btn-text');
        const btnLoading = button.querySelector('.btn-loading');

        if (isLoading) {
            btnText.style.display = 'none';
            btnLoading.style.display = 'inline-flex';
            button.disabled = true;
            button.classList.add('loading');
        } else {
            btnText.style.display = 'inline';
            btnLoading.style.display = 'none';
            button.disabled = false;
            button.classList.remove('loading');
        }
    }

    static validateForm(formData) {
        const errors = [];
        
        Object.keys(formData).forEach(key => {
            if (!formData[key] || formData[key].trim() === '') {
                errors.push(`${key} es requerido`);
            }
        });

        if (formData.email && !ValidationUtils.validateEmail(formData.email).isValid) {
            errors.push('Email no válido');
        }

        return errors;
    }

    static updateUserUI(user) {
        const userActions = document.querySelector('.user-actions');
        if (userActions) {
            userActions.innerHTML = `
                <span class="user-name">Hola, ${user.name}</span>
                <button onclick="AuthService.logout()" class="logout-btn">Cerrar sesión</button>
            `;
        }
    }

    static createStarRating(rating, maxStars = 5) {
        const stars = [];
        for (let i = 1; i <= maxStars; i++) {
            stars.push(i <= rating ? '★' : '☆');
        }
        return stars.join('');
    }

    static debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    static showToast(message, type = 'info', duration = 3000) {
        // Create toast container if it doesn't exist
        let toastContainer = document.getElementById('toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toast-container';
            toastContainer.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 10000;
                max-width: 350px;
            `;
            document.body.appendChild(toastContainer);
        }

        // Create toast element
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.style.cssText = `
            background: ${type === 'success' ? '#4caf50' : type === 'error' ? '#f44336' : '#2196f3'};
            color: white;
            padding: 12px 20px;
            margin-bottom: 10px;
            border-radius: 4px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            animation: slideIn 0.3s ease;
            cursor: pointer;
        `;
        toast.textContent = message;

        // Add click to dismiss
        toast.addEventListener('click', () => {
            toast.remove();
        });

        // Add to container
        toastContainer.appendChild(toast);

        // Auto remove after duration
        setTimeout(() => {
            if (toast.parentNode) {
                toast.style.animation = 'slideOut 0.3s ease';
                setTimeout(() => toast.remove(), 300);
            }
        }, duration);

        // Add CSS animations if not already added
        if (!document.getElementById('toast-styles')) {
            const style = document.createElement('style');
            style.id = 'toast-styles';
            style.textContent = `
                @keyframes slideIn {
                    from { transform: translateX(100%); opacity: 0; }
                    to { transform: translateX(0); opacity: 1; }
                }
                @keyframes slideOut {
                    from { transform: translateX(0); opacity: 1; }
                    to { transform: translateX(100%); opacity: 0; }
                }
            `;
            document.head.appendChild(style);
        }
    }
}
