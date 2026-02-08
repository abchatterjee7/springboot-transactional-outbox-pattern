-- Order Service Database Initialization
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

USE order_service_db;

-- Order service doesn't need initial data
-- Tables will be created automatically by JPA
