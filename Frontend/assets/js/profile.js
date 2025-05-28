// Profile Page Logic
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Profile page loaded');
    initializeProfilePage();
});

let currentUser = null;
let isEditing = false;

async function initializeProfilePage() {
    console.log('🎯 Initializing profile page...');
    
    // Check authentication
    if (!AuthService.isAuthenticated()) {
        console.log('❌ User not authenticated, redirecting...');
        window.location.href = 'login.html';
        return;
    }

    try {
        showLoading(true);
        
        // Load current user data
        await loadCurrentUser();
        
        // Setup event listeners
        setupEventListeners();
        
        // Load user's contact forms
        await loadUserContactForms();
        
        // Show admin features if user is admin
        if (UserService.isAdmin()) {
            showAdminFeatures();
            await loadAdminStats();
        }
        
        console.log('✅ Profile page initialized successfully');
    } catch (error) {
        console.error('💥 Error initializing profile page:', error);
        showMessage('Error al cargar el perfil', 'error');
    } finally {
        showLoading(false);
    }
}

async function loadCurrentUser() {
    console.log('👤 Loading current user...');
    
    try {
        currentUser = await UserService.refreshUserInfo();
        if (currentUser) {
            displayUserInfo(currentUser);
            updateUserInterface();
        } else {
            throw new Error('No se pudo obtener la información del usuario');
        }
    } catch (error) {
        console.error('Error loading user:', error);
        throw error;
    }
}

function displayUserInfo(user) {
    console.log('🎨 Displaying user info:', user);
    
    // Update display elements
    document.getElementById('displayName').textContent = user.name || '-';
    document.getElementById('displayEmail').textContent = user.email || '-';
    
    // Verification status
    const verifiedElement = document.getElementById('displayVerified');
    if (user.verified) {
        verifiedElement.innerHTML = '<span class="verified-badge"><i class="fas fa-check"></i> Verificado</span>';
    } else {
        verifiedElement.innerHTML = '<span class="unverified-badge"><i class="fas fa-clock"></i> Pendiente</span>';
        document.getElementById('resendVerificationBtn').style.display = 'block';
        document.getElementById('verificationStatus').textContent = 'Tu email aún no está verificado';
    }
    
    // Role display
    const roleElement = document.getElementById('displayRole');
    const profileRoleBadge = document.getElementById('profileRoleBadge');
    const avatarLarge = document.getElementById('profileAvatarLarge');
    
    if (user.role && user.role.name === 'ADMIN') {
        roleElement.innerHTML = '<span class="admin-badge"><i class="fas fa-crown"></i> Administrador</span>';
        profileRoleBadge.textContent = 'ADMIN';
        profileRoleBadge.classList.add('show');
        avatarLarge.classList.add('admin');
    } else {
        roleElement.innerHTML = '<span class="user-badge"><i class="fas fa-user"></i> Usuario</span>';
        profileRoleBadge.classList.remove('show');
        avatarLarge.classList.remove('admin');
    }
    
    // Member since (using created date or current date as fallback)
    const memberSince = user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'Información no disponible';
    document.getElementById('displayMemberSince').textContent = memberSince;
    
    // Update edit form
    document.getElementById('editName').value = user.name || '';
    document.getElementById('editEmail').value = user.email || '';
}

function updateUserInterface() {
    // Update navbar user info
    if (typeof UserService !== 'undefined') {
        UserService.updateUserDisplay();
        UserService.updateUserAvatar();
    }
}

function setupEventListeners() {
    console.log('🔧 Setting up event listeners...');
    
    // Tab switching
    setupTabSwitching();
    
    // Profile editing
    setupProfileEditing();
    
    // Security actions
    setupSecurityActions();
    
    // Admin actions
    setupAdminActions();
    
    // Logout
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
}

function setupTabSwitching() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetTab = button.getAttribute('data-tab');
            
            // Remove active class from all buttons and contents
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));
            
            // Add active class to clicked button and corresponding content
            button.classList.add('active');
            document.getElementById(`${targetTab}-tab`).classList.add('active');
            
            console.log(`📑 Switched to tab: ${targetTab}`);
        });
    });
}

