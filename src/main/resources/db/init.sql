-- Crust & Cloud Production Planner Database Schema

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'OPERATOR',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products/Recipes table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    prep_time VARCHAR(50),
    baking_time VARCHAR(50),
    yield_amount VARCHAR(100),
    difficulty VARCHAR(20) DEFAULT 'Easy',
    image VARCHAR(50),
    ingredients TEXT,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Production batches table
CREATE TABLE IF NOT EXISTS production_batches (
    id BIGSERIAL PRIMARY KEY,
    batch_number VARCHAR(100) NOT NULL UNIQUE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    status VARCHAR(50) NOT NULL DEFAULT 'Waiting',
    sales_order VARCHAR(100),
    total_qty INTEGER DEFAULT 0,
    so_co_excess INTEGER DEFAULT 0,
    exch_loss INTEGER DEFAULT 0,
    excess INTEGER DEFAULT 0,
    samples INTEGER DEFAULT 0,
    carry_over INTEGER DEFAULT 0,
    theor_excess INTEGER DEFAULT 0,
    batch_qty INTEGER DEFAULT 0,
    capacity INTEGER DEFAULT 0,
    dough VARCHAR(50),
    proc_time INTEGER DEFAULT 0,
    start_sponge VARCHAR(20),
    end_dough VARCHAR(20),
    end_batch VARCHAR(20),
    order_batch VARCHAR(20),
    line_batch VARCHAR(20),
    plan_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Production lines table
CREATE TABLE IF NOT EXISTS production_lines (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    current_product_id BIGINT REFERENCES products(id),
    current_batch_id BIGINT REFERENCES production_batches(id),
    status VARCHAR(50) DEFAULT 'stopped',
    progress INTEGER DEFAULT 0,
    current_stage VARCHAR(100),
    start_time VARCHAR(20),
    estimated_end VARCHAR(20),
    temperature DECIMAL(5,2),
    humidity DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Schedules table
CREATE TABLE IF NOT EXISTS schedules (
    id BIGSERIAL PRIMARY KEY,
    schedule_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Schedule shifts table
CREATE TABLE IF NOT EXISTS schedule_shifts (
    id BIGSERIAL PRIMARY KEY,
    schedule_id BIGINT NOT NULL REFERENCES schedules(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    time_slot VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'scheduled',
    batches INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alerts table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    production_line_id BIGINT REFERENCES production_lines(id),
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@crustcloud.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRGdjGj/n3.qJKlF/JgKjZCvfUfLy', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Insert products
INSERT INTO products (id, name, category, prep_time, baking_time, yield_amount, difficulty, image, ingredients, status) VALUES
(1, 'Everyday Bread 8s', 'Loaf', '30 min', '45 min', '8 slices', 'Easy', '🍞', 'Flour,Yeast,Water,Salt,Sugar', 'active'),
(2, 'Soft Roll White 6s', 'Rolls', '45 min', '20 min', '6 rolls', 'Easy', '🥖', 'Flour,Milk,Butter,Yeast,Egg', 'active'),
(3, 'Melon Pan Classic', 'Specialty', '60 min', '15 min', '12 pieces', 'Medium', '🍈', 'Flour,Sugar,Butter,Cookie Dough,Melon Essence', 'active'),
(4, 'Hokkaido Milk Bread', 'Loaf', '120 min', '35 min', '1 loaf', 'Hard', '🥛', 'Tangzhong,Milk,Cream,Flour,Butter', 'active'),
(5, 'Croissant Plain', 'Pastry', '180 min', '18 min', '8 pieces', 'Hard', '🥐', 'Flour,Butter,Yeast,Milk,Sugar', 'draft'),
(6, 'Danish Pastry', 'Pastry', '150 min', '15 min', '12 pieces', 'Hard', '🥮', 'Puff Pastry,Custard,Fruits,Glaze', 'draft')
ON CONFLICT DO NOTHING;

-- Reset sequence for products
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));

-- Insert production batches
INSERT INTO production_batches (id, batch_number, product_id, status, sales_order, total_qty, so_co_excess, exch_loss, excess, samples, carry_over, theor_excess, batch_qty, capacity, dough, proc_time, start_sponge, end_dough, end_batch, order_batch, line_batch, plan_date) VALUES
(1, 'B-2026-0423-001', 1, 'Waiting', NULL, 10, 0, 0, 0, 2, 0, 0, 10, 2340, '275 kg', 292, '00:00', '04:52', '08:59', '1st', '1st', CURRENT_DATE),
(2, 'B-2026-0423-002', 2, 'InProgress', 'SO-2024-001', 15, 2, 0, 1, 1, 0, 0, 15, 1800, '200 kg', 245, '01:30', '05:15', '09:30', '2nd', '2nd', CURRENT_DATE),
(3, 'B-2026-0423-003', 3, 'Completed', 'SO-2024-003', 20, 0, 1, 0, 3, 2, 1, 20, 1500, '180 kg', 180, '02:00', '04:30', '07:00', '3rd', '3rd', CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- Reset sequence for production_batches
SELECT setval('production_batches_id_seq', (SELECT MAX(id) FROM production_batches));

-- Insert production lines
INSERT INTO production_lines (id, name, current_product_id, current_batch_id, status, progress, current_stage, start_time, estimated_end, temperature, humidity) VALUES
(1, 'Line A - Loaf', 1, 1, 'running', 78, 'Baking', '00:00', '08:59', 185.0, 65.0),
(2, 'Line B - Rolls', 2, 2, 'running', 45, 'Makeup Dividing', '01:30', '09:30', 28.0, 70.0),
(3, 'Line C - Specialty', 3, 3, 'paused', 62, 'Makeup Panning', '02:00', '07:00', 25.0, 68.0)
ON CONFLICT DO NOTHING;

-- Reset sequence for production_lines
SELECT setval('production_lines_id_seq', (SELECT MAX(id) FROM production_lines));

-- Insert schedules
INSERT INTO schedules (id, schedule_date) VALUES
(1, CURRENT_DATE),
(2, CURRENT_DATE + INTERVAL '1 day')
ON CONFLICT DO NOTHING;

-- Reset sequence for schedules
SELECT setval('schedules_id_seq', (SELECT MAX(id) FROM schedules));

-- Insert schedule shifts
INSERT INTO schedule_shifts (schedule_id, product_id, time_slot, status, batches) VALUES
(1, 1, '00:00 - 09:00', 'scheduled', 1),
(1, 2, '09:00 - 15:00', 'scheduled', 2),
(1, 3, '15:00 - 21:00', 'scheduled', 1),
(2, 4, '00:00 - 08:00', 'pending', 2),
(2, 5, '08:00 - 14:00', 'pending', 3)
ON CONFLICT DO NOTHING;

-- Insert alerts
INSERT INTO alerts (type, message, production_line_id, is_read) VALUES
('warning', 'Line C paused - Awaiting ingredients', 3, FALSE),
('info', 'Line A entered baking stage', 1, FALSE),
('success', 'Quality check passed - Batch B-0321-005', NULL, TRUE)
ON CONFLICT DO NOTHING;
