--changeset create-test-data context:dev
INSERT INTO order_request (order_id, queue_id, status, queue_position, waiting_time, total_price, reference_number, in_queue)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'QUEUE001', 'pending', 1, 5, 10.99, 'ORD15032022123500123', true),
    ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'QUEUE002', 'processing', 2, 8, 15.75, 'ORD15032022123600234', true),
    ('123e4567-e89b-12d3-a456-556642440000', 'QUEUE001', 'completed', 0, 0, 20.50, 'ORD15032022123700345', false),
    ('6ba7b810-9dad-11d1-80b4-00c04fd430c9', 'QUEUE003', 'pending', 3, 12, 8.25, 'ORD15032022123800456', true),
    ('98765432-e89b-12d3-a456-556655440000', 'QUEUE002', 'canceled', 0, 0, 12.00, 'ORD15032022123900567', false);
