-- Development seed data for MediTourism.
-- Run manually against a local/dev database after Flyway migrations.
--
-- Example:
--   psql "$SPRING_DATASOURCE_URL" -U "$SPRING_DATASOURCE_USERNAME" -f src/main/resources/db/dev-data.sql
--
-- This file intentionally includes a few "bad but currently allowed by DB" rows
-- to make hardening issues visible during manual/API testing:
-- - user 4 is blocked but can still exist as verified
-- - review 5 has rating 6, outside a normal 1-5 range
-- - report 3 targets both a review and a comment
-- - report 4 has no target at all
-- - contact form 4 belongs to user 2 but uses a different email

BEGIN;

TRUNCATE TABLE
    contact_forms,
    reports,
    community_rules,
    comments,
    reviews,
    clinic_treatment,
    clinics,
    treatments,
    blocked_users,
    notifications,
    users,
    roles
RESTART IDENTITY CASCADE;

INSERT INTO roles (id, name, description)
VALUES
    (1, 'ADMIN', 'Administrator role'),
    (2, 'USER', 'Regular user role');

-- Password hash reused from V2__Insert.sql for all demo users.
-- Use the existing known project password for local testing.
INSERT INTO users (id, email, name, password, is_verified, role_id)
VALUES
    (1, 'admin@example.com', 'Demo Admin', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 1),
    (2, 'ana.patient@example.com', 'Ana Patient', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (3, 'bruno.patient@example.com', 'Bruno Patient', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (4, 'blocked.patient@example.com', 'Blocked Patient', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (5, 'unverified.patient@example.com', 'Unverified Patient', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', FALSE, 2),
    (6, 'owner.patient@example.com', 'Owner Patient', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2);

INSERT INTO blocked_users (user_id, reason, date)
VALUES
    (4, 'Seeded blocked user: use to test blocked-user enforcement across actions.', CURRENT_TIMESTAMP - INTERVAL '3 days');

INSERT INTO notifications (id, user_id, title, content, date)
VALUES
    (1, 2, 'Nuevo comentario en tu reseña', 'Bruno comento en tu resena sobre Clinica Andina.', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (2, 3, 'Nuevo comentario a tu comentario', 'Ana respondio a uno de tus comentarios.', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (3, 4, 'Cuenta bloqueada', 'Tu cuenta fue marcada como bloqueada para pruebas de desarrollo.', CURRENT_TIMESTAMP - INTERVAL '3 days'),
    (4, 1, 'Reporte pendiente', 'Hay reportes de contenido para revisar.', CURRENT_TIMESTAMP - INTERVAL '6 hours');

INSERT INTO treatments (id, name, description)
VALUES
    (1, 'Implantes dentales', 'Evaluacion, planeacion y cirugia de implantes dentales.'),
    (2, 'Blanqueamiento dental', 'Procedimiento estetico dental con supervision profesional.'),
    (3, 'Cirugia refractiva', 'Correccion visual con laser para pacientes candidatos.'),
    (4, 'Chequeo cardiologico', 'Consulta, electrocardiograma y examenes de seguimiento.'),
    (5, 'Rehabilitacion fisica', 'Plan de fisioterapia para recuperacion funcional.');

INSERT INTO clinics (id, name, email, address, description)
VALUES
    (1, 'Clinica Andina', 'contacto@clinicaandina.test', 'Carrera 7 #80-10, Bogota', 'Clinica general con enfoque en turismo medico.'),
    (2, 'Dental Smile Medellin', 'hola@dentalsmile.test', 'Calle 10 #43A-20, Medellin', 'Centro odontologico especializado en estetica e implantes.'),
    (3, 'Vision Laser Center', 'info@visionlaser.test', 'Avenida 19 #120-30, Bogota', 'Centro oftalmologico para cirugia refractiva.'),
    (4, 'Cardio Travel Care', 'contact@cardiotravel.test', 'Calle 5 #38-55, Cali', 'Atencion cardiovascular para pacientes nacionales e internacionales.');

INSERT INTO clinic_treatment (clinic_id, treatment_id, price)
VALUES
    (1, 1, 4200.00),
    (1, 4, 350.00),
    (1, 5, 120.00),
    (2, 1, 3900.00),
    (2, 2, 280.00),
    (3, 3, 1800.00),
    (4, 4, 420.00),
    (4, 5, 160.00);

INSERT INTO reviews (id, user_id, clinic_id, content, date, rating)
VALUES
    (1, 2, 1, 'La atencion fue clara y el equipo medico explico cada paso antes del procedimiento.', CURRENT_TIMESTAMP - INTERVAL '12 days', 5),
    (2, 3, 2, 'Buen resultado dental, aunque la agenda tuvo retrasos el primer dia.', CURRENT_TIMESTAMP - INTERVAL '9 days', 4),
    (3, 6, 3, 'El diagnostico fue rapido y la coordinacion del viaje estuvo bien organizada.', CURRENT_TIMESTAMP - INTERVAL '5 days', 5),
    (4, 4, 1, 'Resena creada por usuario bloqueado para probar restricciones de escritura posteriores.', CURRENT_TIMESTAMP - INTERVAL '4 days', 2),
    (5, 2, 4, 'Rating fuera de rango intencional para evidenciar falta de constraint de 1 a 5.', CURRENT_TIMESTAMP - INTERVAL '2 days', 6),
    (6, 3, 1, 'Contenido reciente para probar endpoint de ultimas resenas.', CURRENT_TIMESTAMP - INTERVAL '8 hours', 3);

INSERT INTO comments (id, review_id, father_comment_id, user_id, content, date)
VALUES
    (1, 1, NULL, 3, 'Gracias por el detalle, estoy comparando esta clinica para el mismo tratamiento.', CURRENT_TIMESTAMP - INTERVAL '11 days'),
    (2, 1, 1, 2, 'Con gusto. Recomiendo pedir la cotizacion detallada antes de viajar.', CURRENT_TIMESTAMP - INTERVAL '10 days'),
    (3, 2, NULL, 2, 'Tambien tuve retrasos, pero el seguimiento posterior fue bueno.', CURRENT_TIMESTAMP - INTERVAL '8 days'),
    (4, 3, NULL, 1, 'Comentario de administrador para revisar tono y moderacion.', CURRENT_TIMESTAMP - INTERVAL '5 days'),
    (5, 4, NULL, 4, 'Comentario de usuario bloqueado para probar acciones restringidas.', CURRENT_TIMESTAMP - INTERVAL '4 days'),
    (6, 6, NULL, 6, 'Comentario reciente para probar listados por usuario y por resena.', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    (7, 6, 6, 3, 'Respuesta anidada para validar father_comment_id.', CURRENT_TIMESTAMP - INTERVAL '5 hours');

INSERT INTO community_rules (id, rule_text)
VALUES
    (1, 'No publicar datos personales sensibles de pacientes o profesionales.'),
    (2, 'No publicar contenido ofensivo, discriminatorio o amenazante.'),
    (3, 'No publicar spam ni promociones no verificadas.'),
    (4, 'Mantener las resenas basadas en experiencias reales.');

INSERT INTO reports (id, reporter_user, target_review_id, target_comment_id, community_rule_id, reason)
VALUES
    (1, 3, 4, NULL, 4, 'Esta resena debe revisarse porque pertenece a un usuario bloqueado.'),
    (2, 2, NULL, 5, 2, 'Comentario reportado para probar moderacion sobre comentarios.'),
    (3, 6, 5, 7, 1, 'Caso invalido intencional: tiene review y comment al mismo tiempo.'),
    (4, 1, NULL, NULL, 3, 'Caso invalido intencional: no tiene target_review_id ni target_comment_id.');

INSERT INTO contact_forms (id, user_id, treatment_id, preferred_clinic_id, email, message, number, date)
VALUES
    (1, 2, 1, 2, 'ana.patient@example.com', 'Quiero una cotizacion para implantes dentales y fechas disponibles.', '+57 300 000 0001', CURRENT_TIMESTAMP - INTERVAL '7 days'),
    (2, 3, 3, 3, 'bruno.patient@example.com', 'Necesito saber si soy candidato para cirugia refractiva.', '+57 300 000 0002', CURRENT_TIMESTAMP - INTERVAL '6 days'),
    (3, 6, 4, 4, 'owner.patient@example.com', 'Busco un chequeo cardiologico completo durante mi viaje.', '+57 300 000 0003', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (4, 2, 5, 1, 'different.email@example.com', 'Caso para probar que el endpoint publico permite email distinto al usuario asociado.', '+57 300 000 0004', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (5, 4, 2, 2, 'blocked.patient@example.com', 'Caso para probar formulario creado por usuario bloqueado.', '+57 300 000 0005', CURRENT_TIMESTAMP - INTERVAL '12 hours');

SELECT setval(pg_get_serial_sequence('roles', 'id'), COALESCE((SELECT MAX(id) FROM roles), 1), TRUE);
SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 1), TRUE);
SELECT setval(pg_get_serial_sequence('notifications', 'id'), COALESCE((SELECT MAX(id) FROM notifications), 1), TRUE);
SELECT setval(pg_get_serial_sequence('treatments', 'id'), COALESCE((SELECT MAX(id) FROM treatments), 1), TRUE);
SELECT setval(pg_get_serial_sequence('clinics', 'id'), COALESCE((SELECT MAX(id) FROM clinics), 1), TRUE);
SELECT setval(pg_get_serial_sequence('reviews', 'id'), COALESCE((SELECT MAX(id) FROM reviews), 1), TRUE);
SELECT setval(pg_get_serial_sequence('comments', 'id'), COALESCE((SELECT MAX(id) FROM comments), 1), TRUE);
SELECT setval(pg_get_serial_sequence('community_rules', 'id'), COALESCE((SELECT MAX(id) FROM community_rules), 1), TRUE);
SELECT setval(pg_get_serial_sequence('reports', 'id'), COALESCE((SELECT MAX(id) FROM reports), 1), TRUE);
SELECT setval(pg_get_serial_sequence('contact_forms', 'id'), COALESCE((SELECT MAX(id) FROM contact_forms), 1), TRUE);

COMMIT;
