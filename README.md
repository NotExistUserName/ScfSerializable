# ScfSerializable
自定义idea插件
插件介绍：

1、自动为类添加@SCFSerializable，为字段添加@SCFMember，如果字段为泛型自动设置generic为true

2、自动调整@SCFMember的顺序

3、自动修改orderId相同的值

4、该插件与从idea官网下载的插件无异，直接使用本地安装即可（因为公司内部框架，不上传idea官网）

5、该插件默认快捷键为ctrl+shift+alt+m(因为系统快捷键较多，所以默认该快捷键)，在快捷键搜索框搜SCFSerializable就能自定义该插件快捷键

自定义idea插件步骤：

1、File -> New -> Project -> Intellij Platfrom Plugin 

   -> SDK choose idea proviered (default) -> next 
   
   -> enter project name(just self design)
   
   -> finish -> edit the plugin xml(this is the plugin core xml,should edit seriously)
   
   -> create package under src -> choose package then enter right 
   
   -> New -> Plugin DevKit -> then enter your logic code
   
   
