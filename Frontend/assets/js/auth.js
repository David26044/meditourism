document.addEventListener('DOMContentLoaded', function() {
    // Elementos del DOM
    const container = document.getElementById('container');
    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const forgotPasswordModal = document.getElementById('forgotPasswordModal');
    const forgotPasswordLink = document.getElementById('forgotPasswordLink');
    const closeForgotModal = document.getElementById('closeForgotModal');
    const forgotPasswordForm = document.getElementById('forgotPasswordForm');

    // Variables globales
    let cachedUser = null;

    // Form toggle functionality
    signUpButton.addEventListener('click', () => {
        container.classList.add("right-panel-active");
        clearAllMessages();
    });

    signInButton.addEventListener('click', () => {
        container.classList.remove("right-panel-active");
        clearAllMessages();
    });

    // Forgot password modal functionality
    forgotPasswordLink.addEventListener('click', (e) => {
        e.preventDefault();
        forgotPasswordModal.style.display = 'block';
        clearAllMessages();
    });

    closeForgotModal.addEventListener('click', () => {
        forgotPasswordModal.style.display = 'none';
        clearAllMessages();
    });

    window.addEventListener('click', (e) => {
        if (e.target === forgotPasswordModal) {
            forgotPasswordModal.style.display = 'none';
            clearAllMessages();
        }
    });

    // Real-time validation setup
    setupRealTimeValidation();

    // Form submissions
    loginForm.addEventListener('submit', handleLogin);
    registerForm.addEventListener('submit', handleRegister);
    forgotPasswordForm.addEventListener('submit', handleForgotPassword);

    // Check for remembered user
    checkRememberedUser();

    // Check user session
    checkUserSession();

    // Clear all messages function
    function clearAllMessages() {
        const messages = document.querySelectorAll('.message');
        messages.forEach(msg => {
            msg.style.display = 'none';
            msg.textContent = '';
        });
    }

    // Setup real-time validation
    function setupRealTimeValidation() {
        // Login form validation
        const loginEmail = document.getElementById('loginEmail');
        const loginPassword = document.getElementById('loginPassword');

        loginEmail.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.validateEmail(loginEmail.value);
            ValidationUtils.showValidationState('loginEmail', 'loginEmailIcon', 'loginEmailError', validation);
        }, 300));

        loginPassword.addEventListener('input', UIUtils.debounce(() => {
            const validation = { 
                isValid: loginPassword.value.length >= 6, 
                message: loginPassword.value.length < 6 ? 'Mínimo 6 caracteres' : '' 
            };
            ValidationUtils.showValidationState('loginPassword', 'loginPasswordIcon', 'loginPasswordError', validation);
        }, 300));

        // Register form validation
        const registerUsername = document.getElementById('registerUsername');
        const registerEmail = document.getElementById('registerEmail');
        const registerPassword = document.getElementById('registerPassword');
        const confirmPassword = document.getElementById('confirmPassword');

        registerUsername.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.validateUsername(registerUsername.value);
            ValidationUtils.showValidationState('registerUsername', 'usernameIcon', 'usernameError', validation);
        }, 300));

        registerEmail.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.validateEmail(registerEmail.value);
            ValidationUtils.showValidationState('registerEmail', 'emailIcon', 'emailError', validation);
        }, 300));

        registerPassword.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.updatePasswordStrength(registerPassword.value);
            ValidationUtils.showValidationState('registerPassword', 'passwordIcon', 'passwordError', validation);
            
            // Revalidate confirmation if it has content
            if (confirmPassword.value) {
                const confirmValidation = ValidationUtils.validatePasswordConfirmation(registerPassword.value, confirmPassword.value);
                ValidationUtils.showValidationState('confirmPassword', 'confirmPasswordIcon', 'confirmPasswordError', confirmValidation);
            }
        }, 300));

        confirmPassword.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.validatePasswordConfirmation(registerPassword.value, confirmPassword.value);
            ValidationUtils.showValidationState('confirmPassword', 'confirmPasswordIcon', 'confirmPasswordError', validation);
        }, 300));

        // Forgot password validation
        const forgotEmail = document.getElementById('forgotEmail');
        forgotEmail.addEventListener('input', UIUtils.debounce(() => {
            const validation = ValidationUtils.validateEmail(forgotEmail.value);
            ValidationUtils.showValidationState('forgotEmail', 'forgotEmailIcon', 'forgotEmailError', validation);
        }, 300));
    }

    // Handle login
    async function handleLogin(e) {
        e.preventDefault();
        
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;
        const rememberMe = document.getElementById('rememberMe').checked;
        const loginBtn = document.getElementById('loginBtn');
        const loginMessage = document.getElementById('loginMessage');

        // Validate inputs
        if (!ValidationUtils.validateEmail(email).isValid) {
            UIUtils.showMessage(loginMessage, 'Por favor ingresa un email válido', 'error');
            return;
        }

        if (password.length < 6) {
            UIUtils.showMessage(loginMessage, 'La contraseña debe tener al menos 6 caracteres', 'error');
            return;
        }

        try {
            UIUtils.setLoadingState(loginBtn, true);
            
            const response = await AuthService.login(email, password);
            
            if (response.success) {
                // Handle remember me
                if (rememberMe) {
                    localStorage.setItem('rememberedEmail', email);
                    localStorage.setItem('rememberUser', 'true');
                } else {
                    localStorage.removeItem('rememberedEmail');
                    localStorage.removeItem('rememberUser');
                }

                UIUtils.showMessage(loginMessage, response.message, 'success');
                
                // Redirect after delay
                setTimeout(() => {
                    window.location.href = 'home.html'; // Mismo nombre que en version_vieja
                }, 1500);
            } else {
                UIUtils.showMessage(loginMessage, response.message, 'error');
            }
        } catch (error) {
            console.error('Error en login:', error);
            UIUtils.showMessage(loginMessage, 'Error de conexión. Intenta nuevamente.', 'error');
        } finally {
            UIUtils.setLoadingState(loginBtn, false);
        }
    }

    // Handle register
    async function handleRegister(e) {
        e.preventDefault();
        
        const username = document.getElementById('registerUsername').value;
        const email = document.getElementById('registerEmail').value;
        const password = document.getElementById('registerPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const registerBtn = document.getElementById('registerBtn');
        const registerMessage = document.getElementById('registerMessage');

        // Validate all inputs
        const validations = {
            username: ValidationUtils.validateUsername(username),
            email: ValidationUtils.validateEmail(email),
            password: ValidationUtils.validatePassword(password),
            confirmPassword: ValidationUtils.validatePasswordConfirmation(password, confirmPassword)
        };

        const hasErrors = Object.values(validations).some(v => !v.isValid);
        
        if (hasErrors) {
            UIUtils.showMessage(registerMessage, 'Por favor corrige los errores en el formulario', 'error');
            return;
        }

        try {
            UIUtils.setLoadingState(registerBtn, true);
            
            const response = await AuthService.register(username, email, password);
            
            if (response.success) {
                UIUtils.showMessage(registerMessage, response.message, 'success');
                
                // Switch to login form after delay
                setTimeout(() => {
                    container.classList.remove("right-panel-active");
                    document.getElementById('loginEmail').value = email;
                    registerForm.reset();
                    clearPasswordStrength();
                }, 3000);
            } else {
                UIUtils.showMessage(registerMessage, response.message, 'error');
            }
        } catch (error) {
            console.error('Error en registro:', error);
            UIUtils.showMessage(registerMessage, 'Error de conexión. Intenta nuevamente.', 'error');
        } finally {
            UIUtils.setLoadingState(registerBtn, false);
        }
    }

    // Handle forgot password
    async function handleForgotPassword(e) {
        e.preventDefault();
        
        const email = document.getElementById('forgotEmail').value;
        const forgotBtn = document.getElementById('forgotBtn');
        const forgotMessage = document.getElementById('forgotMessage');

        if (!ValidationUtils.validateEmail(email).isValid) {
            UIUtils.showMessage(forgotMessage, 'Por favor ingresa un email válido', 'error');
            return;
        }

        try {
            UIUtils.setLoadingState(forgotBtn, true);
            
            const response = await AuthService.forgotPassword(email);
            
            if (response.success) {
                UIUtils.showMessage(forgotMessage, response.message, 'success');
                
                setTimeout(() => {
                    forgotPasswordModal.style.display = 'none';
                    document.getElementById('forgotEmail').value = '';
                }, 3000);
            } else {
                UIUtils.showMessage(forgotMessage, response.message, 'error');
            }
        } catch (error) {
            console.error('Error en forgot password:', error);
            UIUtils.showMessage(forgotMessage, 'Error de conexión. Intenta nuevamente.', 'error');
        } finally {
            UIUtils.setLoadingState(forgotBtn, false);
        }
    }

    // Check remembered user
    function checkRememberedUser() {
        const rememberUser = localStorage.getItem('rememberUser');
        const rememberedEmail = localStorage.getItem('rememberedEmail');
        
        if (rememberUser === 'true' && rememberedEmail) {
            document.getElementById('loginEmail').value = rememberedEmail;
            document.getElementById('rememberMe').checked = true;
        }
    }

    // Check user session
    function checkUserSession() {
        if (AuthService.isAuthenticated()) {
            window.location.href = 'home.html';
        }
    }

    // Clear password strength meter
    function clearPasswordStrength() {
        const strengthMeter = document.getElementById('strengthFill');
        const strengthText = document.getElementById('strengthText');
        
        if (strengthMeter && strengthText) {
            strengthMeter.style.width = '0%';
            strengthText.textContent = 'Ingresa una contraseña';
            strengthText.style.color = '#666';
        }
    }
});

// Función global para toggle de contraseña (fuera del DOMContentLoaded)
function togglePassword(inputId, toggleButton) {
    const input = document.getElementById(inputId);
    const icon = toggleButton.querySelector('i');
    
    if (!input || !icon) return;
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
        toggleButton.title = 'Ocultar contraseña';
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
        toggleButton.title = 'Mostrar contraseña';
    }
}
