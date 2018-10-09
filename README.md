# AnyCrawler
基于webmagic的一个Android爬虫库，webmagic是java的一个爬虫库。
# webmagic
https://github.com/code4craft/webmagic

Android废弃了Httpclient导致直接使用webmagic会有冲突，无法运行，这里主要是修改了网页抓取部分，改用了Okhttp3来实现，作了简单的封装，目前只能抓取图片，后续会支持文字，视频等内容的抓取，也可以自定义抓取规则，抓取指定的内容。

# 使用方式

allprojects {
    repositories {
        ...
        maven {
            url 'https://jitpack.io'
        }
    }
}

implementation 'com.github.YuntaoWei:AnyCrawler:-SNAPSHOT'
