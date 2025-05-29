// Admin Panel Main Logic
document.addEventListener('DOMContentLoaded', function() {
    console.log('🛡️ Admin panel loaded');
    initializeAdminPanel();
});

let currentUser = null;
let allUsers = [];
let allRoles = [];
let blockedUsers = [];
let dashboardCharts = {}; // Store chart instances

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
        // Load basic stats
        await Promise.all([
            loadUsersCount(),
            loadContactFormsCount(),
            loadTreatmentsCount(),
            loadReviewsCount()
        ]);
        
        // Load analytics data
        await loadDashboardAnalytics();
        
        // Load recent activity
        await loadRecentActivity();
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
    }
}

async function loadDashboardAnalytics() {
    try {
        console.log('📈 Loading dashboard analytics...');
        const analytics = await AdminService.getDashboardAnalytics();
        
        // Update detailed stats
        updateDetailedStats(analytics);
        
        // Update trends in stat cards
        updateStatTrends(analytics);
        
        // Create charts
        createDashboardCharts(analytics);
        
        console.log('✅ Dashboard analytics loaded successfully');
    } catch (error) {
        console.error('Error loading dashboard analytics:', error);
        UIUtils.showToast('Error al cargar análisis del dashboard', 'error');
    }
}

function updateDetailedStats(analytics) {
    // User analytics
    document.getElementById('verified-users').textContent = analytics.users?.verified_count || analytics.users?.verified || 0;
    document.getElementById('blocked-users').textContent = blockedUsers.length;
    document.getElementById('admin-users').textContent = analytics.users?.admin_count || analytics.users?.admins || 0;
    document.getElementById('new-users-week').textContent = analytics.users?.new_users_week || 0;
    document.getElementById('last-user-date').textContent = analytics.users?.last_user_date || '-';

    // Form analytics
    document.getElementById('forms-today').textContent = analytics.forms?.forms_today || 0;
    document.getElementById('forms-week').textContent = analytics.forms?.forms_week || 0;
    document.getElementById('most-common-inquiry').textContent = analytics.forms?.most_common_inquiry_type || analytics.forms?.mostCommonInquiry || '-';
    document.getElementById('most-consulted-treatment').textContent = analytics.forms?.most_consulted_treatment_name || analytics.forms?.mostConsultedTreatment || '-';
    document.getElementById('last-form-date').textContent = analytics.forms?.last_form_date || '-';

    // Treatment analytics
    document.getElementById('active-treatments').textContent = analytics.treatments?.active_count || 0;

    // Review analytics
    document.getElementById('avg-rating').textContent = analytics.reviews?.average_rating || analytics.reviews?.avgRating || '0.0';
    document.getElementById('five-star-reviews').textContent = analytics.reviews?.five_star_reviews_count || analytics.reviews?.fiveStarReviews || 0;
    document.getElementById('one-star-reviews').textContent = analytics.reviews?.one_star_reviews_count || analytics.reviews?.oneStarReviews || 0;
    document.getElementById('reviews-week').textContent = analytics.reviews?.reviews_this_week_count || analytics.reviews?.reviewsWeek || 0;
    document.getElementById('last-review-date').textContent = analytics.reviews?.last_review_date || '-';
}

function updateStatTrends(analytics) {
    // Update user trend
    const usersTrend = document.getElementById('users-trend');
    if (usersTrend && analytics.users) {
        const growth = analytics.users.monthlyGrowth || 0;
        usersTrend.textContent = `+${growth}% este mes`;
        usersTrend.className = `stat-trend ${growth > 0 ? 'positive' : growth < 0 ? 'negative' : 'neutral'}`;
    }

    // Update forms trend
    const formsTrend = document.getElementById('forms-trend');
    if (formsTrend && analytics.forms) {
        const growth = analytics.forms.monthlyGrowth || 0;
        formsTrend.textContent = `+${growth}% este mes`;
        formsTrend.className = `stat-trend ${growth > 0 ? 'positive' : growth < 0 ? 'negative' : 'neutral'}`;
    }

    // Update reviews trend
    const reviewsTrend = document.getElementById('reviews-trend');
    if (reviewsTrend && analytics.reviews) {
        reviewsTrend.textContent = `Promedio: ${analytics.reviews.avgRating || analytics.reviews.average_rating || 0}`;
        reviewsTrend.className = 'stat-trend neutral';
    }
}

