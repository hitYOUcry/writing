1.groovy、gradle基础
2.RxJava、谷歌架构组件基础
3.多进程下操作数据库

http://qzs.qq.com/open/mobile/video_play/index.html?vid=102_y0511xuk679
http://qzs.qq.com/open/mobile/video_play/index.html?vid=102_j0322vr3i3d
http://qzs.qq.com/open/mobile/video_play/index.html?vid=b0023c0lrs8

关于ViewModel/LiveData的使用有些建议，供大家参考探讨：
1.当UI展示所需要的数据只来自于Engine拉取到的数据时，这时数据获取逻辑简单，没有封装Repository的必要。工程内有一个BaseViewModel，它持有了一个Engine，数据只来自于Engine时使用BaseViewModel就可以。
2.当UI展示所需要的数据来源比较复杂（JceCache/数据库/Engine...）时，建议单独抽出Repository类承载数据获取逻辑。

使用ViewModel/LiveData可以：
    I.更彻底的拆分数据拉取和UI更新（原先的在Fragment/Activity的EngineCallback中更新UI）
    II.Activity重建的时候，ViewModel数据会被保存，不会重新拉取
    III.跟随它所在的LifecycleOwner生命周期结束而销毁
对于第一种情况(数据只来自于Engine)，因为数据获取逻辑本身比较简单，直接在EngineCallback里更新数据在逻辑上也比较清晰，再抽出一层ViewModel会多出一些工作量，显得有点多余。这个大家可以探讨下有没必要再封装一层ViewModel？

key:booking_download_switch //预约下载开关
value:1 //1代表打开，2代表关闭


key:booking_download_daily_times //预约下载每天可以完成下载的次数
value:5