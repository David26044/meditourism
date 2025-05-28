// Admin Panel Main Logic
document.addEventListener('DOMContentLoaded', function() {
    console.log('🛡️ Admin panel loaded');
    initializeAdminPanel();
});

let currentUser = null;
let allUsers = [];
let allRoles = [];
let blockedUsers = [];

async function initializeAdminPanel() {
    console.log('🎯 Initializing admin panel...');
    
    try {
        showAdminLoading(true);
        
        // Check authentication
        if (!AuthService.isAuthenticated()) {
            console.log('❌ User not authenticated, redirecting...');
            window.location.href = 'login.html';
            return;
        }

        // Load current user and verify admin status
        currentUser = await UserService.refreshUserInfo();
        
        if (!currentUser) {
            console.log('❌ Could not load user info, redirecting...');
            window.location.href = 'login.html';
            return;
        }

        // Verify admin permissions
        if (!UserService.isAdmin()) {
            console.log('❌ User is not admin, redirecting...');
            alert('No tienes permisos de administrador para acceder a esta página');
            window.location.href = 'home.html';
            return;
        }

        console.log('✅ Admin user verified:', currentUser);
        
        // Update admin user display
        updateAdminUserDisplay();
        
        // Setup navigation
        setupAdminNavigation();
        
        // Load dashboard data
        await loadDashboardData();
        
        // Load initial data
        await loadInitialData();
        
        console.log('✅ Admin panel initialized successfully');
    } catch (error) {
        console.error('💥 Error initializing admin panel:', error);
        alert('Error al cargar el panel de administración');
        window.location.href = 'home.html';
    } finally {
        showAdminLoading(false);
    }
}

async function loadInitialData() {
    try {
        // Load roles for user management
        allRoles = await AdminService.getAllRoles();
        console.log('Roles loaded:', allRoles);
        
        // Load blocked users
        blockedUsers = await AdminService.getAllBlockedUsers();
        console.log('Blocked users loaded:', blockedUsers);
    } catch (error) {
        console.error('Error loading initial data:', error);
    }
}

function updateAdminUserDisplay() {
    const adminUserName = document.getElementById('admin-user-name');
    if (adminUserName && currentUser) {
        adminUserName.textContent = `${currentUser.name} (Admin)`;
    }
}

function setupAdminNavigation() {
    const navItems = document.querySelectorAll('.admin-nav-item');
    
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            const section = item.getAttribute('data-section');
            switchAdminSection(section);
            
            // Update active nav item
            navItems.forEach(nav => nav.classList.remove('active'));
            item.classList.add('active');
        });
    });
}

function switchAdminSection(sectionName) {
    // Hide all sections
    const sections = document.querySelectorAll('.admin-section');
    sections.forEach(section => section.classList.remove('active'));
    
    // Show target section
    const targetSection = document.getElementById(`${sectionName}-section`);
    if (targetSection) {
        targetSection.classList.add('active');
        
        // Load section data if needed
        loadSectionData(sectionName);
    }
}

async function loadSectionData(sectionName) {
    switch(sectionName) {
        case 'dashboard':
            await loadDashboardData();
            break;
        case 'users':
            await loadUsersData();
            break;
        case 'contact-forms':
            await loadContactFormsData();
            break;
        case 'treatments':
            await loadTreatmentsData();
            break;
        case 'reviews':
            await loadReviewsData();
            break;
        default:
            console.log(`No specific loader for section: ${sectionName}`);
    }
}

async function loadDashboardData() {
    console.log('📊 Loading dashboard data...');
    
    try {
        // Load stats
        await Promise.all([
            loadUsersCount(),
            loadContactFormsCount(),
            loadTreatmentsCount(),
            loadReviewsCount()
        ]);
        
        // Load recent activity
        await loadRecentActivity();
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
    }
}

async function loadUsersCount() {
    try {
        const response = await apiRequest(API_CONFIG.ENDPOINTS.USERS);
        if (response.ok) {
            const users = await response.json();
            document.getElementById('total-users').textContent = users.length;
        }
    } catch (error) {
        console.error('Error loading users count:', error);
        document.getElementById('total-users').textContent = '-';
    }
}

