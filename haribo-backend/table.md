### auction

```mysql
auction_id			경매 id (pk)
auction_makedate 	생성일시
auction_makerid 	경매 생성자 id
auction_goodsid 	경매작품id
auction_status 		상태
auction_start 		시작일시
auction_end 		종료일시
auction_min 		최저가
auction_contract 	컨트랙트 주소
```

---

### member

```mysql
mem_id			회원 아이디 (pk)
mem_name		이름
mem_mail		이메일
mem_registdate	등록일시
mem_pass		비밀번호
```

---

### art

```mysql
art_id			작품 id(pk)
art_name		작품 이름
art_detail		작품 설명
art_isopen		작품 공개여부
art_status		작품 상태
art_mem			작품 회원id (fk - member)
```

---

### wallet	

```mysql
wallet_id			지갑 id(pk)
wallet_addr			지갑 주소
wallet_mem			지갑 소유자 id (fk - member)
wallet_money		지갑 잔액
wallet_chargecount	지갑 충전 회수
```

---

### transaction 

기본이 영어이기 때문에 아래 두개반 바꿈

``` mysql
trancation_id		트랜젝션 id
trancation_savedate	트랜젝션 저장일시
```

---

### own

``` mysql
own_id 		작품 소유 id (pk)
own_mem 	소유자 id (fk - member)
own_art 	작품 id (fk - art)
own_start 	소유 시작 일자
own_end 	소유 종료 일자
```

---

### bid

```mysql
bid_id 			경매 입찰 id (pk)
bid_mem			경매 참여자 id (fk - member)
bid_auction		경매 id (fk - auction)
bid_date		입찰 일시
bid_price		입찰 금액
bid_issuccess	낙찰 여부
```

