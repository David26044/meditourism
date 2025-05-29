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
        
        if (isLoading) {
            button.disabled = true;
            button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Cargando...';
        } else {
            button.disabled = false;
            // Restore original content
            const originalText = button.getAttribute('data-original-text');
            if (originalText) {
                button.innerHTML = originalText;
            }
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
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <div class="toast-content">
                <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-circle' : 'info-circle'}"></i>
                <span>${message}</span>
            </div>
        `;

        document.body.appendChild(toast);
        
        // Show toast
        setTimeout(() => toast.classList.add('show'), 100);
        
        // Hide and remove toast
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                if (toast.parentNode) {
                    toast.parentNode.removeChild(toast);
                }
            }, 300);
        }, duration);
    }
}
