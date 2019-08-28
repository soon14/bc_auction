## 이더리움 설치
```
sudo apt-get update
sudo apt-get install software-properties-common
sudo add-apt-repository -y ppa:ethereum/ethereum
sudo apt-get install ethereum
geth version
```

#genesis.json 
```
{
       "config": {
               "chainId": 15151,
               "homesteadBlock": 0,
               "eip155Block": 0,
               "eip158Block": 0
       },
       "alloc": {},
       "coinbase": "0x0000000000000000000000000000000000000000",
       "difficulty": "0x10",
       "extraData": "",
       "gasLimit": "9999999",
       "nonce": "0xdeadbeefdeadbeef",
       "mixhash": "0x0000000000000000000000000000000000000000000000000000000000000000",
       "parentHash": "0x0000000000000000000000000000000000000000000000000000000000000000",
       "timestamp": "0x00"
}
```
# json complie for javascript
geth --datadir /home/ubuntu/dev/eth00/ init /home/ubuntu/dev/eth00/CustomGenesis.json
geth --datadir /home/ubuntu/dev/eth01/ init /home/ubuntu/dev/eth01/CustomGenesis.json
geth --datadir /home/ubuntu/dev/eth02/ init /home/ubuntu/dev/eth02/CustomGenesis.json

# ethereum start
geth --networkid 15151 --maxpeers 3 --datadir /home/ubuntu/dev/eth00/ --port 30300 console
geth --networkid 15151 --maxpeers 3 --datadir /home/ubuntu/dev/eth01/ --port 30301 console
geth --networkid 15151 --maxpeers 3 --datadir ~/dev/eth02 --port 30302 --rpc --rpcport 8545 --rpcaddr 0.0.0.0 --rpccorsdomain "*" --rpcapi "admin,net,miner,eth,rpc,web3,txpool,debug,db,personal" console

# 위의 내용을 한 번에 실행 할 CustomLaunch.sh 생성
```
#!/bin/bash

geth --networkid 15150 --maxpeers 3 --datadir /home/vagrant/dev/eth00/ --port 30300 console
```

# aws transaction send ethereum
to 01 from 00
eth.sendTransaction({from:"0x5dc668a8c3a5e4aa9cad38250fa2578dc7f18b24", to:"0x4aa73246616b664372649d09edad88390d684fab", value:web3.toWei(1, "ether")});






