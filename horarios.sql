-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 22-08-2021 a las 18:13:31
-- Versión del servidor: 8.0.17
-- Versión de PHP: 7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `horarios`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horarios`
--

CREATE TABLE `horarios` (
  `id` bigint(20) NOT NULL,
  `hora` time DEFAULT NULL,
  `contador_reservaciones` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(40) DEFAULT NULL,
  `nombre_visible` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`, `nombre_visible`) VALUES
(1, 'ROLE_USUARIO', 'Usuario');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `username` varchar(25) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `password` varchar(70) DEFAULT NULL,
  `signed_up_with_google` bit(1) DEFAULT NULL,
  `rol_id` bigint(20) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios_horarios`
--

CREATE TABLE `usuarios_horarios` (
  `usuario_id` bigint(20) DEFAULT NULL,
  `horario_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Disparadores `usuarios_horarios`
--
DELIMITER $$
CREATE TRIGGER `decrementar_contador_reservaciones` AFTER DELETE ON `usuarios_horarios` FOR EACH ROW BEGIN
    IF (SELECT contador_reservaciones FROM horarios WHERE id=OLD.horario_id) > 0 THEN BEGIN
        UPDATE horarios SET contador_reservaciones=contador_reservaciones-1 WHERE id=OLD.horario_id;
    END; END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `incrementar_contador_reservaciones` AFTER INSERT ON `usuarios_horarios` FOR EACH ROW BEGIN
    IF (SELECT contador_reservaciones FROM horarios WHERE id=NEW.horario_id) < 8 THEN BEGIN
        UPDATE horarios SET contador_reservaciones=contador_reservaciones+1 WHERE id=NEW.horario_id;
    END; END IF;
END
$$
DELIMITER ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `horarios`
--
ALTER TABLE `horarios`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rol_id` (`rol_id`);

--
-- Indices de la tabla `usuarios_horarios`
--
ALTER TABLE `usuarios_horarios`
  ADD KEY `usuario_id` (`usuario_id`),
  ADD KEY `horario_id` (`horario_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `horarios`
--
ALTER TABLE `horarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`);

--
-- Filtros para la tabla `usuarios_horarios`
--
ALTER TABLE `usuarios_horarios`
  ADD CONSTRAINT `usuarios_horarios_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `usuarios_horarios_ibfk_2` FOREIGN KEY (`horario_id`) REFERENCES `horarios` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
