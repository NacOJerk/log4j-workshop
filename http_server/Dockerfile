FROM ubuntu:latest

RUN apt update && apt install -y openssh-server

RUN apt-get install -y python3 python3-pip  

WORKDIR /home/site

COPY requirements.txt requirements.txt

RUN python3 -m pip install -r requirements.txt

COPY . .

RUN rm -rf files
RUN rm Dockerfile
RUN mv startUp.sh /tmp/startUp.sh

RUN adduser --home /home/site/files file_uploader
RUN  echo 'file_uploader:file_uploader' | chpasswd


RUN service ssh start

EXPOSE 2222 8080
RUN chmod +x /tmp/startUp.sh

ENTRYPOINT ["/tmp/startUp.sh"]
