class ValidationUtils {
    static emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    static passwordRegex = {
        minLength: /.{8,}/,
        hasLower: /[a-z]/,
        hasUpper: /[A-Z]/,
        hasNumber: /\d/,
        hasSpecial: /[!@#$%^&*(),.?":{}|<>]/
    };

    static validateEmail(email) {
        return {
            isValid: this.emailRegex.test(email),
            message: this.emailRegex.test(email) ? '' : 'Email inválido'
        };
    }

    static validateUsername(username) {
        const isValid = username.length >= 2;
        return {
            isValid,
            message: isValid ? '' : 'El nombre debe tener al menos 2 caracteres'
        };
    }

    static validatePassword(password) {
        const checks = {
            minLength: this.passwordRegex.minLength.test(password),
            hasLower: this.passwordRegex.hasLower.test(password),
            hasUpper: this.passwordRegex.hasUpper.test(password),
            hasNumber: this.passwordRegex.hasNumber.test(password),
            hasSpecial: this.passwordRegex.hasSpecial.test(password)
        };

        const passedChecks = Object.values(checks).filter(Boolean).length;
        const strength = this.getPasswordStrength(passedChecks);
        
        return {
            isValid: passedChecks >= 3,
            strength,
            checks,
            message: this.getPasswordMessage(checks)
        };
    }

    static validatePasswordConfirmation(password, confirmPassword) {
        const isValid = password === confirmPassword && password.length > 0;
        return {
            isValid,
            message: isValid ? '' : 'Las contraseñas no coinciden'
        };
    }

    static getPasswordStrength(passedChecks) {
        if (passedChecks <= 1) return { level: 'weak', text: 'Muy débil', color: '#ff4444' };
        if (passedChecks <= 2) return { level: 'fair', text: 'Débil', color: '#ff8800' };
        if (passedChecks <= 3) return { level: 'good', text: 'Buena', color: '#ffaa00' };
        if (passedChecks <= 4) return { level: 'strong', text: 'Fuerte', color: '#88cc00' };
        return { level: 'very-strong', text: 'Muy fuerte', color: '#00cc44' };
    }

    static getPasswordMessage(checks) {
        const missing = [];
        if (!checks.minLength) missing.push('8 caracteres mínimo');
        if (!checks.hasLower) missing.push('una minúscula');
        if (!checks.hasUpper) missing.push('una mayúscula');
        if (!checks.hasNumber) missing.push('un número');
        if (!checks.hasSpecial) missing.push('un carácter especial');
        
        return missing.length > 0 ? `Falta: ${missing.join(', ')}` : '';
    }

    static showValidationState(inputId, iconId, errorId, validation) {
        const input = document.getElementById(inputId);
        const icon = document.getElementById(iconId);
        const error = document.getElementById(errorId);

        if (!input || !icon || !error) return;

        // Reset classes
        input.classList.remove('valid', 'invalid');
        icon.className = 'validation-icon';

        if (input.value.length === 0) {
            error.textContent = '';
            return;
        }

        if (validation.isValid) {
            input.classList.add('valid');
            icon.classList.add('valid');
            icon.innerHTML = '<i class="fas fa-check"></i>';
            error.textContent = '';
        } else {
            input.classList.add('invalid');
            icon.classList.add('invalid');
            icon.innerHTML = '<i class="fas fa-times"></i>';
            error.textContent = validation.message;
        }
    }

    static updatePasswordStrength(password) {
        const validation = this.validatePassword(password);
        const strengthMeter = document.getElementById('strengthFill');
        const strengthText = document.getElementById('strengthText');

        if (!strengthMeter || !strengthText) return validation;

        const percentage = (Object.values(validation.checks).filter(Boolean).length / 5) * 100;
        
        strengthMeter.style.width = `${percentage}%`;
        strengthMeter.style.backgroundColor = validation.strength.color;
        strengthText.textContent = validation.strength.text;
        strengthText.style.color = validation.strength.color;

        return validation;
    }
}
