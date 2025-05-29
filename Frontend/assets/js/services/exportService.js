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

            // Dashboard Summary
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
                const treatments = await TreatmentService.getAllTreatments();
                if (treatments && treatments.length > 0) {
                    const treatmentsData = [
                        ['ID', 'Nombre', 'Descripción', 'Precio', 'Duración', 'Categoría']
                    ];
                    treatments.forEach(treatment => {
                        treatmentsData.push([
                            treatment.id || '',
                            treatment.name || '',
                            treatment.description || '',
                            treatment.price || '',
                            treatment.duration || '',
                            treatment.category || ''
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
                ['ID', 'Nombre', 'Descripción', 'Precio (USD)', 'Duración', 'Categoría', 'Estado', 'Fecha Creación']
            ];

            treatments.forEach(treatment => {
                treatmentsData.push([
                    treatment.id || '',
                    treatment.name || '',
                    treatment.description || '',
                    treatment.price || '',
                    treatment.duration || '',
                    treatment.category || '',
                    treatment.active ? 'Activo' : 'Inactivo',
                    treatment.created_at ? this.formatDateTime(treatment.created_at) : ''
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
}

window.ExportService = ExportService;
