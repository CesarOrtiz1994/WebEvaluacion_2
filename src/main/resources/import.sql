
INSERT INTO USERS (CORREO, PASSWORD, NOMBRES, APELLIDOS, FECHA_NAC, QUESTION, SECRET_QUESTION, FOTO, ENABLED) VALUES ('admin01@uteq.edu.mx', '$2a$10$0brTBIzhapsmz7.vIEomg.Hnt54zKpQyZeehxIaZSq6VAEtyhHyE2', 'admin', '01', '2000-01-01', '¿Cuál es tu color favorito?', 'azul', 'avatar.png',1);

INSERT INTO authorities (CORREO, authority) VALUES ('admin01@uteq.edu.mx', 'ROLE_ADMIN');
INSERT INTO authorities (CORREO, authority) VALUES ('admin01@uteq.edu.mx', 'ROLE_MAESTRO');
INSERT INTO authorities (CORREO, authority) VALUES ('admin01@uteq.edu.mx', 'ROLE_ESTUDIANTE');
