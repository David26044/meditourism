// Header functionality
document.addEventListener('DOMContentLoaded', function() {
    initializeHeader();
});

function initializeHeader() {
    updateAuthButtons();
    setupUserDropdown();
    
    // Actualizar información del usuario si está autenticado
    if (AuthService.isAuthenticated()) {
        UserService.updateUserDisplay();
        // Refrescar info del servidor y actualizar badge de admin
        UserService.refreshUserInfo().then(() => {
            UserService.updateUserDisplay();
        });
    }
}

function updateAuthButtons() {
    const authButtons = document.querySelector('.auth-buttons');
    const userMenu = document.querySelector('.user-menu');
    
    if (!authButtons) return;

    if (AuthService.isAuthenticated()) {
        // Usuario autenticado - mostrar menú de usuario
        authButtons.style.display = 'none';
        if (userMenu) {
            userMenu.style.display = 'flex';
        }
    } else {
        // Usuario no autenticado - mostrar botones de login/registro
        authButtons.style.display = 'flex';
        if (userMenu) {
            userMenu.style.display = 'none';
        }
    }
}

function setupUserDropdown() {
    const userMenuButton = document.querySelector('.user-menu-button');
    const dropdownMenu = document.querySelector('.user-dropdown');
    
    if (!userMenuButton || !dropdownMenu) return;

    userMenuButton.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropdownMenu.classList.toggle('show');
    });

    // Cerrar dropdown al hacer click fuera
    document.addEventListener('click', function(e) {
        if (!userMenuButton.contains(e.target)) {
            dropdownMenu.classList.remove('show');
        }
    });

    // Setup admin panel link - connect to profile.html admin tab
    const adminPanelLink = document.querySelector('.admin-panel-link');
    if (adminPanelLink) {
        if (UserService.isAdmin()) {
            adminPanelLink.style.display = 'block';
            adminPanelLink.href = 'profile.html';
            adminPanelLink.addEventListener('click', function(e) {
                e.preventDefault();
                localStorage.setItem('activeProfileTab', 'admin');
                window.location.href = 'profile.html';
            });
        } else {
            adminPanelLink.style.display = 'none';
        }
    }

    // Setup logout button
    const logoutButton = document.querySelector('.logout-btn');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(e) {
            e.preventDefault();
            AuthService.logout();
        });
    }
}
