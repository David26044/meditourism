document.addEventListener('DOMContentLoaded', function() {
    // Configuración de la API
    const API_BASE_URL = 'http://localhost:8080/system/api'; // Updated base URL
    
    // Elementos del DOM
    const container = document.querySelector('.container');
    const registerBtn = document.querySelector('.register-btn');
    const loginBtn = document.querySelector('.login-btn');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const loginMessage = document.getElementById('loginMessage');
    const registerMessage = document.getElementById('registerMessage');

    // Configuración común para fetch
    const fetchConfig = {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'ngrok-skip-browser-warning': 'true'
        }
    };

    // Función para mostrar mensajes
    function showMessage(element, message, type) {
        element.textContent = message;
        element.className = 'message ' + type;
        setTimeout(() => {
            element.textContent = '';
            element.className = 'message';
        }, 5000);
    }

    // Función para enviar correo de confirmación
    async function sendConfirmationEmail(email, username) {
        try {
            const response = await fetch(`${API_BASE_URL}/send-welcome-email`, {
                ...fetchConfig,
                method: 'POST',
                body: JSON.stringify({
                    recipient: email,
                    subject: '¡Bienvenido a Turismo Médico!',
                    body: `
                        ¡Gracias por registrarte, ${username}!
                        Tu cuenta en Turismo Médico ha sido creada exitosamente.
                        Ahora puedes acceder a todos nuestros servicios médicos.
                        Si no realizaste este registro, por favor contacta con soporte.
                    `
                })
            });
            
            if (!response.ok) {
                console.warn('El correo no pudo enviarse, pero el registro fue exitoso');
            }
        } catch (error) {
            console.error('Error enviando correo:', error);
        }
    }

    // Variable para almacenar la información del usuario autenticado
    let cachedUser = null;

    // Función para verificar si el usuario está logeado
    async function checkUserSession() {
        if (cachedUser) {
            // Si ya tenemos la información del usuario, no hacemos otra solicitud
            updateUserUI(cachedUser);
            return;
        }

        const authToken = localStorage.getItem('authToken');
        if (authToken) {
            try {
                const response = await fetch(`${API_BASE_URL}/users/me`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const user = await response.json();
                    cachedUser = user; // Cachear la información del usuario
                    updateUserUI(user);
                } else {
                    console.error('No se pudo obtener la información del usuario');
                }
            } catch (error) {
                console.error('Error al verificar la sesión del usuario:', error);
            }
        }
    }

    // Función para actualizar la interfaz con la información del usuario
    function updateUserUI(user) {
        const userActions = document.querySelector('.user-actions');
        userActions.innerHTML = `<span class="user-name">Hola, ${user.name}</span>`;
    }

    // Llamar a la función para verificar la sesión del usuario al cargar la página
    checkUserSession();

    // Manejar el registro
    async function handleRegister(e) {
        e.preventDefault();
        
        const username = document.getElementById('registerUsername').value;
        const email = document.getElementById('registerEmail').value;
        const password = document.getElementById('registerPassword').value;

        // Validación básica
        if (!username || !email || !password) {
            showMessage(registerMessage, 'Por favor completa todos los campos', 'error');
            return;
        }

        // Validación de email
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            showMessage(registerMessage, 'Por favor ingresa un email válido', 'error');
            return;
        }

        try {
            // 1. Registrar al usuario
            const registerResponse = await fetch(`${API_BASE_URL}/auth/register`, { // Updated endpoint
                ...fetchConfig,
                method: 'POST',
                body: JSON.stringify({
                    name: username,
                    email: email,
                    password: password
                })
            });

            if (registerResponse.ok) {
                // 2. Enviar correo de confirmación (en segundo plano)
                sendConfirmationEmail(email, username);
                
                // 3. Mostrar mensaje al usuario
                showMessage(registerMessage, '¡Registro exitoso! Revisa tu correo para la confirmación.', 'success');
                
                // 4. Cambiar a vista de login después de 3 segundos
                setTimeout(() => {
                    container.classList.remove('active');
                    registerForm.reset();
                }, 3000);
                
            } else if (registerResponse.status === 409) {
                showMessage(registerMessage, 'Este correo ya está registrado', 'error');
            } else {
                showMessage(registerMessage, 'Error en el registro. Intenta nuevamente.', 'error');
            }
        } catch (error) {
            console.error('Error en registro:', error);
            showMessage(registerMessage, 'Error de conexión con el servidor', 'error');
        }
    }

    // Manejar el login
    async function handleLogin(e) {
        e.preventDefault();
        
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, { // Updated endpoint
                ...fetchConfig,
                method: 'POST',
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });

            if (response.status === 200) {
                const data = await response.json();
                
                if (!data.token) {
                    throw new Error('El servidor no devolvió un token válido');
                }
                
                localStorage.setItem('authToken', data.token);
                window.location.href = 'home.html';
                
            } else if (response.status === 401) {
                showMessage(loginMessage, 'Contraseña incorrecta', 'error');
            } else if (response.status === 404) {
                showMessage(loginMessage, 'Usuario no encontrado', 'error');
            } else {
                const errorData = await response.json().catch(() => ({}));
                showMessage(loginMessage, errorData.message || `Error inesperado (${response.status})`, 'error');
            }
            
        } catch (error) {
            console.error('Error en login:', error);
            showMessage(loginMessage, error.message || 'Error de conexión con el servidor', 'error');
        }
    }

    // Variable global para almacenar el rating seleccionado
    let selectedRating = 0;

    // Función para manejar el rating con estrellas
    function handleStarRating() {
        const stars = document.querySelectorAll('.rating__star');
        stars.forEach((star, index) => {
            star.addEventListener('click', () => {
                // Actualizar el rating seleccionado
                selectedRating = index + 1;

                // Remover la clase 'active' de todas las estrellas
                stars.forEach(s => s.classList.remove('active'));

                // Agregar la clase 'active' a las estrellas hasta la seleccionada
                for (let i = 0; i <= index; i++) {
                    stars[i].classList.add('active');
                }
            });
        });
    }

    // Manejar el envío del formulario de reseñas
    const commentForm = document.querySelector('.comment-form');
    if (commentForm) {
        commentForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            // Verificar que se haya seleccionado un rating
            if (selectedRating === 0) {
                alert('Por favor selecciona una calificación antes de enviar tu comentario.');
                return;
            }

            // Obtener el ID del usuario autenticado
            const authToken = localStorage.getItem('authToken');
            if (!authToken) {
                alert('Debes iniciar sesión para enviar una reseña.');
                return;
            }

            let userId;
            try {
                const userResponse = await fetch(`${API_BASE_URL}/users/me`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${authToken}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (userResponse.ok) {
                    const userData = await userResponse.json();
                    userId = userData.id; // Obtener el ID del usuario
                } else {
                    throw new Error('No se pudo obtener el ID del usuario.');
                }
            } catch (error) {
                console.error('Error al obtener el ID del usuario:', error);
                alert('Hubo un error al obtener tu información de usuario. Intenta nuevamente.');
                return;
            }

            // Crear el objeto reviewData con los datos del formulario
            const reviewData = {
                clinicId: document.getElementById('contact-service').value, // ID de la clínica
                content: document.getElementById('comment').value, // Contenido del comentario
                rating: selectedRating, // Calificación capturada
                date: new Date().toISOString(), // Fecha actual en formato ISO
                userId: userId // ID del usuario autenticado
            };

            console.log('Datos enviados:', reviewData); // Verificar los datos enviados

            // Enviar los datos al backend
            submitReview(reviewData);
        });
    }

    // Función para enviar la reseña al backend
    async function submitReview(reviewData) {
        const authToken = localStorage.getItem('authToken');
        try {
            const response = await fetch(`${API_BASE_URL}/reviews`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${authToken}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reviewData),
            });

            if (response.ok) {
                alert('Reseña enviada con éxito.');
                location.reload(); // Recargar la página para limpiar el formulario
            } else {
                const errorData = await response.json();
                console.error('Error al enviar la reseña:', errorData);
                alert('Hubo un error al enviar tu reseña. Intenta nuevamente.');
            }
        } catch (error) {
            console.error('Error al enviar la reseña:', error);
            alert('Hubo un error al conectar con el servidor.');
        }
    }

    // Función para obtener y mostrar las últimas 3 reseñas
    async function fetchLatestReviews() {
        try {
            const response = await fetch(`${API_BASE_URL}/reviews/latest`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });

            if (response.ok) {
                const reviews = await response.json();
                displayLatestReviews(reviews);
            } else {
                console.error('Error al obtener las reseñas:', response.status);
            }
        } catch (error) {
            console.error('Error al obtener las reseñas:', error);
        }
    }

    // Función para mostrar las últimas 3 reseñas en el DOM
    function displayLatestReviews(reviews) {
        const testimonialsGrid = document.querySelector('.testimonials-grid');
        testimonialsGrid.innerHTML = ''; // Limpiar contenido previo

        reviews.forEach(review => {
            const reviewElement = document.createElement('article');
            reviewElement.classList.add('testimonial-card');
            reviewElement.innerHTML = `
                <div class="testimonial-card__content">
                    <p class="testimonial-card__quote">"${review.content}"</p>
                </div>
                <div class="testimonial-card__rating">
                    <span class="testimonial-card__stars">${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</span>
                </div>
            `;
            testimonialsGrid.appendChild(reviewElement);
        });
    }

    // Llamar a la función para obtener y mostrar las últimas 3 reseñas al cargar la página
    fetchLatestReviews();

    // Llamar a la función para inicializar el manejo de estrellas
    handleStarRating();

    // Event Listeners
    registerBtn.addEventListener('click', () => container.classList.add('active'));
    loginBtn.addEventListener('click', () => container.classList.remove('active'));
    registerForm.addEventListener('submit', handleRegister);
    loginForm.addEventListener('submit', handleLogin);
});