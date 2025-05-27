document.addEventListener('DOMContentLoaded', function() {
    // Check authentication
    checkAuthentication();
    
    // Initialize page
    initializePage();
    
    // Load user data
    loadUserData();
    
    // Setup event listeners
    setupEventListeners();
});

function checkAuthentication() {
    if (!AuthService.isAuthenticated()) {
        window.location.href = 'login.html';
        return;
    }
}

function initializePage() {
    // Show loading
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'flex';
    }
    
    // Hide loading after delay
    setTimeout(() => {
        if (loadingOverlay) {
            loadingOverlay.style.display = 'none';
        }
    }, 1000);
}

function setupEventListeners() {
    // User menu toggle
    const userAvatar = document.getElementById('userAvatar');
    const userDropdown = document.getElementById('userDropdown');
    
    if (userAvatar && userDropdown) {
        userAvatar.addEventListener('click', (e) => {
            e.stopPropagation();
            userDropdown.classList.toggle('show');
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', () => {
            userDropdown.classList.remove('show');
        });
    }
    
    // Logout button
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            handleLogout();
        });
    }
    
    // Mobile menu toggle
    const mobileToggle = document.getElementById('mobileToggle');
    const navMenu = document.getElementById('navMenu');
    
    if (mobileToggle && navMenu) {
        mobileToggle.addEventListener('click', () => {
            navMenu.classList.toggle('active');
            mobileToggle.classList.toggle('active');
        });
    }
    
    // Action cards
    const actionCards = document.querySelectorAll('.action-card');
    actionCards.forEach(card => {
        card.addEventListener('click', () => {
            const action = card.dataset.action;
            handleActionCard(action);
        });
    });
    
    // Navigation links
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const target = link.getAttribute('href');
            handleNavigation(target);
        });
    });
}

async function loadUserData() {
    try {
        // Get user from localStorage first (faster)
        const localUser = UserService.getCurrentUserFromStorage();
        if (localUser) {
            updateUserDisplay(localUser);
        }
        
        // Simulate data while no backend
        const mockUser = {
            name: localUser?.name || 'Usuario',
            email: localUser?.email || 'usuario@odontomar.com'
        };
        updateUserDisplay(mockUser);
        
    } catch (error) {
        console.error('Error loading user data:', error);
        const fallbackUser = { name: 'Usuario', email: 'usuario@odontomar.com' };
        updateUserDisplay(fallbackUser);
    }
}

function updateUserDisplay(user) {
    // Update user name in navigation
    const userAvatar = document.getElementById('userAvatar');
    if (userAvatar && user.name) {
        const initials = user.name.split(' ').map(n => n[0]).join('').toUpperCase();
        userAvatar.innerHTML = `<span>${initials}</span>`;
        userAvatar.title = user.name;
    }
    
    // Update welcome message if exists
    const welcomeText = document.querySelector('.hero-text h1');
    if (welcomeText && user.name) {
        const firstName = user.name.split(' ')[0];
        welcomeText.innerHTML = `Bienvenido ${firstName} a <span class="highlight">Odontomar</span>`;
    }
}

function handleActionCard(action) {
    switch(action) {
<<<<<<< HEAD
        case 'agendar-cita':
            setTimeout(() => {
                hideLoading();
                UIUtils.showToast('Redirigiendo a agendar cita dental...', 'info');
                // window.location.href = 'appointments.html';
            }, 1000);
            break;
            
        case 'operatoria-dental':
            setTimeout(() => {
                hideLoading();
                showPricingModal('operatoria');
            }, 1000);
            break;
            
        case 'implantes':
            setTimeout(() => {
                hideLoading();
                showPricingModal('implantes');
            }, 1000);
            break;
            
        case 'estetica-dental':
            setTimeout(() => {
                hideLoading();
                showPricingModal('estetica');
            }, 1000);
            break;
            
        case 'buscar-medicos':
            setTimeout(() => {
                hideLoading();
                UIUtils.showToast('Mostrando especialistas dentales...', 'info');
                // window.location.href = 'specialists.html';
            }, 1000);
=======
        case 'operatoria':
            console.log('Redirigiendo a operatoria dental');
            break;
        case 'endodoncia':
            console.log('Redirigiendo a endodoncia');
            break;
        case 'implantes':
            console.log('Redirigiendo a implantología');
            break;
        case 'estetica':
            console.log('Redirigiendo a estética dental');
>>>>>>> fbf3d3672da66eaf7c35854c17498cd3d53f08ce
            break;
        default:
            console.log('Action not implemented:', action);
    }
}