async function loadContactFormsCount() {
    try {
        const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS);
        if (response.ok) {
            const forms = await response.json();
            document.getElementById('total-contact-forms').textContent = forms.length;
        }
    } catch (error) {
        console.error('Error loading contact forms count:', error);
        document.getElementById('total-contact-forms').textContent = '-';
    }
}

async function loadTreatmentsCount() {
    try {
        const response = await apiRequest(API_CONFIG.ENDPOINTS.TREATMENTS);
        if (response.ok) {
            const treatments = await response.json();
            document.getElementById('total-treatments').textContent = treatments.length;
        }
    } catch (error) {
        console.error('Error loading treatments count:', error);
        document.getElementById('total-treatments').textContent = '-';
    }
}

async function loadReviewsCount() {
    try {
        const response = await apiRequest(API_CONFIG.ENDPOINTS.REVIEWS);
        if (response.ok) {
            const reviews = await response.json();
            document.getElementById('total-reviews').textContent = reviews.length;
        }
    } catch (error) {
        console.error('Error loading reviews count:', error);
        document.getElementById('total-reviews').textContent = '-';
    }
}

async function loadRecentActivity() {
    const activityContainer = document.getElementById('recent-activity');
    
    try {
        // Simulate recent activity - in real app would come from API
        const activities = [
            {
                type: 'user',
                message: 'Nuevo usuario registrado',
                time: '2 minutos atrás',
                icon: 'fas fa-user-plus'
            },
            {
                type: 'contact',
                message: 'Nueva consulta recibida',
                time: '15 minutos atrás',
                icon: 'fas fa-envelope'
            },
            {
                type: 'review',
                message: 'Nueva reseña publicada',
                time: '1 hora atrás',
                icon: 'fas fa-star'
            }
        ];
        
        activityContainer.innerHTML = activities.map(activity => `
            <div class="activity-item">
                <div class="activity-icon">
                    <i class="${activity.icon}"></i>
                </div>
                <div class="activity-content">
                    <p>${activity.message}</p>
                    <span class="activity-time">${activity.time}</span>
                </div>
            </div>
        `).join('');
        
    } catch (error) {
        console.error('Error loading recent activity:', error);
        activityContainer.innerHTML = '<p class="error">Error al cargar actividad reciente</p>';
    }
}

async function loadUsersData() {
    const usersContainer = document.getElementById('users-list');
    
    try {
        usersContainer.innerHTML = '<p class="loading">Cargando usuarios...</p>';
        
        allUsers = await AdminService.getAllUsers();
        displayUsersTable(allUsers);
    } catch (error) {
        console.error('Error loading users:', error);
        usersContainer.innerHTML = '<p class="error">Error al cargar usuarios</p>';
    }
}

