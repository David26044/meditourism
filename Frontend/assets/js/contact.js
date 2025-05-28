// Contact Form Logic
document.addEventListener('DOMContentLoaded', function() {
    initializeContactForm();
});

async function initializeContactForm() {
    // Verificar autenticación
    if (!AuthService.isAuthenticated()) {
        showMessage('Debes iniciar sesión para enviar un formulario de contacto', 'error');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        return;
    }

    // Cargar tratamientos disponibles
    await loadTreatments();
    
    // Configurar formulario
    setupContactFormHandler();
    
    // Setup phone validation
    const phoneField = document.getElementById('phone');
    if (phoneField) {
        phoneField.addEventListener('blur', () => {
            const validation = ValidationUtils.validatePhone(phoneField.value);
            if (phoneField.value && !validation.isValid) {
                showFieldError('phone', validation.message);
            } else {
                clearFieldError('phone');
            }
        });
    }
    
    // Pre-llenar datos del usuario si están disponibles
    await prefillUserData();
}

async function loadTreatments() {
    try {
        showLoading(true);
        const result = await TreatmentService.getAllTreatments();
        
        if (result.success && result.data) {
            populateTreatmentSelect(result.data);
        } else {
            console.error('Error al cargar tratamientos:', result.message);
            addDefaultTreatmentOptions();
        }
    } catch (error) {
        console.error('Error cargando tratamientos:', error);
        addDefaultTreatmentOptions();
    } finally {
        showLoading(false);
    }
}

function populateTreatmentSelect(treatments) {
    const select = document.getElementById('treatmentId');
    if (!select) return;

    // Limpiar opciones existentes excepto la primera
    select.innerHTML = '<option value="">Seleccione un tratamiento</option>';
    
    treatments.forEach(treatment => {
        const option = document.createElement('option');
        option.value = treatment.id;
        option.textContent = treatment.name;
        option.dataset.description = treatment.description || '';
        select.appendChild(option);
    });
}

function addDefaultTreatmentOptions() {
    const select = document.getElementById('treatmentId');
    if (!select) return;

    const defaultOptions = [
        { value: '', text: 'Seleccione un tratamiento' },
        { value: '1', text: 'Operatoria Dental' },
        { value: '2', text: 'Endodoncia' },
        { value: '3', text: 'Implantología' },
        { value: '4', text: 'Estética Dental' },
        { value: '5', text: 'Periodoncia' },
        { value: '6', text: 'Cirugía Oral' },
        { value: '7', text: 'Rehabilitación Oral' },
        { value: '8', text: 'Consulta General' }
    ];

    select.innerHTML = '';
    defaultOptions.forEach(opt => {
        const option = document.createElement('option');
        option.value = opt.value;
        option.textContent = opt.text;
        select.appendChild(option);
    });
}

async function prefillUserData() {
    try {
        const user = AuthService.getCurrentUser();
        if (user) {
            const emailField = document.getElementById('email');
            const fullNameField = document.getElementById('fullName');
            
            if (emailField && user.email) {
                emailField.value = user.email;
                // Add visual indicator if email is not verified
                if (!user.verified) {
                    emailField.title = 'Email no verificado - Verifica tu email desde tu perfil';
                }
            }
            
            if (fullNameField && user.name) {
                fullNameField.value = user.name;
            }
        }
    } catch (error) {
        console.error('Error al pre-llenar datos del usuario:', error);
    }
}

function setupContactFormHandler() {
    const form = document.getElementById('contactForm');
    if (!form) {
        console.error('❌ Formulario de contacto no encontrado');
        return;
    }
    
    console.log('✅ Configurando manejador del formulario de contacto');

    form.addEventListener('submit', handleContactFormSubmit);
    
    // Contador de caracteres para el mensaje
    const messageField = document.getElementById('message');
    const charCountElement = document.getElementById('charCount');
    
    if (messageField && charCountElement) {
        messageField.addEventListener('input', () => {
            const count = messageField.value.length;
            charCountElement.textContent = count;
            
            if (count > 500) {
                charCountElement.style.color = 'red';
            } else if (count > 450) {
                charCountElement.style.color = 'orange';
            } else {
                charCountElement.style.color = '';
            }
        });
    }
    
    // Validación en tiempo real
    const fields = ['fullName', 'email', 'treatmentId', 'inquiryType', 'message'];
    fields.forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('blur', () => {
                console.log(`Campo ${fieldId} perdió el foco, validando...`);
                validateField(fieldId);
            });
            field.addEventListener('input', () => clearFieldError(fieldId));
        }
    });
    
    console.log('✅ Manejador del formulario configurado correctamente');
}