<<<<<<< HEAD
function showPricingModal(service) {
    const pricingData = {
        operatoria: {
            title: 'Operatoria Dental',
            services: [
                { name: 'Obturación 1 superficie', price: '$100.000' },
                { name: 'Obturación hasta 3 superficies', price: '$300.000' },
                { name: 'Resinas con nanocerámicos', price: 'Consultar' }
            ]
        },
        implantes: {
            title: 'Implantología Dental',
            services: [
                { name: 'Implante completo con corona metal cerámica', price: '$3.500.000' },
                { name: 'Regeneración ósea adicional', price: '+$1.000.000' },
                { name: 'Corona en circonio', price: '+$750.000' }
            ]
        },
        estetica: {
            title: 'Estética Dental',
            services: [
                { name: 'Aclaramiento dental (por sesión)', price: '$680.000' },
                { name: 'Diseño de sonrisa (20 dientes)', price: '$3.900.000' },
                { name: 'Carillas de silicato de litio', price: '$22.000.000' }
            ]
        }
    };
    
    const data = pricingData[service];
    if (data) {
        UIUtils.showToast(`${data.title}: ${data.services[0].name} desde ${data.services[0].price}`, 'info');
    }
}

function handleNavigation(e) {
    e.preventDefault();
    const href = e.target.getAttribute('href');
=======
function handleNavigation(target) {
    // Simple hash navigation for now
    window.location.hash = target;
>>>>>>> fbf3d3672da66eaf7c35854c17498cd3d53f08ce
    
    // Close mobile menu if open
    const navMenu = document.getElementById('navMenu');
    const mobileToggle = document.getElementById('mobileToggle');
    if (navMenu && mobileToggle) {
        navMenu.classList.remove('active');
        mobileToggle.classList.remove('active');
    }
}

<<<<<<< HEAD
function loadUserData() {
    const user = AuthService.getCurrentUser();
    
    if (user) {
        // Update user avatar with initials
        updateUserAvatar(user.name || user.email);
        
        // Load dental-specific data
        loadUserAppointments();
        loadDentalHistory();
        loadLocationPreferences();
    }
}

function updateUserAvatar(name) {
    const userAvatar = document.getElementById('userAvatar');
    if (userAvatar && name) {
        const initials = name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
        userAvatar.innerHTML = `<span>${initials}</span>`;
    }
}

function loadUserAppointments() {
    // Simulate loading appointments
    // In a real app, this would fetch from the API
    console.log('Loading user appointments...');
}

function loadDentalHistory() {
    // Simulate loading dental history
    console.log('Loading dental history...');
}

function loadLocationPreferences() {
    // Load preferred clinic location
    console.log('Loading location preferences...');
}

function loadUserNotifications() {
    // Simulate loading notifications
    // In a real app, this would fetch from the API
    console.log('Loading user notifications...');
}

function handleLogout(e) {
    e.preventDefault();
    
=======
async function handleLogout() {
>>>>>>> fbf3d3672da66eaf7c35854c17498cd3d53f08ce
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        try {
            AuthService.logout();
        } catch (error) {
            console.error('Error during logout:', error);
            // Force logout anyway
            localStorage.clear();
            window.location.href = 'login.html';
        }
    }
<<<<<<< HEAD
`;

// Inject mobile styles
const styleSheet = document.createElement('style');
styleSheet.textContent = mobileNavStyles;
document.head.appendChild(styleSheet);

// Add dental clinic information
const clinicInfo = {
    director: 'Dr. Carlos Monroy',
    registration: '79 708 910',
    experience: '16 años',
    locations: ['Marsella', 'Chapinero', 'Lago de los Héroes'],
    specialties: [
        'Operatoria Dental',
        'Endodoncia', 
        'Implantología',
        'Estética Dental',
        'Ortodoncia',
        'Periodoncia',
        'Cirugía Oral',
        'Rehabilitación Oral'
    ],
    equipment: [
        'Unidades odontológicas eléctricas',
        'Escáner dental digital',
        'Localizadores apicales',
        'Lámpara para aclaramiento',
        'Piezas de alta velocidad',
        'Servicio de radiología'
    ]
};

// Display clinic information function
function displayClinicInfo() {
    console.log('Odontomar Clinic Info:', clinicInfo);
}

// Initialize clinic info on page load
document.addEventListener('DOMContentLoaded', function() {
    // ...existing code...
    displayClinicInfo();
});
=======
}
>>>>>>> fbf3d3672da66eaf7c35854c17498cd3d53f08ce