function displayUsersTable(users) {
    const usersContainer = document.getElementById('users-list');
    
    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Verificado</th>
                    <th>Estado</th>
                    <th>Fecha Registro</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${users.map(user => {
                    const isBlocked = blockedUsers.some(blocked => blocked.blockedUser.id === user.id);
                    const isCurrentUser = user.id === currentUser.id;
                    
                    return `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.email}</td>
                            <td>
                                <span class="role-badge ${user.role?.name === 'ADMIN' ? 'admin' : 'user'}">
                                    ${user.role?.name === 'ADMIN' ? 'Admin' : 'Usuario'}
                                </span>
                            </td>
                            <td>
                                <span class="status-badge ${user.verified ? 'verified' : 'unverified'}">
                                    ${user.verified ? 'Sí' : 'No'}
                                </span>
                            </td>
                            <td>
                                <span class="status-badge ${isBlocked ? 'blocked' : 'active'}">
                                    ${isBlocked ? 'Bloqueado' : 'Activo'}
                                </span>
                            </td>
                            <td>${new Date(user.createdAt).toLocaleDateString()}</td>
                            <td class="actions-cell">
                                <button class="admin-btn small" onclick="viewUser(${user.id})" title="Ver detalles">
                                    <i class="fas fa-eye"></i>
                                </button>
                                ${!isCurrentUser ? `
                                    <button class="admin-btn small primary" onclick="changeUserRole(${user.id})" title="Cambiar rol">
                                        <i class="fas fa-user-cog"></i>
                                    </button>
                                    ${isBlocked ? `
                                        <button class="admin-btn small success" onclick="unblockUser(${user.id})" title="Desbloquear">
                                            <i class="fas fa-unlock"></i>
                                        </button>
                                    ` : `
                                        <button class="admin-btn small warning" onclick="blockUser(${user.id})" title="Bloquear">
                                            <i class="fas fa-ban"></i>
                                        </button>
                                    `}
                                ` : `
                                    <span class="text-muted">Tu cuenta</span>
                                `}
                            </td>
                        </tr>
                    `;
                }).join('')}
            </tbody>
        </table>
    `;
    
    usersContainer.innerHTML = tableHTML;
}

// User management functions
async function viewUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const blockedInfo = blockedUsers.find(blocked => blocked.blockedUser.id === userId);
    
    const userDetails = `
        <div class="user-details">
            <h3>Detalles del Usuario</h3>
            <div class="detail-grid">
                <div class="detail-item">
                    <strong>ID:</strong> ${user.id}
                </div>
                <div class="detail-item">
                    <strong>Nombre:</strong> ${user.name}
                </div>
                <div class="detail-item">
                    <strong>Email:</strong> ${user.email}
                </div>
                <div class="detail-item">
                    <strong>Rol:</strong> ${user.role?.name || 'Sin rol'}
                </div>
                <div class="detail-item">
                    <strong>Verificado:</strong> ${user.verified ? 'Sí' : 'No'}
                </div>
                <div class="detail-item">
                    <strong>Fecha de registro:</strong> ${new Date(user.createdAt).toLocaleString()}
                </div>
                ${blockedInfo ? `
                    <div class="detail-item blocked-info">
                        <strong>Estado:</strong> Bloqueado
                    </div>
                    <div class="detail-item blocked-info">
                        <strong>Razón:</strong> ${blockedInfo.reason}
                    </div>
                    <div class="detail-item blocked-info">
                        <strong>Bloqueado el:</strong> ${new Date(blockedInfo.createdAt).toLocaleString()}
                    </div>
                ` : ''}
            </div>
        </div>
    `;
    
    showModal('Información del Usuario', userDetails);
}

async function changeUserRole(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const roleOptions = allRoles.map(role => 
        `<option value="${role.id}" ${user.role?.id === role.id ? 'selected' : ''}>${role.name}</option>`
    ).join('');
    
    const roleForm = `
        <div class="role-form">
            <h3>Cambiar Rol de Usuario</h3>
            <p><strong>Usuario:</strong> ${user.name} (${user.email})</p>
            <p><strong>Rol actual:</strong> ${user.role?.name || 'Sin rol'}</p>
            
            <form id="roleChangeForm">
                <div class="form-group">
                    <label for="newRole">Nuevo Rol:</label>
                    <select id="newRole" required>
                        <option value="">Seleccionar rol</option>
                        ${roleOptions}
                    </select>
                </div>
                <div class="form-actions">
                    <button type="button" class="admin-btn secondary" onclick="closeModal()">Cancelar</button>
                    <button type="submit" class="admin-btn primary">Cambiar Rol</button>
                </div>
            </form>
        </div>
    `;
    
    showModal('Cambiar Rol', roleForm);
    
    document.getElementById('roleChangeForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const newRoleId = document.getElementById('newRole').value;
        
        if (!newRoleId) {
            alert('Selecciona un rol');
            return;
        }
        
        try {
            showAdminLoading(true);
            await AdminService.updateUserRole(userId, parseInt(newRoleId));
            
            UIUtils.showToast('Rol actualizado correctamente', 'success');
            closeModal();
            await loadUsersData();
        } catch (error) {
            console.error('Error changing user role:', error);
            alert('Error al cambiar el rol del usuario: ' + error.message);
        } finally {
            showAdminLoading(false);
        }
    });
}

async function blockUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const blockForm = `
        <div class="block-form">
            <h3>Bloquear Usuario</h3>
            <p><strong>Usuario:</strong> ${user.name} (${user.email})</p>
            <p class="warning">⚠️ Esta acción impedirá que el usuario acceda al sistema.</p>
            
            <form id="blockUserForm">
                <div class="form-group">
                    <label for="blockReason">Razón del bloqueo:</label>
                    <textarea id="blockReason" rows="3" required placeholder="Describe la razón del bloqueo..."></textarea>
                </div>
                <div class="form-actions">
                    <button type="button" class="admin-btn secondary" onclick="closeModal()">Cancelar</button>
                    <button type="submit" class="admin-btn danger">Bloquear Usuario</button>
                </div>
            </form>
        </div>
    `;
    
    showModal('Bloquear Usuario', blockForm);
    
    document.getElementById('blockUserForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const reason = document.getElementById('blockReason').value.trim();
        
        if (!reason) {
            alert('Ingresa una razón para el bloqueo');
            return;
        }
        
        try {
            showAdminLoading(true);
            await AdminService.blockUser(userId, reason);
            
            UIUtils.showToast('Usuario bloqueado correctamente', 'success');
            closeModal();
            
            // Reload data
            blockedUsers = await AdminService.getAllBlockedUsers();
            await loadUsersData();
        } catch (error) {
            console.error('Error blocking user:', error);
            alert('Error al bloquear el usuario: ' + error.message);
        } finally {
            showAdminLoading(false);
        }
    });
}

async function unblockUser(userId) {
    const blockedUser = blockedUsers.find(blocked => blocked.blockedUser.id === userId);
    if (!blockedUser) return;
    
    if (confirm(`¿Estás seguro de que quieres desbloquear a ${blockedUser.blockedUser.name}?`)) {
        try {
            showAdminLoading(true);
            await AdminService.unblockUser(blockedUser.id);
            
            UIUtils.showToast('Usuario desbloqueado correctamente', 'success');
            
            // Reload data
            blockedUsers = await AdminService.getAllBlockedUsers();
            await loadUsersData();
        } catch (error) {
            console.error('Error unblocking user:', error);
            alert('Error al desbloquear el usuario: ' + error.message);
        } finally {
            showAdminLoading(false);
        }
    }
}

// Contact Forms Data Loading
async function loadContactFormsData() {
    const formsContainer = document.getElementById('contact-forms-list');
    
    try {
        formsContainer.innerHTML = '<p class="loading">Cargando formularios de contacto...</p>';
        
        const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS);
        if (response.ok) {
            const forms = await response.json();
            displayContactFormsTable(forms);
        } else {
            throw new Error('Error al cargar formularios');
        }
    } catch (error) {
        console.error('Error loading contact forms:', error);
        formsContainer.innerHTML = '<p class="error">Error al cargar formularios de contacto</p>';
    }
}

function displayContactFormsTable(forms) {
    const formsContainer = document.getElementById('contact-forms-list');
    
    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Tratamiento</th>
                    <th>Tipo Consulta</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${forms.map(form => `
                    <tr>
                        <td>${form.id}</td>
                        <td>${form.fullName}</td>
                        <td>${form.email}</td>
                        <td>${form.treatment?.name || 'No especificado'}</td>
                        <td>${form.inquiryType}</td>
                        <td>${new Date(form.createdAt).toLocaleDateString()}</td>
                        <td>
                            <span class="status-badge ${form.status || 'pending'}">
                                ${form.status === 'responded' ? 'Respondido' : 'Pendiente'}
                            </span>
                        </td>
                        <td class="actions-cell">
                            <button class="admin-btn small" onclick="viewContactForm(${form.id})" title="Ver detalles">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="admin-btn small danger" onclick="deleteContactForm(${form.id})" title="Eliminar">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    formsContainer.innerHTML = tableHTML;
}

