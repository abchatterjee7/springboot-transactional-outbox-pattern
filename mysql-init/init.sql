CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

USE inventory_service_db;

INSERT INTO INVENTORY_TBL (product_name, product_type, stock_quantity, price) VALUES
('Laptop', 'Electronics', 50, 999.99),
('Mouse', 'Electronics', 200, 29.99),
('Keyboard', 'Electronics', 150, 79.99),
('Monitor', 'Electronics', 75, 299.99),
('Webcam', 'Electronics', 100, 89.99);
