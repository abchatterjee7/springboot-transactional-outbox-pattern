-- Inventory Service Database Initialization
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS inventory_service_db;
USE inventory_service_db;

CREATE TABLE IF NOT EXISTS INVENTORY_TBL (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_type VARCHAR(100) NOT NULL,
    stock_quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

INSERT INTO INVENTORY_TBL (product_name, product_type, stock_quantity, price) VALUES
('Laptop', 'Electronics', 50, 999.99),
('Mouse', 'Electronics', 200, 29.99),
('Keyboard', 'Electronics', 150, 79.99),
('Monitor', 'Electronics', 75, 299.99),
('Webcam', 'Electronics', 100, 89.99);
