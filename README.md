2017/9/27
 下一步功能： update by endAction & scroll


 重点重构思考：
 事实上类似于 setTrimmerTime setShowThumbCount 等一系列方法真的很蠢
 思考于 Config ，引用 IConfig ，将一系列参数变化为 动态方法 获取最为简便实用