function createDashboardCharts(analytics) {
    // Destroy existing charts
    Object.values(dashboardCharts).forEach(chart => {
        if (chart) chart.destroy();
    });
    dashboardCharts = {};

    // Only create charts if analytics data is available
    if (analytics.users?.registrationTrend) {
        createUsersChart(analytics.users.registrationTrend);
    }
    
    if (analytics.forms?.treatmentTrend) {
        createContactFormsChart(analytics.forms.treatmentTrend);
    }
    
    if (analytics.reviews?.ratingDistribution) {
        createReviewsChart(analytics.reviews.ratingDistribution);
    }
    
    if (analytics.trends?.activityByHour) {
        createActivityChart(analytics.trends.activityByHour);
    }
}

function createUsersChart(registrationTrend) {
    const ctx = document.getElementById('usersChart');
    if (!ctx) return;

    const labels = Array.from({length: 30}, (_, i) => {
        const date = new Date();
        date.setDate(date.getDate() - (29 - i));
        return date.toLocaleDateString('es-ES', { month: 'short', day: 'numeric' });
    });

    dashboardCharts.users = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Nuevos Usuarios',
                data: registrationTrend,
                borderColor: '#2c5aa0',
                backgroundColor: 'rgba(44, 90, 160, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}

function createContactFormsChart(treatmentTrend) {
    const ctx = document.getElementById('contactFormsChart');
    if (!ctx) return;

    const topTreatments = treatmentTrend.slice(0, 8); // Show top 8 treatments

    dashboardCharts.contactForms = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: topTreatments.map(t => t.name),
            datasets: [{
                label: 'Consultas',
                data: topTreatments.map(t => t.count),
                backgroundColor: [
                    '#2c5aa0', '#4a90e2', '#27ae60', '#f39c12',
                    '#e74c3c', '#9b59b6', '#1abc9c', '#34495e'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}

function createReviewsChart(ratingDistribution) {
    const ctx = document.getElementById('reviewsChart');
    if (!ctx) return;

    const data = [1, 2, 3, 4, 5].map(rating => ratingDistribution[rating] || 0);

    dashboardCharts.reviews = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['1 Estrella', '2 Estrellas', '3 Estrellas', '4 Estrellas', '5 Estrellas'],
            datasets: [{
                data: data,
                backgroundColor: [
                    '#e74c3c', '#f39c12', '#f1c40f', '#2ecc71', '#27ae60'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function createActivityChart(activityByHour) {
    const ctx = document.getElementById('activityChart');
    if (!ctx) return;

    const labels = Array.from({length: 24}, (_, i) => `${i}:00`);

    dashboardCharts.activity = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Actividad',
                data: activityByHour,
                backgroundColor: 'rgba(74, 144, 226, 0.8)',
                borderColor: '#4a90e2',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}

async function loadUsersCount() {
    try {
        const users = await UserService.getAllUsers();
        document.getElementById('total-users').textContent = users?.length || 0;
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
        const treatments = await TreatmentService.getAllTreatments();
        document.getElementById('total-treatments').textContent = treatments?.length || 0;
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
                    const isBlocked = blockedUsers.some(blocked => blocked.user_id === user.id || (blocked.user && blocked.user.id === user.id));
                    const isCurrentUser = user.id === currentUser.id;
                    const isAdmin = user.role?.name === 'ADMIN';
                    
                    return `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.email}</td>
                            <td>
                                <span class="role-badge ${isAdmin ? 'admin' : 'user'}">
                                    ${isAdmin ? 'Admin' : 'Usuario'}
                                </span>
                            </td>
                            <td>
                                <span class="status-badge ${user.is_verified || user.verified ? 'verified' : 'unverified'}">
                                    ${user.is_verified || user.verified ? 'Sí' : 'No'}
                                </span>
                            </td>
                            <td>
                                <span class="status-badge ${isBlocked ? 'blocked' : 'active'}">
                                    ${isBlocked ? 'Bloqueado' : 'Activo'}
                                </span>
                            </td>
                            <td>${new Date(user.created_at || user.createdAt).toLocaleDateString()}</td>
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
                        <td>${form.full_name || form.fullName}</td>
                        <td>${form.email}</td>
                        <td>${form.treatment?.name || 'N/A'}</td>
                        <td>${form.inquiry_type || form.inquiryType}</td>
                        <td>${new Date(form.created_at || form.createdDate).toLocaleDateString()}</td>
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
        
        const treatments = await TreatmentService.getAllTreatments();
        
        if (treatments && Array.isArray(treatments)) {
            displayTreatmentsTable(treatments);
            updateTreatmentAnalytics(treatments);
        } else {
            throw new Error('Formato de datos de tratamientos inesperado');
        }
    } catch (error) {
        console.error('Error loading treatments:', error);
        treatmentsContainer.innerHTML = `<p class="error">Error al cargar tratamientos: ${error.message}</p>`;
    }
}

function displayTreatmentsTable(treatments) {
    const container = document.getElementById('treatments-list');
    if (!container) {
        console.error('Treatments container not found');
        return;
    }

    if (!treatments || treatments.length === 0) {
        container.innerHTML = '<p>No hay tratamientos para mostrar.</p>';
        return;
    }

    const tableHTML = `
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Fecha Creación</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                ${treatments.map(treatment => `
                    <tr>
                        <td>${treatment.id || 'N/A'}</td>
                        <td>${treatment.name || 'Sin nombre'}</td>
                        <td>${treatment.description || 'Sin descripción'}</td>
                        <td>${treatment.created_at ? new Date(treatment.created_at).toLocaleDateString() : 'N/A'}</td>
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

function updateTreatmentAnalytics(treatments) {
    document.getElementById('active-treatments').textContent = treatments.length;
    
    // Find most recent treatment
    const sortedTreatments = treatments.filter(t => t.created_at)
                                      .sort((a, b) => new Date(b.created_at) - new Date(a.created_at));
    if (sortedTreatments.length > 0) {
        document.getElementById('last-treatment-date').textContent = 
            new Date(sortedTreatments[0].created_at).toLocaleDateString();
    }
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
                        <td>${generateStarRating(review.rating)} (${review.rating || 0})</td>
                        <td>${new Date(review.created_at || review.date).toLocaleDateString()}</td>
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

// User management functions
async function viewUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    const blockedInfo = blockedUsers.find(blocked => blocked.user_id === userId || (blocked.user && blocked.user.id === userId));
    
    showModal(`
        <h3>Detalles del Usuario</h3>
        <div class="user-details">
            <p><strong>ID:</strong> ${user.id}</p>
            <p><strong>Nombre:</strong> ${user.name}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Rol:</strong> ${user.role?.name === 'ADMIN' ? 'Administrador' : 'Usuario'}</p>
            <p><strong>Verificado:</strong> ${user.is_verified || user.verified ? 'Sí' : 'No'}</p>
            <p><strong>Fecha de registro:</strong> ${new Date(user.created_at || user.createdAt).toLocaleString()}</p>
            ${blockedInfo ? `
                <p><strong>Estado:</strong> <span class="status-blocked">BLOQUEADO</span></p>
                <p><strong>Razón del bloqueo:</strong> ${blockedInfo.reason}</p>
                <p><strong>Fecha de bloqueo:</strong> ${new Date(blockedInfo.blocked_at || blockedInfo.date).toLocaleString()}</p>
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
            await AdminService.updateUserRole(userId, newRoleId);
            UIUtils.showToast('Rol actualizado exitosamente', 'success');
            await loadUsersData();
        } catch (error) {
            UIUtils.showToast('Error al actualizar rol: ' + error.message, 'error');
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
        UIUtils.showToast('Usuario bloqueado exitosamente', 'success');
        await loadUsersData();
        await loadInitialData();
    } catch (error) {
        UIUtils.showToast('Error al bloquear usuario: ' + error.message, 'error');
    }
}

async function unblockUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return;
    
    if (confirm(`¿Estás seguro de desbloquear a ${user.name}?`)) {
        try {
            const blockedEntry = blockedUsers.find(b => b.user_id === userId || (b.user && b.user.id === userId));
            if (blockedEntry) {
                await AdminService.unblockUser(blockedEntry.id);
                UIUtils.showToast('Usuario desbloqueado exitosamente', 'success');
                await loadUsersData();
                await loadInitialData();
            } else {
                UIUtils.showToast('No se encontró registro de bloqueo para este usuario.', 'warning');
            }
        } catch (error) {
            UIUtils.showToast('Error al desbloquear usuario: ' + error.message, 'error');
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
                    <p><strong>Nombre completo:</strong> ${form.full_name || form.fullName}</p>
                    <p><strong>Email:</strong> ${form.email}</p>
                    <p><strong>Teléfono:</strong> ${form.phone || 'No proporcionado'}</p>
                    <p><strong>Tratamiento:</strong> ${form.treatment?.name || 'N/A'}</p>
                    <p><strong>Tipo de consulta:</strong> ${form.inquiry_type || form.inquiryType}</p>
                    <p><strong>Clínica preferida:</strong> ${form.preferred_clinic || form.preferredClinic || 'No especificada'}</p>
                    <p><strong>Fecha:</strong> ${new Date(form.created_at || form.createdDate).toLocaleString()}</p>
                    <p><strong>Mensaje:</strong></p>
                    <div class="message-content">${form.message}</div>
                    <p><strong>Acepta términos:</strong> ${form.accept_terms || form.acceptTerms ? 'Sí' : 'No'}</p>
                    <p><strong>Acepta marketing:</strong> ${form.accept_marketing || form.acceptMarketing ? 'Sí' : 'No'}</p>
                </div>
            `);
        }
    } catch (error) {
        UIUtils.showToast('Error al cargar detalles de la consulta', 'error');
    }
}

async function deleteContactForm(formId) {
    if (confirm('¿Estás seguro de eliminar esta consulta?')) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.CONTACT_FORMS}/${formId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                UIUtils.showToast('Consulta eliminada exitosamente', 'success');
                await loadContactFormsData();
            }
        } catch (error) {
            UIUtils.showToast('Error al eliminar consulta', 'error');
        }
    }
}

// Treatment functions
async function viewTreatment(treatmentId) {
    try {
        const treatment = await TreatmentService.getTreatmentById(treatmentId);
        if (treatment) {
            showModal(`
                <h3>Detalles del Tratamiento</h3>
                <div class="treatment-details">
                    <p><strong>ID:</strong> ${treatment.id}</p>
                    <p><strong>Nombre:</strong> ${treatment.name}</p>
                    <p><strong>Descripción:</strong> ${treatment.description || 'Sin descripción'}</p>
                </div>
            `);
        }
    } catch (error) {
        UIUtils.showToast('Error al cargar detalles del tratamiento', 'error');
    }
}

async function editTreatment(treatmentId) {
    try {
        const treatment = await TreatmentService.getTreatmentById(treatmentId);
        if (!treatment) {
            UIUtils.showToast('Error al cargar tratamiento', 'error');
            return;
        }

        showModal(`
            <h3>Editar Tratamiento</h3>
            <form id="editTreatmentForm" class="admin-form">
                <div class="form-group">
                    <label for="editTreatmentName">Nombre:</label>
                    <input type="text" id="editTreatmentName" value="${treatment.name || ''}" required>
                </div>
                <div class="form-group">
                    <label for="editTreatmentDescription">Descripción:</label>
                    <textarea id="editTreatmentDescription" rows="4">${treatment.description || ''}</textarea>
                </div>
                <div class="form-actions">
                    <button type="submit" class="admin-btn primary">Actualizar Tratamiento</button>
                    <button type="button" class="admin-btn secondary" onclick="closeModal()">Cancelar</button>
                </div>
            </form>
        `);
        
        document.getElementById('editTreatmentForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const treatmentData = {
                name: document.getElementById('editTreatmentName').value.trim(),
                description: document.getElementById('editTreatmentDescription').value.trim()
            };

            if (!treatmentData.description) {
                delete treatmentData.description;
            }
            
            try {
                const updated = await TreatmentService.updateTreatment(treatmentId, treatmentData);
                
                if (updated) {
                    UIUtils.showToast('Tratamiento actualizado exitosamente', 'success');
                    closeModal();
                    await loadTreatmentsData();
                } else {
                    UIUtils.showToast('Error al actualizar tratamiento', 'error');
                }
            } catch (error) {
                UIUtils.showToast('Error al actualizar tratamiento', 'error');
            }
        });
    } catch (error) {
        UIUtils.showToast('Error al cargar tratamiento para edición', 'error');
    }
}

async function deleteTreatment(treatmentId) {
    if (confirm('¿Estás seguro de eliminar este tratamiento?')) {
        try {
            const result = await TreatmentService.deleteTreatment(treatmentId);
            if (result) {
                UIUtils.showToast('Tratamiento eliminado exitosamente', 'success');
                await loadTreatmentsData();
            } else {
                UIUtils.showToast('Error al eliminar tratamiento', 'error');
            }
        } catch (error) {
            UIUtils.showToast('Error al eliminar tratamiento', 'error');
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
                <h3>Detalles de la Reseña</h3>
                <div class="review-details">
                    <p><strong>ID:</strong> ${review.id}</p>
                    <p><strong>Usuario:</strong> ${review.user?.name || 'Usuario eliminado'}</p>
                    <p><strong>Clínica:</strong> ${review.clinic?.name || 'N/A'}</p>
                    <p><strong>Calificación:</strong> ${generateStarRating(review.rating)} (${review.rating || 0})</p>
                    <p><strong>Fecha:</strong> ${new Date(review.created_at || review.date).toLocaleString()}</p>
                    <p><strong>Contenido:</strong></p>
                    <div class="review-content">${review.comment || review.content || 'Sin contenido'}</div>
                </div>
            `);
        }
    } catch (error) {
        UIUtils.showToast('Error al cargar detalles de la reseña', 'error');
    }
}

async function deleteReview(reviewId) {
    if (confirm('¿Estás seguro de eliminar esta reseña?')) {
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.REVIEWS}/${reviewId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                UIUtils.showToast('Reseña eliminada exitosamente', 'success');
                await loadReviewsData();
            }
        } catch (error) {
            UIUtils.showToast('Error al eliminar reseña', 'error');
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

// Utility functions
function showAdminLoading(show) {
    const loader = document.getElementById('admin-loading');
    if (loader) {
        loader.style.display = show ? 'flex' : 'none';
    }
}

function generateStarRating(rating) {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
        if (i <= rating) {
            stars.push('★');
        } else {
            stars.push('☆');
        }
    }
    return stars.join('');
}

// Logout function
function adminLogout() {
    if (confirm('¿Estás seguro de cerrar sesión?')) {
        AuthService.logout();
        window.location.href = 'login.html';
    }
}

//# sourceMappingURL=admin.js.map
