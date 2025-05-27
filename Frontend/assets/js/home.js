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
            break;
        default:
            console.log('Action not implemented:', action);
    }
}

function handleNavigation(target) {
    // Simple hash navigation for now
    window.location.hash = target;
    
    // Close mobile menu if open
    const navMenu = document.getElementById('navMenu');
    const mobileToggle = document.getElementById('mobileToggle');
    if (navMenu && mobileToggle) {
        navMenu.classList.remove('active');
        mobileToggle.classList.remove('active');
    }
}

async function handleLogout() {
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
}
