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

async function loadContactFormsData() {
    const contactFormsContainer = document.getElementById('contact-forms-list');
    
    try {
        contactFormsContainer.innerHTML = '<p class="loading">Cargando consultas...</p>';
        
        const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS);
        if (response.ok) {
            const contactForms = await response.json();
            displayContactFormsTable(contactForms);
        } else {
            throw new Error('Error al cargar consultas');
        }
    } catch (error) {
        console.error('Error loading contact forms:', error);
        contactFormsContainer.innerHTML = '<p class="error">Error al cargar consultas</p>';
    }
}

function displayContactFormsTable(contactForms) {
    const container = document.getElementById('contact-forms-list');
    
    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Usuario</th>
                    <th>Nombre Completo</th>
                    <th>Email</th>
                    <th>Tratamiento</th>
                    <th>Tipo de Consulta</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${contactForms.map(form => `
                    <tr>
                        <td>${form.id}</td>
                        <td>${form.user ? form.user.name : 'Anónimo'}</td>
                        <td>${form.fullName}</td>
                        <td>${form.email}</td>
                        <td>${form.treatment?.name || 'N/A'}</td>
                        <td>${form.inquiryType}</td>
                        <td>${new Date(form.createdDate).toLocaleDateString()}</td>
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
    
    container.innerHTML = tableHTML;
}

async function loadTreatmentsData() {
    const treatmentsContainer = document.getElementById('treatments-list');
    
    try {
        treatmentsContainer.innerHTML = '<p class="loading">Cargando tratamientos...</p>';
        
        const response = await apiRequest(API_CONFIG.ENDPOINTS.TREATMENTS);
        if (response.ok) {
            const treatments = await response.json();
            displayTreatmentsTable(treatments);
        } else {
            throw new Error('Error al cargar tratamientos');
        }
    } catch (error) {
        console.error('Error loading treatments:', error);
        treatmentsContainer.innerHTML = '<p class="error">Error al cargar tratamientos</p>';
    }
}

function displayTreatmentsTable(treatments) {
    const container = document.getElementById('treatments-list');
    
    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Duración</th>
                    <th>Precio Base</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${treatments.map(treatment => `
                    <tr>
                        <td>${treatment.id}</td>
                        <td>${treatment.name}</td>
                        <td>${treatment.description?.substring(0, 50) + '...' || 'Sin descripción'}</td>
                        <td>${treatment.duration || 'No especificada'}</td>
                        <td>$${treatment.basePrice?.toLocaleString() || 'No especificado'}</td>
                        <td class="actions-cell">
                            <button class="admin-btn small" onclick="viewTreatment(${treatment.id})" title="Ver detalles">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="admin-btn small primary" onclick="editTreatment(${treatment.id})" title="Editar">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="admin-btn small danger" onclick="deleteTreatment(${treatment.id})" title="Eliminar">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    container.innerHTML = tableHTML;
}

async function loadReviewsData() {
    const reviewsContainer = document.getElementById('reviews-list');
    
    try {
        reviewsContainer.innerHTML = '<p class="loading">Cargando reviews...</p>';
        
        const response = await apiRequest(API_CONFIG.ENDPOINTS.REVIEWS);
        if (response.ok) {
            const reviews = await response.json();
            displayReviewsTable(reviews);
        } else {
            throw new Error('Error al cargar reviews');
        }
    } catch (error) {
        console.error('Error loading reviews:', error);
        reviewsContainer.innerHTML = '<p class="error">Error al cargar reviews</p>';
    }
}

function displayReviewsTable(reviews) {
    const container = document.getElementById('reviews-list');
    
    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Usuario</th>
                    <th>Clínica</th>
                    <th>Calificación</th>
                    <th>Contenido</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${reviews.map(review => `
                    <tr>
                        <td>${review.id}</td>
                        <td>${review.user?.name || 'Usuario eliminado'}</td>
                        <td>${review.clinic?.name || 'N/A'}</td>
                        <td>
                            <div class="rating-display">
                                ${generateStarRating(review.rating)}
                                <span>(${review.rating || 0})</span>
                            </div>
                        </td>
                        <td>${review.content?.substring(0, 50) + '...' || 'Sin contenido'}</td>
                        <td>${new Date(review.date).toLocaleDateString()}</td>
                        <td class="actions-cell">
                            <button class="admin-btn small" onclick="viewReview(${review.id})" title="Ver detalles">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="admin-btn small danger" onclick="deleteReview(${review.id})" title="Eliminar">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    container.innerHTML = tableHTML;
}

function generateStarRating(rating) {
    const stars = [];
    const fullStars = Math.floor(rating || 0);
    const hasHalfStar = (rating || 0) % 1 !== 0;
    
    for (let i = 0; i < fullStars; i++) {
        stars.push('<i class="fas fa-star"></i>');
    }
    
    if (hasHalfStar) {
        stars.push('<i class="fas fa-star-half-alt"></i>');
    }
    
    const emptyStars = 5 - Math.ceil(rating || 0);
    for (let i = 0; i < emptyStars; i++) {
        stars.push('<i class="far fa-star"></i>');
    }
    
    return stars.join('');
}

// User management functions
async function viewUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const blockedInfo = blockedUsers.find(blocked => blocked.blockedUser.id === userId);
    
    showModal(`
        <h3>Detalles del Usuario</h3>
        <div class="user-details">
            <p><strong>ID:</strong> ${user.id}</p>
            <p><strong>Nombre:</strong> ${user.name}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Rol:</strong> ${user.role?.name === 'ADMIN' ? 'Administrador' : 'Usuario'}</p>
            <p><strong>Verificado:</strong> ${user.verified ? 'Sí' : 'No'}</p>
            <p><strong>Fecha de registro:</strong> ${new Date(user.createdAt).toLocaleString()}</p>
            ${blockedInfo ? `
                <p><strong>Estado:</strong> <span class="status-blocked">BLOQUEADO</span></p>
                <p><strong>Razón del bloqueo:</strong> ${blockedInfo.reason}</p>
                <p><strong>Fecha de bloqueo:</strong> ${new Date(blockedInfo.blockedDate).toLocaleString()}</p>
            ` : `<p><strong>Estado:</strong> <span class="status-active">ACTIVO</span></p>`}
        </div>
    `);
}

async function changeUserRole(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const currentRole = user.role?.name;
    const newRole = currentRole === 'ADMIN' ? 'USER' : 'ADMIN';
    const newRoleId = currentRole === 'ADMIN' ? 2 : 1;
    
    if (confirm(`¿Estás seguro de cambiar el rol de ${user.name} a ${newRole}?`)) {
        try {
            const response = await AdminService.updateUserRole(userId, newRoleId);
            showMessage('Rol actualizado exitosamente', 'success');
            await loadUsersData();
        } catch (error) {
            showMessage('Error al actualizar rol: ' + error.message, 'error');
        }
    }
}

async function blockUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const reason = prompt(`¿Cuál es la razón para bloquear a ${user.name}?`);
    if (!reason) return;
    
    try {
        await AdminService.blockUser(userId, reason);
        showMessage('Usuario bloqueado exitosamente', 'success');
        await loadUsersData();
        await loadInitialData();
    } catch (error) {
        showMessage('Error al bloquear usuario: ' + error.message, 'error');
    }
}

async function unblockUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    if (confirm(`¿Estás seguro de desbloquear a ${user.name}?`)) {
        try {
            const blockedUser = blockedUsers.find(b => b.blockedUser.id === userId);
            if (blockedUser) {
                await AdminService.unblockUser(blockedUser.id);
                showMessage('Usuario desbloqueado exitosamente', 'success');
                await loadUsersData();
                await loadInitialData();
            }
        } catch (error) {
            showMessage('Error al desbloquear usuario: ' + error.message, 'error');
        }
    }
}

// Contact Forms functions
async function viewContactForm(formId) {
    try {
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/${formId}`);
        if (response.ok) {
            const form = await response.json();
            showModal(`
                <h3>Detalles de la Consulta</h3>
                <div class="form-details">
                    <p><strong>ID:</strong> ${form.id}</p>
                    <p><strong>Usuario:</strong> ${form.user ? form.user.name : 'Anónimo'}</p>
                    <p><strong>Nombre completo:</strong> ${form.fullName}</p>
                    <p><strong>Email:</strong> ${form.email}</p>
                    <p><strong>Teléfono:</strong> ${form.phone || 'No proporcionado'}</p>
                    <p><strong>Tratamiento:</strong> ${form.treatment?.name || 'N/A'}</p>
                    <p><strong>Tipo de consulta:</strong> ${form.inquiryType}</p>
                    <p><strong>Clínica preferida:</strong> ${form.preferredClinic || 'No especificada'}</p>
                    <p><strong>Fecha:</strong> ${new Date(form.createdDate).toLocaleString()}</p>
                    <p><strong>Mensaje:</strong></p>
                    <div class="message-content">${form.message}</div>
                    <p><strong>Acepta términos:</strong> ${form.acceptTerms ? 'Sí' : 'No'}</p>
                    <p><strong>Acepta marketing:</strong> ${form.acceptMarketing ? 'Sí' : 'No'}</p>
                </div>
            `);
        }
    } catch (error) {
        showMessage('Error al cargar detalles de la consulta', 'error');
    }
}

async function deleteContactForm(formId) {
    if (confirm('¿Estás seguro de eliminar esta consulta?')) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/${formId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                showMessage('Consulta eliminada exitosamente', 'success');
                await loadContactFormsData();
            }
        } catch (error) {
            showMessage('Error al eliminar consulta', 'error');
        }
    }
}

