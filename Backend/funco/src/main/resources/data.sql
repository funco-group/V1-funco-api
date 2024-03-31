INSERT INTO funco.member (cash, nickname, introduction, profile_url, oauth_server_id, oauth_server, status)
VALUES (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://example.com/profiles/parksewoong.jpg', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://example.com/profiles/hwangjuyoung.jpg', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://example.com/profiles/sojaeyeol.jpg', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://example.com/profiles/eomsohyun.jpg', 'oauth_eomsohyun', 'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://example.com/profiles/leeseonjoo.jpg', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://example.com/profiles/leetaeho.jpg', 'oauth_leetaeho', 'GOOGLE',
        'NORMAL');


INSERT INTO holding_coin (member_id, ticker, volume, average_price)
VALUES (1, 'KRW-BTC', 1.0, 95000000),
       (1, 'KRW-ETH', 1.0, 3000000),
       (1, 'KRW-BCH', 1.0, 1500),
       (1, 'KRW-SOL', 1.0, 1500),
       (2, 'KRW-BTC', 1.0, 95000000),
       (2, 'KRW-ETH', 1.0, 3000000),
       (2, 'KRW-BCH', 1.0, 1500),
       (2, 'KRW-SOL', 2.0, 1500),
       (3, 'KRW-BTC', 1.0, 95000000),
       (3, 'KRW-ETH', 1.0, 3000000),
       (3, 'KRW-BCH', 1.0, 1500),
       (3, 'KRW-SOL', 1.0, 1500),
       (4, 'KRW-BTC', 1.0, 95000000),
       (4, 'KRW-ETH', 1.0, 3000000),
       (4, 'KRW-BCH', 1.0, 1500),
       (4, 'KRW-SOL', 1.0, 1500),
       (5, 'KRW-BTC', 1.0, 95000000),
       (5, 'KRW-ETH', 1.0, 3000000),
       (5, 'KRW-BCH', 1.0, 1500),
       (5, 'KRW-SOL', 1.0, 1500);

INSERT INTO funco.follow (return_rate, settled, cash, commission, follower_id, following_id, investment, settle_date,
                          settlement)
VALUES (0.05, true, 50000, 1000, 1, 2, 100000, '2024-03-30 08:00:00', 52500),
       (NULL, false, 80000, NULL, 1, 3, 120000, NULL, NULL),
       (0.06, true, 70000, 2000, 2, 1, 150000, '2024-03-29 10:00:00', 75000),
       (0.04, true, 60000, 1200, 2, 4, 90000, '2024-03-30 11:00:00', 62400),
       (0.07, true, 90000, 2500, 3, 1, 180000, '2024-03-29 12:00:00', 94500),
       (NULL, false, 40000, NULL, 3, 2, 80000, NULL, NULL),
       (0.08, true, 100000, 3000, 4, 1, 200000, '2024-03-29 14:00:00', 108000),
       (0.06, true, 85000, 1800, 4, 3, 160000, '2024-03-30 15:00:00', 106800),
       (0.05, true, 95000, 2100, 5, 1, 210000, '2024-03-29 16:00:00', 110250),
       (0.04, true, 75000, 1500, 5, 2, 140000, '2024-03-30 17:00:00', 73500),
       (NULL, false, 30000, NULL, 6, 1, 130000, NULL, NULL),
       (0.09, true, 105000, 2800, 6, 3, 175000, '2024-03-29 19:00:00', 131250);


-- Member 테이블에 존재하는 회원 ID를 사용하여 알림 데이터를 insert합니다.
INSERT INTO Notification (member_id, type, message, read_yn) VALUES
(1, 'BUY', 'BTC 0.001개 900000000에 체결.', false),
(1, 'SELL', 'BTC 0.001개 900000000에 체결.', false),
(1, 'FOLLOW', '황주영님이 500000원 팔로우하셨습니다.', true);
