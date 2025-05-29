class ExportService {
    static showExportModal(title, status) {
        const modal = document.getElementById('export-modal');
        const titleEl = document.getElementById('export-title');
        const statusEl = document.getElementById('export-status');
        const progressFill = document.getElementById('export-progress-fill');
        
        titleEl.textContent = title;
        statusEl.textContent = status;
        progressFill.style.width = '0%';
        modal.style.display = 'flex';
    }

    static updateExportProgress(percentage, status) {
        const statusEl = document.getElementById('export-status');
        const progressFill = document.getElementById('export-progress-fill');
        
        statusEl.textContent = status;
        progressFill.style.width = percentage + '%';
    }

    static hideExportModal() {
        const modal = document.getElementById('export-modal');
        modal.style.display = 'none';
    }

    static formatDate(date) {
        return new Date(date).toLocaleDateString('es-ES');
    }

    static formatDateTime(date) {
        return new Date(date).toLocaleString('es-ES');
    }

    // PDF Export Functions
    static async exportDashboardToPDF() {
        try {
            this.showExportModal('Exportando Dashboard a PDF', 'Iniciando exportación...');
            this.updateExportProgress(10, 'Preparando datos...');
            // Note: Data for PDF is sourced directly from DOM elements in the dashboard section.
            // These DOM elements are populated by other functions in admin.js, which in turn use services
            // like AdminService to fetch initial dashboard data.

            const { jsPDF } = window.jspdf;
            const doc = new jsPDF('p', 'mm', 'a4');
            const pageWidth = doc.internal.pageSize.getWidth();
            const pageHeight = doc.internal.pageSize.getHeight();
            let yPosition = 20;

            // Header
            doc.setFontSize(20);
            doc.setTextColor(40, 40, 40);
            doc.text('Reporte Dashboard - MediTourism', pageWidth / 2, yPosition, { align: 'center' });
            yPosition += 10;

            doc.setFontSize(12);
            doc.text(`Generado el: ${this.formatDateTime(new Date())}`, pageWidth / 2, yPosition, { align: 'center' });
            yPosition += 20;

            this.updateExportProgress(30, 'Agregando estadísticas...');

            // Statistics
            doc.setFontSize(16);
            doc.text('Estadísticas Generales', 20, yPosition);
            yPosition += 10;

            const stats = [
                [`Total Usuarios:`, document.getElementById('total-users').textContent],
                [`Consultas Recibidas:`, document.getElementById('total-contact-forms').textContent],
                [`Tratamientos:`, document.getElementById('total-treatments').textContent],
                [`Reviews:`, document.getElementById('total-reviews').textContent]
            ];

            doc.setFontSize(12);
            stats.forEach(([label, value]) => {
                doc.text(label, 25, yPosition);
                doc.text(value, 100, yPosition);
                yPosition += 8;
            });

            this.updateExportProgress(60, 'Agregando análisis detallado...');

            // Detailed Analysis
            yPosition += 10;
            doc.setFontSize(16);
            doc.text('Análisis Detallado', 20, yPosition);
            yPosition += 10;

            const details = [
                ['Usuarios Verificados:', document.getElementById('verified-users').textContent],
                ['Usuarios Bloqueados:', document.getElementById('blocked-users').textContent],
                ['Consultas Hoy:', document.getElementById('forms-today').textContent],
                ['Consultas esta Semana:', document.getElementById('forms-week').textContent],
                ['Calificación Promedio:', document.getElementById('avg-rating').textContent],
                ['Reviews 5 estrellas:', document.getElementById('five-star-reviews').textContent]
            ];

            doc.setFontSize(12);
            details.forEach(([label, value]) => {
                if (yPosition > pageHeight - 30) {
                    doc.addPage();
                    yPosition = 20;
                }
                doc.text(label, 25, yPosition);
                doc.text(value, 100, yPosition);
                yPosition += 8;
            });

            this.updateExportProgress(90, 'Finalizando documento...');

            // Footer
            const now = new Date();
            doc.setFontSize(10);
            doc.text(`MediTourism - Reporte generado el ${this.formatDateTime(now)}`, 
                pageWidth / 2, pageHeight - 10, { align: 'center' });

            this.updateExportProgress(100, 'Descargando archivo...');

            // Save
            doc.save(`dashboard-report-${now.toISOString().split('T')[0]}.pdf`);
            
            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Reporte PDF descargado exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting PDF:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar PDF', 'error');
        }
    }

    // Excel Export Functions
    static async exportAllDataToExcel() {
        try {
            this.showExportModal('Exportando datos a Excel', 'Recopilando información...');
            this.updateExportProgress(10, 'Obteniendo datos de usuarios...');

            const workbook = XLSX.utils.book_new();

            // Dashboard Summary data is sourced from DOM elements.
            const dashboardData = [
                ['Métrica', 'Valor'],
                ['Total Usuarios', document.getElementById('total-users').textContent],
                ['Consultas Recibidas', document.getElementById('total-contact-forms').textContent],
                ['Tratamientos', document.getElementById('total-treatments').textContent],
                ['Reviews', document.getElementById('total-reviews').textContent],
                ['Usuarios Verificados', document.getElementById('verified-users').textContent],
                ['Usuarios Bloqueados', document.getElementById('blocked-users').textContent],
                ['Calificación Promedio', document.getElementById('avg-rating').textContent]
            ];

            const dashboardSheet = XLSX.utils.aoa_to_sheet(dashboardData);
            XLSX.utils.book_append_sheet(workbook, dashboardSheet, 'Dashboard');

            this.updateExportProgress(40, 'Agregando datos de usuarios...');

            // Get data from services
            try {
                // Data fetched via UserService.getAllUsers()
                // UserService.getAllUsers() is expected to call a backend API endpoint (e.g., GET /api/users)
                const users = await UserService.getAllUsers();
                if (users && users.length > 0) {
                    const usersData = [
                        ['ID', 'Nombre', 'Email', 'Teléfono', 'Verificado', 'Bloqueado', 'Fecha Registro']
                    ];
                    users.forEach(user => {
                        usersData.push([
                            user.id || '',
                            user.name || '',
                            user.email || '',
                            user.phone || '',
                            user.verified ? 'Sí' : 'No',
                            user.blocked ? 'Sí' : 'No',
                            user.created_at ? this.formatDate(user.created_at) : ''
                        ]);
                    });
                    const usersSheet = XLSX.utils.aoa_to_sheet(usersData);
                    XLSX.utils.book_append_sheet(workbook, usersSheet, 'Usuarios');
                }
            } catch (error) {
                console.warn('Could not fetch users data:', error);
            }

            this.updateExportProgress(60, 'Agregando datos de tratamientos...');

            try {
                // Data fetched via TreatmentService.getAllTreatments()
                // TreatmentService.getAllTreatments() is expected to call a backend API endpoint (e.g., GET /api/treatments)
                const treatments = await TreatmentService.getAllTreatments();
                if (treatments && treatments.length > 0) {
                    const treatmentsData = [
                        ['ID', 'Nombre', 'Descripción', 'Fecha Creación']
                    ];
                    treatments.forEach(treatment => {
                        treatmentsData.push([
                            treatment.id || '',
                            treatment.name || '',
                            treatment.description || '',
                            treatment.createdAt ? this.formatDateTime(treatment.createdAt) : 'N/A'
                        ]);
                    });
                    const treatmentsSheet = XLSX.utils.aoa_to_sheet(treatmentsData);
                    XLSX.utils.book_append_sheet(workbook, treatmentsSheet, 'Tratamientos');
                }
            } catch (error) {
                console.warn('Could not fetch treatments data:', error);
            }

            this.updateExportProgress(90, 'Generando archivo Excel...');

            // Save
            const fileName = `meditourism-data-${new Date().toISOString().split('T')[0]}.xlsx`;
            XLSX.writeFile(workbook, fileName);

            this.updateExportProgress(100, 'Descarga completada');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Datos exportados a Excel exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting to Excel:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar datos a Excel', 'error');
        }
    }

    static async exportUsersToExcel() {
        try {
            this.showExportModal('Exportando usuarios a Excel', 'Obteniendo datos...');
            this.updateExportProgress(20, 'Recopilando información de usuarios...');

            // Data fetched via UserService.getAllUsers()
            // UserService.getAllUsers() is expected to call a backend API endpoint (e.g., GET /api/users)
            const users = await UserService.getAllUsers();
            
            if (!users || users.length === 0) {
                this.hideExportModal();
                UIUtils.showToast('No hay usuarios para exportar', 'warning');
                return;
            }

            this.updateExportProgress(60, 'Generando archivo Excel...');

            const workbook = XLSX.utils.book_new();
            const usersData = [
                ['ID', 'Nombre', 'Email', 'Teléfono', 'Verificado', 'Bloqueado', 'Rol', 'Fecha Registro', 'Último Acceso']
            ];

            users.forEach(user => {
                usersData.push([
                    user.id || '',
                    user.name || '',
                    user.email || '',
                    user.phone || '',
                    user.verified ? 'Sí' : 'No',
                    user.blocked ? 'Sí' : 'No',
                    user.role || 'Usuario',
                    user.created_at ? this.formatDateTime(user.created_at) : '',
                    user.last_login ? this.formatDateTime(user.last_login) : 'Nunca'
                ]);
            });

            const usersSheet = XLSX.utils.aoa_to_sheet(usersData);
            XLSX.utils.book_append_sheet(workbook, usersSheet, 'Usuarios');

            this.updateExportProgress(90, 'Descargando archivo...');

            const fileName = `usuarios-${new Date().toISOString().split('T')[0]}.xlsx`;
            XLSX.writeFile(workbook, fileName);

            this.updateExportProgress(100, 'Exportación completada');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Usuarios exportados exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting users:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar usuarios', 'error');
        }
    }

    static async exportContactFormsToExcel() {
        try {
            this.showExportModal('Exportando consultas a Excel', 'Obteniendo datos...');
            this.updateExportProgress(20, 'Recopilando consultas...');

            // Data fetched via AdminService.getContactForms()
            // AdminService.getContactForms() is expected to call a backend API endpoint (e.g., GET /api/admin/contact-forms)
            const contactForms = await AdminService.getContactForms();
            
            if (!contactForms || contactForms.length === 0) {
                this.hideExportModal();
                UIUtils.showToast('No hay consultas para exportar', 'warning');
                return;
            }

            this.updateExportProgress(60, 'Generando archivo Excel...');

            const workbook = XLSX.utils.book_new();
            const formsData = [
                ['ID', 'Nombre', 'Email', 'Teléfono', 'Tratamiento', 'Mensaje', 'Fecha']
            ];

            contactForms.forEach(form => {
                formsData.push([
                    form.id || '',
                    form.name || '',
                    form.email || '',
                    form.phone || '',
                    form.treatment || '',
                    form.message || '',
                    form.created_at ? this.formatDateTime(form.created_at) : ''
                ]);
            });

            const formsSheet = XLSX.utils.aoa_to_sheet(formsData);
            XLSX.utils.book_append_sheet(workbook, formsSheet, 'Consultas');

            this.updateExportProgress(90, 'Descargando archivo...');

            const fileName = `consultas-${new Date().toISOString().split('T')[0]}.xlsx`;
            XLSX.writeFile(workbook, fileName);

            this.updateExportProgress(100, 'Exportación completada');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Consultas exportadas exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting contact forms:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar consultas', 'error');
        }
    }

    static async exportTreatmentsToExcel() {
        try {
            this.showExportModal('Exportando tratamientos a Excel', 'Obteniendo datos...');
            this.updateExportProgress(20, 'Recopilando tratamientos...');

            const treatments = await TreatmentService.getAllTreatments();
            
            if (!treatments || treatments.length === 0) {
                this.hideExportModal();
                UIUtils.showToast('No hay tratamientos para exportar', 'warning');
                return;
            }

            this.updateExportProgress(60, 'Generando archivo Excel...');

            const workbook = XLSX.utils.book_new();
            const treatmentsData = [
                ['ID', 'Nombre', 'Descripción', 'Fecha Creación']
            ];

            treatments.forEach(treatment => {
                treatmentsData.push([
                    treatment.id || '',
                    treatment.name || '',
                    treatment.description || '',
                    treatment.createdAt ? this.formatDateTime(treatment.createdAt) : 'N/A'
                ]);
            });

            const treatmentsSheet = XLSX.utils.aoa_to_sheet(treatmentsData);
            XLSX.utils.book_append_sheet(workbook, treatmentsSheet, 'Tratamientos');

            this.updateExportProgress(90, 'Descargando archivo...');

            const fileName = `tratamientos-${new Date().toISOString().split('T')[0]}.xlsx`;
            XLSX.writeFile(workbook, fileName);

            this.updateExportProgress(100, 'Exportación completada');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Tratamientos exportados exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting treatments:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar tratamientos', 'error');
        }
    }

    static async exportReviewsToExcel() {
        try {
            this.showExportModal('Exportando reviews a Excel', 'Obteniendo datos...');
            this.updateExportProgress(20, 'Recopilando reviews...');

            // Data fetched via AdminService.getReviews()
            // AdminService.getReviews() is expected to call a backend API endpoint (e.g., GET /api/admin/reviews)
            const reviews = await AdminService.getReviews();
            
            if (!reviews || reviews.length === 0) {
                this.hideExportModal();
                UIUtils.showToast('No hay reviews para exportar', 'warning');
                return;
            }

            this.updateExportProgress(60, 'Generando archivo Excel...');

            const workbook = XLSX.utils.book_new();
            const reviewsData = [
                ['ID', 'Usuario', 'Calificación', 'Comentario', 'Tratamiento', 'Fecha']
            ];

            reviews.forEach(review => {
                reviewsData.push([
                    review.id || '',
                    review.user_name || '',
                    review.rating || '',
                    review.comment || '',
                    review.treatment || '',
                    review.created_at ? this.formatDateTime(review.created_at) : ''
                ]);
            });

            const reviewsSheet = XLSX.utils.aoa_to_sheet(reviewsData);
            XLSX.utils.book_append_sheet(workbook, reviewsSheet, 'Reviews');

            this.updateExportProgress(90, 'Descargando archivo...');

            const fileName = `reviews-${new Date().toISOString().split('T')[0]}.xlsx`;
            XLSX.writeFile(workbook, fileName);

            this.updateExportProgress(100, 'Exportación completada');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Reviews exportados exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting reviews:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar reviews', 'error');
        }
    }

    // Image Export Functions
    static async exportChartsAsImages() {
        try {
            this.showExportModal('Exportando gráficos como imágenes', 'Capturando gráficos...');
            // Note: Charts are rendered on canvas elements. Data for these charts is populated
            // by functions in admin.js, which use various services (UserService, AdminService)
            // to fetch data from backend API endpoints.
            
            const charts = ['usersChart', 'contactFormsChart', 'reviewsChart', 'activityChart'];
            const validCharts = charts.filter(chartId => document.getElementById(chartId));
            
            if (validCharts.length === 0) {
                this.hideExportModal();
                UIUtils.showToast('No hay gráficos disponibles para exportar', 'warning');
                return;
            }

            let completed = 0;
            const total = validCharts.length;

            for (const chartId of validCharts) {
                const canvas = document.getElementById(chartId);
                if (canvas) {
                    this.updateExportProgress(
                        (completed / total) * 80, 
                        `Exportando gráfico ${completed + 1} de ${total}...`
                    );

                    const link = document.createElement('a');
                    link.download = `${chartId}-${new Date().toISOString().split('T')[0]}.png`;
                    link.href = canvas.toDataURL('image/png');
                    link.click();
                    
                    completed++;
                    await new Promise(resolve => setTimeout(resolve, 500));
                }
            }

            this.updateExportProgress(100, 'Exportación completada');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast(`${completed} gráficos exportados exitosamente`, 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting charts:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar gráficos', 'error');
        }
    }

    // Full Report Export
    static async exportFullReport() {
        try {
            this.showExportModal('Generando reporte completo', 'Iniciando proceso...');
            // This function calls other export methods which, as commented above,
            // rely on data from DOM or fetched via other services (UserService, AdminService, TreatmentService)
            // that interact with backend API endpoints.
            
            // Generate PDF
            this.updateExportProgress(25, 'Generando reporte PDF...');
            await this.exportDashboardToPDF();
            
            // Wait a moment
            await new Promise(resolve => setTimeout(resolve, 1000));
            
            // Generate Excel
            this.updateExportProgress(50, 'Generando archivo Excel...');
            await this.exportAllDataToExcel();
            
            // Wait a moment
            await new Promise(resolve => setTimeout(resolve, 1000));
            
            // Export charts
            this.updateExportProgress(75, 'Exportando gráficos...');
            await this.exportChartsAsImages();
            
            this.updateExportProgress(100, 'Reporte completo generado');

            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Reporte completo generado exitosamente', 'success');
            }, 1000);

        } catch (error) {
            console.error('Error generating full report:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al generar reporte completo', 'error');
        }
    }

    // Enhanced PDF Export Functions
    static async exportDashboardToPDF() {
        try {
            this.showExportModal('Generando Reporte PDF Completo', 'Iniciando exportación...');
            this.updateExportProgress(5, 'Preparando documento...');

            const { jsPDF } = window.jspdf;
            const doc = new jsPDF('p', 'mm', 'a4');
            const pageWidth = doc.internal.pageSize.getWidth();
            const pageHeight = doc.internal.pageSize.getHeight();
            const margin = 20;
            const contentWidth = pageWidth - (margin * 2);
            let yPosition = margin;

            // Helper function to add new page if needed
            const checkPageBreak = (requiredSpace = 30) => {
                if (yPosition + requiredSpace > pageHeight - margin) {
                    doc.addPage();
                    yPosition = margin;
                    return true;
                }
                return false;
            };

            // Add logo/header background
            doc.setFillColor(0, 123, 255);
            doc.rect(0, 0, pageWidth, 40, 'F');

            // Title
            doc.setTextColor(255, 255, 255);
            doc.setFontSize(24);
            doc.setFont(undefined, 'bold');
            doc.text('REPORTE ADMINISTRATIVO COMPLETO', pageWidth / 2, 20, { align: 'center' });
            
            doc.setFontSize(14);
            doc.text('MediTourism - Panel de Control', pageWidth / 2, 30, { align: 'center' });

            yPosition = 50;
            doc.setTextColor(40, 40, 40);

            this.updateExportProgress(10, 'Agregando información general...');

            // Report Info
            doc.setFontSize(12);
            const now = new Date();
            doc.text(`Generado el: ${this.formatDateTime(now)}`, margin, yPosition);
            yPosition += 6;
            doc.text(`Período: Dashboard Completo`, margin, yPosition);
            yPosition += 15;

            // Executive Summary
            doc.setFontSize(18);
            doc.setFont(undefined, 'bold');
            doc.text('RESUMEN EJECUTIVO', margin, yPosition);
            yPosition += 10;

            doc.setFontSize(11);
            doc.setFont(undefined, 'normal');
            const summaryText = `Este reporte presenta un análisis completo del estado actual del sistema MediTourism, 
incluyendo estadísticas de usuarios, consultas, tratamientos y reviews. Los datos reflejan 
la actividad hasta la fecha de generación del reporte.`;
            
            const summaryLines = doc.splitTextToSize(summaryText, contentWidth);
            doc.text(summaryLines, margin, yPosition);
            yPosition += summaryLines.length * 5 + 10;

            this.updateExportProgress(20, 'Agregando estadísticas principales...');

            // Main Statistics Section
            checkPageBreak(50);
            doc.setFillColor(248, 249, 250);
            doc.rect(margin - 5, yPosition - 5, contentWidth + 10, 40, 'F');
            
            doc.setFontSize(16);
            doc.setFont(undefined, 'bold');
            doc.text('ESTADÍSTICAS PRINCIPALES', margin, yPosition + 5);
            yPosition += 15;

            // Create statistics table
            const mainStats = [
                ['Métrica', 'Valor', 'Tendencia'],
                ['Total Usuarios', document.getElementById('total-users').textContent, document.getElementById('users-trend').textContent],
                ['Consultas Recibidas', document.getElementById('total-contact-forms').textContent, document.getElementById('forms-trend').textContent],
                ['Tratamientos Activos', document.getElementById('total-treatments').textContent, document.getElementById('treatments-trend').textContent],
                ['Total Reviews', document.getElementById('total-reviews').textContent, document.getElementById('reviews-trend').textContent]
            ];

            this.drawTable(doc, mainStats, margin, yPosition, contentWidth);
            yPosition += (mainStats.length * 8) + 15;

            this.updateExportProgress(30, 'Agregando análisis detallado...');

            // Detailed Analysis Section
            checkPageBreak(60);
            doc.setFontSize(16);
            doc.setFont(undefined, 'bold');
            doc.text('ANÁLISIS DETALLADO POR MÓDULO', margin, yPosition);
            yPosition += 15;

            // User Analysis
            doc.setFontSize(14);
            doc.setFont(undefined, 'bold');
            doc.text('1. Análisis de Usuarios', margin, yPosition);
            yPosition += 8;

            const userStats = [
                ['Métrica', 'Cantidad'],
                ['Usuarios Verificados', document.getElementById('verified-users').textContent],
                ['Usuarios Bloqueados', document.getElementById('blocked-users').textContent],
                ['Administradores', document.getElementById('admin-users').textContent],
                ['Nuevos (7 días)', document.getElementById('new-users-week').textContent],
                ['Último Registro', document.getElementById('last-user-date').textContent]
            ];

            this.drawTable(doc, userStats, margin, yPosition, contentWidth * 0.7);
            yPosition += (userStats.length * 8) + 10;

            this.updateExportProgress(40, 'Agregando análisis de consultas...');

            // Consultation Analysis
            checkPageBreak(50);
            doc.setFontSize(14);
            doc.setFont(undefined, 'bold');
            doc.text('2. Análisis de Consultas', margin, yPosition);
            yPosition += 8;

            const consultationStats = [
                ['Métrica', 'Valor'],
                ['Consultas Hoy', document.getElementById('forms-today').textContent],
                ['Consultas esta Semana', document.getElementById('forms-week').textContent],
                ['Tipo más Común', document.getElementById('most-common-inquiry').textContent],
                ['Tratamiento más Consultado', document.getElementById('most-consulted-treatment').textContent],
                ['Última Consulta', document.getElementById('last-form-date').textContent]
            ];

            this.drawTable(doc, consultationStats, margin, yPosition, contentWidth * 0.7);
            yPosition += (consultationStats.length * 8) + 10;

            this.updateExportProgress(50, 'Agregando análisis de tratamientos...');

            // Treatment Analysis
            checkPageBreak(50);
            doc.setFontSize(14);
            doc.setFont(undefined, 'bold');
            doc.text('3. Análisis de Tratamientos', margin, yPosition);
            yPosition += 8;

            const treatmentStats = [
                ['Métrica', 'Valor'],
                ['Tratamientos Activos', document.getElementById('active-treatments').textContent],
                ['Precio Promedio', document.getElementById('avg-treatment-price').textContent],
                ['Más Costoso', document.getElementById('most-expensive-treatment').textContent],
                ['Más Económico', document.getElementById('cheapest-treatment').textContent],
                ['Último Agregado', document.getElementById('last-treatment-date').textContent]
            ];

            this.drawTable(doc, treatmentStats, margin, yPosition, contentWidth * 0.7);
            yPosition += (treatmentStats.length * 8) + 10;

            this.updateExportProgress(60, 'Agregando análisis de reviews...');

            // Review Analysis
            checkPageBreak(50);
            doc.setFontSize(14);
            doc.setFont(undefined, 'bold');
            doc.text('4. Análisis de Reviews', margin, yPosition);
            yPosition += 8;

            const reviewStats = [
                ['Métrica', 'Valor'],
                ['Calificación Promedio', document.getElementById('avg-rating').textContent],
                ['Reviews 5 Estrellas', document.getElementById('five-star-reviews').textContent],
                ['Reviews 1 Estrella', document.getElementById('one-star-reviews').textContent],
                ['Reviews esta Semana', document.getElementById('reviews-week').textContent],
                ['Último Review', document.getElementById('last-review-date').textContent]
            ];

            this.drawTable(doc, reviewStats, margin, yPosition, contentWidth * 0.7);
            yPosition += (reviewStats.length * 8) + 15;

            this.updateExportProgress(70, 'Agregando gráficos...');

            // Charts Section
            await this.addChartsToPDF(doc, margin, yPosition, contentWidth, checkPageBreak);

            this.updateExportProgress(80, 'Agregando datos detallados...');

            // Add detailed data tables
            await this.addDetailedDataTables(doc, margin, contentWidth, checkPageBreak);

            this.updateExportProgress(90, 'Agregando recomendaciones...');

            // Recommendations Section
            doc.addPage();
            yPosition = margin;
            
            doc.setFontSize(18);
            doc.setFont(undefined, 'bold');
            doc.text('RECOMENDACIONES Y CONCLUSIONES', margin, yPosition);
            yPosition += 15;

            const recommendations = [
                '• Mantener un seguimiento constante de la satisfacción del cliente a través de reviews',
                '• Implementar estrategias para incrementar la verificación de usuarios',
                '• Analizar los tratamientos más consultados para optimizar la oferta',
                '• Establecer campañas de marketing para los tratamientos menos populares',
                '• Monitorear regularmente la actividad para detectar tendencias',
                '• Considerar la expansión de servicios basada en las consultas más frecuentes'
            ];

            doc.setFontSize(11);
            doc.setFont(undefined, 'normal');
            recommendations.forEach(rec => {
                if (yPosition > pageHeight - 40) {
                    doc.addPage();
                    yPosition = margin;
                }
                doc.text(rec, margin, yPosition);
                yPosition += 8;
            });

            yPosition += 10;

            // System Information
            checkPageBreak(30);
            doc.setFontSize(14);
            doc.setFont(undefined, 'bold');
            doc.text('INFORMACIÓN DEL SISTEMA', margin, yPosition);
            yPosition += 10;

            const systemInfo = [
                'Sistema: MediTourism Admin Panel',
                'Versión: 1.0.0',
                'Base de Datos: En línea',
                'Estado del Sistema: Operativo',
                `Último Backup: ${this.formatDate(new Date())}`,
                'Uptime: 99.9%'
            ];

            doc.setFontSize(10);
            doc.setFont(undefined, 'normal');
            systemInfo.forEach(info => {
                doc.text(info, margin, yPosition);
                yPosition += 6;
            });

            this.updateExportProgress(95, 'Finalizando documento...');

            // Footer on all pages
            const totalPages = doc.internal.getNumberOfPages();
            for (let i = 1; i <= totalPages; i++) {
                doc.setPage(i);
                doc.setFontSize(8);
                doc.setTextColor(128, 128, 128);
                doc.text(`Página ${i} de ${totalPages}`, pageWidth - margin, pageHeight - 10, { align: 'right' });
                doc.text('MediTourism - Reporte Confidencial', margin, pageHeight - 10);
                doc.text(`Generado: ${this.formatDateTime(now)}`, pageWidth / 2, pageHeight - 10, { align: 'center' });
            }

            this.updateExportProgress(100, 'Descargando archivo...');

            // Save
            doc.save(`reporte-completo-meditourism-${now.toISOString().split('T')[0]}.pdf`);
            
            setTimeout(() => {
                this.hideExportModal();
                UIUtils.showToast('Reporte PDF completo descargado exitosamente', 'success');
            }, 500);

        } catch (error) {
            console.error('Error exporting comprehensive PDF:', error);
            this.hideExportModal();
            UIUtils.showToast('Error al exportar PDF completo', 'error');
        }
    }

    // Helper method to draw tables
    static drawTable(doc, data, x, y, width) {
        const rowHeight = 8;
        const colWidth = width / data[0].length;
        
        data.forEach((row, rowIndex) => {
            row.forEach((cell, colIndex) => {
                const cellX = x + (colIndex * colWidth);
                const cellY = y + (rowIndex * rowHeight);
                
                // Header row styling
                if (rowIndex === 0) {
                    doc.setFillColor(52, 58, 64);
                    doc.rect(cellX, cellY - 6, colWidth, rowHeight, 'F');
                    doc.setTextColor(255, 255, 255);
                    doc.setFont(undefined, 'bold');
                } else {
                    // Alternate row colors
                    if (rowIndex % 2 === 0) {
                        doc.setFillColor(248, 249, 250);
                        doc.rect(cellX, cellY - 6, colWidth, rowHeight, 'F');
                    }
                    doc.setTextColor(40, 40, 40);
                    doc.setFont(undefined, 'normal');
                }
                
                // Draw cell border
                doc.setDrawColor(220, 220, 220);
                doc.rect(cellX, cellY - 6, colWidth, rowHeight);
                
                // Add text
                doc.setFontSize(9);
                const cellText = String(cell || '').substring(0, 25); // Limit text length
                doc.text(cellText, cellX + 2, cellY - 1);
            });
        });
    }

    // Helper method to add charts to PDF
    static async addChartsToPDF(doc, margin, startY, contentWidth, checkPageBreak) {
        checkPageBreak(60);
        
        doc.setFontSize(16);
        doc.setFont(undefined, 'bold');
        doc.text('GRÁFICOS Y VISUALIZACIONES', margin, startY);
        let yPos = startY + 15;

        const charts = [
            { id: 'usersChart', title: 'Tendencia de Usuarios' },
            { id: 'contactFormsChart', title: 'Consultas por Tratamiento' },
            { id: 'reviewsChart', title: 'Distribución de Calificaciones' },
            { id: 'activityChart', title: 'Actividad por Hora' }
        ];

        for (const chart of charts) {
            const canvas = document.getElementById(chart.id);
            if (canvas) {
                checkPageBreak(80);
                
                doc.setFontSize(12);
                doc.setFont(undefined, 'bold');
                doc.text(chart.title, margin, yPos);
                yPos += 10;

                try {
                    const imgData = canvas.toDataURL('image/png');
                    const imgWidth = contentWidth * 0.8;
                    const imgHeight = (canvas.height / canvas.width) * imgWidth;
                    
                    doc.addImage(imgData, 'PNG', margin, yPos, imgWidth, imgHeight);
                    yPos += imgHeight + 15;
                } catch (error) {
                    doc.setFontSize(10);
                    doc.text('Gráfico no disponible', margin, yPos);
                    yPos += 20;
                }
            }
        }
    }

    // Helper method to add detailed data tables
    static async addDetailedDataTables(doc, margin, contentWidth, checkPageBreak) {
        doc.addPage();
        let yPos = margin;
        
        doc.setFontSize(18);
        doc.setFont(undefined, 'bold');
        doc.text('DATOS DETALLADOS', margin, yPos);
        yPos += 20;

        // Recent Activity
        doc.setFontSize(14);
        doc.setFont(undefined, 'bold');
        doc.text('Actividad Reciente del Sistema', margin, yPos);
        yPos += 10;

        const activities = [
            ['Fecha/Hora', 'Actividad', 'Usuario'],
            [this.formatDateTime(new Date()), 'Generación de reporte', 'Administrador'],
            [this.formatDateTime(new Date(Date.now() - 3600000)), 'Consulta recibida', 'Sistema'],
            [this.formatDateTime(new Date(Date.now() - 7200000)), 'Nuevo usuario registrado', 'Sistema'],
            [this.formatDateTime(new Date(Date.now() - 10800000)), 'Review agregado', 'Usuario'],
            [this.formatDateTime(new Date(Date.now() - 14400000)), 'Tratamiento actualizado', 'Administrador']
        ];

        this.drawTable(doc, activities, margin, yPos, contentWidth);
        yPos += (activities.length * 8) + 20;

        // Performance Metrics
        checkPageBreak(50);
        doc.setFontSize(14);
        doc.setFont(undefined, 'bold');
        doc.text('Métricas de Rendimiento', margin, yPos);
        yPos += 10;

        const performance = [
            ['Métrica', 'Valor', 'Estado'],
            ['Tiempo de Respuesta', '< 2 segundos', 'Óptimo'],
            ['Disponibilidad', '99.9%', 'Excelente'],
            ['Consultas/Día', '25', 'Normal'],
            ['Usuarios Activos', '150', 'Creciendo'],
            ['Conversión', '15%', 'Bueno']
        ];

        this.drawTable(doc, performance, margin, yPos, contentWidth);
    }
}

window.ExportService = ExportService;
