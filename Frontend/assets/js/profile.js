// Profile Page Logic
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Profile page loaded');
    initializeProfilePage();
});

let currentUser = null;
let isEditing = false;
let cropCanvas = null;
let cropCtx = null;
let currentImageData = null;

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
        
        // Check if admin tab should be activated
        const activeTab = localStorage.getItem('activeProfileTab');
        if (activeTab === 'admin' && UserService.isAdmin()) {
            activateTab('admin');
            localStorage.removeItem('activeProfileTab');
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
    
    // Update header with user name
    const headerUserName = document.getElementById('headerUserName');
    if (headerUserName && user.name) {
        headerUserName.textContent = user.name;
    }
    
    // Update display elements with null checks
    const displayName = document.getElementById('displayName');
    if (displayName) displayName.textContent = user.name || '-';
    
    const displayEmail = document.getElementById('displayEmail');
    if (displayEmail) displayEmail.textContent = user.email || '-';
    
    const displayPhone = document.getElementById('displayPhone');
    if (displayPhone) displayPhone.textContent = user.phone || '-';
    
    const displayCity = document.getElementById('displayCity');
    if (displayCity) displayCity.textContent = user.city || '-';
    
    const displayBirthDate = document.getElementById('displayBirthDate');
    if (displayBirthDate) {
        displayBirthDate.textContent = user.birthDate ? 
            new Date(user.birthDate).toLocaleDateString() : '-';
    }
    
    // Update avatar
    if (user.avatar) {
        const profileImage = document.getElementById('profileImage');
        if (profileImage) {
            profileImage.src = user.avatar;
            profileImage.style.display = 'block';
        }
        
        const avatarIcon = document.getElementById('profileAvatarLarge')?.querySelector('i');
        if (avatarIcon) avatarIcon.style.display = 'none';
        
        const removeBtn = document.getElementById('removeAvatarBtn');
        if (removeBtn) removeBtn.style.display = 'block';
    }
    
    // Verification status
    const verifiedElement = document.getElementById('displayVerified');
    if (verifiedElement) {
        if (user.verified) {
            verifiedElement.innerHTML = '<span class="verified-badge"><i class="fas fa-check"></i> Verificado</span>';
        } else {
            verifiedElement.innerHTML = '<span class="unverified-badge"><i class="fas fa-clock"></i> Pendiente</span>';
            const resendBtn = document.getElementById('resendVerificationBtn');
            if (resendBtn) resendBtn.style.display = 'block';
            
            const verificationStatus = document.getElementById('verificationStatus');
            if (verificationStatus) verificationStatus.textContent = 'Tu email aún no está verificado';
        }
    }
    
    // Role display
    const roleElement = document.getElementById('displayRole');
    const profileRoleBadge = document.getElementById('profileRoleBadge');
    const avatarLarge = document.getElementById('profileAvatarLarge');
    
    if (roleElement) {
        if (user.role && user.role.name === 'ADMIN') {
            roleElement.innerHTML = '<span class="admin-badge"><i class="fas fa-crown"></i> Administrador</span>';
            if (profileRoleBadge) {
                profileRoleBadge.textContent = 'ADMIN';
                profileRoleBadge.classList.add('show');
            }
            if (avatarLarge) avatarLarge.classList.add('admin');
        } else {
            roleElement.innerHTML = '<span class="user-badge"><i class="fas fa-user"></i> Usuario</span>';
            if (profileRoleBadge) profileRoleBadge.classList.remove('show');
            if (avatarLarge) avatarLarge.classList.remove('admin');
        }
    }
    
    // Member since (using created date or current date as fallback)
    const memberSince = user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'Información no disponible';
    const displayMemberSince = document.getElementById('displayMemberSince');
    if (displayMemberSince) displayMemberSince.textContent = memberSince;
    
    // Update edit form with null checks
    const editName = document.getElementById('editName');
    if (editName) editName.value = user.name || '';
    
    const editEmail = document.getElementById('editEmail');
    if (editEmail) editEmail.value = user.email || '';
    
    const editPhone = document.getElementById('editPhone');
    if (editPhone) editPhone.value = user.phone || '';
    
    const editCity = document.getElementById('editCity');
    if (editCity) editCity.value = user.city || '';
    
    const editBirthDate = document.getElementById('editBirthDate');
    if (editBirthDate) editBirthDate.value = user.birthDate || '';
    
    const editBio = document.getElementById('editBio');
    if (editBio) editBio.value = user.bio || '';
    
    // Load preferences
    loadUserPreferences(user);
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
    
    // Avatar management
    setupAvatarManagement();
    
    // Preferences
    setupPreferences();
}

function setupTabSwitching() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetTab = button.getAttribute('data-tab');
            activateTab(targetTab);
        });
    });
}

