# 우분투에 도커 설치 준비
sudo apt update
sudo apt install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update

apt-cache policy docker-ce

# 도커 설치
sudo apt install docker-ce docker-ce-cli containerd.io

# 서비스 실행 확인(도커)
sudo systemctl status docker

# 도커 이미지 다운로드(haribo-mySQL)
docker pull emblockit/haribo-mysql

# 도커 이미지 확인
docker images

# 컨테이너 실행
docker run -d -p 3306:3306 --name haribo-mysql emblockit/haribo-mysql

# 컨테이너 실행 확인
docker ps -a

# 도커 컨테이너 실행 로그 보기
docker logs -f haribo-mysql

# 도커 컨테이너 중지 및 실행
docker stop haribo-mysql
docker start haribo-mysql

# 현재 접속중인 사용자에게 권한주기(docker는 root권한이 기본이기 때문)
sudo usermod -a -G docker $USER
sudo service docker restart

# Dockerfile 작성
```
FROM ubuntu

COPY ./go-ethereum /home/go-ethereum
WORKDIR /home/go-ethereum/

RUN apt-get update
RUN apt-get install -y build-essential libgmp3-dev golang git

RUN git checkout refs/tags/v1.5.5
RUN make geth
RUN cp build/bin/geth /usr/local/bin

WORKDIR /home/DATA_STORE/
COPY ./CustomGenesis.json /home/DATA_STORE

RUN geth --datadir "/home/DATA_STORE" init /home/DATA_STORE/CustomGenesis.json
```

