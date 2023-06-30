# v1
数据来源自ip2region开源仓库v1版本ip.merge.txt，使用maker转换为db文件

# v2
数据来源自ip2region开源仓库v2版本ip.merge.txt，使用maker转换为xdb文件

# apnic
数据来源自apnic，使用Apnic2StdConverter处理为ip.merge.txt格式，再转换为xdb文件

## xdb文件生成方式
> 1. 获取最新数据
> 
> curl http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest -o ip.apnic-source.txt

> 2. 生成ip2region可以处理的格式
>
> Apnic2StdConverter.main()生成ip.merge_apnic.txt
 
> 3. 生成xdb文件
>
> java -jar ip2region-maker-1.0.0.jar --src=${src} --dst=${dst}
>
> 示例：
>
> java -jar ip2region-maker-1.0.0.jar --src=./apnic/ip.merge_apnic.txt  --dst=./apnic/ip2region_apnic.xdb
