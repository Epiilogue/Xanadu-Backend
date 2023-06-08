import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.neu.ware.entity.CenterStorageRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class SqlTest {

    @Test
    public  void TestSqlGenerate(){
        UpdateWrapper<CenterStorageRecord> updateWrapper = new UpdateWrapper<CenterStorageRecord>().setSql("lock_num=lock_num+" + 10)
                .setSql("allocate_able_num=allocate_able_num-" + 5)
                .eq("product_id", 1).ge("allocate_able_num", 20).eq("id", 3);
    }
}