// Treatment functions
async function viewTreatment(treatmentId) {
    try {
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.TREATMENTS}/${treatmentId}`);
        if (response.ok) {
            const treatment = await response.json();
            showModal(`
                <h3>Detalles del Tratamiento</h3>
                <div class="treatment-details">
                    <p><strong>ID:</strong> ${treatment.id}</p>
                    <p><strong>Nombre:</strong> ${treatment.name}</p>
                    <p><strong>Descripción:</strong> ${treatment.description || 'Sin descripción'}</p>
                    <p><strong>Duración:</strong> ${treatment.duration || 'No especificada'}</p>
                    <p><strong>Precio base:</strong> $${treatment.basePrice?.toLocaleString() || 'No especificado'}</p>
                </div>
            `);
        }
    } catch (error) {
        showMessage('Error al cargar detalles del tratamiento', 'error');
    }
}

async function editTreatment(treatmentId) {
    // This would open an edit modal - for now show a simple prompt
    showMessage('Funcionalidad de edición en desarrollo', 'info');
}

async function deleteTreatment(treatmentId) {
    if (confirm('¿Estás seguro de eliminar este tratamiento?')) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.TREATMENTS}/${treatmentId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                showMessage('Tratamiento eliminado exitosamente', 'success');
                await loadTreatmentsData();
            }
        } catch (error) {
            showMessage('Error al eliminar tratamiento', 'error');
        }
    }
}

// Review functions
async function viewReview(reviewId) {
    try {
        const response = await apiRequest(`${API_CONFIG.ENDPOINTS.REVIEWS}/${reviewId}`);
        if (response.ok) {
            const review = await response.json();
            showModal(`
                <h3>Detalles de la Review</h3>
                <div class="review-details">
                    <p><strong>ID:</strong> ${review.id}</p>
                    <p><strong>Usuario:</strong> ${review.user?.name || 'Usuario eliminado'}</p>
                    <p><strong>Clínica:</strong> ${review.clinic?.name || 'N/A'}</p>
                    <p><strong>Calificación:</strong> ${generateStarRating(review.rating)} (${review.rating || 0}/5)</p>
                    <p><strong>Fecha:</strong> ${new Date(review.date).toLocaleString()}</p>
                    <p><strong>Contenido:</strong></p>
                    <div class="review-content">${review.content || 'Sin contenido'}</div>
                </div>
            `);
        }
    } catch (error) {
        showMessage('Error al cargar detalles de la review', 'error');
    }
}

async function deleteReview(reviewId) {
    if (confirm('¿Estás seguro de eliminar esta review?')) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.REVIEWS}/${reviewId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                showMessage('Review eliminada exitosamente', 'success');
                await loadReviewsData();
            }
        } catch (error) {
            showMessage('Error al eliminar review', 'error');
        }
    }
}

// Refresh functions
async function refreshUsers() {
    await loadUsersData();
}

async function refreshContactForms() {
    await loadContactFormsData();
}

async function refreshReviews() {
    await loadReviewsData();
}

// Modal functions
function showModal(content) {
    const modal = document.createElement('div');
    modal.className = 'admin-modal';
    modal.innerHTML = `
        <div class="admin-modal-content">
            <div class="admin-modal-header">
                <button class="admin-modal-close" onclick="closeModal()">&times;</button>
            </div>
            <div class="admin-modal-body">
                ${content}
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // Close on background click
    modal.addEventListener('click', (e) => {
        if (e.target === modal) closeModal();
    });
}