async function handleContactFormSubmit(e) {
    e.preventDefault();
    
    console.log('=== INICIO DEL ENVÍO DEL FORMULARIO ===');
    
    if (!validateForm()) {
        console.log('❌ Validación del formulario falló');
        return;
    }

    const submitBtn = document.getElementById('submitBtn');
    const originalText = submitBtn.innerHTML;
    
    try {
        // Deshabilitar botón y mostrar loading
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
        
        // Validate user can perform this action (check if blocked)
        if (UserService.isAuthenticated()) {
            await UserService.validateUserCanPerformAction('enviar consulta');
        }

        // Obtener datos del formulario
        const formData = getFormData();
        console.log('📝 Datos del formulario obtenidos:', formData);
        
        // Validar que todos los campos requeridos estén presentes
        if (!formData.userId || !formData.treatmentId || !formData.fullName || !formData.email || !formData.message || !formData.inquiryType) {
            throw new Error('Faltan campos requeridos en el formulario');
        }
        
        // Enviar formulario al backend
        console.log('🚀 Enviando formulario al backend...');
        const result = await ContactService.submitContactForm(formData);
        
        console.log('📨 Respuesta del backend:', result);
        
        if (result.success) {
            console.log('✅ Formulario enviado exitosamente');
            
            // Enviar email de notificación en segundo plano
            console.log('📧 Enviando email de notificación...');
            try {
                await EmailService.sendContactEmail({
                    email: formData.email,
                    message: formData.message,
                    treatmentName: getTreatmentName(formData.treatmentId),
                    userName: formData.fullName
                });
            } catch (emailError) {
                console.warn('Error enviando email, pero formulario fue guardado:', emailError);
            }
            
            showMessage('Formulario enviado exitosamente. Te contactaremos pronto.', 'success');
            resetForm();
        } else {
            console.log('❌ Error en la respuesta del backend:', result);
            showMessage(result.message || 'Error al enviar el formulario', 'error');
        }
    } catch (error) {
        console.error('💥 Error al enviar formulario:', error);
        console.error('Stack trace:', error.stack);
        
        let errorMessage = 'Error de conexión. Intenta nuevamente.';
        if (error.message.includes('campos requeridos')) {
            errorMessage = 'Por favor, completa todos los campos requeridos.';
        } else if (error.message.includes('suspendida')) {
            errorMessage = 'Tu cuenta ha sido suspendida. No puedes enviar consultas en este momento.';
        }
        
        showMessage(errorMessage, 'error');
    } finally {
        // Restaurar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
        console.log('=== FIN DEL ENVÍO DEL FORMULARIO ===');
    }
}

function getFormData() {
    console.log('📋 Obteniendo datos del formulario...');
    
    const user = AuthService.getCurrentUser();
    console.log('👤 Usuario actual:', user);
    
    if (!user || !user.id) {
        throw new Error('Usuario no válido o sin ID');
    }
    
    const formData = {
        userId: user.id,
        treatmentId: parseInt(document.getElementById('treatmentId')?.value) || null,
        fullName: document.getElementById('fullName')?.value?.trim() || '',
        email: document.getElementById('email')?.value?.trim() || '',
        phone: document.getElementById('phone')?.value?.trim() || null,
        inquiryType: document.getElementById('inquiryType')?.value?.trim() || '',
        preferredClinic: document.getElementById('preferredClinic')?.value?.trim() || null,
        message: document.getElementById('message')?.value?.trim() || '',
        acceptTerms: document.getElementById('acceptTerms')?.checked || false,
        acceptMarketing: document.getElementById('acceptMarketing')?.checked || false
    };
    
    console.log('📦 Objeto formData final:', formData);
    return formData;
}

