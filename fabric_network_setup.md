### 패브릭 네트워크 구축 하기

```
1. AuctionConsortium  폴더를 깃에 올린다.
2. vagrant 에서 git을 설치해 AuctionConsortium 폴더를 내려 받는다
3. sudo apt install python-pip
4. sudo pip install docker-compose
```



#### Kafka-zookeeper cluster 실행

``` 
docker-compose –f zookeeper-compose.yaml up -d
docker-compose -f kafka-compose.yaml up -d
```

---

### 권한 설정 하기

```linux
chmod 777 파일이름

777은
사용자, 그룹 사용자, 외부 사용자의 권한을 의미하고 rdx 순이다.
r = 4
d = 2
x = 1

3개를 다주면 4+2+1 = 7

----------------------------------
만약에 test.txt 파일의 권한을 본인은 모든 권한, 그룹 사용자는 읽기와 실행이, 외부 사용자는 읽기만 가능하게 하고 싶다면

chomd 754 test.txt

로 실행하면 된다.
```







### 네트워크 구동 순서

```
채널 구성요소 생성
- 패브릭 채널 구성을 위한 트랜잭션(~.tx)와 Genesis block(~.block) 생성

./configtxgen AuctionOrdererGenesis -outputBlock orderer-genesis.block -channelID ordererchannel001 

./configtxgen -profile AuctionChannel -outputCreateChannelTx auctionchannel001.tx -channelID auctionchannel001
```



생성된 트랜잭션 파일을 이동시킨다.

```
mv auctionchannel1001.tx ./channel-artifacts
```



