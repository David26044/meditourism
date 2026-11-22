INSERT INTO users (id, email, name, password, is_verified, role_id)
VALUES
    (1, 'ana.gomez@example.com', 'Ana Gómez', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (2, 'bruno.diaz@example.com', 'Bruno Díaz', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (3, 'carla.rojas@example.com', 'Carla Rojas', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (4, 'daniel.perez@example.com', 'Daniel Pérez', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (5, 'elena.martin@example.com', 'Elena Martín', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (6, 'felipe.castro@example.com', 'Felipe Castro', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (7, 'gabriela.ruiz@example.com', 'Gabriela Ruiz', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (8, 'hector.vargas@example.com', 'Héctor Vargas', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (9, 'bloqueado@example.com', 'Usuario Bloqueado', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', TRUE, 2),
    (10, 'sin.verificar@example.com', 'Usuario Sin Verificar', '$2a$12$jX6O17Dcu62ZBBk2qUTGxOsVUHJEOc/c.k6Ny1Kd6UDsG3VId82Ve', FALSE, 2);

INSERT INTO blocked_users (user_id, reason, date)
VALUES
    (9, 'Usuario de prueba bloqueado para validar las restricciones de escritura.', CURRENT_TIMESTAMP - INTERVAL '17 days');

INSERT INTO treatments (id, name, description)
VALUES
    (1, 'Implantes dentales', 'Evaluación, planeación y cirugía de implantes dentales con corona definitiva.'),
    (2, 'Blanqueamiento dental', 'Procedimiento estético dental con supervisión profesional.'),
    (3, 'Ortodoncia invisible', 'Alineadores transparentes con controles periódicos presenciales o virtuales.'),
    (4, 'Diseño de sonrisa', 'Carillas y ajustes estéticos para armonizar la sonrisa.'),
    (5, 'Cirugía refractiva', 'Corrección visual con láser para miopía, hipermetropía y astigmatismo.'),
    (6, 'Cirugía de cataratas', 'Reemplazo del cristalino con lente intraocular.'),
    (7, 'Chequeo cardiológico', 'Consulta, electrocardiograma, ecocardiograma y exámenes de seguimiento.'),
    (8, 'Rehabilitación física', 'Plan de fisioterapia para recuperación funcional.'),
    (9, 'Reemplazo de rodilla', 'Artroplastia total de rodilla con plan de rehabilitación incluido.'),
    (10, 'Rinoplastia', 'Cirugía estética y funcional de la nariz.'),
    (11, 'Liposucción', 'Remoción de grasa localizada con anestesia general.'),
    (12, 'Chequeo ejecutivo', 'Valoración médica completa con laboratorios e imágenes en uno o dos días.');

INSERT INTO clinics (id, name, email, address, description)
VALUES
    (1, 'Clínica Altavista Salud', 'contacto@altavista.test', 'Carrera 7 #80-10, Bogotá', 'Clínica general con enfoque en turismo médico.'),
    (2, 'Dental Sonrisa Paisa', 'hola@sonrisapaisa.test', 'Calle 10 #43A-20, Medellín', 'Centro odontológico especializado en estética e implantes.'),
    (3, 'Centro Visión Clara', 'info@visionclara.test', 'Avenida 19 #120-30, Bogotá', 'Centro oftalmológico para cirugía refractiva y cataratas.'),
    (4, 'Corazón del Valle Cardio', 'citas@corazondelvalle.test', 'Calle 5 #38-55, Cali', 'Atención cardiovascular para pacientes nacionales e internacionales.'),
    (5, 'Ortopédica del Caribe', 'contacto@ortopedicacaribe.test', 'Carrera 53 #76-120, Barranquilla', 'Cirugía ortopédica y rehabilitación.'),
    (6, 'Estética Bahía Cartagena', 'info@esteticabahia.test', 'Avenida San Martín #8-40, Cartagena', 'Cirugía plástica con paquetes de recuperación frente al mar.'),
    (7, 'Odonto Santander', 'citas@odontosantander.test', 'Calle 36 #27-15, Bucaramanga', 'Odontología integral a precios competitivos.'),
    (8, 'Clínica Cafetera Integral', 'contacto@cafeteraintegral.test', 'Avenida Circunvalar #12-50, Pereira', 'Medicina general, cardiología y oftalmología en el Eje Cafetero.'),
    (9, 'Ojo Sano Centro Oftalmológico', 'hola@ojosano.test', 'Carrera 43A #1-50, Medellín', 'Cirugía ocular con tecnología láser.'),
    (10, 'Rehabilita Bogotá', 'info@rehabilita.test', 'Calle 100 #19-60, Bogotá', 'Ortopedia y fisioterapia de alta complejidad.'),
    (11, 'Sierra Nevada Medical', 'contact@sierranevada.test', 'Carrera 2 #18-30, Santa Marta', 'Cirugía estética y chequeos médicos con atención en inglés.'),
    (12, 'Clínica Montaña Azul', 'citas@montanaazul.test', 'Calle 7 Sur #42-70, Medellín', 'Odontología estética y cirugía plástica.');

INSERT INTO clinic_treatment (clinic_id, treatment_id, price)
VALUES
    (1, 1, 4100.00),
    (1, 7, 380.00),
    (1, 8, 140.00),
    (1, 9, 9800.00),
    (1, 12, 650.00),
    (2, 1, 3800.00),
    (2, 2, 260.00),
    (2, 3, 2900.00),
    (2, 4, 3400.00),
    (3, 5, 1750.00),
    (3, 6, 2300.00),
    (4, 7, 410.00),
    (4, 12, 590.00),
    (5, 8, 130.00),
    (5, 9, 9200.00),
    (6, 4, 3900.00),
    (6, 10, 3600.00),
    (6, 11, 3100.00),
    (7, 1, 3500.00),
    (7, 2, 240.00),
    (7, 3, 2700.00),
    (8, 6, 2100.00),
    (8, 7, 350.00),
    (8, 8, 110.00),
    (8, 12, 520.00),
    (9, 5, 1650.00),
    (9, 6, 2200.00),
    (10, 8, 125.00),
    (10, 9, 9500.00),
    (11, 10, 3300.00),
    (11, 11, 2900.00),
    (11, 12, 610.00),
    (12, 3, 3000.00),
    (12, 4, 3600.00),
    (12, 10, 3800.00);

INSERT INTO reviews (id, user_id, clinic_id, content, date, rating)
VALUES
    (1, 1, 2, 'Me hice dos implantes. Me explicaron todo antes de empezar y el resultado se ve natural.', CURRENT_TIMESTAMP - INTERVAL '60 days', 5),
    (2, 2, 2, 'Buen blanqueamiento, aunque la cita inicial se retrasó casi una hora.', CURRENT_TIMESTAMP - INTERVAL '55 days', 4),
    (3, 3, 3, 'La cirugía refractiva fue rápida y al día siguiente ya veía bien. Muy recomendados.', CURRENT_TIMESTAMP - INTERVAL '52 days', 5),
    (4, 4, 1, 'El chequeo ejecutivo fue completo, pero los resultados tardaron más de lo prometido.', CURRENT_TIMESTAMP - INTERVAL '50 days', 3),
    (5, 5, 6, 'Rinoplastia con muy buen acompañamiento. La coordinación del hotel y los traslados fue impecable.', CURRENT_TIMESTAMP - INTERVAL '48 days', 5),
    (6, 6, 5, 'Me operaron la rodilla y la recuperación va según lo planeado. El equipo de fisioterapia es excelente.', CURRENT_TIMESTAMP - INTERVAL '46 days', 5),
    (7, 7, 4, 'Atención cardiológica seria y clara. Me dieron un informe en inglés para mi médico en casa.', CURRENT_TIMESTAMP - INTERVAL '44 days', 5),
    (8, 8, 7, 'Precio justo para la ortodoncia invisible, pero la comunicación por mensajes fue lenta.', CURRENT_TIMESTAMP - INTERVAL '42 days', 3),
    (9, 1, 9, 'Cirugía de cataratas sin complicaciones. El seguimiento posterior fue muy atento.', CURRENT_TIMESTAMP - INTERVAL '40 days', 5),
    (10, 2, 10, 'Las sesiones de rehabilitación me ayudaron mucho después de mi lesión de hombro.', CURRENT_TIMESTAMP - INTERVAL '38 days', 4),
    (11, 3, 11, 'La liposucción salió bien, pero el postoperatorio fue más doloroso de lo que me explicaron.', CURRENT_TIMESTAMP - INTERVAL '36 days', 3),
    (12, 4, 12, 'Diseño de sonrisa espectacular. Valió cada peso del viaje a Medellín.', CURRENT_TIMESTAMP - INTERVAL '34 days', 5),
    (13, 5, 8, 'Clínica muy organizada. Hice el chequeo cardiológico y el ejecutivo en dos días.', CURRENT_TIMESTAMP - INTERVAL '32 days', 4),
    (14, 6, 3, 'Me cobraron un examen adicional que no estaba en la cotización inicial.', CURRENT_TIMESTAMP - INTERVAL '30 days', 2),
    (15, 7, 2, 'Volví para un segundo implante y la experiencia fue igual de buena.', CURRENT_TIMESTAMP - INTERVAL '28 days', 5),
    (16, 8, 6, 'El diseño de sonrisa quedó bien, aunque tuve que volver para un ajuste.', CURRENT_TIMESTAMP - INTERVAL '27 days', 4),
    (17, 1, 5, 'La sala de espera estaba llena y esperé más de dos horas para la valoración.', CURRENT_TIMESTAMP - INTERVAL '26 days', 2),
    (18, 2, 4, 'Excelente trato del cardiólogo. Resolvió todas mis dudas con paciencia.', CURRENT_TIMESTAMP - INTERVAL '25 days', 5),
    (19, 3, 7, 'El blanqueamiento duró poco; a los dos meses ya había perdido tono.', CURRENT_TIMESTAMP - INTERVAL '24 days', 2),
    (20, 4, 9, 'Cirugía refractiva con tecnología moderna. El personal habla inglés sin problema.', CURRENT_TIMESTAMP - INTERVAL '23 days', 5),
    (21, 5, 1, 'La rehabilitación física es buena, pero el parqueadero es un caos.', CURRENT_TIMESTAMP - INTERVAL '22 days', 3),
    (22, 6, 11, 'Chequeo ejecutivo completo con vista al mar. La atención fue cálida.', CURRENT_TIMESTAMP - INTERVAL '21 days', 4),
    (23, 7, 12, 'La ortodoncia invisible va bien, aunque los controles se agendan con mucha anticipación.', CURRENT_TIMESTAMP - INTERVAL '20 days', 4),
    (24, 8, 10, 'Me reemplazaron la rodilla y a las seis semanas ya caminaba sin bastón.', CURRENT_TIMESTAMP - INTERVAL '19 days', 5),
    (25, 9, 1, 'Reseña escrita antes de que la cuenta fuera bloqueada, para probar restricciones.', CURRENT_TIMESTAMP - INTERVAL '18 days', 2),
    (26, 1, 6, 'No recomiendo la liposucción aquí: la valoración fue apresurada.', CURRENT_TIMESTAMP - INTERVAL '17 days', 1),
    (27, 2, 8, 'Cirugía de cataratas en ambos ojos. Todo el proceso fue claro y bien coordinado.', CURRENT_TIMESTAMP - INTERVAL '16 days', 5),
    (28, 3, 2, 'El diseño de sonrisa superó mis expectativas. El doctor fue muy detallista.', CURRENT_TIMESTAMP - INTERVAL '14 days', 5),
    (29, 4, 5, 'La recuperación de la rodilla fue lenta y sentí poco seguimiento después del alta.', CURRENT_TIMESTAMP - INTERVAL '12 days', 2),
    (30, 5, 3, 'Cirugía de cataratas sin dolor. Me sentí seguro todo el tiempo.', CURRENT_TIMESTAMP - INTERVAL '10 days', 5),
    (31, 6, 7, 'Implantes a buen precio, aunque el proceso tomó más visitas de las previstas.', CURRENT_TIMESTAMP - INTERVAL '8 days', 3),
    (32, 7, 9, 'Buena atención, pero la clínica queda lejos del centro y el transporte no estaba incluido.', CURRENT_TIMESTAMP - INTERVAL '6 days', 3),
    (33, 8, 4, 'El chequeo cardiológico detectó algo que mi médico no había visto. Muy agradecido.', CURRENT_TIMESTAMP - INTERVAL '4 days', 5),
    (34, 1, 12, 'La rinoplastia quedó muy natural. El cirujano escuchó lo que yo quería.', CURRENT_TIMESTAMP - INTERVAL '2 days', 5),
    (35, 3, 11, 'Primera consulta para rinoplastia: la valoración fue clara y sin presión para decidir.', CURRENT_TIMESTAMP - INTERVAL '8 hours', 4);

INSERT INTO comments (id, review_id, father_comment_id, user_id, content, date)
VALUES
    (1, 1, NULL, 3, '¿Cuánto tiempo pasó entre la cirugía y la corona definitiva?', CURRENT_TIMESTAMP - INTERVAL '59 days'),
    (2, 1, 1, 1, 'Unos cuatro meses. Hice dos viajes: uno para el implante y otro para la corona.', CURRENT_TIMESTAMP - INTERVAL '58 days'),
    (3, 1, 2, 3, 'Gracias, me sirve mucho para planear las fechas.', CURRENT_TIMESTAMP - INTERVAL '58 days'),
    (4, 2, NULL, 7, 'A mí también me pasó con el retraso, pero el resultado valió la pena.', CURRENT_TIMESTAMP - INTERVAL '54 days'),
    (5, 6, NULL, 2, '¿Cuántas semanas te quedaste en Colombia después de la operación?', CURRENT_TIMESTAMP - INTERVAL '45 days'),
    (6, 6, 5, 6, 'Tres semanas, para hacer las primeras sesiones de fisioterapia allá.', CURRENT_TIMESTAMP - INTERVAL '44 days'),
    (7, 11, NULL, 5, 'Gracias por la sinceridad, es importante saber cómo es el postoperatorio.', CURRENT_TIMESTAMP - INTERVAL '35 days'),
    (8, 14, NULL, 4, 'Pide siempre la cotización por escrito con todos los exámenes incluidos.', CURRENT_TIMESTAMP - INTERVAL '29 days'),
    (9, 14, 8, 6, 'Buen consejo, la próxima vez lo haré así.', CURRENT_TIMESTAMP - INTERVAL '29 days'),
    (10, 17, NULL, 8, 'Yo fui un sábado y tampoco fue rápido. Mejor ir entre semana.', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (11, 25, NULL, 9, 'Comentario de un usuario bloqueado, para probar acciones restringidas.', CURRENT_TIMESTAMP - INTERVAL '18 days'),
    (12, 26, NULL, 2, '¿Hablaste con el coordinador de pacientes? A mí me ayudó cuando tuve dudas.', CURRENT_TIMESTAMP - INTERVAL '16 days'),
    (13, 26, 12, 1, 'No sabía que existía, gracias por el dato.', CURRENT_TIMESTAMP - INTERVAL '16 days'),
    (14, 34, NULL, 5, '¿Me podrías decir el nombre del cirujano? Estoy pensando hacérmela allí.', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (15, 34, 14, 1, 'Claro, te escribo por mensaje privado para no publicar datos personales.', CURRENT_TIMESTAMP - INTERVAL '1 day');

INSERT INTO notifications (id, user_id, title, content, date)
VALUES
    (1, 1, 'Nuevo comentario en tu reseña', 'Carla comentó en tu reseña sobre Dental Sonrisa Paisa.', CURRENT_TIMESTAMP - INTERVAL '59 days'),
    (2, 3, 'Nueva respuesta a tu comentario', 'Ana respondió tu comentario.', CURRENT_TIMESTAMP - INTERVAL '58 days'),
    (3, 6, 'Nuevo comentario en tu reseña', 'Bruno comentó en tu reseña sobre Ortopédica del Caribe.', CURRENT_TIMESTAMP - INTERVAL '45 days'),
    (4, 9, 'Cuenta bloqueada', 'Tu cuenta fue bloqueada por incumplir las normas de la comunidad.', CURRENT_TIMESTAMP - INTERVAL '17 days'),
    (5, 1, 'Nuevo comentario en tu reseña', 'Elena comentó en tu reseña sobre Clínica Montaña Azul.', CURRENT_TIMESTAMP - INTERVAL '1 day');

INSERT INTO community_rules (id, rule_text)
VALUES
    (1, 'No publicar datos personales sensibles de pacientes o profesionales.'),
    (2, 'No publicar contenido ofensivo, discriminatorio o amenazante.'),
    (3, 'No publicar spam ni promociones no verificadas.'),
    (4, 'Mantener las reseñas basadas en experiencias reales.');

INSERT INTO reports (id, reporter_user, target_review_id, target_comment_id, community_rule_id, reason)
VALUES
    (1, 2, 25, NULL, 4, 'La reseña parece escrita para probar el sistema, no una experiencia real.'),
    (2, 4, NULL, 11, 3, 'El comentario no aporta a la conversación y parece spam.'),
    (3, 7, NULL, 14, 1, 'El comentario pide datos personales del cirujano.');

INSERT INTO contact_forms (id, user_id, treatment_id, preferred_clinic_id, email, message, number, date)
VALUES
    (1, 1, 1, 2, 'ana.gomez@example.com', 'Quiero una cotización para dos implantes y conocer las fechas disponibles en marzo.', '+57 300 000 0001', CURRENT_TIMESTAMP - INTERVAL '61 days'),
    (2, 2, 5, 3, 'bruno.diaz@example.com', '¿Soy candidato a cirugía refractiva con cuatro dioptrías de miopía?', '+57 300 000 0002', CURRENT_TIMESTAMP - INTERVAL '53 days'),
    (3, 6, 9, 5, 'felipe.castro@example.com', 'Necesito información sobre el reemplazo de rodilla y el tiempo de recuperación.', '+57 300 000 0006', CURRENT_TIMESTAMP - INTERVAL '47 days'),
    (4, 5, 12, 8, 'elena.martin@example.com', 'Busco un chequeo ejecutivo completo para dos personas.', '+57 300 000 0005', CURRENT_TIMESTAMP - INTERVAL '33 days'),
    (5, 7, 3, 12, 'gabriela.ruiz@example.com', '¿Cuántos controles requiere la ortodoncia invisible si vivo fuera del país?', '+57 300 000 0007', CURRENT_TIMESTAMP - INTERVAL '21 days'),
    (6, 8, 10, 6, 'hector.vargas@example.com', 'Quisiera una valoración virtual para rinoplastia antes de viajar.', '+57 300 000 0008', CURRENT_TIMESTAMP - INTERVAL '5 days'),
    (7, 9, 2, 7, 'bloqueado@example.com', 'Formulario enviado por un usuario bloqueado, para probar restricciones.', '+57 300 000 0009', CURRENT_TIMESTAMP - INTERVAL '12 hours');

SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 1), TRUE);
SELECT setval(pg_get_serial_sequence('notifications', 'id'), COALESCE((SELECT MAX(id) FROM notifications), 1), TRUE);
SELECT setval(pg_get_serial_sequence('treatments', 'id'), COALESCE((SELECT MAX(id) FROM treatments), 1), TRUE);
SELECT setval(pg_get_serial_sequence('clinics', 'id'), COALESCE((SELECT MAX(id) FROM clinics), 1), TRUE);
SELECT setval(pg_get_serial_sequence('reviews', 'id'), COALESCE((SELECT MAX(id) FROM reviews), 1), TRUE);
SELECT setval(pg_get_serial_sequence('comments', 'id'), COALESCE((SELECT MAX(id) FROM comments), 1), TRUE);
SELECT setval(pg_get_serial_sequence('community_rules', 'id'), COALESCE((SELECT MAX(id) FROM community_rules), 1), TRUE);
SELECT setval(pg_get_serial_sequence('reports', 'id'), COALESCE((SELECT MAX(id) FROM reports), 1), TRUE);
SELECT setval(pg_get_serial_sequence('contact_forms', 'id'), COALESCE((SELECT MAX(id) FROM contact_forms), 1), TRUE);
