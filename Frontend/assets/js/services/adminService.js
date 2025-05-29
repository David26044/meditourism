class AdminService {
    static async getAllUsers() {
        console.log('🔍 AdminService.getAllUsers() - Obteniendo todos los usuarios...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.USERS);
            if (response.ok) {
                const users = await response.json();
                console.log('✅ Usuarios obtenidos:', users);
                return users;
            } else {
                throw new Error('Error al obtener usuarios');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllUsers:', error);
            throw error;
        }
    }

    static async getAllRoles() {
        console.log('🔍 AdminService.getAllRoles() - Obteniendo roles...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.ROLES);
            if (response.ok) {
                const roles = await response.json();
                console.log('✅ Roles obtenidos:', roles);
                return roles;
            } else {
                throw new Error('Error al obtener roles');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllRoles:', error);
            throw error;
        }
    }

    static async updateUserRole(userId, roleId) {
        console.log(`🔄 AdminService.updateUserRole() - Actualizando rol del usuario ${userId} a rol ${roleId}`);
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${userId}/role?roleId=${roleId}`, {
                method: 'PATCH'
            });
            if (response.ok) {
                const updatedUser = await response.json();
                console.log('✅ Rol actualizado:', updatedUser);
                return updatedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al actualizar rol');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.updateUserRole:', error);
            throw error;
        }
    }

    static async getAllBlockedUsers() {
        console.log('🔍 AdminService.getAllBlockedUsers() - Obteniendo usuarios bloqueados...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.BLOCKED_USERS);
            if (response.ok) {
                const blockedUsers = await response.json();
                console.log('✅ Usuarios bloqueados obtenidos:', blockedUsers);
                return blockedUsers;
            } else {
                throw new Error('Error al obtener usuarios bloqueados');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllBlockedUsers:', error);
            throw error;
        }
    }

    static async blockUser(userId, reason) {
        console.log(`🚫 AdminService.blockUser() - Bloqueando usuario ${userId}`);
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.BLOCKED_USERS, {
                method: 'POST',
                body: JSON.stringify({
                    userId: userId,
                    reason: reason
                })
            });
            if (response.ok) {
                const blockedUser = await response.json();
                console.log('✅ Usuario bloqueado:', blockedUser);
                return blockedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al bloquear usuario');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.blockUser:', error);
            throw error;
        }
    }

    static async unblockUser(blockedUserId) {
        console.log(`✅ AdminService.unblockUser() - Desbloqueando usuario ${blockedUserId}`);
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.BLOCKED_USERS}/${blockedUserId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                const unblockedUser = await response.json();
                console.log('✅ Usuario desbloqueado:', unblockedUser);
                return unblockedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al desbloquear usuario');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.unblockUser:', error);
            throw error;
        }
    }

    static async deleteNormalUser(userId) {
        console.log(`🗑️ AdminService.deleteNormalUser() - Eliminando usuario normal ${userId}`);
        try {
            const response = await apiRequest(`${API_CONFIG.ENDPOINTS.USERS}/${userId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                const deletedUser = await response.json();
                console.log('✅ Usuario normal eliminado:', deletedUser);
                return deletedUser;
            } else {
                const error = await response.json();
                throw new Error(error.message || 'Error al eliminar usuario normal');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.deleteNormalUser:', error);
            throw error;
        }
    }

    static async getDashboardAnalytics() {
        console.log('📊 AdminService.getDashboardAnalytics() - Obteniendo datos analíticos...');
        try {
            // Get all necessary data for analytics
            const [users, contactForms, reviews] = await Promise.all([
                this.getAllUsers(),
                this.getAllContactForms(),
                this.getAllReviews()
            ]);

            // Calculate user analytics
            const userAnalytics = this.calculateUserAnalytics(users);
            
            // Calculate contact form analytics
            const formAnalytics = this.calculateFormAnalytics(contactForms);
            
            // Calculate review analytics
            const reviewAnalytics = this.calculateReviewAnalytics(reviews);

            const analytics = {
                users: userAnalytics,
                forms: formAnalytics,
                reviews: reviewAnalytics,
                trends: this.calculateTrends(users, contactForms, reviews)
            };

            console.log('✅ Analytics calculados:', analytics);
            return analytics;
        } catch (error) {
            console.error('💥 Error en AdminService.getDashboardAnalytics:', error);
            throw error;
        }
    }

    static async getAllContactForms() {
        console.log('🔍 AdminService.getAllContactForms() - Obteniendo formularios de contacto...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.CONTACT_FORMS);
            if (response.ok) {
                const forms = await response.json();
                console.log('✅ Formularios obtenidos:', forms);
                return forms;
            } else {
                throw new Error('Error al obtener formularios de contacto');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllContactForms:', error);
            throw error;
        }
    }

    static async getAllReviews() {
        console.log('🔍 AdminService.getAllReviews() - Obteniendo reviews...');
        try {
            const response = await apiRequest(API_CONFIG.ENDPOINTS.REVIEWS);
            if (response.ok) {
                const reviews = await response.json();
                console.log('✅ Reviews obtenidos:', reviews);
                return reviews;
            } else {
                throw new Error('Error al obtener reviews');
            }
        } catch (error) {
            console.error('💥 Error en AdminService.getAllReviews:', error);
            throw error;
        }
    }

    static calculateUserAnalytics(users) {
        const now = new Date();
        const sevenDaysAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
        const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);

        const verified = users.filter(user => user.verified).length;
        const admins = users.filter(user => user.role?.name === 'ADMIN').length;
        const newUsersWeek = users.filter(user => new Date(user.createdAt) >= sevenDaysAgo).length;
        const newUsersMonth = users.filter(user => new Date(user.createdAt) >= thirtyDaysAgo).length;
        
        const lastUser = users.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))[0];

        // Generate user registration trend (last 30 days)
        const registrationTrend = this.generateDailyTrend(users, 30, 'createdAt');

        return {
            total: users.length,
            verified,
            admins,
            newUsersWeek,
            newUsersMonth,
            lastUserDate: lastUser ? new Date(lastUser.createdAt).toLocaleDateString() : '-',
            registrationTrend,
            monthlyGrowth: newUsersMonth > 0 ? ((newUsersMonth / Math.max(users.length - newUsersMonth, 1)) * 100).toFixed(1) : '0'
        };
    }

    static calculateFormAnalytics(contactForms) {
        const now = new Date();
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const sevenDaysAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
        const thirtyDaysAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);

        const formsToday = contactForms.filter(form => new Date(form.createdDate) >= today).length;
        const formsWeek = contactForms.filter(form => new Date(form.createdDate) >= sevenDaysAgo).length;
        const formsMonth = contactForms.filter(form => new Date(form.createdDate) >= thirtyDaysAgo).length;

        // Most common inquiry type
        const inquiryTypes = contactForms.reduce((acc, form) => {
            acc[form.inquiryType] = (acc[form.inquiryType] || 0) + 1;
            return acc;
        }, {});
        const mostCommonInquiry = Object.entries(inquiryTypes).sort((a, b) => b[1] - a[1])[0];

        // Most consulted treatment
        const treatments = contactForms.filter(form => form.treatment).reduce((acc, form) => {
            acc[form.treatment.name] = (acc[form.treatment.name] || 0) + 1;
            return acc;
        }, {});
        const mostConsultedTreatment = Object.entries(treatments).sort((a, b) => b[1] - a[1])[0];

        const lastForm = contactForms.sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate))[0];

        // Generate treatment consultation trend
        const treatmentTrend = Object.entries(treatments).map(([name, count]) => ({ name, count }));

        return {
            total: contactForms.length,
            formsToday,
            formsWeek,
            formsMonth,
            mostCommonInquiry: mostCommonInquiry ? mostCommonInquiry[0] : '-',
            mostConsultedTreatment: mostConsultedTreatment ? mostConsultedTreatment[0] : '-',
            lastFormDate: lastForm ? new Date(lastForm.createdDate).toLocaleDateString() : '-',
            treatmentTrend,
            monthlyGrowth: formsMonth > 0 ? ((formsMonth / Math.max(contactForms.length - formsMonth, 1)) * 100).toFixed(1) : '0'
        };
    }

    static calculateReviewAnalytics(reviews) {
        const now = new Date();
        const sevenDaysAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);

        const reviewsWeek = reviews.filter(review => new Date(review.date) >= sevenDaysAgo).length;
        
        const avgRating = reviews.length > 0 ? 
            (reviews.reduce((sum, review) => sum + (review.rating || 0), 0) / reviews.length).toFixed(1) : '0.0';

        // Rating distribution
        const ratingDistribution = reviews.reduce((acc, review) => {
            const rating = Math.floor(review.rating || 0);
            acc[rating] = (acc[rating] || 0) + 1;
            return acc;
        }, {});

        const fiveStarReviews = ratingDistribution[5] || 0;
        const oneStarReviews = ratingDistribution[1] || 0;

        const lastReview = reviews.sort((a, b) => new Date(b.date) - new Date(a.date))[0];

        return {
            total: reviews.length,
            avgRating,
            fiveStarReviews,
            oneStarReviews,
            reviewsWeek,
            lastReviewDate: lastReview ? new Date(lastReview.date).toLocaleDateString() : '-',
            ratingDistribution
        };
    }

    static calculateTrends(users, contactForms, reviews) {
        // Generate activity by hour (for the last 24 hours)
        const activityByHour = Array(24).fill(0);
        
        const yesterday = new Date(Date.now() - 24 * 60 * 60 * 1000);
        
        // Count user registrations by hour
        users.filter(user => new Date(user.createdAt) >= yesterday)
             .forEach(user => {
                 const hour = new Date(user.createdAt).getHours();
                 activityByHour[hour]++;
             });

        // Count contact forms by hour
        contactForms.filter(form => new Date(form.createdDate) >= yesterday)
                   .forEach(form => {
                       const hour = new Date(form.createdDate).getHours();
                       activityByHour[hour]++;
                   });

        // Count reviews by hour
        reviews.filter(review => new Date(review.date) >= yesterday)
               .forEach(review => {
                   const hour = new Date(review.date).getHours();
                   activityByHour[hour]++;
               });

        return {
            activityByHour
        };
    }

    static generateDailyTrend(data, days, dateField) {
        const trend = Array(days).fill(0);
        const now = new Date();
        
        data.forEach(item => {
            const itemDate = new Date(item[dateField]);
            const daysDiff = Math.floor((now - itemDate) / (1000 * 60 * 60 * 24));
            if (daysDiff >= 0 && daysDiff < days) {
                trend[days - 1 - daysDiff]++;
            }
        });
        
        return trend;
    }
}

// Make AdminService globally available
if (typeof window !== 'undefined') {
    window.AdminService = AdminService;
}
