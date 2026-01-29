-- ============================================================================
-- SCRIPT DE DATOS DE PRUEBA - HORARIOS UNSIS
-- ============================================================================
-- Crea datos mínimo para todas las tablas base de la aplicación
-- Excluye tablas que se obtienen del frontend: examen, horario_sinodal, horario_especial
-- ============================================================================

-- LIMPIAR DATOS EXISTENTES (OPCIONAL - comentar si quieres conservar datos)
-- DELETE FROM sinodales;
-- DELETE FROM profesor_materia;
-- DELETE FROM materia;
-- DELETE FROM profesor;
-- DELETE FROM aulas;
-- DELETE FROM school_hours;
-- DELETE FROM "user";

-- ============================================================================
-- 1. TABLA: profesor (20 registros)
-- ============================================================================
INSERT INTO profesor (nombre, sabatico) VALUES
('Dr. Juan Pérez López', false),
('Dra. María González García', false),
('Ing. Carlos Rodríguez Martínez', false),
('Lic. Ana Martínez Hernández', true),
('Prof. Luis Sánchez Torres', false),
('Dr. Fernando Díaz Ruiz', false),
('Dra. Patricia Jiménez Morales', false),
('Ing. Roberto Flores Vargas', false),
('Lic. Sandra Romero Castro', false),
('Prof. Miguel Ángel López Soto', false),
('Dr. Alejandro Ramírez Vega', false),
('Dra. Claudia Moreno Reyes', true),
('Ing. David González Espinoza', false),
('Lic. Gloria Vázquez Domínguez', false),
('Prof. Enrique Cortés Núñez', false),
('Dr. Ricardo Molina Pacheco', false),
('Dra. Teresa Navarro García', false),
('Ing. Sergio Cabrera López', false),
('Lic. Victoria Mendoza Silva', false),
('Prof. Ángel Ortega Vargas', false);

-- ============================================================================
-- 2. TABLA: materia (20 registros)
-- ============================================================================
INSERT INTO materia (nombre, es_academia) VALUES
('Matemáticas Discretas', false),
('Programación I', false),
('Algoritmos y Estructuras de Datos', false),
('Bases de Datos', false),
('Programación Web', false),
('Sistemas Operativos', false),
('Redes de Computadoras', false),
('Arquitectura de Computadoras', false),
('Ingeniería de Software', false),
('Compiladores', false),
('Seguridad Informática', false),
('Desarrollo Móvil', false),
('Inteligencia Artificial', false),
('Procesamiento de Imágenes', false),
('Administración de Sistemas', false),
('Teoría de Computación', false),
('Métodos Numéricos', false),
('Estadística Aplicada', false),
('Gestión de Proyectos TI', false),
('Electrónica Digital', true);

-- ============================================================================
-- 3. TABLA: aulas (20 registros)
-- ============================================================================
INSERT INTO aulas (nombre, capacidad) VALUES
('A-101', 30),
('A-102', 30),
('A-103', 35),
('A-104', 35),
('A-201', 40),
('A-202', 40),
('A-203', 45),
('A-204', 45),
('LAB-01 (Laboratorio Computadoras)', 25),
('LAB-02 (Laboratorio Computadoras)', 25),
('LAB-03 (Laboratorio Redes)', 20),
('LAB-04 (Laboratorio Electrónica)', 20),
('AULA-V (Aula Virtual)', 50),
('AULA-W (Aula Virtual)', 50),
('C-001', 30),
('C-002', 30),
('C-003', 35),
('C-004', 35),
('D-101 (Seminario)', 15),
('D-102 (Seminario)', 15);

-- ============================================================================
-- 4. TABLA: school_hours (8 períodos + receso = 9 registros)
-- ============================================================================
INSERT INTO school_hours (period_number, start_time, end_time, is_break, description) VALUES
(1, '08:00:00', '09:00:00', false, 'Período 1'),
(2, '09:00:00', '10:00:00', false, 'Período 2'),
(3, '10:00:00', '10:15:00', true, 'Receso'),
(4, '10:15:00', '11:15:00', false, 'Período 3'),
(5, '11:15:00', '12:15:00', false, 'Período 4'),
(6, '12:15:00', '13:00:00', true, 'Almuerzo'),
(7, '13:00:00', '14:00:00', false, 'Período 5'),
(8, '14:00:00', '15:00:00', false, 'Período 6'),
(9, '15:00:00', '16:00:00', false, 'Período 7');

