class ReviewService {
    static async getAllReviews() {
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.REVIEWS);
            if (response.ok) {
                const reviews = await response.json();
                return { success: true, data: reviews };
            } else {
                const errorData = await response.json();
                return { success: false, message: errorData.message || 'Error al cargar reseñas' };
            }
        } catch (error) {
            console.error('Error in getAllReviews:', error);
            return { success: false, message: 'Error de conexión al cargar reseñas' };
        }
    }

    static async getLatestReviews(limit = 6) {
        try {
            const result = await this.getAllReviews();
            if (result.success) {
                const sortedReviews = result.data
                    .sort((a, b) => new Date(b.date) - new Date(a.date))
                    .slice(0, limit);
                return { success: true, data: sortedReviews };
            }
            return result;
        } catch (error) {
            console.error('Error in getLatestReviews:', error);
            return { success: false, message: 'Error al cargar reseñas recientes' };
        }
    }

    static async createReview(reviewData) {
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.REVIEWS, {
                method: 'POST',
                body: JSON.stringify({
                    clinicId: reviewData.clinicId,
                    rating: reviewData.rating,
                    content: reviewData.content
                })
            });

            if (response.ok) {
                const review = await response.json();
                return { success: true, data: review, message: 'Reseña enviada exitosamente' };
            } else {
                const errorData = await response.json();
                return { success: false, message: errorData.message || 'Error al enviar reseña' };
            }
        } catch (error) {
            console.error('Error in createReview:', error);
            return { success: false, message: 'Error de conexión al enviar reseña' };
        }
    }

    static formatReviewForDisplay(review) {
        return {
            id: review.id,
            userName: review.user?.name || 'Usuario Anónimo',
            userInitials: this.getUserInitials(review.user?.name),
            clinicName: review.clinic?.name || 'Clínica General',
            rating: review.rating || 0,
            content: review.content || '',
            date: review.date ? new Date(review.date).toLocaleDateString() : '',
            canEdit: false
        };
    }

    static getUserInitials(name) {
        if (!name) return 'U';
        const words = name.split(' ');
        if (words.length >= 2) {
            return (words[0][0] + words[1][0]).toUpperCase();
        }
        return name[0].toUpperCase();
    }

    static generateStarRating(rating) {
        const fullStars = Math.floor(rating);
        const hasHalfStar = rating % 1 !== 0;
        let stars = '';

        for (let i = 0; i < fullStars; i++) {
            stars += '<i class="fas fa-star"></i>';
        }

        if (hasHalfStar) {
            stars += '<i class="fas fa-star-half-alt"></i>';
        }

        const emptyStars = 5 - Math.ceil(rating);
        for (let i = 0; i < emptyStars; i++) {
            stars += '<i class="far fa-star"></i>';
        }

        return stars;
    }
}

// Make ReviewService globally available
if (typeof window !== 'undefined') {
    window.ReviewService = ReviewService;
}
