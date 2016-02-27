# fastdfs-client

fastdfs-client is a [FastDFS](https://github.com/happyfish100/fastdfs) java client 
based on [Netty 4](http://netty.io) .

## 前置条件

* Java 8 required.
* Fastdfs 服务器端字符集编码需使用 UTF-8

## maven

```xml
<dependency>
	<groupId>cn.strong</groupId>
	<artifactId>fastdfs-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 使用说明

程序的主要入口为 FastdfsClient 门面类。

FastdfsClient 的后端依赖于 FastdfsExecutor 做实际的组件通信工作，以及一组 tracker 地址列表。

FastdfsExecutor 具有生命周期，它维护了一个通道连接池以及 IO 线程组等资源。
它需要在启动时调用 init() 方法初始化并且在关闭时调用 shutdown() 方法释放资源。
这两个方法分别具有 `@PostConstruct` 和 `@PreDestroy` 注解，可方便容器管理。

```java
FastdfsSettings settings = new FastdfsSettings();
FastdfsExecutor executor = new FastdfsExecutor(settings);

try {
	List<InetSocketAddress> trackers = Arrays.asList(new InetSocketAddress(
				"192.168.20.68", 22122));
	FastdfsClient dfs = new FastdfsClient(executor, trackers);
	
	// do something
} finally {
	executor.close();
}
```

得到 FastdfsClient 示例以后即可通过相应 API 进行上传下载等操作。

```java
byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
client.upload(bytes, bytes.length, "txt", null).action((path, ex) -> {
  if (ex != null) {
    ex.printStackTrace();
  } else {
    System.out.println("upload path: " + path.toString());
    //-> upload path: group1/M00/05/22/wKgURFUP60SEYdruAAAAAEKHwLQ894.txt
  }
});

StoragePath spath = StoragePath.fromFullPath("group1/M00/05/22/wKgURFUP60SEYdruAAAAAEKHwLQ124.txt");
FileOutputStream out = new FileOutputStream("temp.txt");
dfs.download(spath, out).action((path, ex) -> {
  if (ex != null) {
    ex.printStackTrace();
  } else {
    System.out.println("download success!");
    //-> download success!
  }
});
```

其中每个 API 返回的都是一个表示异步操作的 `AsyncAction` 对象，该对象可以通过添加 `Callback` 来添加回调函数。

详细用法，请详见 FastdfsClient API。