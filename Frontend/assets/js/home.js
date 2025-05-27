document.addEventListener('DOMContentLoaded', function() {
    // Check authentication
    if (!AuthService.isAuthenticated()) {
        window.location.href = 'login.html';
        return;
    }

    // Initialize page
    initializePage();
    
    // Event listeners
    setupEventListeners();
    
    // Load user data
    loadUserData();
});

function initializePage() {
    // Hide loading overlay
    hideLoading();
    
    // Setup user menu
    setupUserMenu();
    
    // Setup mobile navigation
    setupMobileNavigation();
    
    // Setup action cards
    setupActionCards();
}

function setupEventListeners() {
    // Logout button
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
    
    // Mobile toggle
    const mobileToggle = document.getElementById('mobileToggle');
    if (mobileToggle) {
        mobileToggle.addEventListener('click', toggleMobileMenu);
    }
    
    // Smooth scrolling for navigation links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', handleNavigation);
    });
}

function setupUserMenu() {
    const userAvatar = document.getElementById('userAvatar');
    const userDropdown = document.getElementById('userDropdown');
    
    if (userAvatar && userDropdown) {
        userAvatar.addEventListener('click', (e) => {
            e.stopPropagation();
            const userMenu = userAvatar.closest('.user-menu');
            userMenu.classList.toggle('active');
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', (e) => {
            const userMenu = userAvatar.closest('.user-menu');
            if (!userMenu.contains(e.target)) {
                userMenu.classList.remove('active');
            }
        });
    }
}

function setupMobileNavigation() {
    const mobileToggle = document.getElementById('mobileToggle');
    const navMenu = document.getElementById('navMenu');
    
    if (mobileToggle && navMenu) {
        // Create mobile menu if it doesn't exist
        if (!document.querySelector('.mobile-nav')) {
            createMobileNav();
        }
    }
}

function createMobileNav() {
    const mobileNav = document.createElement('div');
    mobileNav.className = 'mobile-nav';
    
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        const mobileLink = link.cloneNode(true);
        mobileNav.appendChild(mobileLink);
    });
    
    document.body.appendChild(mobileNav);
}

function toggleMobileMenu() {
    const mobileNav = document.querySelector('.mobile-nav');
    if (mobileNav) {
        mobileNav.classList.toggle('active');
    }
}

function setupActionCards() {
    const actionCards = document.querySelectorAll('.action-card');
    
    actionCards.forEach(card => {
        const button = card.querySelector('.action-btn');
        const action = card.dataset.action;
        
        if (button) {
            button.addEventListener('click', () => handleActionCard(action));
        }
    });
}

function handleActionCard(action) {
    showLoading();
    
    switch(action) {
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
            break;
            
        default:
            hideLoading();
            UIUtils.showToast('Función en desarrollo', 'info');
    }
}

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
    
    if (href.startsWith('#')) {
        // Smooth scroll to section
        const target = document.querySelector(href);
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    } else {
        // Navigate to page
        window.location.href = href;
    }
}

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
    
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
        showLoading();
        
        setTimeout(() => {
            AuthService.logout();
        }, 1000);
    }
}

function showLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'flex';
    }
}

function hideLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'none';
    }
}

// Add mobile navigation styles dynamically
const mobileNavStyles = `
    .mobile-nav {
        position: fixed;
        top: 70px;
        left: 0;
        right: 0;
        background: var(--white);
        box-shadow: var(--shadow-medium);
        transform: translateX(-100%);
        transition: var(--transition);
        z-index: 999;
        padding: 20px;
    }
    
    .mobile-nav.active {
        transform: translateX(0);
    }
    
    .mobile-nav .nav-link {
        display: block;
        padding: 15px 0;
        border-bottom: 1px solid var(--gray-light);
        text-decoration: none;
        color: var(--text-primary);
    }
    
    .mobile-nav .nav-link:last-child {
        border-bottom: none;
    }
    
    @media (min-width: 769px) {
        .mobile-nav {
            display: none;
        }
    }
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