function activateTab(targetTab) {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    
    // Remove active class from all buttons and contents
    tabButtons.forEach(btn => btn.classList.remove('active'));
    tabContents.forEach(content => content.classList.remove('active'));
    
    // Add active class to target button and corresponding content
    const targetButton = document.querySelector(`[data-tab="${targetTab}"]`);
    const targetContent = document.getElementById(`${targetTab}-tab`);
    
    if (targetButton && targetContent) {
        targetButton.classList.add('active');
        targetContent.classList.add('active');
        console.log(`📑 Switched to tab: ${targetTab}`);
    }
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
        document.getElementById('editPhone').value = currentUser.phone || '';
        document.getElementById('editCity').value = currentUser.city || '';
        document.getElementById('editBirthDate').value = currentUser.birthDate || '';
        document.getElementById('editBio').value = currentUser.bio || '';
    }
    
    // Clear errors
    clearFormErrors();
}

function setupAvatarManagement() {
    const uploadBtn = document.getElementById('uploadAvatarBtn');
    const removeBtn = document.getElementById('removeAvatarBtn');
    const avatarInput = document.getElementById('avatarInput');
    const cropModal = document.getElementById('avatarCropModal');
    
    uploadBtn.addEventListener('click', () => {
        avatarInput.click();
    });
    
    avatarInput.addEventListener('change', handleAvatarUpload);
    removeBtn.addEventListener('click', handleAvatarRemove);
    
    // Crop modal controls
    document.getElementById('closeCropModal').addEventListener('click', () => {
        cropModal.classList.remove('show');
    });
    
    document.getElementById('cancelCrop').addEventListener('click', () => {
        cropModal.classList.remove('show');
    });
    
    document.getElementById('saveCroppedAvatar').addEventListener('click', saveCroppedAvatar);
    
    // Initialize crop canvas
    const canvas = document.getElementById('cropCanvas');
    cropCanvas = canvas;
    cropCtx = canvas.getContext('2d');
}

function handleAvatarUpload(e) {
    const file = e.target.files[0];
    if (!file) return;
    
    // Validate file type
    if (!file.type.startsWith('image/')) {
        showMessage('Por favor selecciona un archivo de imagen válido', 'error');
        return;
    }
    
    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
        showMessage('La imagen debe ser menor a 5MB', 'error');
        return;
    }
    
    const reader = new FileReader();
    reader.onload = (e) => {
        currentImageData = e.target.result;
        showCropModal();
    };
    reader.readAsDataURL(file);
}

function showCropModal() {
    const modal = document.getElementById('avatarCropModal');
    modal.classList.add('show');
    
    const img = new Image();
    img.onload = () => {
        const size = 300;
        cropCanvas.width = size;
        cropCanvas.height = size;
        
        const scale = Math.min(size / img.width, size / img.height);
        const x = (size - img.width * scale) / 2;
        const y = (size - img.height * scale) / 2;
        
        cropCtx.clearRect(0, 0, size, size);
        cropCtx.drawImage(img, x, y, img.width * scale, img.height * scale);
    };
    img.src = currentImageData;
}

async function saveCroppedAvatar() {
    try {
        showLoading(true);
        
        // Convert canvas to blob
        const blob = await new Promise(resolve => {
            cropCanvas.toBlob(resolve, 'image/jpeg', 0.8);
        });
        
        // Create FormData for upload
        const formData = new FormData();
        formData.append('avatar', blob, 'avatar.jpg');
        formData.append('userId', currentUser.id);
        
        // Upload avatar - usando apiRequest para consistencia
        const response = await fetch(`${API_CONFIG.BASE_URL}/upload/avatar`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${AuthService.getToken()}`
            },
            body: formData
        });
        
        if (response.ok) {
            const result = await response.json();
            
            // Update user data
            currentUser.avatar = result.avatarUrl;
            localStorage.setItem('user', JSON.stringify(currentUser));
            
            // Update UI
            const profileImage = document.getElementById('profileImage');
            profileImage.src = result.avatarUrl;
            profileImage.style.display = 'block';
            document.getElementById('profileAvatarLarge').querySelector('i').style.display = 'none';
            document.getElementById('removeAvatarBtn').style.display = 'block';
            
            // Update navbar avatar
            updateUserInterface();
            
            // Close modal
            document.getElementById('avatarCropModal').classList.remove('show');
            
            showMessage('Foto de perfil actualizada exitosamente', 'success');
        } else {
            throw new Error('Error al subir la imagen');
        }
    } catch (error) {
        console.error('Error uploading avatar:', error);
        showMessage('Error al subir la foto de perfil', 'error');
    } finally {
        showLoading(false);
    }
}

async function handleAvatarRemove() {
    const confirmed = confirm('¿Estás seguro de que quieres quitar tu foto de perfil?');
    if (!confirmed) return;
    
    try {
        showLoading(true);
        
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${currentUser.id}/avatar`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            // Update user data
            currentUser.avatar = null;
            localStorage.setItem('user', JSON.stringify(currentUser));
            
            // Update UI
            const profileImage = document.getElementById('profileImage');
            profileImage.style.display = 'none';
            document.getElementById('profileAvatarLarge').querySelector('i').style.display = 'block';
            document.getElementById('removeAvatarBtn').style.display = 'none';
            
            updateUserInterface();
            showMessage('Foto de perfil eliminada', 'success');
        } else {
            throw new Error('Error al eliminar la imagen');
        }
    } catch (error) {
        console.error('Error removing avatar:', error);
        showMessage('Error al eliminar la foto de perfil', 'error');
    } finally {
        showLoading(false);
    }
}

