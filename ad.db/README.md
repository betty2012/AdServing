# ad.db

ad.db is the component for storing and selecting advertisments. 

The ad.db works in two modes:
1. Memory only: All data is only in memory and will be lost on shutdown. This is the fastest mode.
2. disk storage: All data is persitent to disk, this mode is slower than the in memory mode but after restart the data is still available.


Select an advertisment
----------------------
ad.db offers many possibilities for selection advertisments
* Country
* Date
* Day
* Distance
* KeyValue
* Keyword
* Site
* State
* Time