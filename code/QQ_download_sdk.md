 ## <center>手Q下载SDK子线程梳理</center>  ##

1. **网络请求线程**
- 代码位置：` com.tencent.tmassistantbase.network.PostHttpRequest `
- 作用：用于网络请求/临时线程
- 名称：YYB_NET_DOWNLOAD_SDK

2. **回传进度/状态线程**
- 代码位置：`com.tencent.tmdownloader. TMAssistantDownloadMessageThread`
- 作用：处理client回调（IPC接收到下载服务的各类回调之后，统一通过这个线程回调给注册了的listener）
- 名称：YYB_DOWNLOAD_SDK_CLIENT

3. **常驻子线程**
- 代码位置：`com.tencent.tmassistantbase.util.HandlerUtils`
- 作用：SDK中除了网络请求之外，不能在主线程执行的任务都交由这个下线程执。
- 名称：YYB_DOWNLOAD_SDK_DEFAULT
