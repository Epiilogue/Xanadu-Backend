package edu.neu.cc;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.collections4.list.FixedSizeList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CustomerCenterApplicationTests {

    @Test
    void testDateTime(){
        System.out.println(new DateTime().monthEnum());
        Date date = DateUtil.date();
        DateUtil.year(date);
        System.out.println(DateUtil.month(date));
        Integer[] l = new Integer[13];
        System.out.println(l[0]+"---"+l[1]);
     }
    @Test
    void contextLoads() {
        DateTime dateTime = DateTime.now();
        System.out.println(dateTime.monthBaseOne());
    }

}
