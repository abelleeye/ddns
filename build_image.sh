VERSION=1.0.0

echo '删除./build文件夹'
rm -rf ./build

echo '打包应用jar。。。'
gradle jar

echo '创建镜像。。。'
docker build -t abelly/ddns:$VERSION .

