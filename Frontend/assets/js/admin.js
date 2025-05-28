// Admin Panel Main Logic
document.addEventListener('DOMContentLoaded', function() {
    console.log('🛡️ Admin panel loaded');
    initializeAdminPanel();
});

let currentUser = null;

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
        
        console.log('✅ Admin panel initialized successfully');
    } catch (error) {
        console.error('💥 Error initializing admin panel:', error);
        alert('Error al cargar el panel de administración');
        window.location.href = 'home.html';
    } finally {
        showAdminLoading(false);
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
        
        const response = await apiRequest(API_CONFIG.ENDPOINTS.USERS);
        if (response.ok) {
            const users = await response.json();
            displayUsersTable(users);
        } else {
            throw new Error('Error al cargar usuarios');
        }
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
                    <th>Fecha Registro</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${users.map(user => `
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
                        <td>${new Date(user.createdAt).toLocaleDateString()}</td>
                        <td>
                            <button class="admin-btn small" onclick="viewUser(${user.id})">
                                <i class="fas fa-eye"></i>
                            </button>
                            ${user.role?.name !== 'ADMIN' ? `
                                <button class="admin-btn small danger" onclick="deleteUser(${user.id})">
                                    <i class="fas fa-trash"></i>
                                </button>
                            ` : ''}
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    usersContainer.innerHTML = tableHTML;
}

async function loadContactFormsData() {
    const formsContainer = document.getElementById('contact-forms-list');
    
    try {
        formsContainer.innerHTML = '<p class="loading">Cargando consultas...</p>';
        
        const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS);
        if (response.ok) {
            const forms = await response.json();
            displayContactFormsTable(forms);
        } else {
            throw new Error('Error al cargar consultas');
        }
    } catch (error) {
        console.error('Error loading contact forms:', error);
        formsContainer.innerHTML = '<p class="error">Error al cargar consultas</p>';
    }
}

function displayContactFormsTable(forms) {
    const formsContainer = document.getElementById('contact-forms-list');
    
    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Usuario</th>
                    <th>Email</th>
                    <th>Tratamiento</th>
                    <th>Tipo</th>
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
                        <td>${form.treatmentName || 'General'}</td>
                        <td>${form.inquiryType}</td>
                        <td>${new Date(form.createdAt).toLocaleDateString()}</td>
                        <td>
                            <span class="status-badge ${form.status?.toLowerCase() || 'pending'}">
                                ${form.status || 'Pendiente'}
                            </span>
                        </td>
                        <td>
                            <button class="admin-btn small" onclick="viewContactForm(${form.id})">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="admin-btn small primary" onclick="replyContactForm(${form.id})">
                                <i class="fas fa-reply"></i>
                            </button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    formsContainer.innerHTML = tableHTML;
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

// Placeholder functions for actions
function viewUser(userId) {
    alert(`Ver usuario ${userId} - Función en desarrollo`);
}

function deleteUser(userId) {
    if (confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
        alert(`Eliminar usuario ${userId} - Función en desarrollo`);
    }
}

function viewContactForm(formId) {
    alert(`Ver consulta ${formId} - Función en desarrollo`);
}

function replyContactForm(formId) {
    alert(`Responder consulta ${formId} - Función en desarrollo`);
}

function refreshUsers() {
    loadUsersData();
}

function refreshContactForms() {
    loadContactFormsData();
}

function refreshReviews() {
    loadReviewsData();
}

// Load more data functions (placeholders)
async function loadTreatmentsData() {
    console.log('Loading treatments data...');
}

async function loadReviewsData() {
    console.log('Loading reviews data...');
}