// Contact Form Actions
async function viewContactForm(formId) {
    try {
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/${formId}`);
        if (response.ok) {
            const form = await response.json();
            
            const formDetails = `
                <div class="contact-form-details">
                    <h3>Detalles del Formulario de Contacto</h3>
                    <div class="detail-grid">
                        <div class="detail-item">
                            <strong>Nombre:</strong> ${form.fullName}
                        </div>
                        <div class="detail-item">
                            <strong>Email:</strong> ${form.email}
                        </div>
                        <div class="detail-item">
                            <strong>Teléfono:</strong> ${form.phone || 'No especificado'}
                        </div>
                        <div class="detail-item">
                            <strong>Tratamiento:</strong> ${form.treatment?.name || 'No especificado'}
                        </div>
                        <div class="detail-item">
                            <strong>Tipo de consulta:</strong> ${form.inquiryType}
                        </div>
                        <div class="detail-item">
                            <strong>Sede preferida:</strong> ${form.preferredClinic || 'No especificada'}
                        </div>
                        <div class="detail-item full-width">
                            <strong>Mensaje:</strong>
                            <p class="message-content">${form.message}</p>
                        </div>
                        <div class="detail-item">
                            <strong>Fecha:</strong> ${new Date(form.createdAt).toLocaleString()}
                        </div>
                    </div>
                </div>
            `;
            
            showModal('Formulario de Contacto', formDetails);
        }
    } catch (error) {
        console.error('Error loading contact form:', error);
        alert('Error al cargar el formulario');
    }
}

async function deleteContactForm(formId) {
    if (confirm('¿Estás seguro de que quieres eliminar este formulario de contacto?')) {
        try {
            showAdminLoading(true);
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/${formId}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                UIUtils.showToast('Formulario eliminado correctamente', 'success');
                await loadContactFormsData();
            } else {
                throw new Error('Error al eliminar formulario');
            }
        } catch (error) {
            console.error('Error deleting contact form:', error);
            alert('Error al eliminar el formulario');
        } finally {
            showAdminLoading(false);
        }
    }
}

// Add refresh functions for manual updates
async function refreshUsers() {
    await loadUsersData();
}

async function refreshContactForms() {
    await loadContactFormsData();
}

async function refreshReviews() {
    await loadReviewsData();
}

// Add treatment modal function
function showAddTreatmentModal() {
    const treatmentForm = `
        <div class="add-treatment-form">
            <h3>Agregar Nuevo Tratamiento</h3>
            <form id="addTreatmentForm">
                <div class="form-group">
                    <label for="treatmentName">Nombre del Tratamiento:</label>
                    <input type="text" id="treatmentName" required>
                </div>
                <div class="form-group">
                    <label for="treatmentDescription">Descripción:</label>
                    <textarea id="treatmentDescription" rows="3"></textarea>
                </div>
                <div class="form-group">
                    <label for="treatmentPrice">Precio Desde:</label>
                    <input type="number" id="treatmentPrice" min="0">
                </div>
                <div class="form-group">
                    <label for="treatmentSpecialty">Especialidad:</label>
                    <select id="treatmentSpecialty">
                        <option value="Operatoria Dental">Operatoria Dental</option>
                        <option value="Endodoncia">Endodoncia</option>
                        <option value="Implantología">Implantología</option>
                        <option value="Estética Dental">Estética Dental</option>
                        <option value="Ortodoncia">Ortodoncia</option>
                        <option value="Periodoncia">Periodoncia</option>
                        <option value="Cirugía Oral">Cirugía Oral</option>
                        <option value="Rehabilitación Oral">Rehabilitación Oral</option>
                    </select>
                </div>
                <div class="form-actions">
                    <button type="button" class="admin-btn secondary" onclick="closeModal()">Cancelar</button>
                    <button type="submit" class="admin-btn primary">Agregar Tratamiento</button>
                </div>
            </form>
        </div>
    `;
    
    showModal('Agregar Tratamiento', treatmentForm);
    
    document.getElementById('addTreatmentForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        // Handle treatment creation
        UIUtils.showToast('Función de agregar tratamiento en desarrollo', 'info');
        closeModal();
    });
}

// Modal functions
function showModal(title, content) {
    const modal = document.createElement('div');
    modal.className = 'admin-modal';
    modal.innerHTML = `
        <div class="admin-modal-content">
            <div class="admin-modal-header">
                <h2>${title}</h2>
                <button class="admin-modal-close" onclick="closeModal()">&times;</button>
            </div>
            <div class="admin-modal-body">
                ${content}
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // Close on outside click
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });
}

function closeModal() {
    const modal = document.querySelector('.admin-modal');
    if (modal) {
        modal.remove();
    }
}

// Utility functions
function showAdminLoading(show) {
    const loadingOverlay = document.getElementById('admin-loading');
    if (loadingOverlay) {
        loadingOverlay.style.display = show ? 'flex' : 'none';
    }
}

function handleAdminLogout() {
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        AuthService.logout();
    }
}

// Load more data functions (placeholders)
async function loadTreatmentsData() {
    console.log('Loading treatments data...');
}

async function loadReviewsData() {
    console.log('Loading reviews data...');
}
