USE mybook_ms_read;


-- DDL
CREATE TABLE `tb_read_book` (
  `id` varchar(36) NOT NULL,
  `date_of_read` bigint NOT NULL,
  `page_of_read` int NOT NULL,
  `issbn` varchar(15) NOT NULL,
  `note` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
