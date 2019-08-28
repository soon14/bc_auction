# ������� ��Ŀ ��ġ �غ�
sudo apt update
sudo apt install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update

apt-cache policy docker-ce

# ��Ŀ ��ġ
sudo apt install docker-ce docker-ce-cli containerd.io

# ���� ���� Ȯ��(��Ŀ)
sudo systemctl status docker

# ��Ŀ �̹��� �ٿ�ε�(haribo-mySQL)
docker pull emblockit/haribo-mysql

# ��Ŀ �̹��� Ȯ��
docker images

# �����̳� ����
docker run -d -p 3306:3306 --name haribo-mysql emblockit/haribo-mysql

# �����̳� ���� Ȯ��
docker ps -a

# ��Ŀ �����̳� ���� �α� ����
docker logs -f haribo-mysql

# ��Ŀ �����̳� ���� �� ����
docker stop haribo-mysql
docker start haribo-mysql

# ���� �������� ����ڿ��� �����ֱ�(docker�� root������ �⺻�̱� ����)
sudo usermod -a -G docker $USER
sudo service docker restart

# Dockerfile �ۼ�
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