function setupPreferences() {
    const saveBtn = document.getElementById('savePreferencesBtn');
    const resetBtn = document.getElementById('resetPreferencesBtn');
    
    saveBtn.addEventListener('click', saveUserPreferences);
    resetBtn.addEventListener('click', resetUserPreferences);
    
    // Add change listeners for immediate feedback
    const toggles = document.querySelectorAll('.preference-item input[type="checkbox"]');
    toggles.forEach(toggle => {
        toggle.addEventListener('change', (e) => {
            console.log(`Preference ${e.target.id} changed to:`, e.target.checked);
        });
    });
}

function loadUserPreferences(user) {
    // Load notification preferences
    document.getElementById('emailNotifications').checked = user.preferences?.emailNotifications ?? true;
    document.getElementById('appointmentReminders').checked = user.preferences?.appointmentReminders ?? true;
    document.getElementById('newsletter').checked = user.preferences?.newsletter ?? false;
    
    // Load privacy preferences
    document.getElementById('publicProfile').checked = user.preferences?.publicProfile ?? false;
    document.getElementById('showReviews').checked = user.preferences?.showReviews ?? true;
    
    // Load language and region
    document.getElementById('languageSelect').value = user.preferences?.language ?? 'es';
    document.getElementById('timezoneSelect').value = user.preferences?.timezone ?? 'America/Bogota';
}

