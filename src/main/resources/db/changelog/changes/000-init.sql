CREATE TABLE IF NOT EXISTS order_request (
    order_id VARCHAR(100) PRIMARY KEY,
    queue_id VARCHAR(100),
    status VARCHAR(50),
    queue_position INT,
    waiting_time INT,
    total_price DECIMAL(10, 2),
    reference_number VARCHAR(100),
    in_queue BOOLEAN
);

CREATE INDEX idx__order_request__queue_id ON order_request(queue_id);