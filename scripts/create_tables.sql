-- Table for roles
CREATE TABLE IF NOT EXISTS roles  (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Table for users
CREATE TABLE IF NOT EXISTS users  (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password TEXT,
    first_name VARCHAR (50) NOT NULL,
    last_name VARCHAR (50) NOT NULL,
    street_address VARCHAR(50),
    city VARCHAR(50),
    province VARCHAR(2),
    postal_code VARCHAR(6),
    email VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(25),
    role_id INTEGER NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- Table for membership_types
CREATE TABLE IF NOT EXISTS membership_types  (
  mem_type_id SERIAL PRIMARY KEY,
  mem_type_name VARCHAR(50) UNIQUE NOT NULL,
  mem_type_description VARCHAR(100),
  mem_type_cost DECIMAL(10,2) NOT NULL
);

-- Table for memberships
CREATE TABLE IF NOT EXISTS memberships  (
  membership_id SERIAL PRIMARY KEY,
  membership_type_id INTEGER NOT NULL,
  member_id INTEGER NOT NULL,
  membership_start DATE NOT NULL,
  membership_end DATE,
  FOREIGN KEY(membership_type_id) REFERENCES membership_types(mem_type_id),
  FOREIGN KEY(member_id) REFERENCES users(user_id),
  UNIQUE (membership_type_id, member_id)
);

-- Table for workout_class_types
CREATE TABLE IF NOT EXISTS workout_class_types  (
    workout_class_type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(100)
);

-- Table for workout_classes
CREATE TABLE IF NOT EXISTS workout_classes  (
    workout_class_id SERIAL PRIMARY KEY,
    workout_class_type_id INTEGER NOT NULL,
    workout_class_desc VARCHAR(50),
    trainer_id INTEGER NOT NULL,
    workout_class_datetime TIMESTAMP NOT NULL,
    FOREIGN KEY (workout_class_type_id) REFERENCES workout_class_types(workout_class_type_id),
    FOREIGN KEY (trainer_id) REFERENCES users(user_id),
    UNIQUE (workout_class_desc, trainer_id, workout_class_datetime)
);

-- Table for merchandise types
CREATE TABLE IF NOT EXISTS merchandise_types (
    merchandise_type_id SERIAL PRIMARY KEY,
    merchandise_type_name VARCHAR(50) UNIQUE NOT NULL
);

-- Table for gym merchandise
CREATE TABLE IF NOT EXISTS gym_merchandise (
    merchandise_id SERIAL PRIMARY KEY,
    merchandise_type_id INTEGER NOT NULL,
    merchandise_name VARCHAR (50) UNIQUE NOT NULL,
    merchandise_price DECIMAL(10,2) NOT NULL,
    quantity_in_stock INTEGER NOT NULL,
    FOREIGN KEY (merchandise_type_id) REFERENCES merchandise_types(merchandise_type_id) 
);

-- Insert role types only if they dont already exist
INSERT INTO roles (name)
VALUES ('admin'), ('trainer'), ('member')
ON CONFLICT (name) DO NOTHING;

-- Insert membership types only if they dont already exist
INSERT INTO membership_types (mem_type_name, mem_type_description, mem_type_cost)
VALUES 
  ('Basic', 'Access to gym equipment during staffed hours only.', 29.99),
  ('Regular', 'Includes basic access plus unlimited classes and free towel service.', 49.99),
  ('Premium', 'All regular features plus 24/7 access, personal training sessions, and sauna use.', 69.99)
ON CONFLICT (mem_type_name) DO NOTHING;

-- Insert merchandise types only if they dont already exist
INSERT INTO merchandise_types (merchandise_type_name)
VALUES 
  ('Clothing'),
  ('Supplements'),
  ('Accessories')
ON CONFLICT (merchandise_type_name) DO NOTHING;

-- Insert sample gym merchandise (only if not already inserted by name)
INSERT INTO gym_merchandise (merchandise_type_id, merchandise_name, merchandise_price, quantity_in_stock)
VALUES 
  (1, 'Gym T-Shirt', 19.99, 50),
  (1, 'Hoodie', 39.99, 30),
  (2, 'Whey Protein (1kg)', 49.99, 20),
  (2, 'Pre-Workout', 29.99, 25),
  (3, 'Water Bottle', 9.99, 100),
  (3, 'Lifting Gloves', 14.99, 40)
ON CONFLICT (merchandise_name) DO NOTHING;

-- ===========DELETE WHEN NOT USED SAMPLE ADMIN MEMBERS AND TRAINERS=========== --

-- Insert sample users with different roles
-- Make sure roles have already been inserted ('admin', 'trainer', 'member')

-- Admins
INSERT INTO users (username, password, first_name, last_name, street_address, city, province, postal_code, email, phone, role_id)
VALUES
  ('admin1', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Bob', 'Ross', '123 Happy Tree Lane', 'CBS', 'NL', 'A1A1A1', 'bob.ross@example.com', '709-555-1000', 
   (SELECT role_id FROM roles WHERE name = 'admin')),
  ('admin2', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Oprah', 'Winfrey', '456 Talk Show Dr', 'St. Johns', 'NL', 'A1A2B2', 'oprah@example.com', '709-555-1001',
   (SELECT role_id FROM roles WHERE name = 'admin'))
ON CONFLICT (email) DO NOTHING;

-- Trainers
INSERT INTO users (username, password, first_name, last_name, street_address, city, province, postal_code, email, phone, role_id)
VALUES
  ('trainer1', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Gordon', 'Ramsay', '789 Kitchen St', 'Mount Pearl', 'NL', 'A1B2C3', 'gordon@example.com', '709-555-1002',
   (SELECT role_id FROM roles WHERE name = 'trainer')),
  ('trainer2', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Serena', 'Williams', '321 Tennis Court', 'Paradise', 'NL', 'A1C3D4', 'serena@example.com', '709-555-1003',
   (SELECT role_id FROM roles WHERE name = 'trainer'))
ON CONFLICT (email) DO NOTHING;

-- Members (1 of each membership type)
INSERT INTO users (username, password, first_name, last_name, street_address, city, province, postal_code, email, phone, role_id)
VALUES
  ('member1', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Nicolas', 'Cage', '654 Movie Blvd', 'CBS', 'NL', 'A1D4E5', 'cage@example.com', '709-555-1004',
   (SELECT role_id FROM roles WHERE name = 'member')),
  ('member2', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Beyonce', 'Knowles', '147 Music Rd', 'St. Johns', 'NL', 'A1E5F6', 'beyonce@example.com', '709-555-1005',
   (SELECT role_id FROM roles WHERE name = 'member')),
  ('member3', '$2a$10$6hK1os51h54ZZ6sS2Pser.sskt21ukhtzIVyj5ZJf6ad5ZynD7zA6', 'Ryan', 'Reynolds', '258 Film St', 'Mount Pearl', 'NL', 'A1F6G7', 'ryan@example.com', '709-555-1006',
   (SELECT role_id FROM roles WHERE name = 'member'))
ON CONFLICT (email) DO NOTHING;

-- Link members to their membership types
-- Make sure the users and membership_types are already inserted

INSERT INTO memberships (membership_type_id, member_id, membership_start, membership_end)
VALUES
  (
    (SELECT mem_type_id FROM membership_types WHERE mem_type_name = 'Basic'),
    (SELECT user_id FROM users WHERE username = 'member1'),
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '3 months'
  ),
  (
    (SELECT mem_type_id FROM membership_types WHERE mem_type_name = 'Regular'),
    (SELECT user_id FROM users WHERE username = 'member2'),
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '6 months'
  ),
  (
    (SELECT mem_type_id FROM membership_types WHERE mem_type_name = 'Premium'),
    (SELECT user_id FROM users WHERE username = 'member3'),
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '1 year'
  )
ON CONFLICT (membership_type_id, member_id) DO NOTHING;


INSERT INTO workout_class_types (name, description)
VALUES
  ('Yoga', 'A relaxing and flexible workout focusing on breathing and balance'),
  ('HIIT', 'High-intensity interval training for fat burning and endurance'),
  ('Spin', 'Indoor cycling class with energetic music'),
  ('Pilates', 'Core strength and flexibility exercises')
ON CONFLICT (name) DO NOTHING;

INSERT INTO workout_classes (workout_class_type_id, workout_class_desc, trainer_id, workout_class_datetime)
VALUES
  (
    (SELECT workout_class_type_id FROM workout_class_types WHERE name = 'Yoga'),
    'Morning Yoga Flow',
    (SELECT user_id FROM users WHERE username = 'trainer1'),
    '2025-08-15 08:00:00'
  ),
  (
    (SELECT workout_class_type_id FROM workout_class_types WHERE name = 'HIIT'),
    'HIIT Blast',
    (SELECT user_id FROM users WHERE username = 'trainer2'),
    '2025-08-15 18:00:00'
  ),
  (
    (SELECT workout_class_type_id FROM workout_class_types WHERE name = 'Spin'),
    'Evening Spin Class',
    (SELECT user_id FROM users WHERE username = 'trainer1'),
    '2025-08-16 19:30:00'
  ),
  (
    (SELECT workout_class_type_id FROM workout_class_types WHERE name = 'Pilates'),
    'Core Pilates',
    (SELECT user_id FROM users WHERE username = 'trainer2'),
    '2025-08-17 07:00:00'
  )
ON CONFLICT (workout_class_desc, trainer_id, workout_class_datetime) DO NOTHING;