async function saveUserPreferences() {
    try {
        showLoading(true);
        
        const preferences = {
            emailNotifications: document.getElementById('emailNotifications').checked,
            appointmentReminders: document.getElementById('appointmentReminders').checked,
            newsletter: document.getElementById('newsletter').checked,
            publicProfile: document.getElementById('publicProfile').checked,
            showReviews: document.getElementById('showReviews').checked,
            language: document.getElementById('languageSelect').value,
            timezone: document.getElementById('timezoneSelect').value
        };
        
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${currentUser.id}/preferences`, {
            method: 'PATCH',
            body: JSON.stringify({ preferences })
        });
        
        if (response.ok) {
            currentUser.preferences = preferences;
            localStorage.setItem('user', JSON.stringify(currentUser));
            showMessage('Preferencias guardadas exitosamente', 'success');
        } else {
            throw new Error('Error al guardar preferencias');
        }
    } catch (error) {
        console.error('Error saving preferences:', error);
        showMessage('Error al guardar las preferencias', 'error');
    } finally {
        showLoading(false);
    }
}

function resetUserPreferences() {
    const confirmed = confirm('¿Estás seguro de que quieres restaurar las preferencias por defecto?');
    if (!confirmed) return;
    
    // Reset to default values
    document.getElementById('emailNotifications').checked = true;
    document.getElementById('appointmentReminders').checked = true;
    document.getElementById('newsletter').checked = false;
    document.getElementById('publicProfile').checked = false;
    document.getElementById('showReviews').checked = true;
    document.getElementById('languageSelect').value = 'es';
    document.getElementById('timezoneSelect').value = 'America/Bogota';
    
    showMessage('Preferencias restauradas por defecto', 'info');
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
    
    // Validate phone (optional but if provided must be valid)
    if (data.phone && !/^\+?[\d\s\-\(\)]{10,}$/.test(data.phone)) {
        showFieldError('phoneError', 'Ingresa un teléfono válido');
        isValid = false;
    }
    
    // Validate birth date (optional but if provided must be reasonable)
    if (data.birthDate) {
        const birthDate = new Date(data.birthDate);
        const today = new Date();
        const age = today.getFullYear() - birthDate.getFullYear();
        
        if (age < 13 || age > 120) {
            showFieldError('birthDateError', 'La fecha de nacimiento no es válida');
            isValid = false;
        }
    }
    
    return isValid;
}

async function handleProfileUpdate(e) {
    e.preventDefault();
    
    console.log('💾 Updating profile...');
    
    const formData = {
        name: document.getElementById('editName').value.trim(),
        email: document.getElementById('editEmail').value.trim(),
        phone: document.getElementById('editPhone').value.trim(),
        city: document.getElementById('editCity').value.trim(),
        birthDate: document.getElementById('editBirthDate').value,
        bio: document.getElementById('editBio').value.trim()
    };
    
    // Remove empty fields
    Object.keys(formData).forEach(key => {
        if (!formData[key]) {
            delete formData[key];
        }
    });
    
    // Validate form
    if (!validateProfileForm(formData)) {
        return;
    }
    
    try {
        showLoading(true);
        
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${currentUser.id}`, {
            method: 'PATCH',
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            const updatedUser = await response.json();
            currentUser = { ...currentUser, ...updatedUser };
            
            // Update localStorage
            localStorage.setItem('user', JSON.stringify(currentUser));
            AuthService.cachedUser = currentUser;
            
            // Update display
            displayUserInfo(currentUser);
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
        const result = await EmailService.sendPasswordResetEmail(currentUser.email);
        
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
        
        const result = await EmailService.sendVerificationEmail(currentUser.email);
        
        if (result.success) {
            showMessage('Email de verificación enviado exitosamente. Revisa tu bandeja de entrada y spam.', 'success');
            
            // Update button state
            const resendBtn = document.getElementById('resendVerificationBtn');
            if (resendBtn) {
                resendBtn.disabled = true;
                resendBtn.innerHTML = '<i class="fas fa-clock"></i> Enviado';
                setTimeout(() => {
                    resendBtn.disabled = false;
                    resendBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Reenviar';
                }, 60000); // Re-enable after 1 minute
            }
        } else {
            showMessage(result.message || 'Error al enviar email de verificación', 'error');
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
    
    // Add admin panel link to tab content
    const adminTab = document.getElementById('admin-tab');
    if (adminTab && !adminTab.querySelector('.admin-panel-link')) {
        const adminPanelLink = document.createElement('div');
        adminPanelLink.className = 'admin-panel-section';
        adminPanelLink.innerHTML = `
            <div class="profile-section">
                <div class="section-header">
                    <h2><i class="fas fa-shield-alt"></i> Panel Administrativo</h2>
                </div>
                <div class="admin-panel-card">
                    <p>Accede al panel administrativo completo para gestionar usuarios, consultas y configuraciones del sistema.</p>
                    <a href="admin.html" class="admin-btn primary admin-panel-link">
                        <i class="fas fa-external-link-alt"></i> Ir al Panel Admin
                    </a>
                </div>
            </div>
        `;
        adminTab.appendChild(adminPanelLink);
    }
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

async function loadUserContactForms() {
    console.log('📧 Loading user contact forms...');
    
    try {
        const loadingElement = document.getElementById('contactFormsLoading');
        const listElement = document.getElementById('contactFormsList');
        const emptyElement = document.getElementById('noContactForms');
        
        if (loadingElement) loadingElement.style.display = 'block';
        if (listElement) listElement.innerHTML = '';
        if (emptyElement) emptyElement.style.display = 'none';
        
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/user/${currentUser.id}`);
        
        if (response.ok) {
            const contactForms = await response.json();
            
            if (loadingElement) loadingElement.style.display = 'none';
            
            if (contactForms.length === 0) {
                if (emptyElement) emptyElement.style.display = 'block';
            } else {
                displayContactForms(contactForms);
            }
        } else {
            throw new Error('Error al cargar las consultas');
        }
    } catch (error) {
        console.error('Error loading contact forms:', error);
        const loadingElement = document.getElementById('contactFormsLoading');
        if (loadingElement) {
            loadingElement.innerHTML = '<p class="error">Error al cargar las consultas</p>';
        }
    }
}

function displayContactForms(contactForms) {
    const container = document.getElementById('contactFormsList');
    if (!container) return;
    
    container.innerHTML = contactForms.map(form => `
        <div class="contact-form-item">
            <div class="form-header">
                <h4>${form.subject || 'Consulta'}</h4>
                <span class="form-date">${new Date(form.createdAt).toLocaleDateString()}</span>
            </div>
            <div class="form-content">
                <p><strong>Tratamiento:</strong> ${form.treatmentName || 'General'}</p>
                <p><strong>Mensaje:</strong> ${form.message}</p>
                <div class="form-status ${form.status?.toLowerCase() || 'pending'}">
                    <i class="fas fa-circle"></i>
                    ${form.status || 'Pendiente'}
                </div>
            </div>
        </div>
    `).join('');
}

function clearFormErrors() {
    const errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(element => {
        element.textContent = '';
        element.style.display = 'none';
    });
    
    const formGroups = document.querySelectorAll('.form-group');
    formGroups.forEach(group => {
        group.classList.remove('error');
    });
}

function showFieldError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
        
        // Add error class to form group
        const formGroup = errorElement.closest('.form-group');
        if (formGroup) {
            formGroup.classList.add('error');
        }
    }
}
