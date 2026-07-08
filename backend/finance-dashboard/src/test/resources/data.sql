INSERT INTO accounts (
    account_id,
    username,
    email,
    hash_password,
    phone_number
) VALUES (
    5,
    'adam',
    'ajlarson0731@gmail.com',
    'abc123',
    '937-479-0303'
);

INSERT INTO accounts (
    account_id,
    username,
    email,
    hash_password,
    phone_number
) VALUES (
    6,
    'chris',
    'clarson1@woh.rr.com',
    'def456',
    '937-416-5220'
);

INSERT INTO accounts (
    account_id,
    username,
    email,
    hash_password,
    phone_number
) VALUES (
    7,
    'amber',
    'anlarson0702@gmail.com',
    'ghi789',
    '937-417-7870'
);

INSERT INTO transactions (
    transaction_id,
    time_created,
    account_id,
    amount,
    currency,
    status,
    category
) VALUES (
    14,
    TIMESTAMP '2026-07-07 20:15:00',
    5,
    123.45,
    'USD',
    'COMPLETED',
    'ELECTRONICS'
);

INSERT INTO transactions (
    transaction_id,
    time_created,
    account_id,
    amount,
    currency,
    status,
    category
) VALUES (
    15,
    TIMESTAMP '2026-07-07 20:30:00',
    6,
    850.00,
    'USD',
    'PENDING',
    'RENT'
);

INSERT INTO transactions (
    transaction_id,
    time_created,
    account_id,
    amount,
    currency,
    status,
    category
) VALUES (
    16,
    TIMESTAMP '2026-07-07 21:00:00',
    7,
    130.75,
    'USD',
    'COMPLETED',
    'FOOD'
);
