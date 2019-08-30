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



