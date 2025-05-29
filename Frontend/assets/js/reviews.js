class ReviewManager {
    constructor() {
        this.modal = null;
        this.form = null;
        this.clinics = [];
        this.currentUser = null;
        this.init();
    }

    init() {
        this.bindElements();
        this.bindEvents();
        this.loadClinics();
        this.checkUserAuthentication();
        this.loadLatestReviews();
    }

    bindElements() {
        this.modal = document.getElementById('reviewModal');
        this.form = document.getElementById('reviewForm');
        this.openModalBtn = document.getElementById('openReviewModalBtn');
        this.closeModalBtn = document.getElementById('closeReviewModal');
        this.cancelBtn = document.getElementById('cancelReviewBtn');
        this.reviewsList = document.getElementById('latestReviews');
        this.clinicSelect = document.getElementById('reviewClinic');
    }

    bindEvents() {
        if (this.openModalBtn) {
            this.openModalBtn.addEventListener('click', () => this.openModal());
        }

        if (this.closeModalBtn) {
            this.closeModalBtn.addEventListener('click', () => this.closeModal());
        }

        if (this.cancelBtn) {
            this.cancelBtn.addEventListener('click', () => this.closeModal());
        }

        if (this.form) {
            this.form.addEventListener('submit', (e) => this.handleSubmit(e));
        }

        if (this.modal) {
            this.modal.addEventListener('click', (e) => {
                if (e.target === this.modal) {
                    this.closeModal();
                }
            });
        }
    }

    async loadClinics() {
        try {
            const result = await ClinicService.getAllClinics();
            if (result.success) {
                this.clinics = result.data;
                this.populateClinicSelect();
            } else {
                console.error('Error loading clinics:', result.message);
            }
        } catch (error) {
            console.error('Error loading clinics:', error);
        }
    }

    populateClinicSelect() {
        if (!this.clinicSelect || !this.clinics) return;

        this.clinicSelect.innerHTML = '<option value="">Selecciona una clínica</option>';
        this.clinics.forEach(clinic => {
            const option = document.createElement('option');
            option.value = clinic.id;
            option.textContent = clinic.name;
            this.clinicSelect.appendChild(option);
        });
    }

    async loadLatestReviews() {
        const reviewsContainer = document.getElementById('latestReviews');
        if (!reviewsContainer) return;

        try {
            reviewsContainer.innerHTML = '<div class="reviews-loading"><i class="fas fa-spinner fa-spin"></i> Cargando reseñas...</div>';
            const result = await ReviewService.getLatestReviews(6);
            if (result.success && result.data.length > 0) {
                this.displayReviews(result.data);
            } else {
                this.showEmptyReviewsState();
            }
        } catch (error) {
            console.error('Error loading reviews:', error);
            reviewsContainer.innerHTML = '<div class="reviews-error">Error al cargar las reseñas</div>';
        }
    }

    displayReviews(reviews) {
        const reviewsContainer = document.getElementById('latestReviews');
        if (!reviewsContainer) return;

        const reviewsHTML = reviews.map(review => {
            const formattedReview = ReviewService.formatReviewForDisplay(review);
            return `
                <div class="review-card">
                    <div class="review-card-header">
                        <div class="reviewer-info">
                            <div class="reviewer-avatar">
                                <span class="avatar-initials">${formattedReview.userInitials}</span>
                            </div>
                            <div class="reviewer-details">
                                <h4 class="reviewer-name">${formattedReview.userName}</h4>
                                <div class="review-clinic-info">
                                    <i class="fas fa-map-marker-alt clinic-icon"></i>
                                    <span class="clinic-name">${formattedReview.clinicName}</span>
                                </div>
                            </div>
                        </div>
                        <div class="review-rating-section">
                            <div class="star-rating">
                                ${ReviewService.generateStarRating(formattedReview.rating)}
                            </div>
                            <span class="rating-value">${formattedReview.rating}/5</span>
                        </div>
                    </div>
                    <div class="review-content-section">
                        <p class="review-text">${formattedReview.content}</p>
                    </div>
                    <div class="review-card-footer">
                        <div class="review-date-info">
                            <i class="fas fa-calendar-alt date-icon"></i>
                            <span class="review-date">${formattedReview.date}</span>
                        </div>
                        <div class="review-actions">
                            <button class="helpful-btn" onclick="UIUtils.showToast('¡Gracias por tu feedback!', 'success')">
                                <i class="fas fa-thumbs-up"></i>
                                <span>Útil</span>
                            </button>
                        </div>
                    </div>
                </div>
            `;
        }).join('');

        reviewsContainer.innerHTML = `
            <div class="reviews-grid">
                ${reviewsHTML}
            </div>
        `;
    }

    showEmptyReviewsState() {
        const reviewsContainer = document.getElementById('latestReviews');
        if (!reviewsContainer) return;

        reviewsContainer.innerHTML = `
            <div class="empty-reviews-state">
                <div class="empty-state-icon">
                    <i class="fas fa-star-half-alt"></i>
                </div>
                <h3 class="empty-state-title">¡Sé el primero en opinar!</h3>
                <p class="empty-state-description">
                    Comparte tu experiencia y ayuda a otros pacientes a tomar la mejor decisión.
                </p>
                <button class="btn-primary empty-state-btn" onclick="document.getElementById('openReviewModalBtn')?.click()">
                    <i class="fas fa-plus"></i>
                    Escribir primera reseña
                </button>
            </div>
        `;
    }

    checkUserAuthentication() {
        if (typeof AuthService !== 'undefined' && AuthService.isAuthenticated()) {
            this.currentUser = AuthService.getCurrentUser();
            this.showReviewButton();
        } else {
            this.hideReviewButton();
        }
    }

    showReviewButton() {
        if (this.openModalBtn) {
            this.openModalBtn.style.display = 'inline-flex';
        }
    }

    hideReviewButton() {
        if (this.openModalBtn) {
            this.openModalBtn.style.display = 'none';
        }
    }

    openModal() {
        if (this.modal) {
            this.modal.classList.add('show');
            this.modal.style.display = 'block';
            document.body.classList.add('modal-open');
        }
    }

    closeModal() {
        if (this.modal) {
            this.modal.classList.remove('show');
            this.modal.style.display = 'none';
            document.body.classList.remove('modal-open');
            this.resetForm();
        }
    }

    resetForm() {
        if (this.form) {
            this.form.reset();
            this.clearErrors();
        }
    }

    async handleSubmit(e) {
        e.preventDefault();

        if (typeof AuthService === 'undefined' || !AuthService.isAuthenticated()) {
            this.showError('Debes iniciar sesión para escribir una reseña');
            return;
        }

        const formData = new FormData(this.form);
        const reviewData = {
            clinicId: parseInt(formData.get('clinicId')),
            rating: parseInt(formData.get('rating')),
            content: formData.get('content').trim()
        };

        if (!this.validateReviewData(reviewData)) {
            return;
        }

        const submitBtn = this.form.querySelector('button[type="submit"]');
        const originalText = submitBtn.innerHTML;

        try {
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';

            const result = await ReviewService.createReview(reviewData);

            if (result.success) {
                UIUtils.showToast('¡Reseña enviada exitosamente! Gracias por compartir tu experiencia.', 'success');
                this.closeModal();
                this.loadLatestReviews();
            } else {
                this.showError(result.message || 'Error al enviar la reseña');
            }
        } catch (error) {
            console.error('Error al enviar reseña:', error);
            this.showError('Error al enviar la reseña. Intenta nuevamente.');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalText;
        }
    }

    validateReviewData(data) {
        if (!data.clinicId) {
            this.showError('Por favor selecciona una clínica');
            return false;
        }
        if (!data.rating || data.rating < 1 || data.rating > 5) {
            this.showError('Por favor selecciona una calificación válida');
            return false;
        }
        if (!data.content || data.content.length < 10) {
            this.showError('El comentario debe tener al menos 10 caracteres');
            return false;
        }
        if (data.content.length > 500) {
            this.showError('El comentario no puede exceder 500 caracteres');
            return false;
        }
        return true;
    }

    showError(message) {
        const errorDiv = document.getElementById('reviewFormError');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
        }
    }

    clearErrors() {
        const errorDiv = document.getElementById('reviewFormError');
        if (errorDiv) {
            errorDiv.style.display = 'none';
        }
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new ReviewManager();
});