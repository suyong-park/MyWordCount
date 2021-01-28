## WordCount 예제에서 변형시킨 것.

#### 1. WordCount에서 모든 문자를 소문자화 시켜서 count 될 수 있도록 변경
#### 2. 대부분의 특수문자들을 제거하고 순수 word만 count 될 수 있도록 변경
#### 3. 사용자로부터 단어를 입력받아 그 단어만을 search, count 할 수 있도록 기능 추가
#### 4. Combiner를 Reducer Class를 그대로 사용하여 적용

---
## 환경 및 사용법

#### 환경
    - Java 1.8
    - Pseudo-distributed Mode
    - Using Ubuntu By Docker
    
    
#### 사용법
```
./bin/hadoop jar mywordcount.jar wordcount <input> <output> <finding word>
```

#### 예시 결과
```
실행 : ./bin/hadoop jar mywordcount.jar wordcount input.txt output enim

결과 : enim	5
```
---
## Combiner 적용 전 후 비교
#### <Combiner 적용 전>
21/01/29 01:12:36 INFO mapred.JobClient:   Map-Reduce Framework\
21/01/29 01:12:36 INFO mapred.JobClient:     Spilled Records=10\
21/01/29 01:12:36 INFO mapred.JobClient:     Map output materialized bytes=61\
21/01/29 01:12:36 INFO mapred.JobClient:     **Reduce input records=5**\
21/01/29 01:12:36 INFO mapred.JobClient:     **Map input records=11**\
21/01/29 01:12:36 INFO mapred.JobClient:     SPLIT_RAW_BYTES=126\
21/01/29 01:12:36 INFO mapred.JobClient:     Map output bytes=45\
21/01/29 01:12:36 INFO mapred.JobClient:     Reduce shuffle bytes=0\
21/01/29 01:12:36 INFO mapred.JobClient:     Reduce input groups=1\
21/01/29 01:12:36 INFO mapred.JobClient:     **Combine output records=0**\
21/01/29 01:12:36 INFO mapred.JobClient:     **Reduce output records=1**\
21/01/29 01:12:36 INFO mapred.JobClient:     **Map output records=5**\
21/01/29 01:12:36 INFO mapred.JobClient:     **Combine input records=0**\
21/01/29 01:12:36 INFO mapred.JobClient:     Total committed heap usage (bytes)=514850816\
21/01/29 01:12:36 INFO mapred.JobClient:   File Input Format Counters\
21/01/29 01:12:36 INFO mapred.JobClient:     Bytes Read=2892\
21/01/29 01:12:36 INFO mapred.JobClient:   FileSystemCounters\
21/01/29 01:12:36 INFO mapred.JobClient:     FILE_BYTES_WRITTEN=102935\
21/01/29 01:12:36 INFO mapred.JobClient:     FILE_BYTES_READ=6209\
21/01/29 01:12:36 INFO mapred.JobClient:   File Output Format Counters\
21/01/29 01:12:36 INFO mapred.JobClient:     Bytes Written=19

#### <Combiner 적용 후>
21/01/29 01:13:28 INFO mapred.JobClient:   Map-Reduce Framework\
21/01/29 01:13:28 INFO mapred.JobClient:     Spilled Records=2\
21/01/29 01:13:28 INFO mapred.JobClient:     Map output materialized bytes=17\
21/01/29 01:13:28 INFO mapred.JobClient:     **Reduce input records=1**\
21/01/29 01:13:28 INFO mapred.JobClient:     **Map input records=11**\
21/01/29 01:13:28 INFO mapred.JobClient:     SPLIT_RAW_BYTES=126\
21/01/29 01:13:28 INFO mapred.JobClient:     Map output bytes=45\
21/01/29 01:13:28 INFO mapred.JobClient:     Reduce shuffle bytes=0\
21/01/29 01:13:28 INFO mapred.JobClient:     Reduce input groups=1\
21/01/29 01:13:28 INFO mapred.JobClient:     **Combine output records=1**\
21/01/29 01:13:28 INFO mapred.JobClient:     **Reduce output records=1**\
21/01/29 01:13:28 INFO mapred.JobClient:     **Map output records=5**\
21/01/29 01:13:28 INFO mapred.JobClient:     **Combine input records=5**\
21/01/29 01:13:28 INFO mapred.JobClient:     Total committed heap usage (bytes)=514850816\
21/01/29 01:13:28 INFO mapred.JobClient:   File Input Format Counters\
21/01/29 01:13:28 INFO mapred.JobClient:     Bytes Read=2892\
21/01/29 01:13:28 INFO mapred.JobClient:   FileSystemCounters\
21/01/29 01:13:28 INFO mapred.JobClient:     FILE_BYTES_WRITTEN=103271\
21/01/29 01:13:28 INFO mapred.JobClient:     FILE_BYTES_READ=6165\
21/01/29 01:13:28 INFO mapred.JobClient:   File Output Format Counters\
21/01/29 01:13:28 INFO mapred.JobClient:     Bytes Written=19

### Combiner 적용 결론
하둡의 MapReduce에서는 Shuffle 단계에서 가장 네트워크 오버헤드가 많이 발생한다. 이에 따라 성능의 저하도 shuffle 구간에서 가장 많이 발생한다.
따라서, Map 단계 이후에 나오게 될 intermediate key-value pair의 양을 줄일 수 있다면 Shuffle 단계에서의 
네트워크 통신량도 감소할 것이고, 이에 따라 성능도 개선될 것이다. 

그래서 Map 단계 이후의 intermediate key-value pair의 수를 줄이기 위해 Combiner Class를 사용한다.
Combiner Classs는 얼마나 호출될지 알 수 없으며, Map Task가 처리되고 나온 intermediate K-V pair에 적용된다.
일반적인 경우에는 Combiner Class에 Reducer Class와 동일한 것을 사용할 수 있다.
다만, 평균 구하기 등의 예시는 예외이다. 

위 예시 프로그램에서 Combiner Class를 사용했다. input data가 상당히 작았지만, 
Reduce input records가 Combiner Class 적용 전보다 줄어든 것을 보아 성능이 개선됐음을 알 수 있다.

