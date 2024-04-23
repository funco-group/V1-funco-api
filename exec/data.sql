create table funco.hello
(
    created_at datetime default current_timestamp() not null,
    id         bigint auto_increment
        primary key,
    updated_at datetime default current_timestamp() not null,
    message    varchar(255)                         null
);

create table funco.member
(
    cash            bigint                               not null comment '가용 현금',
    created_at      datetime default current_timestamp() not null,
    id              bigint auto_increment
        primary key,
    updated_at      datetime default current_timestamp() not null,
    nickname        varchar(20)                          null comment '닉네임',
    introduction    varchar(100)                         null comment '한 줄 소개',
    profile_url     varchar(2100)                        null comment '프로필 이미지 URL',
    oauth_server_id varchar(255)                         not null comment 'OAuth 코드',
    oauth_server    enum ('GOOGLE')                      not null comment 'OAuth 코드',
    status          enum ('NORMAL', 'ADMIN', 'WITHDRAW') not null comment '회원 유형'
);

create table funco.daily_statistics
(
    acc_return_rate   double                               not null comment '누적수익률',
    date              date                                 not null comment '날짜',
    return_rate       double                               not null comment '수익률',
    acc_return_result bigint                               not null comment '누적손익',
    beginning_asset   bigint                               not null comment '기초자산',
    created_at        datetime default current_timestamp() not null,
    ending_asset      bigint                               not null comment '기말자산',
    id                bigint auto_increment
        primary key,
    member_id         bigint                               not null,
    return_result     bigint                               not null comment '손익',
    updated_at        datetime default current_timestamp() not null,
    constraint FKlybwdfunakbewmnoevh8but5g
        foreign key (member_id) references funco.member (id)
);

create table funco.favorite_coin
(
    created_at datetime default current_timestamp() not null,
    id         bigint auto_increment
        primary key,
    member_id  bigint                               not null,
    updated_at datetime default current_timestamp() not null,
    ticker     varchar(20)                          not null comment '코인명',
    constraint FKgun7rakpnrx9wnk4nw7e2muu2
        foreign key (member_id) references funco.member (id)
);

create table funco.follow
(
    return_rate  double                               null comment '수익률',
    settled      bit                                  null comment '정산여부',
    cash         bigint                               not null comment '가용 현금',
    commission   bigint                               null comment '수수료',
    created_at   datetime default current_timestamp() not null,
    follower_id  bigint                               not null,
    following_id bigint                               not null,
    id           bigint auto_increment
        primary key,
    investment   bigint                               not null comment '초기투자금',
    settle_date  datetime(6)                          null comment '정산날짜',
    settlement   bigint                               null comment '정산금액',
    updated_at   datetime default current_timestamp() not null,
    constraint FKkcoemc64xrm83cdmhyaphcuiu
        foreign key (following_id) references funco.member (id),
    constraint FKtps7gpodlrhxlji90u6r3mlng
        foreign key (follower_id) references funco.member (id)
);

create table funco.follow_trade
(
    volume     double                               null comment '수량',
    created_at datetime default current_timestamp() not null,
    follow_id  bigint                               not null,
    id         bigint auto_increment
        primary key,
    order_cash bigint                               null comment '주문 금액',
    price      bigint                               null comment '체결 가격',
    updated_at datetime default current_timestamp() not null,
    ticker     varchar(20)                          null comment '코인명',
    trade_type enum ('BUY', 'SELL')                 null comment '거래 구분',
    constraint FKlbfnpivow3pap7yidchy0g1nf
        foreign key (follow_id) references funco.follow (id)
);

create table funco.following_coin
(
    volume        double                               not null comment '개수',
    average_price bigint                               not null comment '평균단가',
    created_at    datetime default current_timestamp() not null,
    follow_id     bigint                               not null,
    id            bigint auto_increment
        primary key,
    updated_at    datetime default current_timestamp() not null,
    ticker        varchar(20)                          not null comment '코인명',
    constraint FKhr0r430rao0l8svjqi94ehwpr
        foreign key (follow_id) references funco.follow (id)
);

create table funco.holding_coin
(
    volume        double                               not null comment '개수',
    average_price bigint                               not null comment '평균단가',
    created_at    datetime default current_timestamp() not null,
    id            bigint auto_increment
        primary key,
    member_id     bigint                               not null,
    updated_at    datetime default current_timestamp() not null,
    ticker        varchar(20)                          not null comment '코인명',
    constraint FKs94u2c3d6gxd9n9yqajln5a5w
        foreign key (member_id) references funco.member (id)
);

create table funco.monthly_statistics
(
    acc_return_rate   double                               not null comment '누적수익률',
    date              date                                 not null comment '날짜',
    return_rate       double                               not null comment '수익률',
    acc_return_result bigint                               not null comment '누적손익',
    beginning_asset   bigint                               not null comment '기초자산',
    created_at        datetime default current_timestamp() not null,
    ending_asset      bigint                               not null comment '기말자산',
    id                bigint auto_increment
        primary key,
    member_id         bigint                               not null,
    return_result     bigint                               not null comment '손익',
    updated_at        datetime default current_timestamp() not null,
    constraint FKnh3qr2edr4ttfyyapoflva3nh
        foreign key (member_id) references funco.member (id)
);

