INSERT INTO member (cash, created_at, updated_at, nickname, introduction, profile_url, oauth_id, status) VALUES
(10000000, '2024-03-22 10:00:00', '2024-03-22 10:00:00', '황주영', '안녕하세요, User1입니다.', 'http://example.com/profiles/user1.jpg', 'oauthid1', 'NORMAL'),
(20000000, '2024-03-22 11:30:00', '2024-03-22 11:30:00', '박세웅', '시스템 관리자입니다.', 'http://example.com/profiles/adminuser.jpg', 'oauthid2', 'ADMIN'),
(50000000, '2024-03-22 09:20:00', '2024-03-22 09:20:00', '엄소현', '이제 사용하지 않는 계정입니다.', 'http://example.com/profiles/withdrawnuser.jpg', 'oauthid3', 'WITHDRAW'),
(15000000, '2024-03-22 08:15:00', '2024-03-22 08:15:00', '이태호', '안녕하세요, User2입니다. 여행을 좋아해요.', 'http://example.com/profiles/user2.jpg', 'oauthid4', 'NORMAL'),
(30000000, '2024-03-22 12:45:00', '2024-03-22 12:45:00', '김선주', '게임과 프로그래밍을 좋아하는 User3입니다.', 'http://example.com/profiles/user3.jpg', 'oauthid5', 'NORMAL');

INSERT INTO holding_coin (member_id, ticker, volume, average_price) VALUES
(1, 'KRW-BTC', 2.5, 95000000),
(1, 'KRW-ETH', 10.0, 3000000),
(1, 'KRW-XRP', 500.0, 1500);


-- 체결 거래 내역
INSERT INTO trade (member_id, ticker, trade_type, volume, order_cash, price, status)
VALUES (1, 'KRW-BTC', 'BUY', 0.08, 70000000, 500000, false),
       (1, 'KRW-ETH', 'SELL', 1.2, 8300000, 1000000, false);

-- 미체결 거래 내역
INSERT INTO open_trade (member_id, ticker, trade_type, volume, order_cash, price, status)
VALUES (1, 'KRW-BTC', 'BUY', 0.08, 80000000, 700000, false),
       (1, 'KRW-ETH', 'SELL', 1.2, 9110000, 1400000, false);
