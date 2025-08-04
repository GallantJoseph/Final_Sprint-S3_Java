-- Table for roles
CREATE TABLE IF NOT EXISTS roles  (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Table for users
CREATE TABLE IF NOT EXISTS users  (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
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
  mem_type_name VARCHAR(50) NOT NULL,
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
  FOREIGN KEY(member_id) REFERENCES users(user_id)
);

-- Table for workout_class_types
CREATE TABLE IF NOT EXISTS workout_class_types  (
    workout_class_type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
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
    FOREIGN KEY (trainer_id) REFERENCES users(user_id)
);

-- Table for merchandise types
CREATE TABLE IF NOT EXISTS merchandise_types (
    merchandise_type_id SERIAL PRIMARY KEY,
    merchandise_type_name VARCHAR(50) NOT NULL
);

-- Table for gym merchandise
CREATE TABLE IF NOT EXISTS gym_merchandise (
    merchandise_id SERIAL PRIMARY KEY,
    merchandise_type_id INTEGER NOT NULL,
    merchandise_name VARCHAR (50) NOT NULL,
    merchandise_price DECIMAL(10,2) NOT NULL,
    quantity_in_stock INTEGER NOT NULL,
    FOREIGN KEY (merchandise_type_id) REFERENCES merchandise_types(merchandise_type_id) 
);

-- Insert role types only if they don't already exist
INSERT INTO roles (name)
VALUES ('member'), ('admin'), ('trainer')
ON CONFLICT (name) DO NOTHING;