function getTreatmentName(treatmentId) {
    const select = document.getElementById('treatmentId');
    const option = select.querySelector(`option[value="${treatmentId}"]`);
    return option ? option.textContent : 'Tratamiento seleccionado';
}

function validateForm() {
    console.log('🔍 Iniciando validación del formulario...');
    
    const fields = ['fullName', 'email', 'treatmentId', 'inquiryType', 'message', 'acceptTerms'];
    let isValid = true;
    
    fields.forEach(fieldId => {
        console.log(`Validando campo: ${fieldId}`);
        const fieldValid = validateField(fieldId);
        console.log(`Campo ${fieldId} válido: ${fieldValid}`);
        if (!fieldValid) {
            isValid = false;
        }
    });
    
    console.log(`✅ Resultado de la validación: ${isValid}`);
    return isValid;
}

function validateField(fieldId) {
    const field = document.getElementById(fieldId);
    if (!field) {
        console.warn(`⚠️ Campo ${fieldId} no encontrado en el DOM`);
        return false;
    }
    
    let value;
    let isValid = true;
    let errorMessage = '';
    
    // Manejar diferentes tipos de campos
    if (field.type === 'checkbox') {
        value = field.checked;
    } else {
        value = field.value.trim();
    }
    
    console.log(`🔍 Validando ${fieldId} con valor:`, value);
    
    switch (fieldId) {
        case 'fullName':
            if (!value) {
                errorMessage = 'El nombre completo es requerido';
                isValid = false;
            } else if (value.length < 2) {
                errorMessage = 'El nombre debe tener al menos 2 caracteres';
                isValid = false;
            }
            break;
            
        case 'email':
            if (!value) {
                errorMessage = 'El email es requerido';
                isValid = false;
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                errorMessage = 'Ingresa un email válido';
                isValid = false;
            }
            break;
            
        case 'treatmentId':
            if (!value) {
                errorMessage = 'Selecciona un tratamiento';
                isValid = false;
            }
            break;
            
        case 'inquiryType':
            if (!value) {
                errorMessage = 'Selecciona el tipo de consulta';
                isValid = false;
            }
            break;
            
        case 'message':
            if (!value) {
                errorMessage = 'El mensaje es requerido';
                isValid = false;
            } else if (value.length < 10) {
                errorMessage = 'El mensaje debe tener al menos 10 caracteres';
                isValid = false;
            } else if (value.length > 500) {
                errorMessage = 'El mensaje no puede exceder 500 caracteres';
                isValid = false;
            }
            break;
            
        case 'acceptTerms':
            if (!value) {
                errorMessage = 'Debes aceptar los términos y condiciones';
                isValid = false;
            }
            break;
    }
    
    console.log(`Campo ${fieldId} - Válido: ${isValid}, Error: ${errorMessage}`);
    showFieldError(fieldId, errorMessage);
    return isValid;
}

function showFieldError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`${fieldId}Error`);
    
    if (message) {
        field.classList.add('error');
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }
    } else {
        field.classList.remove('error');
        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }
}

function clearFieldError(fieldId) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`${fieldId}Error`);
    
    field.classList.remove('error');
    if (errorElement) {
        errorElement.style.display = 'none';
    }
}

function resetForm() {
    const form = document.getElementById('contactForm');
    if (form) {
        form.reset();
        // Limpiar errores
        const errorElements = form.querySelectorAll('.error-message');
        errorElements.forEach(el => el.style.display = 'none');
        
        const errorFields = form.querySelectorAll('.error');
        errorFields.forEach(field => field.classList.remove('error'));
        
        // Re-llenar email del usuario
        prefillUserData();
    }
}

function showMessage(message, type) {
    const messageElement = document.getElementById('formMessage');
    if (messageElement) {
        messageElement.textContent = message;
        messageElement.className = `form-message ${type}`;
        messageElement.style.display = 'block';
        
        // Auto-ocultar después de 5 segundos para mensajes de éxito
        if (type === 'success') {
            setTimeout(() => {
                messageElement.style.display = 'none';
            }, 5000);
        }
    }
}

function showLoading(show) {
    const loadingElement = document.getElementById('loadingOverlay');
    if (loadingElement) {
        loadingElement.style.display = show ? 'flex' : 'none';
    }
}