create table funco.notification
(
    read_yn    bit                                                      not null comment '읽음여부',
    created_at datetime default current_timestamp()                     not null,
    id         bigint auto_increment
        primary key,
    member_id  bigint                                                   not null,
    updated_at datetime default current_timestamp()                     not null,
    message    varchar(255)                                             not null comment '알림메시지',
    type       enum ('BUY', 'SELL', 'SETTLE', 'FORCE_SETTLE', 'FOLLOW') not null comment '알림타입',
    constraint FK1xep8o2ge7if6diclyyx53v4q
        foreign key (member_id) references funco.member (id)
);

create table funco.open_trade
(
    volume     double                               null comment '수량',
    buy_price  bigint                               null comment '구매한 가격',
    created_at datetime default current_timestamp() not null,
    id         bigint auto_increment
        primary key,
    member_id  bigint                               not null,
    order_cash bigint                               null comment '주문 금액',
    price      bigint                               null comment '가격',
    updated_at datetime default current_timestamp() not null,
    ticker     varchar(20)                          null comment '코인명',
    trade_type enum ('BUY', 'SELL')                 null comment '거래 구분',
    constraint FKfpnusbau3d7guiuhxukkmltyl
        foreign key (member_id) references funco.member (id)
);

create table funco.trade
(
    volume     double                               null comment '수량',
    created_at datetime default current_timestamp() not null,
    id         bigint auto_increment
        primary key,
    member_id  bigint                               not null,
    order_cash bigint                               null comment '주문 금액',
    price      bigint                               null comment '체결 가격',
    updated_at datetime default current_timestamp() not null,
    ticker     varchar(20)                          null comment '코인명',
    trade_type enum ('BUY', 'SELL')                 null comment '거래 구분',
    constraint FK81wfg93lq5ss4he99pa7utsbf
        foreign key (member_id) references funco.member (id)
);

INSERT INTO member (cash, nickname, introduction, profile_url, oauth_server_id, oauth_server, status)
VALUES (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE', 'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
        'NORMAL'),
       (100000000, '박세웅', '안녕하세요! 박세웅입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=1', 'oauth_parksewoong',
        'GOOGLE', 'NORMAL'),
       (100000000, '황주영', '반가워요! 황주영입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=2', 'oauth_hwangjuyoung',
        'GOOGLE', 'NORMAL'),
       (100000000, '소재열', '안녕하세요. 저는 소재열이에요.', 'https://www.dummyimage.com/600x400/000/fff&text=3', 'oauth_sojaeyeol',
        'GOOGLE', 'NORMAL'),
       (100000000, '엄소현', '반가워요! 엄소현입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=4', 'oauth_eomsohyun',
        'GOOGLE',
        'NORMAL'),
       (100000000, '이선주', '안녕하세요! 이선주입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=5', 'oauth_leeseonjoo',
        'GOOGLE', 'NORMAL'),
       (100000000, '이태호', '반가워요! 이태호입니다.', 'https://www.dummyimage.com/600x400/000/fff&text=6', 'oauth_leetaeho',
        'GOOGLE',
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

INSERT INTO follow (return_rate, settled, cash, commission, follower_id, following_id, investment, settle_date,
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

SET @startDate := '2024-01-01'; -- 시작 날짜 설정
SET @memberId := 7; -- member_id 설정
SET @numRecords := 94; -- 삽입할 데이터의 개수 설정
SET @beginningAsset := 10000000; -- 초기 기초자산 설정
SET @accReturnResult := 0; -- 누적손익 초기화

DROP TEMPORARY TABLE IF EXISTS temp_daily_statistics;
CREATE TEMPORARY TABLE temp_daily_statistics (
                                                 date DATE,
                                                 beginning_asset BIGINT,
                                                 return_result BIGINT,
                                                 return_rate DOUBLE,
                                                 acc_return_rate DOUBLE,
                                                 acc_return_result BIGINT,
                                                 ending_asset BIGINT
);

WHILE @numRecords > 0 DO
        SET @returnResult := FLOOR(RAND() * 20000001) - 10000000;
        SET @endingAsset := @beginningAsset + @returnResult; -- 기말자산 계산
        SET @returnRate := ROUND((@returnResult / @beginningAsset) * 100, 2);
        SET @accReturnResult := @accReturnResult + @returnResult;
        SET @accReturnRate := ROUND((@accReturnResult / 10000000) * 100, 2);

INSERT INTO temp_daily_statistics (date, beginning_asset, return_result, return_rate, acc_return_rate, acc_return_result, ending_asset)
VALUES (@startDate, @beginningAsset, @returnResult, @returnRate, @accReturnRate, @accReturnResult, @endingAsset);

SET @beginningAsset := @endingAsset;
        SET @startDate := DATE_ADD(@startDate, INTERVAL 1 DAY);
        SET @numRecords := @numRecords - 1;
END WHILE;

INSERT INTO funco.daily_statistics (date, beginning_asset, return_result, return_rate, ending_asset, member_id, acc_return_rate, acc_return_result)
SELECT date, beginning_asset, return_result, return_rate, ending_asset, @memberId, acc_return_rate, acc_return_result
FROM temp_daily_statistics;

DROP TEMPORARY TABLE IF EXISTS temp_daily_statistics;

INSERT INTO funco.monthly_statistics (acc_return_rate, date, return_rate, acc_return_result, beginning_asset, ending_asset, member_id, return_result)
VALUES
    (0.05, '2024-01-01', -10.12, -615000, 10000000, 6001000, 7, 116000),
    (0.10, '2024-02-01', 52.03, 166622, 14400000, 10002003, 7, 153270),
    (0.15, '2024-03-01', 16.01, -55214, 10020000, 53055151, 7, 52514);
