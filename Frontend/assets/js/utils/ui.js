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

    static validateEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    static validateForm(formData) {
        const errors = [];
        
        Object.keys(formData).forEach(key => {
            if (!formData[key] || formData[key].trim() === '') {
                errors.push(`${key} es requerido`);
            }
        });

        if (formData.email && !this.validateEmail(formData.email)) {
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
}