-- ============================================================================
-- 5. TABLA: profesor_materia (20 asignaciones profesor-materia)
-- ============================================================================
INSERT INTO profesor_materia (id_profesor, id_materia) VALUES
(1, 1),   -- Dr. Juan Pérez -> Matemáticas Discretas
(2, 2),   -- Dra. María González -> Programación I
(3, 3),   -- Ing. Carlos Rodríguez -> Algoritmos
(4, 4),   -- Lic. Ana Martínez -> Bases de Datos
(5, 5),   -- Prof. Luis Sánchez -> Programación Web
(6, 6),   -- Dr. Fernando Díaz -> Sistemas Operativos
(7, 7),   -- Dra. Patricia Jiménez -> Redes
(8, 8),   -- Ing. Roberto Flores -> Arquitectura
(9, 9),   -- Lic. Sandra Romero -> Ingeniería de Software
(10, 10), -- Prof. Miguel Ángel López -> Compiladores
(11, 11), -- Dr. Alejandro Ramírez -> Seguridad Informática
(12, 12), -- Dra. Claudia Moreno -> Desarrollo Móvil
(13, 13), -- Ing. David González -> Inteligencia Artificial
(14, 14), -- Lic. Gloria Vázquez -> Procesamiento de Imágenes
(15, 15), -- Prof. Enrique Cortés -> Administración de Sistemas
(16, 16), -- Dr. Ricardo Molina -> Teoría de Computación
(17, 17), -- Dra. Teresa Navarro -> Métodos Numéricos
(18, 18), -- Ing. Sergio Cabrera -> Estadística Aplicada
(19, 19), -- Lic. Victoria Mendoza -> Gestión de Proyectos TI
(20, 20); -- Prof. Ángel Ortega -> Electrónica Digital

-- ============================================================================
-- 6. TABLA: sinodales (20 asignaciones sinodales)
-- Nota: id_profesor_sinodal y id_profesor_titular deben ser profesores existentes
--       id_materia debe ser una materia existente
-- ============================================================================
INSERT INTO sinodales (id_profesor_sinodal, id_profesor_titular, id_materia) VALUES
(2, 1, 1),   -- María González como sinodal, Juan Pérez como titular, Matemáticas Discretas
(3, 2, 2),   -- Carlos Rodríguez como sinodal, María González como titular, Programación I
(4, 3, 3),   -- Ana Martínez como sinodal, Carlos Rodríguez como titular, Algoritmos
(5, 4, 4),   -- Luis Sánchez como sinodal, Ana Martínez como titular, Bases de Datos
(6, 5, 5),   -- Fernando Díaz como sinodal, Luis Sánchez como titular, Programación Web
(7, 6, 6),   -- Patricia Jiménez como sinodal, Fernando Díaz como titular, Sistemas Operativos
(8, 7, 7),   -- Roberto Flores como sinodal, Patricia Jiménez como titular, Redes
(9, 8, 8),   -- Sandra Romero como sinodal, Roberto Flores como titular, Arquitectura
(10, 9, 9),  -- Miguel Ángel López como sinodal, Sandra Romero como titular, Ingeniería SW
(11, 10, 10),-- Alejandro Ramírez como sinodal, Miguel Ángel López como titular, Compiladores
(12, 11, 11),-- Claudia Moreno como sinodal, Alejandro Ramírez como titular, Seguridad
(13, 12, 12),-- David González como sinodal, Claudia Moreno como titular, Desarrollo Móvil
(14, 13, 13),-- Gloria Vázquez como sinodal, David González como titular, IA
(15, 14, 14),-- Enrique Cortés como sinodal, Gloria Vázquez como titular, Procesamiento Imágenes
(16, 15, 15),-- Ricardo Molina como sinodal, Enrique Cortés como titular, Admin Sistemas
(17, 16, 16),-- Teresa Navarro como sinodal, Ricardo Molina como titular, Teoría Computación
(18, 17, 17),-- Sergio Cabrera como sinodal, Teresa Navarro como titular, Métodos Numéricos
(19, 18, 18),-- Victoria Mendoza como sinodal, Sergio Cabrera como titular, Estadística
(20, 19, 19),-- Ángel Ortega como sinodal, Victoria Mendoza como titular, Gestión Proyectos
(1, 20, 20); -- Juan Pérez como sinodal, Ángel Ortega como titular, Electrónica Digital

-- ============================================================================
-- 7. TABLA: usuario (usuarios de prueba para autenticación)
-- ============================================================================
-- Password: "password123" (BCrypt hash)
INSERT INTO usuario (nombre, email, username, password, rol, activo) VALUES
('Administrador del Sistema', 'admin@unsis.edu.mx', 'admin', '$2a$10$slYQmyNdGzin7olVgsqFUOJ2mC5FwmY7vKXH8W9G7CbVLYGfIpyWa', 'ADMIN', true),
('Usuario Servicios Escolares', 'user@unsis.edu.mx', 'user', '$2a$10$slYQmyNdGzin7olVgsqFUOJ2mC5FwmY7vKXH8W9G7CbVLYGfIpyWa', 'SERV', true),
('Jefe de Carrera', 'coordinador@unsis.edu.mx', 'jefe', '$2a$10$slYQmyNdGzin7olVgsqFUOJ2mC5FwmY7vKXH8W9G7CbVLYGfIpyWa', 'JEFE', true);

-- ============================================================================
-- CONFIRMACIÓN
-- ============================================================================
-- SELECT COUNT(*) as total_profesores FROM profesor;
-- SELECT COUNT(*) as total_materias FROM materia;
-- SELECT COUNT(*) as total_aulas FROM aulas;
-- SELECT COUNT(*) as total_horarios FROM school_hours;
-- SELECT COUNT(*) as total_asignaciones FROM profesor_materia;
-- SELECT COUNT(*) as total_sinodales FROM sinodales;
-- SELECT COUNT(*) as total_usuarios FROM usuario;