function closeModal() {
    const modal = document.querySelector('.admin-modal');
    if (modal) {
        modal.remove();
    }
}

function showMessage(message, type = 'info') {
    const messageDiv = document.createElement('div');
    messageDiv.className = `admin-message ${type}`;
    messageDiv.textContent = message;
    
    document.body.appendChild(messageDiv);
    
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}

function showAdminLoading(show) {
    const loadingElement = document.getElementById('admin-loading');
    if (loadingElement) {
        loadingElement.style.display = show ? 'flex' : 'none';
    }
}

function handleAdminLogout() {
    if (confirm('¿Estás seguro de cerrar sesión?')) {
        AuthService.logout();
        window.location.href = 'login.html';
    }
}

// Add treatment modal function
function showAddTreatmentModal() {
    showModal(`
        <h3>Agregar Nuevo Tratamiento</h3>
        <form id="addTreatmentForm" class="admin-form">
            <div class="form-group">
                <label for="treatmentName">Nombre:</label>
                <input type="text" id="treatmentName" required>
            </div>
            <div class="form-group">
                <label for="treatmentDescription">Descripción:</label>
                <textarea id="treatmentDescription" rows="4"></textarea>
            </div>
            <div class="form-group">
                <label for="treatmentDuration">Duración:</label>
                <input type="text" id="treatmentDuration">
            </div>
            <div class="form-group">
                <label for="treatmentPrice">Precio base:</label>
                <input type="number" id="treatmentPrice" min="0">
            </div>
            <div class="form-actions">
                <button type="submit" class="admin-btn primary">Crear Tratamiento</button>
                <button type="button" class="admin-btn secondary" onclick="closeModal()">Cancelar</button>
            </div>
        </form>
    `);
    
    document.getElementById('addTreatmentForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const treatmentData = {
            name: document.getElementById('treatmentName').value,
            description: document.getElementById('treatmentDescription').value,
            duration: document.getElementById('treatmentDuration').value,
            basePrice: document.getElementById('treatmentPrice').value
        };
        
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.TREATMENTS, {
                method: 'POST',
                body: JSON.stringify(treatmentData)
            });
            
            if (response.ok) {
                showMessage('Tratamiento creado exitosamente', 'success');
                closeModal();
                await loadTreatmentsData();
            } else {
                showMessage('Error al crear tratamiento', 'error');
            }
        } catch (error) {
            showMessage('Error al crear tratamiento', 'error');
        }
    });
}