function setupProfileEditing() {
    const editBtn = document.getElementById('editInfoBtn');
    const cancelBtn = document.getElementById('cancelEditBtn');
    const saveBtn = document.getElementById('saveInfoBtn');
    const updateForm = document.getElementById('updateProfileForm');
    
    editBtn.addEventListener('click', enterEditMode);
    cancelBtn.addEventListener('click', exitEditMode);
    updateForm.addEventListener('submit', handleProfileUpdate);
}

function enterEditMode() {
    console.log('✏️ Entering edit mode...');
    
    isEditing = true;
    document.getElementById('infoView').style.display = 'none';
    document.getElementById('infoEdit').style.display = 'block';
    document.getElementById('editInfoBtn').style.display = 'none';
}

function exitEditMode() {
    console.log('❌ Exiting edit mode...');
    
    isEditing = false;
    document.getElementById('infoView').style.display = 'block';
    document.getElementById('infoEdit').style.display = 'none';
    document.getElementById('editInfoBtn').style.display = 'block';
    
    // Reset form to original values
    if (currentUser) {
        document.getElementById('editName').value = currentUser.name || '';
        document.getElementById('editEmail').value = currentUser.email || '';
    }
    
    // Clear errors
    clearFormErrors();
}

async function handleProfileUpdate(e) {
    e.preventDefault();
    
    console.log('💾 Updating profile...');
    
    const formData = {
        name: document.getElementById('editName').value.trim(),
        email: document.getElementById('editEmail').value.trim()
    };
    
    // Validate form
    if (!validateProfileForm(formData)) {
        return;
    }
    
    try {
        showLoading(true);
        
        // Update user via API
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${currentUser.id}`, {
            method: 'PATCH',
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            const updatedUser = await response.json();
            currentUser = updatedUser;
            
            // Update localStorage
            localStorage.setItem('user', JSON.stringify(updatedUser));
            AuthService.cachedUser = updatedUser;
            
            // Update display
            displayUserInfo(updatedUser);
            updateUserInterface();
            
            // Exit edit mode
            exitEditMode();
            
            showMessage('Perfil actualizado exitosamente', 'success');
            console.log('✅ Profile updated successfully');
        } else {
            const errorData = await response.json();
            showMessage(errorData.message || 'Error al actualizar el perfil', 'error');
        }
    } catch (error) {
        console.error('Error updating profile:', error);
        showMessage('Error de conexión al actualizar el perfil', 'error');
    } finally {
        showLoading(false);
    }
}

function validateProfileForm(data) {
    let isValid = true;
    
    // Clear previous errors
    clearFormErrors();
    
    // Validate name
    if (!data.name || data.name.length < 2) {
        showFieldError('nameError', 'El nombre debe tener al menos 2 caracteres');
        isValid = false;
    }
    
    // Validate email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!data.email || !emailRegex.test(data.email)) {
        showFieldError('emailError', 'Ingresa un email válido');
        isValid = false;
    }
    
    return isValid;
}

function clearFormErrors() {
    const errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(element => {
        element.style.display = 'none';
        element.textContent = '';
    });
}

function showFieldError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}

async function loadUserContactForms() {
    console.log('📧 Loading user contact forms...');
    
    if (!currentUser || !currentUser.id) {
        console.warn('No current user ID available');
        return;
    }
    
    try {
        const result = await ContactService.getUserContactForms(currentUser.id);
        
        if (result.success && result.data) {
            displayContactForms(result.data);
        } else {
            console.error('Error loading contact forms:', result.message);
            document.getElementById('noContactForms').style.display = 'block';
        }
    } catch (error) {
        console.error('Error loading contact forms:', error);
        document.getElementById('noContactForms').style.display = 'block';
    } finally {
        document.getElementById('contactFormsLoading').style.display = 'none';
    }
}

function displayContactForms(forms) {
    console.log('📋 Displaying contact forms:', forms);
    
    const container = document.getElementById('contactFormsList');
    
    if (!forms || forms.length === 0) {
        document.getElementById('noContactForms').style.display = 'block';
        return;
    }
    
    container.innerHTML = forms.map(form => `
        <div class="contact-form-item">
            <div class="contact-form-header">
                <div>
                    <div class="contact-form-title">
                        ${form.treatmentName || 'Consulta General'} - ${form.inquiryType || 'Información'}
                    </div>
                    <div class="contact-form-date">
                        ${formatDate(form.submittedAt)}
                    </div>
                </div>
                <div class="contact-form-status ${getStatusClass(form.status)}">
                    ${getStatusText(form.status)}
                </div>
            </div>
            <div class="contact-form-content">
                ${form.message || 'Sin mensaje'}
            </div>
            <div class="contact-form-meta">
                <span><i class="fas fa-envelope"></i> ${form.email}</span>
                ${form.phone ? `<span><i class="fas fa-phone"></i> ${form.phone}</span>` : ''}
                ${form.preferredClinic ? `<span><i class="fas fa-map-marker-alt"></i> ${form.preferredClinic}</span>` : ''}
            </div>
        </div>
    `).join('');
}

function formatDate(dateString) {
    if (!dateString) return 'Fecha no disponible';
    return new Date(dateString).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function getStatusClass(status) {
    switch(status?.toLowerCase()) {
        case 'answered': return 'status-answered';
        case 'in_progress': return 'status-in-progress';
        default: return 'status-pending';
    }
}

function getStatusText(status) {
    switch(status?.toLowerCase()) {
        case 'answered': return 'Respondida';
        case 'in_progress': return 'En Proceso';
        default: return 'Pendiente';
    }
}

function setupSecurityActions() {
    // Change password
    const changePasswordBtn = document.getElementById('changePasswordBtn');
    const sendResetEmailBtn = document.getElementById('sendResetEmailBtn');
    const modal = document.getElementById('changePasswordModal');
    const closeModalBtn = document.getElementById('closePasswordModal');
    const cancelPasswordBtn = document.getElementById('cancelPasswordChangeBtn');
    
    changePasswordBtn.addEventListener('click', () => {
        modal.classList.add('show');
    });
    
    closeModalBtn.addEventListener('click', () => {
        modal.classList.remove('show');
    });
    
    cancelPasswordBtn.addEventListener('click', () => {
        modal.classList.remove('show');
    });
    
    sendResetEmailBtn.addEventListener('click', handlePasswordReset);
    
    // Resend verification email
    const resendVerificationBtn = document.getElementById('resendVerificationBtn');
    if (resendVerificationBtn) {
        resendVerificationBtn.addEventListener('click', handleResendVerification);
    }
    
    // Delete account
    const deleteAccountBtn = document.getElementById('deleteAccountBtn');
    deleteAccountBtn.addEventListener('click', handleDeleteAccount);
}

async function handlePasswordReset() {
    console.log('🔑 Sending password reset email...');
    
    if (!currentUser || !currentUser.email) {
        showMessage('Error: No se pudo obtener el email del usuario', 'error');
        return;
    }
    
    try {
        showLoading(true);
        const result = await AuthService.forgotPassword(currentUser.email);
        
        if (result.success) {
            showMessage('Email de restablecimiento enviado exitosamente', 'success');
            document.getElementById('changePasswordModal').classList.remove('show');
        } else {
            showMessage(result.message || 'Error al enviar email de restablecimiento', 'error');
        }
    } catch (error) {
        console.error('Error sending password reset:', error);
        showMessage('Error de conexión', 'error');
    } finally {
        showLoading(false);
    }
}

async function handleResendVerification() {
    console.log('📧 Resending verification email...');
    
    if (!currentUser || !currentUser.email) {
        showMessage('Error: No se pudo obtener el email del usuario', 'error');
        return;
    }
    
    try {
        showLoading(true);
        
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.SEND_VERIFY_EMAIL}?email=${encodeURIComponent(currentUser.email)}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            showMessage('Email de verificación enviado exitosamente', 'success');
        } else {
            showMessage('Error al enviar email de verificación', 'error');
        }
    } catch (error) {
        console.error('Error resending verification:', error);
        showMessage('Error de conexión', 'error');
    } finally {
        showLoading(false);
    }
}

async function handleDeleteAccount() {
    const confirmed = confirm(
        '⚠️ ADVERTENCIA: Esta acción eliminará permanentemente tu cuenta y todos tus datos.\n\n' +
        '¿Estás completamente seguro de que deseas continuar?\n\n' +
        'Esta acción NO se puede deshacer.'
    );
    
    if (!confirmed) return;
    
    const doubleConfirm = confirm(
        'Por favor, confirma una vez más que deseas eliminar tu cuenta.\n\n' +
        'Se perderán todos tus datos, consultas y configuraciones.'
    );
    
    if (!doubleConfirm) return;
    
    console.log('🗑️ Deleting user account...');
    
    try {
        showLoading(true);
        
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${currentUser.id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showMessage('Cuenta eliminada exitosamente', 'success');
            
            // Clear all user data and redirect
            setTimeout(() => {
                AuthService.logout();
            }, 2000);
        } else {
            const errorData = await response.json();
            showMessage(errorData.message || 'Error al eliminar la cuenta', 'error');
        }
    } catch (error) {
        console.error('Error deleting account:', error);
        showMessage('Error de conexión al eliminar la cuenta', 'error');
    } finally {
        showLoading(false);
    }
}

function showAdminFeatures() {
    console.log('🛡️ Showing admin features...');
    
    // Show admin tab
    const adminTabs = document.querySelectorAll('.admin-only');
    adminTabs.forEach(tab => {
        tab.style.display = 'block';
    });
}

async function loadAdminStats() {
    console.log('📊 Loading admin statistics...');
    
    try {
        // Load users count
        const usersResponse = await apiRequest(API_CONFIG.ENDPOINTS.USERS);
        if (usersResponse.ok) {
            const users = await usersResponse.json();
            document.getElementById('totalUsers').textContent = users.length;
        }
        
        // Load contact forms count
        const contactFormsResponse = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS);
        if (contactFormsResponse.ok) {
            const contactForms = await contactFormsResponse.json();
            document.getElementById('totalContactForms').textContent = contactForms.length;
        }
        
        // Load reviews count
        const reviewsResponse = await apiRequest(API_CONFIG.ENDPOINTS.REVIEWS);
        if (reviewsResponse.ok) {
            const reviews = await reviewsResponse.json();
            document.getElementById('totalReviews').textContent = reviews.length;
        }
        
        console.log('✅ Admin stats loaded');
    } catch (error) {
        console.error('Error loading admin stats:', error);
    }
}

function setupAdminActions() {
    const viewUsersBtn = document.getElementById('viewAllUsersBtn');
    const viewContactFormsBtn = document.getElementById('viewAllContactFormsBtn');
    const manageTreatmentsBtn = document.getElementById('manageTreatmentsBtn');
    
    if (viewUsersBtn) {
        viewUsersBtn.addEventListener('click', () => {
            showMessage('Panel de usuarios en desarrollo', 'info');
        });
    }
    
    if (viewContactFormsBtn) {
        viewContactFormsBtn.addEventListener('click', () => {
            showMessage('Panel de consultas en desarrollo', 'info');
        });
    }
    
    if (manageTreatmentsBtn) {
        manageTreatmentsBtn.addEventListener('click', () => {
            showMessage('Panel de tratamientos en desarrollo', 'info');
        });
    }
}

function handleLogout(e) {
    e.preventDefault();
    
    const confirmed = confirm('¿Estás seguro de que quieres cerrar sesión?');
    if (confirmed) {
        showLoading(true);
        setTimeout(() => {
            AuthService.logout();
        }, 1000);
    }
}

function showLoading(show) {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = show ? 'flex' : 'none';
    }
}

function showMessage(message, type = 'info') {
    const container = document.getElementById('messageContainer');
    if (!container) return;
    
    const messageEl = document.createElement('div');
    messageEl.className = `message ${type}`;
    
    const icon = type === 'success' ? 'check-circle' : 
                 type === 'error' ? 'exclamation-circle' : 
                 'info-circle';
    
    messageEl.innerHTML = `
        <i class="fas fa-${icon}"></i>
        <span>${message}</span>
    `;
    
    container.appendChild(messageEl);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (messageEl.parentNode) {
            messageEl.parentNode.removeChild(messageEl);
        }
    }, 5000);
}
