document.addEventListener('DOMContentLoaded', function() {
    // Obtener token de la URL
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    
    // Elementos del DOM
    const resetPasswordForm = document.getElementById('resetPasswordForm');
    const newPassword = document.getElementById('newPassword');
    const confirmNewPassword = document.getElementById('confirmNewPassword');
    const resetBtn = document.getElementById('resetBtn');
    const resetMessage = document.getElementById('resetMessage');

    // Verificar si hay token
    if (!token) {
        UIUtils.showMessage(resetMessage, 'Token inválido o expirado. Por favor solicita un nuevo enlace de recuperación.', 'error');
        resetBtn.disabled = true;
        return;
    }

    // Setup validación en tiempo real
    setupRealTimeValidation();

    // Manejar envío del formulario
    resetPasswordForm.addEventListener('submit', handleResetPassword);

    function setupRealTimeValidation() {
        newPassword.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.updatePasswordStrength(newPassword.value);
            ValidationUtils.showValidationState('newPassword', 'newPasswordIcon', 'newPasswordError', validation);
            
            // Revalidar confirmación si tiene contenido
            if (confirmNewPassword.value) {
                const confirmValidation = ValidationUtils.validatePasswordConfirmation(newPassword.value, confirmNewPassword.value);
                ValidationUtils.showValidationState('confirmNewPassword', 'confirmNewPasswordIcon', 'confirmNewPasswordError', confirmValidation);
            }
        }, 300));

        confirmNewPassword.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.validatePasswordConfirmation(newPassword.value, confirmNewPassword.value);
            ValidationUtils.showValidationState('confirmNewPassword', 'confirmNewPasswordIcon', 'confirmNewPasswordError', validation);
        }, 300));
    }

    async function handleResetPassword(e) {
        e.preventDefault();
        
        const newPasswordValue = newPassword.value;
        const confirmPasswordValue = confirmNewPassword.value;

        // Validar contraseñas
        const passwordValidation = ValidationUtils.validatePassword(newPasswordValue);
        const confirmValidation = ValidationUtils.validatePasswordConfirmation(newPasswordValue, confirmPasswordValue);

        if (!passwordValidation.isValid) {
            UIUtils.showMessage(resetMessage, 'La contraseña no cumple con los requisitos mínimos', 'error');
            return;
        }

        if (!confirmValidation.isValid) {
            UIUtils.showMessage(resetMessage, 'Las contraseñas no coinciden', 'error');
            return;
        }

        try {
            UIUtils.setLoadingState(resetBtn, true);
            
            const response = await AuthService.resetPassword(token, newPasswordValue);
            
            if (response.success) {
                UIUtils.showMessage(resetMessage, 'Contraseña cambiada exitosamente. Redirigiendo al login...', 'success');
                
                // Limpiar formulario
                resetPasswordForm.reset();
                clearPasswordStrength();
                
                // Redirigir al login después de 3 segundos
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 3000);
            } else {
                UIUtils.showMessage(resetMessage, response.message || 'Error al cambiar la contraseña', 'error');
            }
        } catch (error) {
            console.error('Error en reset password:', error);
            UIUtils.showMessage(resetMessage, 'Error de conexión. Intenta nuevamente.', 'error');
        } finally {
            UIUtils.setLoadingState(resetBtn, false);
        }
    }

    function clearPasswordStrength() {
        const strengthMeter = document.getElementById('strengthFill');
        const strengthText = document.getElementById('strengthText');
        
        if (strengthMeter && strengthText) {
            strengthMeter.style.width = '0%';
            strengthText.textContent = 'Ingresa una contraseña';
            strengthText.style.color = '#666';
        }
    }

    // Verificar validez del token al cargar
    verifyToken();

    async function verifyToken() {
        try {
            // Intentar decodificar el token para verificar si es válido
            const payload = parseJwt(token);
            const currentTime = Math.floor(Date.now() / 1000);
            
            if (payload.exp < currentTime) {
                UIUtils.showMessage(resetMessage, 'El enlace ha expirado. Por favor solicita un nuevo enlace de recuperación.', 'error');
                resetBtn.disabled = true;
            }
        } catch (error) {
            UIUtils.showMessage(resetMessage, 'Token inválido. Por favor solicita un nuevo enlace de recuperación.', 'error');
            resetBtn.disabled = true;
        }
    }

    // Función helper para decodificar JWT
    function parseJwt(token) {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            return JSON.parse(jsonPayload);
        } catch (error) {
            throw new Error('Invalid token');
        }
    }
});
